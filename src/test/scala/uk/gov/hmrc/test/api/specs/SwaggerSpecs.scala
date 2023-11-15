package uk.gov.hmrc.test.api.specs

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.parser.core.models.ParseOptions
import org.openapi4j.schema.validator.ValidationData
import org.openapi4j.schema.validator.v3.SchemaValidator
import org.scalatest.AppendedClues.convertToClueful
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.test.api.client.HttpClient
import uk.gov.hmrc.test.api.conf.TestConfiguration

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.jdk.StreamConverters._

class SwaggerSpecs extends AnyWordSpec with SwaggerSpec {
  var outcomeAuditing: String = TestConfiguration.url("outcome-auditing")

  "Api platform swagger specification" should behave.like(validOpenApiSpecAt(outcomeAuditing, "/api/conf/1.0/application.yaml"))
}

trait SwaggerSpec {
  this: AnyWordSpec =>

  val parseOptions = new ParseOptions()
  parseOptions.setResolve(true)
  parseOptions.setResolveFully(true)

  val mapper = new ObjectMapper()
  mapper.setSerializationInclusion(Include.NON_NULL);

  val applicationJson = "application/json"
  val client = new HttpClient() {}

  def validOpenApiSpecAt(host: String, openApiUrl: String, userAgent: String = "allowed-test-hmrc-service") {

    "should parse" in {
      val result = new OpenAPIV3Parser().readLocation(s"$host$openApiUrl", null, parseOptions)
      result.getMessages.size() shouldBe 0 withClue result.getMessages
    }

    "should contain valid examples" when {
      val openApi = new OpenAPIV3Parser().read(s"$host$openApiUrl", null, parseOptions)

      openApi.getPaths.forEach { case (path, p) =>
        val verbs = Option(p.getGet).map("GET" -> _) ++ Option(p.getPost).map("POST" -> _)
        verbs.foreach { case (verb, r) =>
          val request = r.getRequestBody.getContent.get(applicationJson)
          val requestExamples = getExamples(request)

          s"$path $verb request examples" in {
            val json = mapper.writeValueAsString(request.getSchema)
            val validator = new SchemaValidator(null, mapper.readTree(json))

            assume(requestExamples.nonEmpty) withClue "No examples were found for this request"
            requestExamples.foreach { e =>
              val vd = new ValidationData()
              validator.validate(e.asInstanceOf[JsonNode], vd)

              vd.isValid shouldBe true withClue vd.results()
            }
          }

          val responses = getResponses(r)
          responses.collect { case (statusCode, Some(r)) =>
            s"$path $verb $statusCode response examples" in {
              val json = mapper.writeValueAsString(r.getSchema)
              val validator = new SchemaValidator(null, mapper.readTree(json))

              val examples = getExamples(r)
              assume(requestExamples.nonEmpty) withClue "No examples were found for this response"

              examples.foreach { e =>
                val vd = new ValidationData()
                validator.validate(e.asInstanceOf[JsonNode], vd)

                vd.isValid shouldBe true withClue vd.results()
              }
            }
          }
        }
      }
    }

    "should elicit valid responses from the service" when {
      val openApi = new OpenAPIV3Parser().read(s"$host$openApiUrl", null, parseOptions)

      openApi.getPaths.forEach { case (path, p) =>
        val verbs = Option(p.getGet).map("GET" -> _) ++ Option(p.getPost).map("POST" -> _)

        val requests = verbs.map { case (verb, r) =>
          val request = r.getRequestBody.getContent.get(applicationJson)
          val responses = getResponses(r)

          verb -> (request, responses)
        }

        requests.foreach { case (verb, (request, responses)) =>
          val examples = getExamples(request)
          if (examples.isEmpty) {
            s"$verb $path (no examples found)" in {
              assume(examples.nonEmpty) withClue "No examples were found for this request"
            }
          }

          examples.foreach { e =>
            val headers = Seq("Content-Type" -> applicationJson, "User-Agent" -> userAgent)
            val req = verb match {
              case "GET" => client.get(s"$host$path", headers: _*)
              case "POST" =>
                client.post(s"$host$path", mapper.writeValueAsString(e.asInstanceOf[JsonNode]), headers: _*)
            }

            val response = Await.result(req, 10.seconds)
            Option(responses(response.status.toString)).collect { case Some(r) =>
              s"$verb $path - ${response.status}" in {
                val json = mapper.writeValueAsString(r.getSchema)
                val validator = new SchemaValidator(null, mapper.readTree(json))

                val vd = new ValidationData()
                validator.validate(mapper.readTree(response.body), vd)

                vd.isValid shouldBe true withClue vd.results()
              }
            }
          }
        }
      }
    }
  }

  private def getResponses(r: Operation) = {
    r.getResponses.entrySet().stream().map(e => {
      e.getKey -> Option(e.getValue.getContent).map(_.get(applicationJson))
    }).toScala(Map)
  }

  def getExamples(request: MediaType) = {
    val requestExample = Option(request.getExample)
    val requestExamples = Option(request.getExamples).map(_.values().stream().toScala(Seq)).getOrElse(Seq()).map(_.getValue)
    val schemaExample = Option(request.getSchema.getExample)
    val schemaExamples = Option(request.getSchema.getExamples).map(_.stream().toScala(Seq)).getOrElse(Seq())

    requestExample ++ requestExamples ++ schemaExample ++ schemaExamples
  }
}

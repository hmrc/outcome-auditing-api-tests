import sbt._

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"         %% "api-test-runner"          % "0.10.0",
    "org.wiremock"         % "wiremock"                 % "3.13.1",
    "io.swagger.parser.v3" % "swagger-parser"           % "2.1.31",
    "org.openapi4j"        % "openapi-schema-validator" % "1.0.7"
  ).map(_ % Test)
}

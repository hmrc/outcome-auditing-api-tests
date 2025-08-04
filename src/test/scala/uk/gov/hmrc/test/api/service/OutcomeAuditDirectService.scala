/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.test.api.service

import play.api.libs.ws.StandaloneWSRequest
import uk.gov.hmrc.test.api.client.HttpClient
import uk.gov.hmrc.test.api.conf.TestConfiguration
import uk.gov.hmrc.test.api.helpers.Endpoints

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class OutcomeAuditDirectService extends HttpClient {
  var outcomeAuditing: String = TestConfiguration.url("outcome-auditing")
  val userAgent: String       = TestConfiguration.userAgent
  val basicAuth: String       = "Basic b3V0Y29tZS1hdWRpdGluZzpsb2NhbC10ZXN0LXRva2Vu"
  val contentType: String     = "application/json"

  def postOutcomeAuditDirectly(
    outcomeAuditDetails: String,
    host: String = outcomeAuditing
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      post(
        s"$host/${Endpoints.OUTCOME_AUDITING}",
        outcomeAuditDetails,
        ("Content-Type", s"$contentType"),
        ("User-Agent", s"$userAgent"),
        ("Authorization", s"$basicAuth")
      ),
      10.seconds
    )
}

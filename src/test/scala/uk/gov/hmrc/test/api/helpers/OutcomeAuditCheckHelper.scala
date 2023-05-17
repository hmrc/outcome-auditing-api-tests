/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.test.api.helpers

import play.api.libs.json.Json
import play.api.libs.ws.StandaloneWSRequest
import uk.gov.hmrc.outcomeauditing.model.request.nino.NinoInsightsOutcomeRequest
import uk.gov.hmrc.test.api.service.{OutcomeAuditDirectService, OutcomeAuditProxyService}
import uk.gov.hmrc.outcomeauditing.model.response.Response
import uk.gov.hmrc.outcomeauditing.model.response.Response.Implicits.responseFormat
import uk.gov.hmrc.test.api.models.BadRequest

class OutcomeAuditCheckHelper {
  val outcomeAuditDirectService: OutcomeAuditDirectService = new OutcomeAuditDirectService
  val outcomeAuditProxyService: OutcomeAuditProxyService = new OutcomeAuditProxyService

  def callOutcomeAuditingAPIDirectly(
    outcomeAuditDetails: String
  ): Response = {
    val response: StandaloneWSRequest#Self#Response =
      outcomeAuditDirectService.postOutcomeAuditDirectly(outcomeAuditDetails)
    Json.parse(response.body).as[Response]
  }

  def callOutcomeAuditingViaProxy(
    outcomeAuditDetails: String
  ): Response = {
    val response: StandaloneWSRequest#Self#Response =
      outcomeAuditProxyService.postOutcomeAuditViaProxy(outcomeAuditDetails)
    Json.parse(response.body).as[Response]
  }

  def parseInvalidOutcomeAuditCheckResponseFromAPI(
    outcomeAuditDetails: String
  ): BadRequest = {
    val response: StandaloneWSRequest#Self#Response =
      callInvalidOutcomeAuditCheckResponseFromAPI(outcomeAuditDetails)
    Json.parse(response.body).as[BadRequest]
  }

  def callInvalidOutcomeAuditCheckResponseFromAPI(
    outcomeAuditDetails: String
  ): StandaloneWSRequest#Self#Response =
    outcomeAuditProxyService.postInvalidOutcomeAuditCheck(outcomeAuditDetails)
}

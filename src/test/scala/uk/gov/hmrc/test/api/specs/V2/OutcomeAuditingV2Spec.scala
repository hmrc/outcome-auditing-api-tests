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

package uk.gov.hmrc.test.api.specs.V2

import com.github.tomakehurst.wiremock.client.WireMock.{matchingJsonPath, postRequestedFor, urlEqualTo, verify}
import uk.gov.hmrc.test.api.conf.TestConfiguration
import uk.gov.hmrc.test.api.specs.{BaseSpec, WireMockTrait}
import uk.gov.hmrc.test.api.testdata.OutcomeAuditV2

class OutcomeAuditingV2Spec extends BaseSpec with OutcomeAuditV2 with WireMockTrait {

  Feature("Verifying Outcome Auditing V2 API directly") {

    Scenario("Verify the Outcome Auditing API when valid JSON is provided") {

      Given("a valid outcome payload is provided")

      When("the outcome auditing V2 api is invoked directly")
      val actual = outcomeAuditCheckHelper.callOutcomeAuditingV2APIDirectly(validOutcomeAuditingV2Json)

      Then("the api returns a 201")
      assert(actual.status == 201)

      verify(
        postRequestedFor(urlEqualTo("/write/audit"))
          .withRequestBody(
            matchingJsonPath(
              "$[?(" +
                s"@.auditSource == '${TestConfiguration.expectedServiceName}'" +
                s"&& @.auditType == 'OutcomeReportingSubmitted'" +
                s"&& @.detail.correlationData.correlationId == '33df37a4-a535-41fe-8032-7ab718b45526'" +
                s"&& @.detail.correlationData.correlationIdType == 'ACKNOWLEDGEMENT_ID'" +
                s"&& @.detail.submitter == 'sa-reg'" +
                s"&& @.detail.decisionData[0].businessEvent == 'SARegistrationSubmitted'" +
                s"&& @.detail.decisionData[0].decision == 'ACCEPTED'" +
                s"&& @.detail.decisionData[0].evidence[0].decisionMethod == 'AUTOMATIC'" +
                s"&& @.detail.decisionData[0].evidence[0].decisionSystem == 'sa-reg'" +
                s"&& @.detail.decisionData[0].evidence[0].decisionTimestamp == '2025-07-09T08:14:13Z'" +
                s"&& @.detail.decisionData[0].evidence[0].decisionAuthority == 'sa-reg-authority'" +
                s"&& @.detail.decisionData[0].evidence[0].attributes[0].attributeType == 'UTR'" +
                s"&& @.detail.decisionData[0].evidence[0].attributes[0].attributeValue == '123456789'" +
                s"&& @.detail.decisionData[0].evidence[0].attributes[1].attributeType == 'NINO'" +
                s"&& @.detail.decisionData[0].evidence[0].attributes[1].attributeValue == '987654321'" +
                ")]"
            )
          )
      )
    }

    Scenario("Verify the Outcome Auditing API when invalid JSON is provided") {

      Given("an invalid outcome payload is provided")

      When("the outcome auditing V2 api is invoked directly")
      val actual = outcomeAuditCheckHelper.callOutcomeAuditingV2APIDirectly(invalidOutcomeAuditingV2Json)

      Then("the api returns a 400")
      assert(actual.status == 400)
    }

  }



}

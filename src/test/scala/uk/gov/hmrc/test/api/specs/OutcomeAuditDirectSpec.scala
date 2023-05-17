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

package uk.gov.hmrc.test.api.specs

import com.github.tomakehurst.wiremock.client.WireMock.{matchingJsonPath, postRequestedFor, urlEqualTo, verify}
import org.assertj.core.api.Assertions.assertThat
import uk.gov.hmrc.test.api.conf.TestConfiguration
import uk.gov.hmrc.test.api.testdata.OutcomeAudit

import scala.concurrent.duration.DurationInt

class OutcomeAuditDirectSpec extends BaseSpec with WireMockTrait {

  Feature("Verifying Outcome Auditing API directly") {

    Scenario("Verify the Outcome Auditing API when a valid nino insights outcome is provided") {
      Given("a valid nino insights outcome is provided")

      When("the outcome audit api is invoked directly")
      val actual = outcomeAuditCheckHelper.callOutcomeAuditingAPIDirectly(OutcomeAudit.ninoInsightsInput)

      Then("the api returns a 200")
      assertThat(actual.code).isEqualTo("ok")
      assertThat(actual.message).isEqualTo(s"outcome from ${TestConfiguration.userAgent} processed")

      verify(
        delayedFunction(1.seconds)(
          postRequestedFor(urlEqualTo("/write/audit"))
            .withRequestBody(
              matchingJsonPath(
                "$[?(" +
                s"@.auditSource == '${TestConfiguration.expectedServiceName}'" +
                s"&& @.auditType == 'NinoInsightsCipOutcomeSubmitted'" +
                s"&& @.detail.userAgent == '${TestConfiguration.userAgent}'" +
                s"&& @.detail.submitter == 'dfe'" +
                s"&& @.detail.submission.submissionType == 'nino'" +
                s"&& @.detail.submission.submissionAttribute.nino == 'AB608580X'" +
                s"&& @.detail.outcome.outcomeType == 'insights'" +
                s"&& @.detail.outcome.decision == 'ACCEPTED'" +
                s"&& @.detail.outcome.reasons == 'Some reason'" +
              ")]"
            )
          )
        )
      )
    }

    Scenario("Verify the Outcome Auditing API when a valid bank account insights outcome is provided") {
      Given("a valid bank account insights outcome is provided")

      When("the outcome audit api is invoked directly")
      val actual = outcomeAuditCheckHelper.callOutcomeAuditingAPIDirectly(OutcomeAudit.bankAccountInput)

      Then("the api returns a 200")
      assertThat(actual.code).isEqualTo("ok")
      assertThat(actual.message).isEqualTo(s"outcome from ${TestConfiguration.userAgent} processed")

      verify(
        delayedFunction(1.seconds)(
          postRequestedFor(urlEqualTo("/write/audit"))
            .withRequestBody(
              matchingJsonPath(
                "$[?(" +
                s"@.auditSource == '${TestConfiguration.expectedServiceName}'" +
                s"&& @.auditType == 'BankAccountInsightsCipOutcomeSubmitted'" +
                s"&& @.detail.userAgent == '${TestConfiguration.userAgent}'" +
                s"&& @.detail.submitter == 'ipp'" +
                s"&& @.detail.submission.submissionType == 'bank-account'" +
                s"&& @.detail.submission.submissionAttribute.sortCode == '608580'" +
                s"&& @.detail.submission.submissionAttribute.accountNumber == '48835625'" +
                s"&& @.detail.outcome.outcomeType == 'insights'" +
                s"&& @.detail.outcome.decision == 'ACCEPTED'" +
                s"&& @.detail.outcome.reasons == 'Some reason'" +
              ")]"
            )
          )
        )
      )
    }

    Scenario("Verify the Outcome Auditing API when a valid bank account payment allocation outcome is provided") {
      Given("a valid bank account payment allocation outcome is provided")

      When("the outcome audit api is invoked directly")
      val actual = outcomeAuditCheckHelper.callOutcomeAuditingAPIDirectly(OutcomeAudit.paymentAllocationInput)

      Then("the api returns a 200")
      assertThat(actual.code).isEqualTo("ok")
      assertThat(actual.message).isEqualTo(s"outcome from ${TestConfiguration.userAgent} processed")

      verify(
        delayedFunction(1.seconds)(
          postRequestedFor(urlEqualTo("/write/audit"))
            .withRequestBody(
              matchingJsonPath(
                "$[?(" +
                s"@.auditSource == '${TestConfiguration.expectedServiceName}'" +
                s"&& @.auditType == 'BankAccountPaymentAllocationCipOutcomeSubmitted'" +
                s"&& @.detail.userAgent == '${TestConfiguration.userAgent}'" +
                s"&& @.detail.submitter == 'ipp'" +
                s"&& @.detail.submission.submissionType == 'bank-account'" +
                s"&& @.detail.submission.submissionAttribute.sortCode == '608580'" +
                s"&& @.detail.submission.submissionAttribute.accountNumber == '48835625'" +
                s"&& @.detail.outcome.outcomeType == 'payment-allocation'" +
                s"&& @.detail.outcome.decision == 'PAYMENT_ALLOCATED'" +
                s"&& @.detail.outcome.reasons == 'ACCOUNT_ALLOCATED_TO_DETAILS'" +
                s"&& @.detail.outcome.evidence.sa_utr == '0123456789'" +
                s"&& @.detail.outcome.evidence.paye_ref == 'ABC/A1234'" +
                s"&& @.detail.outcome.evidence.full_name == 'Jane Smith'" +
                s"&& @.detail.outcome.evidence.user_id == '0123456789112345'" +
              ")]"
            )
          )
        )
      )
    }
  }
}

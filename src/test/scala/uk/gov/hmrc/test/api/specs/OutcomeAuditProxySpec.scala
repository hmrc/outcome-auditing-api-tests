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
import uk.gov.hmrc.test.api.testdata.ApiErrors.NOT_AUTHORISED
import uk.gov.hmrc.test.api.testdata.OutcomeAudit

import scala.concurrent.duration.DurationInt

class OutcomeAuditProxySpec extends BaseSpec with WireMockTrait {
  val expectedResponseMessage = s"outcome from outcome-auditing-proxy,${TestConfiguration.userAgent} processed"

  Feature("Verifying Outcome Auditing API via proxy API") {

    Scenario("Verify the Outcome Auditing proxy API when a valid nino insights outcome is provided") {
      Given("a valid nino insights outcome is provided")

      When("the outcome audit proxy api is invoked")
      val actual = outcomeAuditCheckHelper.callOutcomeAuditingViaProxy(OutcomeAudit.ninoInsightsInput)

      Then("the api returns a 200")
      assertThat(actual.code).isEqualTo("ok")
      assertThat(actual.message).isEqualTo(expectedResponseMessage)
    }

    Scenario("Verify the Outcome Auditing proxy API when a valid bank account insights outcome is provided") {
      Given("a valid bank account insights outcome is provided")

      When("the outcome audit proxy api is invoked")
      val actual = outcomeAuditCheckHelper.callOutcomeAuditingViaProxy(OutcomeAudit.bankAccountInput)

      Then("the api returns a 200")
      assertThat(actual.code).isEqualTo("ok")
      assertThat(actual.message).isEqualTo(expectedResponseMessage)
    }

    Scenario("Verify the Outcome Auditing proxy API when a valid bank account payment allocation outcome is provided") {
      Given("a valid bank account payment allocation outcome is provided")

      When("the outcome audit proxy api is invoked")
      val actual = outcomeAuditCheckHelper.callOutcomeAuditingViaProxy(OutcomeAudit.paymentAllocationInput)

      Then("the api returns a 200")
      assertThat(actual.code).isEqualTo("ok")
      assertThat(actual.message).isEqualTo(expectedResponseMessage)
    }

    Scenario("Try to get risking information for a NINO on the risk list without using a user agent") {
      Given("I want to see if we hold any risking information for a NINO")

      When("I use the NINO check insights API to see what information we hold")
      val actual = outcomeAuditCheckHelper.parseInvalidOutcomeAuditCheckResponseFromAPI(OutcomeAudit.paymentAllocationInput)

      Then("My query is rejected")
      assertThat(actual.code).isEqualTo(403)
      assertThat(actual.description).contains(NOT_AUTHORISED)
    }
  }
}

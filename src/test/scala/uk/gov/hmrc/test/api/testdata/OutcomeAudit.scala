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

package uk.gov.hmrc.test.api.testdata

import play.api.libs.json.{JsValue, Json}

object OutcomeAudit {

  val ninoInsightsInput: JsValue =
    Json.obj(
      "correlationId" -> "33df37a4-a535-41fe-8032-7ab718b45526",
      "submitter"     -> "dfe",
      "submission"    -> Json.obj(
        "submissionType"      -> "nino",
        "submissionAttribute" -> Json.obj(
          "nino" -> "AB608580X"
        )
      ),
      "outcome"       -> Json.obj(
        "outcomeType" -> "Insights",
        "decision"    -> "ACCEPTED",
        "reasons"     -> Json.arr("Some reason")
      )
    )

  val bankAccountInput: JsValue =
    Json.obj(
      "correlationId" -> "33df37a4-a535-41fe-8032-7ab718b45526",
      "submitter"     -> "ipp",
      "submission"    -> Json.obj(
        "submissionType"      -> "bank-account",
        "submissionAttribute" -> Json.obj(
          "sortCode"      -> "608580",
          "accountNumber" -> "48835625"
        )
      ),
      "outcome"       -> Json.obj(
        "outcomeType" -> "Insights",
        "decision"    -> "ACCEPTED",
        "reasons"     -> Json.arr("Some reason")
      )
    )

  val paymentAllocationInput: JsValue =
    Json.obj(
      "correlationId" -> "33df37a4-a535-41fe-8032-7ab718b45526",
      "submitter"     -> "ipp",
      "submission"    -> Json.obj(
        "submissionType"      -> "bank-account",
        "submissionAttribute" -> Json.obj(
          "sortCode"      -> "608580",
          "accountNumber" -> "48835625"
        )
      ),
      "outcome"       -> Json.obj(
        "outcomeType" -> "PaymentAllocation",
        "decision"    -> "PAYMENT_ALLOCATED",
        "reasons"     -> Json.arr("ACCOUNT_ALLOCATED_TO_DETAILS"),
        "evidence"    -> Json.obj(
          "sa_utr"    -> "0123456789",
          "paye_ref"  -> "ABC/A1234",
          "full_name" -> "Jane Smith",
          "user_id"   -> "0123456789112345"
        )
      )
    )
}

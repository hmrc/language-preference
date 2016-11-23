/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.languagepreference.controllers

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.{ShouldMatchers, WordSpec}
import org.scalatestplus.play.OneAppPerSuite
import play.api.Play
import play.api.Play._
import play.api.i18n.Lang
import play.api.libs.ws.WS
import play.api.mvc.Cookie
import play.api.test._
import play.api.test.Helpers._
import play.test.WithServer
import uk.gov.hmrc.languagepreference.utils.LanguageConstants._

class LanguageControllerSpec extends WordSpec with ShouldMatchers with PlayRunners with ScalaFutures with DefaultAwaitTimeout with IntegrationPatience with OneAppPerSuite {

  private val refererValue  = "http://gov.uk"
  private val fallbackValue = "http://gov.uk/fallback"

  val mockLanguageController = new LanguageController {
  }

  abstract class ServerWithConfig(conf: Map[String, String] = Map.empty) extends WithServer()

  "The switch language endpoint" should {

    "return with 200 for getpartial. " in
      new ServerWithConfig() {
        val lang: String = "en-GB"
        val res = mockLanguageController.getPartial(lang).apply(FakeRequest())
        status(res) should be (OK)
      }
  }
}

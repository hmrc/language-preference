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

  "The languageController endpoint " should {

    "return with 200 for getpartial. " in
      new ServerWithConfig() {
        val res = mockLanguageController.getPartial().apply(FakeRequest())
        //contentAsString(res) should contain("language-preference/setlang/?lang=cy-GB")
        status(res) should be (OK)
      }

    "get language should be eng" in {
      val res = mockLanguageController.getLang().apply(FakeRequest().withHeaders((REFERER, "/localhost/test/go")))
      status(res) should be(OK)
      cookies(res).get(hmrcLang) match {
        case Some(c: Cookie) => c.value should be(EngLangCode)
        case _ => fail("HMRC_LANG cookie was not found.")
      }
    }

      "set language to Welsh" in {
        val res = mockLanguageController.setLang("cy-GB").apply(FakeRequest().withHeaders((REFERER, "/localhost/test/go")))
        status(res) should be(303)
        cookies(res).get(hmrcLang) match {
          case Some(c: Cookie) => c.value should be(WelshLangCode)
          case _ => fail("HMRC_LANG cookie was not found.")
        }
      }
        "set language to Eng" in {
          val res = mockLanguageController.setLang("en-GB").apply(FakeRequest().withHeaders((REFERER, "/localhost/test/go")))
          status(res) should be (303)
          cookies(res).get(hmrcLang) match {
            case Some(c: Cookie) => c.value should be (EngLangCode)
            case _ => fail("HMRC_LANG cookie was not found.")
          }
    }
  }
}

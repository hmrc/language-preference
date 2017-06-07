/*
 * Copyright 2017 HM Revenue & Customs
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

import java.net.URLEncoder

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.{ShouldMatchers, WordSpec}
import org.scalatestplus.play.OneAppPerSuite
import play.api.Mode
import play.api.mvc.Cookie
import play.api.test._
import play.api.test.Helpers._
import play.test.{WithApplication, WithServer}
import uk.gov.hmrc.languagepreference.utils.LanguageConstants._

class LanguageControllerSpec extends WordSpec with ShouldMatchers with PlayRunners with ScalaFutures with DefaultAwaitTimeout with IntegrationPatience with OneAppPerSuite {

  private val refererValue  = "http://gov.uk"
  private val fallbackValue = "http://gov.uk/fallback"

  val mockLanguageController = new LanguageController {
    override lazy val env = "Prod"
  }

  abstract class ServerWithConfig(conf: Map[String, String] = Map.empty) extends WithServer()

  "The languageController endpoint " should {

    "when get language is invoked, it should always return English (for now)" in {
      val result = mockLanguageController.getLang().apply(FakeRequest())
      status(result) should be (OK)
      contentAsString(result) shouldBe (EngLangCode)
      //contentType(result) shouldBe(JSON)
    }


    "set the hmrc language cookie to Welsh and forward to specified page when language is set to Welsh" in {

      val returnUrl = "/personal-account"
      val result = mockLanguageController.setLang("cy-GB", returnUrl).apply(FakeRequest())
      status(result) should be(SEE_OTHER)
      header(LOCATION, result) should contain(returnUrl)
      cookies(result).get(hmrcLang) match {
        case Some(c: Cookie) => c.value should be(WelshLangCode)
        case _ => fail("HMRC_LANG cookie was not found.")
      }
      await(result).header.headers("Location") contains returnUrl
    }


    "set the hmrc language cookie to English and forward to specified page when language is set to English " in {
      val returnUrl = "/personal-account/tax-credits-summary"
      val result = mockLanguageController.setLang("en-GB", returnUrl).apply(FakeRequest())
      status(result) should be (SEE_OTHER)
      header(LOCATION, result) should contain(returnUrl)
      cookies(result).get(hmrcLang) match {
        case Some(c: Cookie) => c.value should be (EngLangCode)
        case _ => fail("HMRC_LANG cookie was not found.")
      }
    }


    "not forward to the specified address, when passing a url starting with http, in Production mode" in {
      val returnUrl = "http://my-phishing-site.com"
      val result = mockLanguageController.setLang("en-GB", returnUrl).apply(FakeRequest())
      status(result) should be (SEE_OTHER)
      header(LOCATION, result) should contain("/")
      cookies(result).get(hmrcLang) match {
        case Some(c: Cookie) => c.value should be (EngLangCode)
        case _ => fail("HMRC_LANG cookie was not found.")
      }
    }

  }
}

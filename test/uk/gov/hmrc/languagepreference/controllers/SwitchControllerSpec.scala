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

import org.scalatest.{WordSpec, _}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.OneAppPerSuite
import play.api.Play
import play.api.i18n.{Lang, Messages}
import play.api.mvc.Cookie
import play.api.test.Helpers._
import play.api.test.{DefaultAwaitTimeout, FakeApplication, FakeRequest, PlayRunners}
import play.api.i18n.MessagesApi
import play.test.WithServer
import uk.gov.hmrc.languagepreference.utils.LanguageConstants._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

/**
  * Created by markhmrc on 14/11/16.
  */
class SwitchControllerSpec  extends WordSpec with ShouldMatchers with PlayRunners with ScalaFutures with DefaultAwaitTimeout with IntegrationPatience with OneAppPerSuite {

  private val refererValue  = "http://gov.uk"


  abstract class ServerWithConfig(conf: Map[String, String] = Map.empty) extends WithServer()


  val mockSwitchController = new SwitchController(app.injector.instanceOf[MessagesApi]) {
  }


  "The switch language endpoint" should {


    "set the language in a cookie for switch to welsh." in
      new ServerWithConfig() {
        val res = mockSwitchController.switchToWelsh()(FakeRequest().withHeaders(REFERER -> refererValue))
        cookies(res).get(Play.langCookieName) match {
          case Some(c: Cookie) => c.value should be (WelshLangCode)
          case _ => fail("PLAY_LANG cookie was not found.")
        }
      }

    "set the language in a cookie for switch to english." in
      new ServerWithConfig() {
        val res = mockSwitchController.switchToEnglish()(FakeRequest().withHeaders(REFERER -> refererValue))
        cookies(res).get(Play.langCookieName) match {
          case Some(c: Cookie) => c.value should be (EnglishLangCode)
          case _ => fail("PLAY_LANG cookie was not found.")
        }
      }

    "set the redirect location to the value of the referer header for welsh." in
      new ServerWithConfig() {
        val request = FakeRequest().withHeaders(REFERER -> refererValue)
        val res = mockSwitchController.switchToWelsh()(request)
        redirectLocation(res) should be(Some(refererValue))
      }

    "set the redirect location to the value of the referer header for english." in
      new ServerWithConfig() {
        val request = FakeRequest().withHeaders(REFERER -> refererValue)
        val res = mockSwitchController.switchToEnglish()(request)
        redirectLocation(res) should be(Some(refererValue))
      }

    "Throw exception with out a referrer for switch to welsh " in
      new ServerWithConfig() {
        a [Exception] should be thrownBy(mockSwitchController.switchToWelsh()(FakeRequest()))
      }

    "Throw exception with out a referrer for switch to english " in
      new ServerWithConfig() {
        a [Exception] should be thrownBy(mockSwitchController.switchToEnglish()(FakeRequest()))
      }
  }
}

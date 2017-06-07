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

import play.api.Mode
import play.api.mvc._
import uk.gov.hmrc.languagepreference.utils.LanguageConstants._
import uk.gov.hmrc.play.config.{RunMode, ServicesConfig}
import uk.gov.hmrc.play.frontend.controller.FrontendController


trait LanguageController extends FrontendController with ServicesConfig with RunMode {


  // Could be iterated later, to allow language preference
  // to be retrieved from a persistant store (preferences?).
  // For now, this just offers a default language
  def getLang() = Action { implicit request =>
    Ok("en-GB")
  }


  def setLang(langToSet:String, url:String) = Action { implicit request =>
    Redirect(sanitisedUrl(url)).withCookies(cookie(langToSet))
  }

  //Prevent spoofing from external (phishing) host.
  private def sanitisedUrl(url: String): String = {
    if((env == Mode.Prod.toString) && url.startsWith("http")){"/"}
    else url
  }

  private def cookie(langToSet: String) = {
    if(langToSet == WelshLangCode) {langCookie(WelshLangCode)}
    else {langCookie(EngLangCode)}
  }
}


object LanguageController extends LanguageController

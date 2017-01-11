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

import javax.inject.Singleton
import play.api.Logger
import play.api.mvc._
import uk.gov.hmrc.languagepreference.utils.LanguageConstants._
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.frontend.controller.FrontendController
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

/**
  * LanguageController that switches the language of the current web application.
  *
  * This trait provides a means of switching the current language and redirecting the user
  * back to their original location. It expects a fallbackURL to be defined when implemented.
  * It also expects a languageMap to be defined, this provides a way of mapping strings to Lang objects.
  *
  */

@Singleton
class LanguageController extends FrontendController with ServicesConfig {


  def getPartial() = Action { implicit request =>
    val englishSwitchUrl = baseUrl(baseurl) + uk.gov.hmrc.languagepreference.controllers.routes.LanguageController.setLang(EngLangCode).url
    val  welshSwitchUrl = baseUrl(baseurl) + uk.gov.hmrc.languagepreference.controllers.routes.LanguageController.setLang(WelshLangCode).url
    Logger.debug("Partial created + host are " + request.host)

    Ok(uk.gov.hmrc.languagepreference.views.html.language_selection( englishSwitchUrl,welshSwitchUrl))
  }

  def getLang() = Action {
    implicit  request =>

    // option 1
      Logger.debug("************** DEBUG INFO  START ******************  ")
      Logger.debug("host :  " + request.host)
      Logger.debug("hc.headers:  " + hc.headers)
      Logger.debug("hc.headers:  " + hc.otherHeaders)
      Logger.debug("request.headers  :  " + request.headers.headers)
      Logger.debug("request.session  :  " + request.session)
      Logger.debug("hc.extraHeaders :  " + hc.extraHeaders)
      Logger.debug("************** DEBUG INFO END ******************  ")
    request.session.get(hmrcLang) match
      {
        case Some(cookie:String) =>  Ok(cookie)
        //failure condition ??
        case _ => { Logger.debug("default case " + request.session.get(hmrcLang))
          val cookie = createCookie(EngLangCode)
          Ok(EngLangCode).withSession(createLangHeader(cookie)) }
      }
  }

  def setLang(langToSet:String) = Action {
    implicit request =>
            // option 1
    langToSet match {
      case WelshLangCode => redirectToReferrer(langToSet)
      case _ => redirectToReferrer(langToSet)
    }
  }

  def redirectToReferrer(langToSet: String)(implicit request :Request[AnyContent]) = {
    val referrer = request.headers.get(REFERER)
    val cookie = createCookie(langToSet)
    referrer match {
      case Some(_) => {
        Redirect(referrer.get).withSession(createLangHeader(cookie))//.withCookies(cookie)
       }
      case None => throw new Exception("No Referer Identified")
    }

  }
}

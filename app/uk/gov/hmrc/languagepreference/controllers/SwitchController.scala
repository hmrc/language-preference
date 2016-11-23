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

import javax.inject.{Inject, Singleton}

import play.api.Logger
import play.api.i18n.{I18nSupport, Lang, MessagesApi}
import play.api.mvc.Action
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.play.microservice.controller.BaseController

/**
  * Created by markhmrc on 14/11/16.
  */
@Singleton
class SwitchController  @Inject()(val messagesApi: MessagesApi) extends FrontendController with ServicesConfig  with I18nSupport{

  def switchToWelsh() = Action { implicit request =>
    val redirect = request.headers.get(REFERER)
    redirect match {
      case Some(_) =>{Logger.info("Calling swicth to welsh")
        Redirect (redirect.get).withLang (Lang ("cy", "GB") )
      }
      case None => throw new Exception("No referer")
    }
  }

  def switchToEnglish() = Action { implicit request =>
    val redirect = request.headers.get(REFERER)
    redirect match {
      case Some(_) => {
        Logger.info("Switch to english")
        Redirect (redirect.get).withLang(Lang("en", "GB"))
      }
      case None => throw new Exception("No referer")
    }

  }

}

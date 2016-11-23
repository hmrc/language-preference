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

package uk.gov.hmrc.languagepreference.utils

import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.{TimeZone, ULocale}
import org.joda.time.{DateTime, LocalDate}
import play.api.Play
import play.api.i18n.{I18nSupport, Lang, Messages, MessagesApi}
import play.api.mvc._

/** This object provides access to common language utilities.
  *
  * This object contains language codes for English and Welsh and a
  * function to return the current language based on a request header.
  *
  * Additionally, a Dates object is provided which provides helper
  * functions to return correctly formatted dates in both English
  * and Welsh.
  *
  */
object LanguageConstants  {

  val EnglishLangCode = "en-GB"
  val WelshLangCode   = "cy-GB"

  val English = Lang(EnglishLangCode)
  val Welsh   = Lang(WelshLangCode)

}

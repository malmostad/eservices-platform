/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
package org.motrice.coordinatrice

import grails.converters.*

class RestI18nController {

  def activityLabelService
  def formLabelService
  def guideUrlService

  private final static Integer CONFLICT_STATUS = 409
  private final static CONT_DISP = 'Content-Disposition'

  /**
   * Get one or more activity labels.
   * $PREFIX/activitylabel/$procdefkey/$locale/$activityname? [?version=$procdefversion]
   * where $id must be a process definition id.
   */
  def activityLabelGet(String procdefkey, String locale, String activityname) {
    if (log.debugEnabled) log.debug "ACTIVITY LABEL GET: ${params}"
    def list = activityLabelService.findLabels(procdefkey, locale, activityname, params.version)

    if (list?.size() > 0) {
      response.status = 200
      render list as JSON
    } else {
      if (log.debugEnabled) log.debug "ACTIVITY LABEL GET >> 404: procdef=${procdefkey}, locale=${locale}, activity: ${activityname}"
      render(status: 404)
    }
  }

  /**
   * Get one or more guideUrl labels.
   * $PREFIX/guideurl/$procdefkey/$locale/$activityname? [?version=$procdefversion]
   * where $id must be a process definition id.
   */
  def guideUrlGet(String procdefkey, String locale, String activityname) {
    if (log.debugEnabled) log.debug "GUIDEURL GET: ${params}"
    def guideMap = guideUrlService.generateUrlData(procdefkey, locale, activityname, params.version)

    if (guideMap.url) {
      response.status = 200
      render guideMap as JSON
    } else {
      if (log.debugEnabled) log.debug "GUIDEURL GET >> 404: procdef=${procdefkey}, locale=${locale}, activity: ${activityname}"
      render(status: 404)
    }
  }

  /**
   * Get a start form label.
   * $PREFIX/startformlabel/$appname/$formname/$locale [?version=$formdefversion]
   */
  def startFormLabelGet(String appname, String formname, String locale) {
    if (log.debugEnabled) log.debug "START FORM LABEL GET: ${params}"
    def label = formLabelService.findLabel(appname, formname, locale, params.version)
    if (label) {
      def map = [appName: appname, formName: formname, formdefId: label.formdefId,
      formdefVer: label.formdefVer, locale: label.locale, label: label.label]
      response.status = 200
      render map as JSON
    } else {
      if (log.debugEnabled) log.debug "START FORM LABEL GET >> 404 appname=${appname}, formname=${formname}, locale=${locale}, version=${params.version}"
    }
  }

}

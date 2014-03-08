package org.motrice.coordinatrice

import grails.converters.*

class RestGuideUrlController {

  def guideUrlService

  private final static Integer CONFLICT_STATUS = 409
  private final static CONT_DISP = 'Content-Disposition'

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

}

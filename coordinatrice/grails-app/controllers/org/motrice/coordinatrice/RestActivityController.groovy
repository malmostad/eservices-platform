package org.motrice.coordinatrice

import grails.converters.*

class RestActivityController {

  def activityLabelService

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

    if (list) {
      response.status = 200
      render list as JSON
    } else {
      if (log.debugEnabled) log.debug "ACTIVITY LABEL GET >> 404: procdef=${procdefkey}, locale=${locale}, activity: ${activityname}"
      render(status: 404)
    }
  }

}

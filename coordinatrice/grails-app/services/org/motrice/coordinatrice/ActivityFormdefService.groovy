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

import org.apache.commons.logging.LogFactory

/**
 * Actions related to MtfActivityFormDefinition, especially the connections
 * to forms etc
 */
class ActivityFormdefService { 
  // Transaction support mainly for MtfActivityFormDefinition
  static transactional = true

  def procdefService
  private static final log = LogFactory.getLog(this)

  /**
   * Connect activities using a process definition as template.
   * origProc may be the original process definition that was duplicated, or null,
   * srcProc must be a process definition with activity connections,
   * tgtProc must be the target process definition,
   * The process definitions must have their activities.
   */
  def connectActivityForms(Procdef origProc, Procdef srcProc, Procdef tgtProc) {
    if (log.debugEnabled) log.debug "connectActivityForms << ${origProc}, ${srcProc}, ${tgtProc}"
    // Decide which process definition to copy from.
    // Pick the original unless it has a different key.
    def oldProc = (origProc?.key == tgtProc.key)? origProc : srcProc
    // Create a map for convenience
    def oldActMap = [:]
    oldProc.activities.each {oldActMap[it.name] = it}
    if (log.debugEnabled) log.debug "connectActivityForms: ${oldProc?.toDump()}, ${oldActMap}"
    def replaceCount = 0
    tgtProc.activities.each {act ->
      def oldAct = oldActMap[act.name]
      if (oldAct) {
	def actFormdef = oldAct.activityFormdef
	if (actFormdef) {
	  // Do not replace an existing activity form definition
	  if (!act.activityFormdef) doConnectActivityForms(act, actFormdef)
	  replaceCount++
	}
      }
    }

    if (log.debugEnabled) log.debug "connectActivityForms >> replacements: ${replaceCount}"
  }

  /**
   * Continue the logic of the above method.
   * tgtAct must be the target activity definition
   * srcActFormdef must be the activity form definition of the
   * previous process definition.
   */
  private doConnectActivityForms(ActDef tgtAct, MtfActivityFormDefinition srcActFormdef) {
    def tgtActivityFormdef = MtfActivityFormDefinition.createFromActDef(tgtAct)
    // Copy the form connection
    tgtActivityFormdef.copyConnection(srcActFormdef)
    if (log.debugEnabled) log.debug "doConnectActivityForms: ${tgtActivityFormdef}"
    if (!tgtActivityFormdef.save()) {
      log.error "doConnectActivityForms: ${tgtActivityFormdef.errors.allErrors.join(',')}"
    }
  }

  /**
   * Update the form connection for an activity definition.
   * actDefInst must be the activity definition to update
   * acc must be a command object containing connection data
   */
  def updateActivityConnection(ActDef actDefInst, ActivityConnectionCommand acc) {
    if (log.debugEnabled) log.debug "updateActivityConnection << ${actDefInst}, ${acc}"

    // Find the activity connection database object (may be null)
    def activityFormdef = MtfActivityFormDefinition.get(actDefInst?.activityFormdef?.id)
    if (!activityFormdef) activityFormdef = MtfActivityFormDefinition.createFromActDef(actDefInst)

    // Create a form connection
    def activityConnection = new TaskFormSpec(acc, actDefInst)
    activityFormdef.assign(activityConnection)

    if (!activityFormdef.save()) {
      log.error "updateActivityConnection: ${activityFormdef.errors.allErrors.join(',')}"
    }

    if (log.debugEnabled) log.debug "updateActivityConnection >> ${activityFormdef}, cnx: ${activityConnection}"
  }

}

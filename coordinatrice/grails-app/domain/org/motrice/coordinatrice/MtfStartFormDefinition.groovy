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

import org.motrice.coordinatrice.pxd.PxdFormdefVer

class MtfStartFormDefinition implements Comparable {

  String authTypeReq

  Long formHandlerId

  String formConnectionKey

  // If the connection is a form (PxdFormdefVer), store its id here
  Long formdefId

  String procdefId

  String userDataXpath

  // Transient property, the procdef identified by procdefId
  Procdef tmpProcdef

  static mapping = {
    id column: 'start_form_definition_id'
    version false
    formHandlerId column: 'form_type_id'
    formdefId column: 'form_connection_dbid'
  }
  static transients = ['formdef', 'formHandlerType', 'tmpProcdef']
  static constraints = {
    authTypeReq nullable: false, maxSize: 255
    procdefId nullable: true, maxSize: 255
    formConnectionKey nullable: true, maxSize: 255
    formdefId nullable: true, min: 0L
    userDataXpath nullable: true, maxSize: 255
  }

  static final DEFAULT_FORM_HANDLER_TYPE = MtfFormType.ORBEON_TYPE_ID

  static create(Procdef procdef, PxdFormdefVer pfv) {
    new MtfStartFormDefinition(procdefId: procdef.uuid, authTypeReq: 'USERSESSION',
    formHandlerId: DEFAULT_FORM_HANDLER_TYPE, formdefId: pfv.id, formConnectionKey: pfv.path)
  }

  static final START_FORM_Q = 'select s.id, s.formHandlerId, s.formConnectionKey, ' +
    's.formdefId, s.procdefId ' +
    'from MtfStartFormDefinition s where s.procdefId=? order by s.formConnectionKey'

  /**
   * Get start form data for a process version.
   * Return a List of StartFormView.
   * NOTE: Big UNEXPLAINED problems with Hibernate here.
   */
  static List startFormList(String procdefId) {
    MtfStartFormDefinition.executeQuery(START_FORM_Q, [procdefId]).collect {tup ->
      new StartFormView(id: tup[0], formHandlerId: tup[1], formConnectionKey: tup[2],
      formdefId: tup[3], procdefId: tup[4])
    }
  }

  PxdFormdefVer getFormdef() {
    def result = PxdFormdefVer.get(formdefId)
    if (log.debugEnabled) log.debug "getFormdef >> ${result}"
    return result
  }

  MtfFormType getFormHandlerType() {
    MtfFormType.get(formHandlerId)
  }

  String toDisplay() {
    "[StartFormDef ${formdef?.path} ${procdefId}]"
  }

  String toString() {
    formdef?.path
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    (procdefId + formConnectionKey).hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof MtfStartFormDefinition) {
      def other = obj as MtfStartFormDefinition
      result = procdefId == other.procdefId
      if (result) result = (!formConnectionKey && !other.formConnectionKey) ||
      (formConnectionKey == other.formConnectionKey)
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (MtfStartFormDefinition)obj
    def result = procdefId.compareTo(other.procdefId)
    if (result == 0 && formConnectionKey) {
      result = formConnectionKey.compareTo(other.formConnectionKey)
    }

    return result
  }

}

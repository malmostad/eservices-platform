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

class MtfActivityFormDefinition {

  String procdefId

  String actdefId

  MtfFormType formHandlerType

  String formConnectionKey

  // If the connection involves an activity, store the activity name here
  String formConnectionLabel

  // If the connection is a form (PxdFormdefVer), store its id here
  Long formdefId

  static mapping = {
    id column: 'activity_form_definition_id'
    version false
    procdefId column: 'procdef_id'
    actdefId column: 'actdef_id'
    formHandlerType column: 'form_type_id'
    formConnectionKey column: 'form_connection_key'
    formdefId column: 'form_connection_dbid'
    formConnectionLabel column: 'form_connection_label'
  }
  static constraints = {
    procdefId nullable: true, maxSize: 255
    actdefId nullable: true, maxSize: 255
    formConnectionKey nullable: true, maxSize: 511
    formdefId nullable: true, min: 0L
    formConnectionLabel nullable: true
  }

  static MtfActivityFormDefinition createFromActDef(ActDef actDefInst) {
    def afd = new MtfActivityFormDefinition()
    afd.procdefId = actDefInst.process.uuid
    afd.actdefId = actDefInst.uuid
    return afd
  }

  def assign(TaskFormSpec taskFormSpec) {
    formHandlerType = MtfFormType.get(taskFormSpec.state)
    formConnectionKey = taskFormSpec.connectionKey
    def formdef = taskFormSpec.findFormdef()
    if (formdef) formdefId = formdef.id
    formConnectionLabel = taskFormSpec.connectionLabel
  }

  /**
   * Copy connection data from another activity form definition.
   */
  def copyConnection(MtfActivityFormDefinition other) {
    formHandlerType = other.formHandlerType
    formConnectionKey = other.formConnectionKey
    formdefId = other.formdefId
    formConnectionLabel = other.formConnectionLabel
  }

  String toString() {
    "[ActFormDef(${id}): ${procdefId}/${actdefId} cnx=${formConnectionLabel}]"
  }
}

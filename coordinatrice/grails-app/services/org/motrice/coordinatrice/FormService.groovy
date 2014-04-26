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

import org.motrice.coordinatrice.pxd.PxdFormdefVer

/**
 * Actions related to form selection.
 */
class FormService {

  static transactional = true

  // Injection magic
  def procdefService

  private static final log = LogFactory.getLog(this)

  /**
   * Create a list of forms that may be associated with an activity.
   * The list contains the latest published version of all forms.
   * @param actCnx must be the current activity connection
   * @return a map with the following entries:
   * formList: list of published forms (List of PxdFormdefVer)
   * selectedFormId: id of the form currently select in actCnx (Integer)
   */
  Map activityFormSelection(TaskFormSpec actCnx) {
    def formList = PxdFormdefVer.allPublishedForms()
    def selectedFormdefVer = null

    if (actCnx?.formState) {
      selectedFormdefVer = formList.find {it.path == actCnx.connectionKey}
    }

    return [formList: formList, selectedFormId: selectedFormdefVer?.id]
  }

  /**
   * Add process definitions (transient property) to a List of
   * MtfStartFormDefinition
   */
  List addProcdefs(List startFormList) {
    if (log.debugEnabled) log.debug "addProcdefs << List(${startFormList.size()})"
    startFormList.collect {item ->
      item.tmpProcdef = procdefService.findShallowProcdef(item.procdefId)
      return item
    }
  }

  /**
   * Get a start form selection list.
   * Throw an exception if the form_connection_dbid is null.
   */
  List startFormSelection() {
    def selection = []
    def formsInUse = new TreeSet()
    def formList = MtfStartFormDefinition.list()
    formList.each {tup ->
      if (tup.formdefId == null) {
	def msg = 'Null form_connection_dbid found in mtf_start_form_definition. ' +
	  'Manually initialized?'
	throw new ServiceException(msg, 'startform.initialization.problem')
      }
      formsInUse.add(tup.formdefId)
    }
    PxdFormdefVer.allPublishedForms().each {
      if (!formsInUse.contains(it.id)) selection.add(it)
    }

    return selection
  }

  /**
   * Check if a form definition is used as a start form
   */
  MtfStartFormDefinition findAsStartForm(PxdFormdefVer pfv) {
    MtfStartFormDefinition.findByFormdefId(pfv.id)
  }

}

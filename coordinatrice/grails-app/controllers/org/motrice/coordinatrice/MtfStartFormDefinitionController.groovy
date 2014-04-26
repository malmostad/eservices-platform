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

import org.springframework.dao.DataIntegrityViolationException

/**
 * Manage start forms -- not a plain crud controller.
 * Some fields of the domain never appear on screen.
 */
class MtfStartFormDefinitionController {

  def formService

  //static allowedMethods = [delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  /**
   * List start forms
   */
  def list(Integer max) {
    if (log.debugEnabled) log.debug "LIST ${params}"
    params.max = Math.min(max ?: 20, 100)
    params.offset = params.offset as Integer ?: 0
    if (!params.sort) params.sort = 'formConnectionKey'
    def mtfEntityList = MtfStartFormDefinition.list(params)
    def total = MtfStartFormDefinition.count()
    mtfEntityList = formService.addProcdefs(mtfEntityList)
    // Add a map to form definitions
    def formdefMap = mtfEntityList.inject([:]) {map, cnx ->
      map[cnx.id] = cnx.formdef.formdef
      return map
    }
    [startFormdefList: mtfEntityList, startFormdefTotal: total, formdefMap: formdefMap]
  }

  def show(Long id) {
    def startFormdefInst = MtfStartFormDefinition.get(id)
    if (!startFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
      redirect(action: "list")
      return
    }

    [startFormdefInst: startFormdefInst]
  }

  def delete(Long id) {
    if (log.debugEnabled) log.debug "DELETE: ${params}"
    def startFormdefInst = MtfStartFormDefinition.get(id)
    if (!startFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
      redirect(controller: 'procdef', action: 'edit', id: params.procdefId)
      return
    }

    def formName = startFormdefInst.formConnectionKey

    try {
      startFormdefInst.delete(flush: true)
      flash.message = message(code: 'startform.deleted.label', args: [formName])
      redirect(controller: 'procdef', action: 'edit', id: params.procdefId)
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
      redirect(controller: 'procdef', action: 'edit', id: params.procdefId)
    }
  }
}

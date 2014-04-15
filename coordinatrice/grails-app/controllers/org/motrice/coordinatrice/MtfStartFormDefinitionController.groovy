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

package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException

/**
 * Only used for deleting start forms from BnProcDef.edit
 */
class MtfStartFormDefinitionController {

  //static allowedMethods = [delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [mtfStartFormDefinitionInstList: MtfStartFormDefinition.list(params), mtfStartFormDefinitionInstTotal: MtfStartFormDefinition.count()]
  }

  def show(Long id) {
    def mtfStartFormDefinitionInst = MtfStartFormDefinition.get(id)
    if (!mtfStartFormDefinitionInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
      redirect(action: "list")
      return
    }

    [mtfStartFormDefinitionInst: mtfStartFormDefinitionInst]
  }

  def delete(Long id) {
    if (log.debugEnabled) log.debug "DELETE: ${params}"
    def mtfStartFormDefinitionInst = MtfStartFormDefinition.get(id)
    if (!mtfStartFormDefinitionInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
      redirect(controller: 'bnProcDef', action: 'edit', id: params.bnProcDefId)
      return
    }

    def formName = mtfStartFormDefinitionInst.formPath

    try {
      mtfStartFormDefinitionInst.delete(flush: true)
      flash.message = message(code: 'startform.deleted.label', args: [formName])
      redirect(controller: 'bnProcDef', action: 'edit', id: params.bnProcDefId)
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
      redirect(controller: 'bnProcDef', action: 'edit', id: params.bnProcDefId)
    }
  }
}

package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException
import org.motrice.coordinatrice.pxd.PxdFormdefVer
import org.motrice.coordinatrice.MtfStartFormDefinition

/**
 * Process definition controller tailored to Bonita
 */
class ProcDefController {

  def activityService

  static allowedMethods = [update: 'POST']

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    if (!params.sort) params.sort = 'uuid'
    [procDefInstList: ProcDef.list(params), procDefInstTotal: ProcDef.count()]
  }

  def show(Long id) {
    def procDefInst = ProcDef.get(id)
    if (!procDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procDef.label', default: 'ProcDef'), id])
      redirect(action: "list")
      return
    }

    [procDefInst: procDefInst]
  }

  /**
   * Edit the start form connection (nothing else may be changed)
   */
  def edit(Long id) {
    if (log.debugEnabled) log.debug "EDIT ${params}"
    def procDefInst = ProcDef.get(id)
    if (!procDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procDef.label', default: 'ProcDef'), id])
      redirect(action: "list")
      return
    }

    def selection = activityService.startFormSelection(procDefInst)
    
    [procDefInst: procDefInst, formList: selection]
  }

  /**
   * Add a form to the start forms
   */
  def update(StartFormSelectionCommand sfsc) {
    if (log.debugEnabled) {
      log.debug "UPDATE ${params}"
      log.debug "SFSC ${sfsc}"
    }

    def procDefInst = ProcDef.get(sfsc.id)
    if (!procDefInst) {
      redirect(action: "edit", id: procDefInst.id)
      return
    }

    def formId = params.form.id as Integer
    
    if (formId < 0) {
      flash.message = message(code: 'startform.selection.no.form.selected')
      redirect(action: "edit", id: procDefInst.id)
      return
    }

    if (log.debugEnabled) log.debug "update.procDefInst: ${procDefInst?.uuid}"

    // We have taken precautions to remove existing start forms from the selection
    // If we add an existing one anyway there will be a DataIntegrityViolationException
    def inUse = activityService.checkStartFormInUse(sfsc.form)
    if (inUse) {
      flash.message = message(code: 'startform.selection.form.in.use', args: [sfsc.formPath])
      redirect(action: "edit", id: procDefInst.id)
      return
    }

    def startForm = new MtfStartFormDefinition(processDefinitionUuid: procDefInst.uuid,
    authTypeReq: 'USERSESSION', formPath: sfsc.formPath)

    if (log.debugEnabled) log.debug "update.startForm: ${startForm}"

    if (!startForm.save(flush: true)) {
      redirect(action: "edit", id: procDefInst.id)
      return
    }

    flash.message = message(code: 'startform.updated.label', args: [startForm])
    redirect(action: "edit", id: procDefInst.id)
  }

}

class StartFormSelectionCommand {
  Integer id
  PxdFormdefVer form

  String getFormPath() {
    form.path
  }

  String toString() {
    "[StartFormCmd(process ${id}): ${form}]"
  }
}

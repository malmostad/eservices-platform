package org.motrice.coordinatrice.bonita

import org.springframework.dao.DataIntegrityViolationException
import org.motrice.coordinatrice.pxd.PxdFormdefVer
import org.motrice.coordinatrice.MtfStartFormDefinition

class BnProcDefController {

  def activityService

  static allowedMethods = [update: 'POST']

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    if (!params.sort) params.sort = 'uuid'
    [bnProcDefInstList: BnProcDef.list(params), bnProcDefInstTotal: BnProcDef.count()]
  }

  def show(Long id) {
    def bnProcDefInst = BnProcDef.get(id)
    if (!bnProcDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnProcDef.label', default: 'BnProcDef'), id])
      redirect(action: "list")
      return
    }

    [bnProcDefInst: bnProcDefInst]
  }

  /**
   * Edit the start form connection (nothing else may be changed)
   */
  def edit(Long id) {
    if (log.debugEnabled) log.debug "EDIT ${params}"
    def bnProcDefInst = BnProcDef.get(id)
    if (!bnProcDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnProcDef.label', default: 'BnProcDef'), id])
      redirect(action: "list")
      return
    }

    def selection = activityService.startFormSelection(bnProcDefInst)
    
    [bnProcDefInst: bnProcDefInst, formList: selection]
  }

  /**
   * Add a form to the start forms
   */
  def update(StartFormSelectionCommand sfsc) {
    if (log.debugEnabled) {
      log.debug "UPDATE ${params}"
      log.debug "SFSC ${sfsc}"
    }

    def bnProcDefInst = BnProcDef.get(sfsc.id)
    if (!bnProcDefInst) {
      redirect(action: "edit", id: bnProcDefInst.id)
      return
    }

    def formId = params.form.id as Integer
    
    if (formId < 0) {
      flash.message = message(code: 'startform.selection.no.form.selected')
      redirect(action: "edit", id: bnProcDefInst.id)
      return
    }

    if (log.debugEnabled) log.debug "update.bnProcDefInst: ${bnProcDefInst?.uuid}"

    // We have taken precautions to remove existing start forms from the selection
    // If we add an existing one anyway there will be a DataIntegrityViolationException
    def inUse = activityService.checkStartFormInUse(sfsc.form)
    if (inUse) {
      flash.message = message(code: 'startform.selection.form.in.use', args: [sfsc.formPath])
      redirect(action: "edit", id: bnProcDefInst.id)
      return
    }

    def startForm = new MtfStartFormDefinition(processDefinitionUuid: bnProcDefInst.uuid,
    authTypeReq: 'USERSESSION', formPath: sfsc.formPath)

    if (log.debugEnabled) log.debug "update.startForm: ${startForm}"

    if (!startForm.save(flush: true)) {
      redirect(action: "edit", id: bnProcDefInst.id)
      return
    }

    flash.message = message(code: 'startform.updated.label', args: [startForm])
    redirect(action: "edit", id: bnProcDefInst.id)
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

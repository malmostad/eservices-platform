package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException
import org.motrice.coordinatrice.pxd.PxdFormdefVer
import org.motrice.coordinatrice.MtfStartFormDefinition

/**
 * Process definition controller tailored to Bonita
 */
class ProcdefController {

  def formService
  def procdefService
  def processEngineService

  static allowedMethods = [update: 'POST']

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    if (log.debugEnabled) log.debug "LIST ${params}"
    params.max = Math.min(max ?: 10, 100)
    if (!params.sort) params.sort = 'uuid'
    // TODO: Currently no paging, all process definitions are shown on a single page
    def procdefInstList = procdefService.allProcessDefinitions()
    [procdefInstList: procdefInstList, procdefInstTotal: procdefInstList.size()]
  }

  def show() {
    if (log.debugEnabled) log.debug "SHOW ${params}"
    def uuid = params.id
    def procdefInst = procdefService.findProcessDefinition(uuid)
    if (!procdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "list")
      return
    }

    def state = procdefInst.state
    [procdefInst: procdefInst, editable: state.editable]
  }

  def diagramDownload() {
    if (log.debugEnabled) log.debug "DIAGRAM ${params}"
    def uuid = params.id
    def diagramMap = processEngineService.findProcessDiagram(uuid)
    if (!diagramMap.procdef) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "show", id: uuid)
      return
    }

    def procdef = diagramMap.procdef
    response.contentType = diagramMap.ctype
    response.setHeader('Content-Disposition', "filename=${procdef.diagramResourceName}")
    response.outputStream << diagramMap.bytes
  }

  /**
   * Edit the start form connection (nothing else may be changed)
   */
  def edit() {
    if (log.debugEnabled) log.debug "EDIT ${params}"
    def uuid = params.id
    def procdefInst = procdefService.findProcessDefinition(uuid)
    if (!procdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "list")
      return
    } else if (!procdefInst.state.editable) {
      flash.message = message(code: 'procdef.state.not.editable', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "show", id: uuid)
      return
    }

    def selection = formService.startFormSelection()
    [procdefInst: procdefInst, formList: selection]
  }

  /**
   * Add a form to the start forms
   */
  def update(StartFormSelectionCommand sfsc) {
    if (log.debugEnabled) {
      log.debug "UPDATE ${params}"
      log.debug "SFSC ${sfsc}"
    }

    def procdefInst = procdefService.findProcessDefinition(sfsc.id)
    if (!procdefInst) {
      redirect(action: "edit", id: procdefInst.uuid)
      return
    } else if (!procdefInst.state.editable) {
      flash.message = message(code: 'procdef.state.not.editable', args: [message(code: 'procdef.label', default: 'Procdef'), id])
      redirect(action: "list")
      return
    }

    def formId = params.form.id as Integer
    
    if (formId < 0) {
      flash.message = message(code: 'startform.selection.no.form.selected')
      redirect(action: "edit", id: procdefInst.uuid)
      return
    }

    if (log.debugEnabled) log.debug "update.procdefInst: ${procdefInst?.uuid}"

    // We have taken precautions to remove existing start forms from the selection
    // If we add an existing one anyway there will be a DataIntegrityViolationException
    def inUse = formService.checkStartFormInUse(sfsc.form)
    if (inUse) {
      flash.message = message(code: 'startform.selection.form.in.use', args: [sfsc.formPath])
      redirect(action: "edit", id: procdefInst.uuid)
      return
    }

    def startForm = new MtfStartFormDefinition(processDefinitionId: procdefInst.uuid,
    authTypeReq: 'USERSESSION', formPath: sfsc.formPath)

    if (log.debugEnabled) log.debug "update.startForm: ${startForm}"

    if (!startForm.save(flush: true)) {
      redirect(action: "edit", id: procdefInst.uuid)
      return
    }

    flash.message = message(code: 'startform.updated.label', args: [startForm])
    redirect(action: "edit", id: procdefInst.uuid)
  }

  /**
   * Edit the start form connection (nothing else may be changed)
   */
  def newversion() {
    if (log.debugEnabled) log.debug "NEW VERSION ${params}"
    def uuid = params.id
    def procdefList = null

    try {
      procdefList = processEngineService.duplicateProcdef(uuid)
    } catch (ServiceException exc) {
      log.error "newversion ${exc?.message}"
      if (exc.key) {
	flash.message = message(code: exc.key, args: exc.args ?: [])
	redirect(action: "list")
	return
      }
    }

    flash.message = message(code: 'procdef.newversion.count', args: [procdefList.size()])
    redirect(action: 'list')
  }

  /**
   * Withdraw the process from use, set it in state Retired
   */
  def retire() {
    if (log.debugEnabled) log.debug "RETIRE ${params}"
    def procdefInst = procdefService.findProcessDefinition(params.id)
    if (!procdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), id])
      redirect(action: "list")
      return
    } else if (!procdefInst.state.editable) {
      flash.message = message(code: 'procdef.state.not.editable', args: [message(code: 'procdef.label', default: 'Procdef'), id])
      redirect(action: "list")
      return
    }

    def selection = formService.startFormSelection()
    [procdefInst: procdefInst, formList: selection]
  }

}

class StartFormSelectionCommand {
  String id
  PxdFormdefVer form

  String getFormPath() {
    form.path
  }

  String toString() {
    "[StartFormCmd(process ${id}): ${form}]"
  }
}

package org.motrice.coordinatrice.bonita

import org.springframework.dao.DataIntegrityViolationException
import org.motrice.coordinatrice.ActivityConnection
import org.motrice.coordinatrice.MtfActivityFormDefinition
import org.motrice.coordinatrice.pxd.PxdFormdefVer

class BnActDefController {

  def activityService

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [bnActDefInstList: BnActDef.list(params), bnActDefInstTotal: BnActDef.count()]
  }

  def show(Long id) {
    def bnActDefInst = BnActDef.get(id)
    if (!bnActDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), id])
      redirect(action: "list")
      return
    }

    [bnActDefInst: bnActDefInst]
  }

  /**
   * Edit the activity connection (nothing else may be changed)
   * The activity connection is a self-contained object, MtfActivityFormDefinition
   */
  def edit(Long id) {
    if (log.debugEnabled) log.debug "EDIT ${params}"
    def bnActDefInst = BnActDef.get(id)
    if (!bnActDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), id])
      redirect(action: "list")
      return
    }

    // Create a list containing the activities of this process, except this activity
    def activityList = []
    bnActDefInst.process.activities.each {activity ->
      if (activity != bnActDefInst) activityList << activity
    }

    // Note that activityFormdef may be null, no error
    def activityConnection = new ActivityConnection(bnActDefInst,
						    bnActDefInst?.activityFormdef?.formPath)
    def formMap = activityService.activityFormSelection(activityConnection)
    
    [bnActDefInst: bnActDefInst, activityList: activityList,
    activityConnection: activityConnection, formList: formMap.formList,
    selectedFormId: formMap.selectedFormId]
  }

  /**
   * Update the activity connection.
   * The only possibly updated object is MtfActivityFormDefinition
   */
  def update(ActivityConnectionCommand acc) {
    if (log.debugEnabled) {
      log.debug "UPDATE ${params}"
      log.debug "ACC ${acc}"
    }
    
    def bnActDefInst = BnActDef.get(acc.id)
    if (!bnActDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), id])
      redirect(action: "list")
      return
    }

    // Find the activity connection database object (may be null)
    def activityFormdef = MtfActivityFormDefinition.get(bnActDefInst?.activityFormdef?.id)
    if (!activityFormdef) {
      activityFormdef =
	new MtfActivityFormDefinition(activityDefinitionUuid: bnActDefInst.uuid)
    }

    def activityConnection = new ActivityConnection(acc, bnActDefInst)
    activityFormdef.formPath = activityConnection.toString()

    if (log.debugEnabled) log.debug "update.activityFormdef: ${activityFormdef}"

    // DEBUG
    if (!activityFormdef.save(flush: true, failOnError: true)) {
      redirect(controller: 'bnProcDef', action: "show", id: bnActDefInst.process.id)
      render(view: "edit", id: bnActDefInst.id)
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'mtfActivityFormDefinition.label', default: 'ActivityFormDefinition'), activityFormdef.id])
    redirect(controller: 'bnProcDef', action: "show", id: bnActDefInst.process.id)
  }

}

class ActivityConnectionCommand { 
  Integer id
  Integer connectionState
  PxdFormdefVer form
  BnActDef activity

  String toString() {
    "[ACC: id=${id} cst=${connectionState} form=${form} act=${activity?.uuid}]"
  }

}
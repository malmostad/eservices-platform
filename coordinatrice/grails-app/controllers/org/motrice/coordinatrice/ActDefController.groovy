package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException
import org.motrice.coordinatrice.ActivityConnection
import org.motrice.coordinatrice.MtfActivityFormDefinition
import org.motrice.coordinatrice.pxd.PxdFormdefVer

/**
 * Activity definition controller
 */
class ActDefController {

  def formService
  def processEngineService

  def show() {
    if (log.debugEnabled) log.debug "SHOW ${params}"
    def actDefInst = processEngineService.findActivityDefinition(params.id)
    if (!actDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'actDef.label', default: 'ActDef'), id])
      redirect(controller: 'procdef', action: 'list')
      return
    }

    [actDefInst: actDefInst]
  }

  /**
   * Edit the activity connection (nothing else may be changed)
   * The activity connection is a self-contained object, MtfActivityFormDefinition
   */
  def edit() {
    if (log.debugEnabled) log.debug "EDIT ${params}"
    def actDefInst = processEngineService.findActivityDefinition(params.id)
    if (!actDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'actDef.label', default: 'ActDef'), id])
      redirect(controller: 'procdef', action: 'list')
      return
    } else if (!actDefInst.process.state.editable) {
      flash.message = message(code: 'procdef.state.not.editable', args: [message(code: 'procdef.label', default: 'Procdef'), id])
      redirect(action: "list")
      return
    }

    // Create a list containing the activities of this process, except this activity
    // Only human activities are interesting in this context
    def activityList = []
    actDefInst.process.userActivities.each {activity ->
      if (activity != actDefInst) activityList << activity
    }

    // Note that activityFormdef may be null, no error
    def activityConnection = new ActivityConnection(actDefInst,
						    actDefInst?.activityFormdef?.formPath)
    def formMap = formService.activityFormSelection(activityConnection)
    
    [actDefInst: actDefInst, activityList: activityList,
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
    
    def actDefInst = processEngineService.findActivityDefinition(params.id)
    if (!actDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'actDef.label', default: 'ActDef'), id])
      redirect(controller: 'procdef', action: 'list')
      return
    } else if (!actDefInst.process.state.editable) {
      flash.message = message(code: 'procdef.state.not.editable', args: [message(code: 'procdef.label', default: 'Procdef'), id])
      redirect(action: "list")
      return
    }

    // Find the activity connection database object (may be null)
    def activityFormdef = MtfActivityFormDefinition.get(actDefInst?.activityFormdef?.id)
    if (!activityFormdef) {
      activityFormdef =
	new MtfActivityFormDefinition(processDefinitionId: actDefInst.process.uuid,
	activityDefinitionId: actDefInst.uuid)
    }

    def activityConnection = new ActivityConnection(acc, actDefInst)
    activityFormdef.formPath = activityConnection.toString()

    if (log.debugEnabled) log.debug "update.activityFormdef: ${activityFormdef}"

    if (!activityFormdef.save(flush: true)) {
      render(view: "edit", id: actDefInst.id)
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'mtfActivityFormDefinition.label', default: 'ActivityFormDefinition'), activityFormdef.id])
    redirect(controller: 'procdef', action: "show", id: actDefInst.process.uuid)
  }

}

class ActivityConnectionCommand { 
  String id
  Integer connectionState
  PxdFormdefVer form

  String toString() {
    "[ACC: id=${id} cst=${connectionState} form=${form}]"
  }

}
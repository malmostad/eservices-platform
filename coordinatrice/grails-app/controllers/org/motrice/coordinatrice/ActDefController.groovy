package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException
import org.motrice.coordinatrice.ActivityConnection
import org.motrice.coordinatrice.MtfActivityFormDefinition
import org.motrice.coordinatrice.pxd.PxdFormdefVer

/**
 * Activity definition controller
 */
class ActDefController {

  def activityFormdefService
  def formService
  def processEngineService

  def show() {
    if (log.debugEnabled) log.debug "SHOW ${params}"
    def id = new ActDefId(params.id)
    def actDefInst = processEngineService.findActivityDefinition(id)
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
    def id = new ActDefId(params.id)
    def actDefInst = processEngineService.findActivityDefinition(id)
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
    def activityConnection = new TaskFormSpec(actDefInst, actDefInst?.activityFormdef)
    def formMap = formService.activityFormSelection(activityConnection)
    
    [actDefInst: actDefInst, activityList: activityList,
    activityConnection: activityConnection, formList: formMap.formList,
    selectedFormId: formMap.selectedFormId]
  }

  /**
   * Update the activity connection.
   * The only possibly updated object is MtfActivityFormDefinition
   * The activity definition id comes as "procdef@actdef" in params.id.
   */
  def update(ActivityConnectionCommand acc) {
    if (log.debugEnabled) log.debug "UPDATE ${params}"
    
    def actDefInst = processEngineService.findActivityDefinition(acc.id)
    if (!actDefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'actDef.label', default: 'ActDef'), acc.id])
      redirect(controller: 'procdef', action: 'list')
      return
    } else if (!actDefInst.process.state.editable) {
      flash.message = message(code: 'procdef.state.not.editable', args: [message(code: 'procdef.label', default: 'Procdef'), acc.id])
      redirect(action: "list")
      return
    }

    acc.assignOtherActivity(params?.sign?.id, params?.notify?.id)
    if (log.debugEnabled) log.debug "ACC ${acc}"
    activityFormdefService.updateActivityConnection(actDefInst, acc)
    redirect(controller: 'procdef', action: "show", id: actDefInst.process.uuid)
  }

}

class ActivityConnectionCommand { 
  // Data binding will construct an ActDefId from the process@activity string.
  // This is the activity that owns a connection.
  ActDefId id
  // Id of the activity to connect to, or null
  String otherActivityId
  Integer connectionState
  PxdFormdefVer form

  // Pick up the right "other" activity id from form input.
  def assignOtherActivity(String signActId, String notifyActId) {
    if (connectionState == TaskFormSpec.SIGN_ACTIVITY_STATE) {
      otherActivityId = signActId
    } else if (connectionState == TaskFormSpec.NOTIFY_ACTIVITY_STATE) {
      otherActivityId = notifyActId
    }
  }

  String toString() {
    "[ACC: id=${id} cst=${connectionState} other=${otherActivityId} form=${form}]"
  }

}

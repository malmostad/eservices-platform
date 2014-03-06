package org.motrice.coordinatrice

class MtfActivityFormDefinition {

  String processDefinitionId

  String activityDefinitionId

  String formPath

  MtfFormType formHandlerType

  String formConnectionKey

  // If the connection involves an activity, store the activity name here
  String formConnectionLabel

  // If the connection is a form (PxdFormdefVer), store its id here
  Long formdefId

  static mapping = {
    id column: 'activityformdefinitionid'
    version false
    processDefinitionId column: 'processdefinitionuuid'
    activityDefinitionId column: 'activitydefinitionuuid'
    formPath column: 'formpath'
    formHandlerType column: 'formtypeid'
    formConnectionKey column: 'form_connection_key'
    formdefId column: 'form_connection_dbid'
    formConnectionLabel column: 'form_connection_label'
  }
  static constraints = {
    processDefinitionId nullable: true, maxSize: 255
    activityDefinitionId nullable: true, maxSize: 255
    formPath nullable: true, maxSize: 255
    formConnectionKey nullable: true, maxSize: 400
    formdefId nullable: true, min: 0L
    formConnectionLabel nullable: true
  }

  static MtfActivityFormDefinition createFromActDef(ActDef actDefInst) {
    def afd = new MtfActivityFormDefinition()
    afd.processDefinitionId = actDefInst.process.uuid
    afd.activityDefinitionId = actDefInst.uuid
    return afd
  }

  def assign(TaskFormSpec taskFormSpec) {
    formHandlerType = MtfFormType.get(taskFormSpec.state)
    formConnectionKey = taskFormSpec.connectionKey
    def formdef = taskFormSpec.findFormdef()
    if (formdef) formdefId = formdef.id
    formConnectionLabel = taskFormSpec.connectionLabel
  }

  String toString() {
    "[ActFormDef(${id}): ${processDefinitionId}/${activityDefinitionId} formpath=${formPath}]"
  }
}

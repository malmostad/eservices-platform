package org.motrice.coordinatrice

class MtfActivityFormDefinition {

  String processDefinitionId

  String activityDefinitionId

  String formPath

  MtfFormType formType

  String formdefKey

  Long formdefDbid

  static mapping = {
    id column: 'activityformdefinitionid'
    version false
    processDefinitionId column: 'processdefinitionuuid'
    activityDefinitionId column: 'activitydefinitionuuid'
    formPath column: 'formpath'
    formType column: 'formtypeid'
    formdefKey column: 'formdefinitionkey'
  }
  static constraints = {
    processDefinitionId nullable: true, maxSize: 255
    activityDefinitionId nullable: true, maxSize: 255
    formPath nullable: true, maxSize: 255
    formdefKey nullable: true, maxSize: 400
    formdefDbid nullable: true, min: 0L
  }

  static MtfActivityFormDefinition createFromActDef(ActDef actDefInst) {
    def afd = new MtfActivityFormDefinition()
    afd.processDefinitionId = actDefInst.process.uuid
    afd.activityDefinitionId = actDefInst.uuid
    return afd
  }

  def assign(TaskFormSpec taskFormSpec) {
    formType = MtfFormType.get(taskFormSpec.state)
    formdefKey = taskFormSpec.path
    def formdef = taskFormSpec.findFormdef()
    if (formdef) formdefDbid = formdef.id
  }

  String toString() {
    "[ActFormDef(${id}): ${processDefinitionId}/${activityDefinitionId} formpath=${formPath}]"
  }
}

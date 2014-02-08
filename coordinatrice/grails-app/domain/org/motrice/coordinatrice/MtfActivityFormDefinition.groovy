package org.motrice.coordinatrice

class MtfActivityFormDefinition {

  String processDefinitionId

  String activityDefinitionId

  String formPath

  static mapping = {
    id column: 'activityformdefinitionid'
    version false
    processDefinitionId column: 'processdefinitionuuid'
    activityDefinitionId column: 'activitydefinitionuuid'
    formPath column: 'formpath'
  }
  static constraints = {
    processDefinitionId nullable: true, maxSize: 255
    activityDefinitionId nullable: true, maxSize: 255
    formPath nullable: true, maxSize: 255
  }

  static MtfActivityFormDefinition createFromActDef(ActDef actDefInst) {
    def afd = new MtfActivityFormDefinition()
    afd.processDefinitionId = actDefInst.process.uuid
    afd.activityDefinitionId = actDefInst.uuid
    return afd
  }

  String toString() {
    "[ActFormDef(${id}): ${processDefinitionId}/${activityDefinitionId} formpath=${formPath}]"
  }
}

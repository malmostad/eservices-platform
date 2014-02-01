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

  String toString() {
    "[ActFormDef: ${id}/${activityDefinitionId} formpath=${formPath}]"
  }
}

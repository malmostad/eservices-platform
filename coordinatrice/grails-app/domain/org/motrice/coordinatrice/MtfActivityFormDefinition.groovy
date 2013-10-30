package org.motrice.coordinatrice

class MtfActivityFormDefinition {

  String activityDefinitionUuid

  String formPath

  static mapping = {
    id column: 'activityformdefinitionid'
    version false
    activityDefinitionUuid column: 'activitydefinitionuuid'
    formPath column: 'formpath'
  }
  static constraints = {
    activityDefinitionUuid nullable: true, maxSize: 255
    formPath nullable: true, maxSize: 255
  }

  String toString() {
    "[ActFormDef: ${id}/${activityDefinitionUuid} formpath=${formPath}]"
  }
}

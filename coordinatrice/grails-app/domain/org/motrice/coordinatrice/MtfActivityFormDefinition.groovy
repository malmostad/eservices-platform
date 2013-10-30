package org.motrice.coordinatrice

class MtfActivityFormDefinition {

  String activityDefinitionUuid

  String formPath

  static belongsTo = [startForm: MtfStartFormDefinition]
  static mapping = {
    id column: 'activityformdefinitionid'
    version false
    activityDefinitionUuid column: 'activitydefinitionuuid'
    formPath column: 'formpath'
    startForm column: 'startformdefinitionid'
  }
  static constraints = {
    activityDefinitionUuid nullable: true, maxSize: 255
    formPath nullable: true, maxSize: 255
    startForm nullable: true
  }

  String toString() {
    "[ActFormDef: ${id}/${activityDefinitionUuid} formpath=${formPath}]"
  }
}

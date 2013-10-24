package org.motrice.coordinatrice

class MtfProcessActivityFormInstance {

  String activityInstanceUuid

  String formDocId

  String formPath

  String processInstanceUuid

  Date submitted

  String userid

  static belongsTo = [startForm: MtfStartFormDefinition]
  static mapping = {
    id column: 'processactivityforminstanceid'
    version false
    activityInstanceUuid column: 'activityinstanceuuid'
    formDocId column: 'formdocid'
    formPath column: 'formpath'
    processInstanceUuid column: 'processinstanceuuid'
    startForm column: 'startformdefinitionid'
  }
  static constraints = {
    activityInstanceUuid nullable: true, maxSize: 255
    formDocId nullable: false, maxSize: 255, unique: true
    formPath nullable: false, maxSize: 255
    processInstanceUuid nullable: true, maxSize: 255
    submitted nullable: true
    userid nullable: false, maxSize: 255
  }
}

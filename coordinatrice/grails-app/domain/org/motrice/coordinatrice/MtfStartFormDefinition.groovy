package org.motrice.coordinatrice

class MtfStartFormDefinition {

  String authTypeReq

  String formPath

  String processDefinitionUuid

  String userDataXpath

  static hasMany = [
  activityFormDefs: MtfActivityFormDefinition,
  activityFormInst: MtfProcessActivityFormInstance
  ]
  static mapping = {
    id column: 'startformdefinitionid'
    version false
    authTypeReq column: 'authtypereq'
    formPath column: 'formpath'
    processDefinitionUuid column: 'processdefinitionuuid'
    userDataXpath column: 'userdataxpath'
  }
  static constraints = {
    authTypeReq nullable: false, maxSize: 255
    formPath nullable: false, maxSize: 255, unique: true
    processDefinitionUuid nullable: true, maxSize: 255
    userDataXpath nullable: true, maxSize: 255
  }
}

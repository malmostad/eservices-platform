package org.motrice.coordinatrice

import org.motrice.coordinatrice.bonita.BnProcDef
import org.motrice.coordinatrice.pxd.PxdFormdefVer

class MtfStartFormDefinition implements Comparable {

  String authTypeReq

  String formPath

  String processDefinitionUuid

  String userDataXpath

  static mapping = {
    id column: 'startformdefinitionid'
    version false
    authTypeReq column: 'authtypereq'
    formPath column: 'formpath'
    processDefinitionUuid column: 'processdefinitionuuid'
    userDataXpath column: 'userdataxpath'
  }
  static transients = ['formdef']
  static constraints = {
    authTypeReq nullable: false, maxSize: 255
    formPath nullable: false, maxSize: 255, unique: true
    processDefinitionUuid nullable: true, maxSize: 255
    userDataXpath nullable: true, maxSize: 255
  }

  PxdFormdefVer getFormdef() {
    def result = PxdFormdefVer.findByPath(formPath)
    if (log.debugEnabled) log.debug "getFormdef >> ${result}"
    return result
  }

  String toString() {
    formPath
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    formPath.hashCode()
  }

  boolean equals(Object obj) {
    (obj instanceof MtfStartFormDefinition) && ((MtfStartFormDefinition)obj).formPath == formPath
  }

  int compareTo(Object obj) {
    def other = (MtfStartFormDefinition)obj
    return formPath.compareTo(obj.formPath)
  }

}

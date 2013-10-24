package org.motrice.coordinatrice.bonita

class BnProcDef {

  String uuid

  String state

  String name

  String label

  String vno

  String type

  Long deployedMillis

  Long undeployedMillis

  static hasMany = [activities: BnActDef]
  static mapping = {
    datasource 'bonita'
    id column: 'dbid_'
    version false
    uuid column: 'proc_uuid_'
    state column: 'state_'
    name column: 'name_'
    label column: 'label_'
    vno column: 'version_'
    type column: 'type_'
    deployedMillis column: 'deployed_date_'
    undeployedMillis column: 'undeployed_date_'
  }
  static transients = ['deployedDate', 'undeployedDate']
  static constraints = {
    uuid nullable: true, maxSize: 255, unique: true
    state nullable: true, maxSize: 255
    name nullable: true, maxSize: 255
    label nullable: true, maxSize: 255
    vno nullable: true, maxSize: 255
    type nullable: true, maxSize: 255
  }

  Date getDeployedDate() {
    new java.util.Date(deployedMillis)
  }

  Date getUndeployedDate() {
    new java.util.Date(undeployedMillis)
  }

  String toString() {
    "[Process: ${uuid} ${type}/${state}]"
  }
}

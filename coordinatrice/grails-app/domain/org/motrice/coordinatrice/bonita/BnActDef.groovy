package org.motrice.coordinatrice.bonita

class BnActDef {

  String uuid

  String name

  String label

  String type

  String processUuid

  static belongsTo = [process: BnProcDef]
  static mapping = {
    datasource 'bonita'
    id column: 'dbid_'
    version false
    uuid column: 'act_uuid_'
    name column: 'name_'
    label column: 'label_'
    type column: 'type_'
    processUuid column: 'process_uuid_'
    process column: 'process_dbid_'
  }
  static constraints = {
    uuid nullable: true, maxSize: 255, unique: true
    name nullable: true, maxSize: 255
    label nullable: true, maxSize: 255
    type nullable: true, maxSize: 255
    processUuid nullable: true, maxSize: 255
  }

  String toString() {
    "[Activity: ${uuid}]"
  }
}

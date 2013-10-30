package org.motrice.coordinatrice.bonita

import org.motrice.coordinatrice.MtfActivityFormDefinition

class BnActDef implements Comparable {

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
  static transients = ['activityFormdef']
  static constraints = {
    uuid nullable: true, maxSize: 255, unique: true
    name nullable: true, maxSize: 255
    label nullable: true, maxSize: 255
    type nullable: true, maxSize: 255
    processUuid nullable: true, maxSize: 255
  }

  MtfActivityFormDefinition getActivityFormdef() {
    MtfActivityFormDefinition.findByActivityDefinitionUuid(uuid)
  }

  String toString() {
    label
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    uuid.hashCode()
  }

  boolean equals(Object obj) {
    (obj instanceof BnActDef) && ((BnActDef)obj).uuid == uuid
  }

  // This comparison is different from comparing paths.
  // Highest version and highest draft number comes first.
  int compareTo(Object obj) {
    def other = (BnActDef)obj
    return uuid.compareTo(obj.uuid)
  }

}

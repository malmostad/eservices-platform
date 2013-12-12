package org.motrice.coordinatrice

import org.motrice.coordinatrice.MtfActivityFormDefinition

class ActDef implements Comparable {

  String uuid

  String name

  String label

  String type

  String processUuid

  // Not a database object, never to be persisted
  static mapWith = 'none'
  static belongsTo = [process: ProcDef]
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
    (obj instanceof ActDef) && ((ActDef)obj).uuid == uuid
  }

  // This comparison is different from comparing paths.
  // Highest version and highest draft number comes first.
  int compareTo(Object obj) {
    def other = (ActDef)obj
    return uuid.compareTo(obj.uuid)
  }

}

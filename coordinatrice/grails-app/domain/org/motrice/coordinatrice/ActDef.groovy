package org.motrice.coordinatrice

import org.motrice.coordinatrice.MtfActivityFormDefinition

/**
 * An activity definition.
 * Currently also a task definition.
 */
class ActDef implements Comparable {

  // Usually far from a uuid, a string that identifies this activity
  // definition in the process definition.
  String uuid

  // The name of this activity as used in human communication
  String name

  // The type of this task. I.e. we assume the activity is a task.
  TaskType type

  // Documentation?
  String documentation

  // Not a database object, never to be persisted
  static mapWith = 'none'
  static belongsTo = [process: Procdef]
  static transients = ['activityFormdef', 'fullId', 'userTask']
  static constraints = {
    uuid maxSize: 64
    name nullable: true, maxSize: 255
    type nullable: true
    documentation nullable: true
  }

  /**
   * Find another activity definition in the same process.
   * otherId must be the uuid of the activity to find.
   */
  ActDef findSibling(String otherId) {
    process.activities.find {it.uuid == otherId}
  }

  MtfActivityFormDefinition getActivityFormdef() {
    def ref = fullId
    MtfActivityFormDefinition.
    findByProcessDefinitionIdAndActivityDefinitionId(ref.procId, ref.actId)
  }

  /**
   * The full id of an activity contains the process definition id
   */
  ActDefId getFullId() {
    new ActDefId(process?.uuid, uuid)
  }

  boolean isUserTask() {
    type?.id == TaskType.TYPE_USER_ID
  }

  String toDisplay() {
    "[ActDef ${process?.uuid}@${uuid}: ${name}, ${type}]"
  }

  String toString() {
    name
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    uuid.hashCode()
  }

  boolean equals(Object obj) {
    (obj instanceof ActDef) && ((ActDef)obj).uuid == uuid
  }

  int compareTo(Object obj) {
    def other = (ActDef)obj
    return uuid.compareTo(obj.uuid)
  }

}

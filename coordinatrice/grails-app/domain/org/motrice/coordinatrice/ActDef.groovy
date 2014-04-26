/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
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
    MtfActivityFormDefinition.findByProcdefIdAndActdefId(ref.procId, ref.actId)
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

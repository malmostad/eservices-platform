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


/**
 * Class matching Activiti suspension type except that the id is Long
 */
class TaskType {

  // Message resource
  String res

  // Default message
  String defaultMessage

  static mapping = {
    table 'crd_task_type'
    id generator: 'assigned'
    cache usage: 'read-only'
  }
  static constraints = {
    res blank: false, maxSize: 80
    defaultMessage blank: false, maxSize: 80
  }

  static final Long TYPE_BUSINESS_RULE_ID = 1L
  static final Long TYPE_MANUAL_ID = 2L
  static final Long TYPE_RECEIVE_ID = 3L
  static final Long TYPE_SCRIPT_ID = 4L
  static final Long TYPE_SEND_ID = 5L
  static final Long TYPE_SERVICE_ID = 6L
  static final Long TYPE_USER_ID = 7L

  static final String TYPE_BUSINESS_RULE_RES = 'taskType.businessRule.label'
  static final String TYPE_MANUAL_RES = 'taskType.manual.label'
  static final String TYPE_RECEIVE_RES = 'taskType.receive.label'
  static final String TYPE_SCRIPT_RES = 'taskType.script.label'
  static final String TYPE_SEND_RES = 'taskType.send.label'
  static final String TYPE_SERVICE_RES = 'taskType.service.label'
  static final String TYPE_USER_RES = 'taskType.user.label'

  static final String TYPE_BUSINESS_RULE_DEF = 'Business rule'
  static final String TYPE_MANUAL_DEF = 'Manual'
  static final String TYPE_RECEIVE_DEF = 'Receive'
  static final String TYPE_SCRIPT_DEF = 'Script'
  static final String TYPE_SEND_DEF = 'Send'
  static final String TYPE_SERVICE_DEF = 'Service'
  static final String TYPE_USER_DEF = 'User'

  // Method for making sure the types exist at bootstrap time
  // No other types may be created
  static TaskType createType(Long identity, String resource, String defMsg) {
    if (!TaskType.get(identity)) {
      def type = new TaskType(res: resource, defaultMessage: defMsg)
      type.id = identity
      type.save(failOnError: true)
    }
  }

  String toString() {
    "[TaskType: ${id}: ${defaultMessage}]"
  }

}

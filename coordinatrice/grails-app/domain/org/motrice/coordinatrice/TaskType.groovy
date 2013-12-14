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

}

package org.motrice.signatrice.audit

/**
 * Each record represents an auditable event.
 * A record should be self-contained, no dependencies on other resources.
 * All properties except event type and description are optional.
 */
class AudEventRecord {
  // Predefined event type, one of the three-character strings below.
  String eventType

  // Auto timestamping. Must be nullable to be automatic.
  Date dateCreated

  // Does the event represent a failure?
  // The value must be false to signify failure.
  // All other values, including null, are taken as non-failure.
  Boolean failure

  // A string identifying the object being operated upon by the event.
  // This property should help distinguish event instances.
  String label

  // Short free text describing the nature of the event.
  // This property refers to the type of event, not instances.
  String description

  // The user causing this event.
  // A readable rendition of the user name or identity.
  // 'user' is an SQL reserved word.
  String userRef

  // Remote IP address
  String remoteAddr

  // Details about the event, except stack trace. Free text, no size limit.
  String details

  // Stack trace, if relevant.
  String stackTrace

  static mapping = {
    datasource 'audit'
    details type: 'text'
    stackTrace type: 'text'
  }
  static constraints = {
    eventType nullable: false, size: 3..8
    dateCreated nullable: true
    failure nullable: true
    label nullable: true, maxSize: 60
    description nullable: false, maxSize: 80
    userRef nullable: true, maxSize: 80
    remoteAddr nullable: true, maxSize: 40
    details nullable: true
    stackTrace nullable: true
  }

  /**
   * Convert an event type name to one of the predefined three-letter codes.
   * Return a code or null if the event type is not found.
   * The event type name may be lower case or capitalized.
   */
  static String toCode(String eventtype) {
    OP2CODE[eventtype]
  }

  String toString() {
    "[EventRecord ${eventType} ${dateCreated?.format('yyyy-MM-dd HH:mm:ss')}: ${description}]"
  }

  String toCompleteString() {
    "[EventRecord ${this.toMap().toMapString()}]"
  }

  // No i18n in this class
  static String CREATE_EVENT_TYPE = 'Cre'
  static String UPDATE_EVENT_TYPE = 'Upd'
  static String DELETE_EVENT_TYPE = 'Del'
  static String ERROR_EVENT_TYPE =  'Err'
  static String SIGNATURE_EVENT_TYPE = 'Sig'
  static String AUTHENTICATION_EVENT_TYPE = 'AcN'
  static String AUTHORIZATION_EVENT_TYPE = 'ArZ'
  static Map OP_NAME = [:]
  static Map OP2CODE = [:]
  static {
    OP_NAME[CREATE_EVENT_TYPE] = 'Create'
    OP_NAME[UPDATE_EVENT_TYPE] = 'Update'
    OP_NAME[DELETE_EVENT_TYPE] = 'Delete'
    OP_NAME[ERROR_EVENT_TYPE] = 'Error'
    OP_NAME[SIGNATURE_EVENT_TYPE] = 'Signature'
    OP_NAME[AUTHENTICATION_EVENT_TYPE] = 'Authentication'
    OP_NAME[AUTHORIZATION_EVENT_TYPE] = 'Authorization'
    OP2CODE['create'] = CREATE_EVENT_TYPE
    OP2CODE['update'] = UPDATE_EVENT_TYPE
    OP2CODE['delete'] = DELETE_EVENT_TYPE
    OP2CODE['error'] = ERROR_EVENT_TYPE
    OP2CODE['signature'] = SIGNATURE_EVENT_TYPE
    OP2CODE['authentication'] = AUTHENTICATION_EVENT_TYPE
    OP2CODE['authorization'] = AUTHORIZATION_EVENT_TYPE
    OP2CODE['Create'] = CREATE_EVENT_TYPE
    OP2CODE['Update'] = UPDATE_EVENT_TYPE
    OP2CODE['Delete'] = DELETE_EVENT_TYPE
    OP2CODE['Error'] = ERROR_EVENT_TYPE
    OP2CODE['Signature'] = SIGNATURE_EVENT_TYPE
    OP2CODE['Authentication'] = AUTHENTICATION_EVENT_TYPE
    OP2CODE['Authorization'] = AUTHORIZATION_EVENT_TYPE
  }

}

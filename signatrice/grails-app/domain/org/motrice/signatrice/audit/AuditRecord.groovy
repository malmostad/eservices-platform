package org.motrice.signatrice.audit

import grails.converters.JSON

/**
 * Each record represents an auditable event.
 * A record should be self-contained, no dependencies on other resources.
 * So there is not even i18n.
 */
class AuditRecord {
  // Auto timestamping
  Date dateCreated

  // Predefined event type, one of the strings below.
  String eventType

  // Flag if the event represents a failure.
  // All non-true values represents success or don't care.
  Boolean failure

  // Short free text describing the nature of the event.
  String description

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
    dateCreated nullable: true
    eventType nullable: false, size: 3..8
    failure nullable: true
    description nullable: false, maxSize: 80
    remoteAddr nullable: true, maxSize: 40
    details nullable: true
    stackTrace nullable: true
  }

  String toString() {
    "[AuditRecord ${this as JSON}]"
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
  static {
    OP_NAME[CREATE_EVENT_TYPE] = 'Create'
    OP_NAME[UPDATE_EVENT_TYPE] = 'Update'
    OP_NAME[DELETE_EVENT_TYPE] = 'Delete'
    OP_NAME[ERROR_EVENT_TYPE] = 'Error'
    OP_NAME[SIGNATURE_EVENT_TYPE] = 'Signature'
    OP_NAME[AUTHENTICATION_EVENT_TYPE] = 'Authentication'
    OP_NAME[AUTHORIZATION_EVENT_TYPE] = 'Authorization'
  }

}

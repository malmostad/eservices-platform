package org.motrice.signatrice.audit

import org.apache.commons.logging.LogFactory

class AuditService {
  static transactional = true

  private static final log = LogFactory.getLog(this)

  def logCreateEvent(String description, String details, request) {
    doLogEvent(AuditRecord.CREATE_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logCreateEvent(Boolean failure, String description, String details,
		     String stackTrace, request)
  {
    doLogEvent(AuditRecord.CREATE_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logUpdateEvent(String description, String details, request) {
    doLogEvent(AuditRecord.UPDATE_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logUpdateEvent(Boolean failure, String description, String details,
		     String stackTrace, request)
  {
    doLogEvent(AuditRecord.UPDATE_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logDeleteEvent(String description, String details, request) {
    doLogEvent(AuditRecord.DELETE_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logDeleteEvent(Boolean failure, String description, String details,
		     String stackTrace, request)
  {
    doLogEvent(AuditRecord.DELETE_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logErrorEvent(String description, String details, request) {
    doLogEvent(AuditRecord.ERROR_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logErrorEvent(Boolean failure, String description, String details,
		    String stackTrace, request)
  {
    doLogEvent(AuditRecord.ERROR_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logSignEvent(String description, String details, request) {
    doLogEvent(AuditRecord.SIGNATURE_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logSignEvent(Boolean failure, String description, String details,
		    String stackTrace, request)
  {
    doLogEvent(AuditRecord.SIGNATURE_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logAuthNEvent(String description, String details, request) {
    doLogEvent(AuditRecord.AUTHENTICATION_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logAuthNEvent(Boolean failure, String description, String details,
		    String stackTrace, request)
  {
    doLogEvent(AuditRecord.AUTHENTICATION_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logAuthZEvent(String description, String details, request) {
    doLogEvent(AuditRecord.AUTHORIZATION_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logAuthZEvent(Boolean failure, String description, String details,
		    String stackTrace, request)
  {
    doLogEvent(AuditRecord.AUTHORIZATION_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  private doLogEvent(String eventType, Boolean failure, String description,
		     String details, String stackTrace, request)
  {
    if (log.debugEnabled) log.debug "Audit ${eventType}/${description} ${failure?'FAILURE':'ok'}"
    def record = new AuditRecord(eventType: eventType, failure: failure,
    description: description, remoteAddr: request?.remoteAddr,
    details: details, stackTrace: stackTrace)
    
    // Safely save the record. Careful to never throw an exception.
    try {
      record.save(flush: true, failOnError: true)
    } catch (Throwable thr) {
      log.error('INTERNAL CONFLICT in event logging, info follows', thr)
      log.error record as String
    }
  }

}

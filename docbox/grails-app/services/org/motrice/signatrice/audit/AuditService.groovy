package org.motrice.signatrice.audit

import org.apache.commons.logging.LogFactory

class AuditService {
  static transactional = true

  private static final log = LogFactory.getLog(this)

  def logCreateEvent(String label, String description, String details, request) {
    doLogEvent(label, AuditRecord.CREATE_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logCreateEvent(String label, Boolean failure, String description, String details,
		     String stackTrace, request)
  {
    doLogEvent(label, AuditRecord.CREATE_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logUpdateEvent(String label, String description, String details, request) {
    doLogEvent(label, AuditRecord.UPDATE_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logUpdateEvent(String label, Boolean failure, String description, String details,
		     String stackTrace, request)
  {
    doLogEvent(label, AuditRecord.UPDATE_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logDeleteEvent(String label, String description, String details, request) {
    doLogEvent(label, AuditRecord.DELETE_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logDeleteEvent(String label, Boolean failure, String description, String details,
		     String stackTrace, request)
  {
    doLogEvent(label, AuditRecord.DELETE_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logErrorEvent(String label, String description, String details, request) {
    doLogEvent(label, AuditRecord.ERROR_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logErrorEvent(String label, Boolean failure, String description, String details,
		    String stackTrace, request)
  {
    doLogEvent(label, AuditRecord.ERROR_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logSignEvent(String label, String description, String details, request) {
    doLogEvent(label, AuditRecord.SIGNATURE_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logSignEvent(String label, Boolean failure, String description, String details,
		    String stackTrace, request)
  {
    doLogEvent(label, AuditRecord.SIGNATURE_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logAuthNEvent(String label, String description, String details, request) {
    doLogEvent(label, AuditRecord.AUTHENTICATION_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logAuthNEvent(String label, Boolean failure, String description, String details,
		    String stackTrace, request)
  {
    doLogEvent(label, AuditRecord.AUTHENTICATION_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  def logAuthZEvent(String label, String description, String details, request) {
    doLogEvent(label, AuditRecord.AUTHORIZATION_EVENT_TYPE, null, description,
	       details, null, request)
  }

  def logAuthZEvent(String label, Boolean failure, String description, String details,
		    String stackTrace, request)
  {
    doLogEvent(label, AuditRecord.AUTHORIZATION_EVENT_TYPE, failure, description,
	       details, stackTrace, request)
  }

  private doLogEvent(String label, String eventType, Boolean failure, String description,
		     String details, String stackTrace, request)
  {
    if (log.debugEnabled) log.debug "Audit ${eventType}/${description} ${failure?'FAILURE':'ok'}"
    def record = new AuditRecord(label: label, eventType: eventType, failure: failure,
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

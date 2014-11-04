package org.motrice.signatrice.audit

import org.motrice.signatrice.ServiceException

/**
 * Methods for adding an event record to the audit log.
 * The *Event methods are all silent, they swallow any exceptions.
 */
class AuditService {
  static transactional = true

  def createEvent(args, request) {
    silentlyAddRecord(args, AudEventRecord.CREATE_EVENT_TYPE, request?.remoteAddr)
  }

  def updateEvent(args, request) {
    silentlyAddRecord(args, AudEventRecord.UPDATE_EVENT_TYPE, request?.remoteAddr)
  }

  def deleteEvent(args, request) {
    silentlyAddRecord(args, AudEventRecord.DELETE_EVENT_TYPE, request?.remoteAddr)
  }

  def errorEvent(args, request) {
    silentlyAddRecord(args, AudEventRecord.ERROR_EVENT_TYPE, request?.remoteAddr)
  }

  def signEvent(args, request) {
    silentlyAddRecord(args, AudEventRecord.SIGNATURE_EVENT_TYPE, request?.remoteAddr)
  }

  def authNEvent(args, request) {
    silentlyAddRecord(args, AudEventRecord.AUTHENTICATION_EVENT_TYPE, request?.remoteAddr)
  }

  def authZEvent(args, request) {
    silentlyAddRecord(args, AudEventRecord.AUTHORIZATION_EVENT_TYPE, request?.remoteAddr)
  }

  /**
   * Add an event record.
   * args must be a Map of name/value pairs (String/String),
   * eventType must be an event type (long form),
   * request must be an HTTP request.
   * Throws ServiceException on conflict.
   */
  AudEventRecord addRecord(args, String eventType, request) {
    if (log.debugEnabled) log.debug "addRecord << ${eventType}"
    def code = AudEventRecord.toCode(eventType)
    if (!code) {
      def msg = "Unknown event type '${eventType}'"
      throw new ServiceException('AUDIT.171', msg)
    }

    return doAddRecord(args, code, request?.remoteAddr)
  }

  /**
   * A wrapper around doAddRecord to catch all exceptions
   */
  private silentlyAddRecord(args, String eventCode, String remoteAddr) {
    try {
      doAddRecord(args, eventCode, remoteAddr)
    } catch (ServiceException exc) {
      log.error "Failed to add event record: ${exc.canonical}"
    } catch (Exception exc) {
      log.error "Failed to add event record: ${exc.message}"
    }
  }

  /**
   * Add an event record.
   * Throw ServiceException on conflict.
   */
  private AudEventRecord doAddRecord(args, String eventCode, String remoteAddr) {
    if (!(args && (args instanceof Map))) {
      def msg = "Arguments cannot be interpreted as a Map"
      throw new ServiceException('AUDIT.173', msg)
    }

    // A description string is mandatory
    if (!args.description) throw new ServiceException('AUDIT.174', 'Event description missing')
    // Some properties should not be set from the outside
    args.eventType = eventCode
    args.dateCreated = null
    if (args.failure instanceof String) {
      args.failure = args.failure.equalsIgnoreCase("true")
    }
    args.remoteAddr = remoteAddr
    AudEventRecord record = args
    
    // Save the record
    try {
      record.save(flush: true, failOnError: true)
    } catch (Throwable thr) {
      log.error('INTERNAL CONFLICT in event logging, info follows', thr)
      log.error record.toCompleteString()
      def msg = "Could not save event record: ${candidate.errors.allErrors.join(', ')}"
      log.error "doAddRecord EXC: ${msg}"
      throw new ServiceException('AUDIT.175', msg)
    }

    if (log.debugEnabled) log.debug "addRecord >> ${record}"
    return record
  }

}

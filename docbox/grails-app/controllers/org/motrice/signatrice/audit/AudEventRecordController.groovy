package org.motrice.signatrice.audit

import org.motrice.docbox.Util
import org.motrice.signatrice.ServiceException

class AudEventRecordController {
  // Inject AuditService
  def auditService

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    if (!params.sort) {
      params.sort = 'dateCreated'
      params.order = 'desc'
    }
    [eventRecordInstList: AudEventRecord.list(params), eventRecordInstTotal: AudEventRecord.count()]
  }

  def show(Long id) {
    def eventRecordInst = AudEventRecord.get(id)
    if (!eventRecordInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'audEventRecord.label', default: 'EventRecord'), id])
      redirect(action: "list")
      return
    }

    [eventRecordInst: eventRecordInst]
  }

  /**
   * REST: Create an audit record.
   * The body must contain a JSON dictionary.
   */
  def createAudit(String eventtype) {
    if (log.debugEnabled) log.debug "AUDIT CREATE ${Util.clean(params)}"
    def array = null

    try {
      array = request.JSON
    } catch (Exception exc) {
      def msg = "AUDIT.172|The request body is not valid JSON"
      render(status: 409, contentType: 'text/plain', text: msg)
      return
    }

    try {
      auditService.addRecord(array, eventtype, request)
    } catch (ServiceException exc) {
      render(status: 409, contentType: 'text/plain', text: exc.canonical)
      return
    }

    render(status: 200)
  }

}

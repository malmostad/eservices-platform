package org.motrice.signatrice

import org.springframework.dao.DataIntegrityViolationException

class SigServiceController {

  def signService

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [sigServiceInstList: SigService.list(params), sigServiceInstTotal: SigService.count()]
  }

  def create() {
    [sigServiceInst: new SigService(params)]
  }

  def save() {
    def sigServiceInst = new SigService(params)
    if (!sigServiceInst.save(flush: true)) {
      render(view: "create", model: [sigServiceInst: sigServiceInst])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'sigService.label', default: 'SigService'), sigServiceInst.id])
    redirect(action: "show", id: sigServiceInst.id)
  }

  def show(Long id) {
    def sigServiceInst = SigService.get(id)
    if (!sigServiceInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigService.label', default: 'SigService'), id])
      redirect(action: "list")
      return
    }

    [sigServiceInst: sigServiceInst]
  }

  /**
   * Check the connection to the service by downloading its WSDL.
   */
  def check(Long id) {
    if (log.debugEnabled) log.debug "check ${params}"
    def sigServiceInst = SigService.get(id)
    if (!sigServiceInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigService.label', default: 'SigService'), id])
      redirect(action: "list")
      return
    }

    try {
      def wsdl = signService.checkService(sigServiceInst)
      def fileName = "${sigServiceInst.alias}-wsdl-${new Date().format('yyyyMMdd-HHmm')}.xml"
      response.contentType = 'application/xml; charset=utf-8'
      response.setHeader('Content-Disposition', "attachment; filename=${fileName}")
      response.outputStream << wsdl.getBytes('UTF-8')
    } catch (ServiceException exc) {
      if (log.debugEnabled) log.debug "check EXCEPTION: ${exc}"
      redirect(action: 'show', id: sigServiceInst.id)
    }
  }

  def edit(Long id) {
    def sigServiceInst = SigService.get(id)
    if (!sigServiceInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigService.label', default: 'SigService'), id])
      redirect(action: "list")
      return
    }

    [sigServiceInst: sigServiceInst]
  }

  def update(Long id, Long version) {
    def sigServiceInst = SigService.get(id)
    if (!sigServiceInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigService.label', default: 'SigService'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (sigServiceInst.version > version) {
	sigServiceInst.errors.rejectValue("version", "default.optimistic.locking.failure",
					  [message(code: 'sigService.label', default: 'SigService')] as Object[],
					  "Another user has updated this SigService while you were editing")
	render(view: "edit", model: [sigServiceInst: sigServiceInst])
	return
      }
    }

    sigServiceInst.properties = params

    if (!sigServiceInst.save(flush: true)) {
      render(view: "edit", model: [sigServiceInst: sigServiceInst])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'sigService.label', default: 'SigService'), sigServiceInst.id])
    redirect(action: "show", id: sigServiceInst.id)
  }

  def delete(Long id) {
    def sigServiceInst = SigService.get(id)
    if (!sigServiceInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigService.label', default: 'SigService'), id])
      redirect(action: "list")
      return
    }

    try {
      sigServiceInst.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'sigService.label', default: 'SigService'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'sigService.label', default: 'SigService'), id])
      redirect(action: "show", id: id)
    }
  }
}

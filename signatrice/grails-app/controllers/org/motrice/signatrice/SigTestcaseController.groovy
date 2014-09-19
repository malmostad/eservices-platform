package org.motrice.signatrice

import org.springframework.dao.DataIntegrityViolationException

import org.motrice.signatrice.cgi.GrpFault

class SigTestcaseController {

  def auditService
  def signService

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    if (log.debugEnabled) log.debug "LIST ${params}"
    params.max = Math.min(max ?: 10, 100)
    [sigTestcaseInstList: SigTestcase.list(params), sigTestcaseInstTotal: SigTestcase.count()]
  }

  def create() {
    [sigTestcaseInst: new SigTestcase(params)]
  }

  def save() {
    def sigTestcaseInst = new SigTestcase(params)
    if (!sigTestcaseInst.save(flush: true)) {
      render(view: "create", model: [sigTestcaseInst: sigTestcaseInst])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), sigTestcaseInst.id])
    redirect(action: "show", id: sigTestcaseInst.id)
  }

  def show(Long id) {
    if (log.debugEnabled) log.debug "SHOW ${params}"
    def sigTestcaseInst = SigTestcase.get(id)
    if (!sigTestcaseInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
      return
    }

    [sigTestcaseInst: sigTestcaseInst]
  }

  def edit(Long id) {
    def sigTestcaseInst = SigTestcase.get(id)
    if (!sigTestcaseInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
      return
    }

    [sigTestcaseInst: sigTestcaseInst]
  }

  def update(Long id, Long version) {
    def sigTestcaseInst = SigTestcase.get(id)
    if (!sigTestcaseInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (sigTestcaseInst.version > version) {
	sigTestcaseInst.errors.rejectValue("version", "default.optimistic.locking.failure",
					   [message(code: 'sigTestcase.label', default: 'SigTestcase')] as Object[],
					   "Another user has updated this SigTestcase while you were editing")
	render(view: "edit", model: [sigTestcaseInst: sigTestcaseInst])
	return
      }
    }

    sigTestcaseInst.properties = params

    if (!sigTestcaseInst.save(flush: true)) {
      render(view: "edit", model: [sigTestcaseInst: sigTestcaseInst])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), sigTestcaseInst.id])
    redirect(action: "show", id: sigTestcaseInst.id)
  }

  def sign(Long id) {
    if (log.debugEnabled) log.debug "SIGN ${params}"
    def sigTestcaseInst = SigTestcase.get(id)
    if (!sigTestcaseInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
      return
    }

    def result = null
    try {
      result = signService.sign(sigTestcaseInst, request)
      if (result && !result.save()) log.error "Test case ${sigTestcaseInst} save: ${result.errors.allErrors.join(', ')}"
      redirect(controller: 'sigResult', action: 'show', id: result.id)
    } catch (ServiceException exc) {
      flash.message = "Sign request failed. ${exc.message}"
      redirect(controller: 'sigTestcase', action: 'show', id: id)
    }
  }

  def delete(Long id) {
    def sigTestcaseInst = SigTestcase.get(id)
    if (!sigTestcaseInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
      return
    }

    try {
      sigTestcaseInst.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "show", id: id)
    }
  }
}

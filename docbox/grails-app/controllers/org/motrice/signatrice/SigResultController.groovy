package org.motrice.signatrice

import org.springframework.dao.DataIntegrityViolationException

class SigResultController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [sigResultInstList: SigResult.list(params), sigResultInstTotal: SigResult.count()]
  }

  def create() {
    [sigResultInst: new SigResult(params)]
  }

  def save() {
    def sigResultInst = new SigResult(params)
    if (!sigResultInst.save(flush: true)) {
      render(view: "create", model: [sigResultInst: sigResultInst])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'sigResult.label', default: 'SigResult'), sigResultInst.id])
    redirect(action: "show", id: sigResultInst.id)
  }

  def show(Long id) {
    def sigResultInst = SigResult.get(id)
    if (!sigResultInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigResult.label', default: 'SigResult'), id])
      redirect(action: "list")
      return
    }

    [sigResultInst: sigResultInst]
  }

  def edit(Long id) {
    def sigResultInst = SigResult.get(id)
    if (!sigResultInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigResult.label', default: 'SigResult'), id])
      redirect(action: "list")
      return
    }

    [sigResultInst: sigResultInst]
  }

  def sign(Long id) {
    def sigResultInst = SigResult.get(id)
    if (!sigResultInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigScheme.label', default: 'SigScheme'), id])
      redirect(action: "list")
      return
    }

    [sigResultInst: sigResultInst, autoStartToken: sigResultInst.autoStartToken, returnUrl: '']
  }

  def update(Long id, Long version) {
    def sigResultInst = SigResult.get(id)
    if (!sigResultInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigResult.label', default: 'SigResult'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (sigResultInst.version > version) {
	sigResultInst.errors.rejectValue("version", "default.optimistic.locking.failure",
					 [message(code: 'sigResult.label', default: 'SigResult')] as Object[],
					 "Another user has updated this SigResult while you were editing")
	render(view: "edit", model: [sigResultInst: sigResultInst])
	return
      }
    }

    sigResultInst.properties = params

    if (!sigResultInst.save(flush: true)) {
      render(view: "edit", model: [sigResultInst: sigResultInst])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'sigResult.label', default: 'SigResult'), sigResultInst.id])
    redirect(action: "show", id: sigResultInst.id)
  }

  def delete(Long id) {
    def sigResultInst = SigResult.get(id)
    if (!sigResultInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigResult.label', default: 'SigResult'), id])
      redirect(action: "list")
      return
    }

    try {
      sigResultInst.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'sigResult.label', default: 'SigResult'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'sigResult.label', default: 'SigResult'), id])
      redirect(action: "show", id: id)
    }
  }
}

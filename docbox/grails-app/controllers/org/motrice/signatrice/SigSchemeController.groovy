package org.motrice.signatrice

import org.springframework.dao.DataIntegrityViolationException

class SigSchemeController {

  def signService

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [sigSchemeObjList: SigScheme.list(params), sigSchemeObjTotal: SigScheme.count()]
  }

  def create() {
    [sigSchemeObj: new SigScheme(params)]
  }

  def save() {
    def sigSchemeObj = new SigScheme(params)
    if (!sigSchemeObj.save(flush: true)) {
      render(view: "create", model: [sigSchemeObj: sigSchemeObj])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'sigScheme.label', default: 'SigScheme'), sigSchemeObj.id])
    redirect(action: "show", id: sigSchemeObj.id)
  }

  def show(Long id) {
    def sigSchemeObj = SigScheme.get(id)
    if (!sigSchemeObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigScheme.label', default: 'SigScheme'), id])
      redirect(action: "list")
      return
    }

    [sigSchemeObj: sigSchemeObj]
  }

  def edit(Long id) {
    def sigSchemeObj = SigScheme.get(id)
    if (!sigSchemeObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigScheme.label', default: 'SigScheme'), id])
      redirect(action: "list")
      return
    }

    [sigSchemeObj: sigSchemeObj]
  }

  def update(Long id, Long version) {
    def sigSchemeObj = SigScheme.get(id)
    if (!sigSchemeObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigScheme.label', default: 'SigScheme'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (sigSchemeObj.version > version) {
	sigSchemeObj.errors.rejectValue("version", "default.optimistic.locking.failure",
					[message(code: 'sigScheme.label', default: 'SigScheme')] as Object[],
					"Another user has updated this SigScheme while you were editing")
	render(view: "edit", model: [sigSchemeObj: sigSchemeObj])
	return
      }
    }

    sigSchemeObj.properties = params

    if (!sigSchemeObj.save(flush: true)) {
      render(view: "edit", model: [sigSchemeObj: sigSchemeObj])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'sigScheme.label', default: 'SigScheme'), sigSchemeObj.id])
    redirect(action: "show", id: sigSchemeObj.id)
  }

  def delete(Long id) {
    def sigSchemeObj = SigScheme.get(id)
    if (!sigSchemeObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigScheme.label', default: 'SigScheme'), id])
      redirect(action: "list")
      return
    }

    try {
      // If you try to delete the current default scheme there will be a constraint violation exception.
      // It is managed properly, but the stack trace appears on the console.
      // The bug is that there is an unnecessary stack trace. Seems to be fixed in later Grails versions.
      sigSchemeObj.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'sigScheme.label', default: 'SigScheme'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'sigScheme.label', default: 'SigScheme'), id])
      redirect(action: "show", id: id)
    }
  }
}

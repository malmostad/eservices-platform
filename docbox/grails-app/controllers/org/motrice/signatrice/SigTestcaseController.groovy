package org.motrice.signatrice

import org.springframework.dao.DataIntegrityViolationException

class SigTestcaseController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [sigTestcaseObjList: SigTestcase.list(params), sigTestcaseObjTotal: SigTestcase.count()]
  }

  def create() {
    [sigTestcaseObj: new SigTestcase(params)]
  }

  def save() {
    def sigTestcaseObj = new SigTestcase(params)
    if (!sigTestcaseObj.save(flush: true)) {
      render(view: "create", model: [sigTestcaseObj: sigTestcaseObj])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), sigTestcaseObj.id])
    redirect(action: "show", id: sigTestcaseObj.id)
  }

  def show(Long id) {
    def sigTestcaseObj = SigTestcase.get(id)
    if (!sigTestcaseObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
      return
    }

    [sigTestcaseObj: sigTestcaseObj]
  }

  def edit(Long id) {
    def sigTestcaseObj = SigTestcase.get(id)
    if (!sigTestcaseObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
      return
    }

    [sigTestcaseObj: sigTestcaseObj]
  }

  def update(Long id, Long version) {
    def sigTestcaseObj = SigTestcase.get(id)
    if (!sigTestcaseObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (sigTestcaseObj.version > version) {
	sigTestcaseObj.errors.rejectValue("version", "default.optimistic.locking.failure",
					  [message(code: 'sigTestcase.label', default: 'SigTestcase')] as Object[],
					  "Another user has updated this SigTestcase while you were editing")
	render(view: "edit", model: [sigTestcaseObj: sigTestcaseObj])
	return
      }
    }

    sigTestcaseObj.properties = params

    if (!sigTestcaseObj.save(flush: true)) {
      render(view: "edit", model: [sigTestcaseObj: sigTestcaseObj])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), sigTestcaseObj.id])
    redirect(action: "show", id: sigTestcaseObj.id)
  }

  def delete(Long id) {
    def sigTestcaseObj = SigTestcase.get(id)
    if (!sigTestcaseObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
      return
    }

    try {
      sigTestcaseObj.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "show", id: id)
    }
  }
}

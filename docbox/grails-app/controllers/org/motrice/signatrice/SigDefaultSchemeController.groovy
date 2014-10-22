package org.motrice.signatrice

import org.springframework.dao.DataIntegrityViolationException

class SigDefaultSchemeController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [sigDefaultSchemeObjList: SigDefaultScheme.list(params), sigDefaultSchemeObjTotal: SigDefaultScheme.count()]
  }

  def show(Long id) {
    def sigDefaultSchemeObj = SigDefaultScheme.get(id)
    if (!sigDefaultSchemeObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigDefaultScheme.label', default: 'SigDefaultScheme'), id])
      redirect(action: "list")
      return
    }

    [sigDefaultSchemeObj: sigDefaultSchemeObj]
  }

  def edit(Long id) {
    def sigDefaultSchemeObj = SigDefaultScheme.get(id)
    if (!sigDefaultSchemeObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigDefaultScheme.label', default: 'SigDefaultScheme'), id])
      redirect(action: "list")
      return
    }

    [sigDefaultSchemeObj: sigDefaultSchemeObj]
  }

  def update(Long id, Long version) {
    def sigDefaultSchemeObj = SigDefaultScheme.get(id)
    if (!sigDefaultSchemeObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigDefaultScheme.label', default: 'SigDefaultScheme'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (sigDefaultSchemeObj.version > version) {
	sigDefaultSchemeObj.errors.rejectValue("version", "default.optimistic.locking.failure",
					       [message(code: 'sigDefaultScheme.label', default: 'SigDefaultScheme')] as Object[],
					       "Another user has updated this SigDefaultScheme while you were editing")
	render(view: "edit", model: [sigDefaultSchemeObj: sigDefaultSchemeObj])
	return
      }
    }

    sigDefaultSchemeObj.properties = params

    if (!sigDefaultSchemeObj.save(flush: true)) {
      render(view: "edit", model: [sigDefaultSchemeObj: sigDefaultSchemeObj])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'sigDefaultScheme.label', default: 'SigDefaultScheme'), sigDefaultSchemeObj.id])
    redirect(action: "show", id: sigDefaultSchemeObj.id)
  }

}

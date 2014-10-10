package org.motrice.tdocbox

import org.springframework.dao.DataIntegrityViolationException

class TdbDrillController {
  // Injected service
  def tdbDrillService

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [tdbDrillObjList: TdbDrill.list(params), tdbDrillObjTotal: TdbDrill.count()]
  }

  def create() {
    [tdbDrillObj: new TdbDrill(params)]
  }

  def save() {
    def tdbDrillObj = new TdbDrill(params)
    if (!tdbDrillObj.save(flush: true)) {
      render(view: "create", model: [tdbDrillObj: tdbDrillObj])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'tdbDrill.label', default: 'TdbDrill'), tdbDrillObj.id])
    redirect(action: "show", id: tdbDrillObj.id)
  }

  def show(Long id) {
    def tdbDrillObj = TdbDrill.get(id)
    if (!tdbDrillObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbDrill.label', default: 'TdbDrill'), id])
      redirect(action: "list")
      return
    }

    [tdbDrillObj: tdbDrillObj]
  }

  def run(Long id) {
    def tdbDrillObj = TdbDrill.get(id)
    if (!tdbDrillObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbDrill.label', default: 'TdbDrill'), id])
      redirect(action: "list")
      return
    }

    tdbDrillService.perform(tdbDrillObj)
    render(view: 'show', model: [tdbDrillObj: tdbDrillObj])
  }

  def edit(Long id) {
    def tdbDrillObj = TdbDrill.get(id)
    if (!tdbDrillObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbDrill.label', default: 'TdbDrill'), id])
      redirect(action: "list")
      return
    }

    [tdbDrillObj: tdbDrillObj]
  }

  def update(Long id, Long version) {
    def tdbDrillObj = TdbDrill.get(id)
    if (!tdbDrillObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbDrill.label', default: 'TdbDrill'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (tdbDrillObj.version > version) {
	tdbDrillObj.errors.rejectValue("version", "default.optimistic.locking.failure",
				       [message(code: 'tdbDrill.label', default: 'TdbDrill')] as Object[],
				       "Another user has updated this TdbDrill while you were editing")
	render(view: "edit", model: [tdbDrillObj: tdbDrillObj])
	return
      }
    }

    tdbDrillObj.properties = params

    if (!tdbDrillObj.save(flush: true)) {
      render(view: "edit", model: [tdbDrillObj: tdbDrillObj])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'tdbDrill.label', default: 'TdbDrill'), tdbDrillObj.id])
    redirect(action: "show", id: tdbDrillObj.id)
  }

  def delete(Long id) {
    def tdbDrillObj = TdbDrill.get(id)
    if (!tdbDrillObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbDrill.label', default: 'TdbDrill'), id])
      redirect(action: "list")
      return
    }

    try {
      tdbDrillObj.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'tdbDrill.label', default: 'TdbDrill'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tdbDrill.label', default: 'TdbDrill'), id])
      redirect(action: "show", id: id)
    }
  }
}

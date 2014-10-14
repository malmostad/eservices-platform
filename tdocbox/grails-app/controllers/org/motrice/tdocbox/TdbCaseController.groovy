package org.motrice.tdocbox

import org.springframework.dao.DataIntegrityViolationException

class TdbCaseController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    if (!params.sort) {
      params.sort = 'timeStamp'
      params.order = 'desc'
    }
    [tdbCaseObjList: TdbCase.list(params), tdbCaseObjTotal: TdbCase.count()]
  }

  def create() {
    [tdbCaseObj: new TdbCase(params)]
  }

  def save() {
    def tdbCaseObj = new TdbCase(params)
    if (!tdbCaseObj.save(flush: true)) {
      render(view: "create", model: [tdbCaseObj: tdbCaseObj])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'tdbCase.label', default: 'TdbCase'), tdbCaseObj.id])
    redirect(action: "show", id: tdbCaseObj.id)
  }

  def show(Long id) {
    def tdbCaseObj = TdbCase.get(id)
    if (!tdbCaseObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbCase.label', default: 'TdbCase'), id])
      redirect(action: "list")
      return
    }

    [tdbCaseObj: tdbCaseObj]
  }

  def edit(Long id) {
    def tdbCaseObj = TdbCase.get(id)
    if (!tdbCaseObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbCase.label', default: 'TdbCase'), id])
      redirect(action: "list")
      return
    }

    [tdbCaseObj: tdbCaseObj]
  }

  def update(Long id, Long version) {
    def tdbCaseObj = TdbCase.get(id)
    if (!tdbCaseObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbCase.label', default: 'TdbCase'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (tdbCaseObj.version > version) {
	tdbCaseObj.errors.rejectValue("version", "default.optimistic.locking.failure",
				      [message(code: 'tdbCase.label', default: 'TdbCase')] as Object[],
				      "Another user has updated this TdbCase while you were editing")
	render(view: "edit", model: [tdbCaseObj: tdbCaseObj])
	return
      }
    }

    tdbCaseObj.properties = params

    if (!tdbCaseObj.save(flush: true)) {
      render(view: "edit", model: [tdbCaseObj: tdbCaseObj])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'tdbCase.label', default: 'TdbCase'), tdbCaseObj.id])
    redirect(action: "show", id: tdbCaseObj.id)
  }

  def delete(Long id) {
    def tdbCaseObj = TdbCase.get(id)
    if (!tdbCaseObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbCase.label', default: 'TdbCase'), id])
      redirect(action: "list")
      return
    }

    try {
      tdbCaseObj.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'tdbCase.label', default: 'TdbCase'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tdbCase.label', default: 'TdbCase'), id])
      redirect(action: "show", id: id)
    }
  }
}

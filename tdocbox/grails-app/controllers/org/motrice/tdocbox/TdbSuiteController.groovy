package org.motrice.tdocbox

import org.springframework.dao.DataIntegrityViolationException

class TdbSuiteController {

  def tdbDrillService
  def setupService
  
  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    if (!params.sort) {
      params.sort = 'name'
      params.order = 'asc'
    }
    [tdbSuiteObjList: TdbSuite.list(params), tdbSuiteObjTotal: TdbSuite.count()]
  }

  def create() {
    [tdbSuiteObj: new TdbSuite(params)]
  }

  def save() {
    def tdbSuiteObj = new TdbSuite(params)
    if (!tdbSuiteObj.save(flush: true)) {
      render(view: "create", model: [tdbSuiteObj: tdbSuiteObj])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), tdbSuiteObj.id])
    redirect(action: "show", id: tdbSuiteObj.id)
  }

  def run(Long id) {
    setupService.initialize()
    def tdbSuiteObj = TdbSuite.get(id)
    if (!tdbSuiteObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), id])
      redirect(action: "list")
      return
    }

    def cs = null
    // Use "find" to break the iteration on exception
    tdbSuiteObj.drills.find {drill ->
      cs = tdbDrillService.perform(tdbSuiteObj, drill, cs)
      return cs?.exception
    }

    if (cs) {
      redirect(controller: 'TdbCase', action: 'show', id: cs.id)
    } else {
      render(view: 'show', model: [tdbSuiteObj: tdbSuiteObj])
    }
  }

  def show(Long id) {
    def tdbSuiteObj = TdbSuite.get(id)
    if (!tdbSuiteObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), id])
      redirect(action: "list")
      return
    }

    [tdbSuiteObj: tdbSuiteObj]
  }

  def edit(Long id) {
    def tdbSuiteObj = TdbSuite.get(id)
    if (!tdbSuiteObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), id])
      redirect(action: "list")
      return
    }

    [tdbSuiteObj: tdbSuiteObj]
  }

  def update(Long id, Long version) {
    def tdbSuiteObj = TdbSuite.get(id)
    if (!tdbSuiteObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (tdbSuiteObj.version > version) {
	tdbSuiteObj.errors.rejectValue("version", "default.optimistic.locking.failure",
				       [message(code: 'tdbSuite.label', default: 'TdbSuite')] as Object[],
				       "Another user has updated this TdbSuite while you were editing")
	render(view: "edit", model: [tdbSuiteObj: tdbSuiteObj])
	return
      }
    }

    tdbSuiteObj.properties = params

    if (!tdbSuiteObj.save(flush: true)) {
      render(view: "edit", model: [tdbSuiteObj: tdbSuiteObj])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), tdbSuiteObj.id])
    redirect(action: "show", id: tdbSuiteObj.id)
  }

  def delete(Long id) {
    def tdbSuiteObj = TdbSuite.get(id)
    if (!tdbSuiteObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), id])
      redirect(action: "list")
      return
    }

    try {
      tdbSuiteObj.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), id])
      redirect(action: "show", id: id)
    }
  }
}

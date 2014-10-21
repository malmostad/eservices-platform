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
    if (log.debugEnabled) log.debug "CREATE << ${params}"
    [tdbSuiteObj: new TdbSuite(params)]
  }

  def save() {
    if (log.debugEnabled) log.debug "SAVE << ${params}"
    def tdbSuiteObj = new TdbSuite(params)
    if (!tdbSuiteObj.save(flush: true)) {
      render(view: "create", model: [tdbSuiteObj: tdbSuiteObj])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), tdbSuiteObj.id])
    redirect(action: "show", id: tdbSuiteObj.id)
  }

  /**
   * Run a test suite.
   * A parameter named prevTestRun (a TdbCase id) may be present.
   * In such case this suite continues a preceding test run.
   */
  def run(Long id) {
    if (log.debugEnabled) log.debug "RUN << ${id}, params: ${params}"
    setupService.initialize()
    def tdbSuiteObj = TdbSuite.get(id)
    if (!tdbSuiteObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbSuite.label', default: 'TdbSuite'), id])
      redirect(action: "list")
      return
    }

    if (log.debugEnabled) log.debug "run << ${id} : ${tdbSuiteObj}"

    // Perform all the drills of the suite in the order determined by their names.
    // The outer try-catch is for catching exceptions in the test machinery itself.
    def cs = null
    // If this suite is chained from a previous one, get the test run.
    if (params.prevTestRun) {
      cs = TdbCase.get(params.prevTestRun)
      // Reset the display url to avoid another display.
      if (cs) {
	cs.displayUrl = null
	cs.save()
      }
    }
    try {
      // Use "find" to break the iteration on exception, i.e. if a REST invocation
      // returns a HTTP status not in the 2xx range.
      // There is no other way to break out of a Groovy iteration!
      tdbSuiteObj.drills.find {drill ->
	cs = tdbDrillService.perform(tdbSuiteObj, drill, cs)
	return cs?.exception
      }
    } catch (ServiceException exc) {
      flash.message = exc.message
      render(view: 'show', model: [tdbSuiteObj: tdbSuiteObj])
      return
    }

    if (cs?.exception) {
      flash.message = cs.exception
      redirect(controller: 'TdbCase', action: 'show', id: cs.id)
    } else if (cs?.displayUrl) {
      render(view: 'display', model: [tdbSuiteObj: tdbSuiteObj, testRun: cs, chainedSuite: tdbSuiteObj?.chainedSuite])
    } else if (cs) {
      redirect(controller: 'tdbCase', action: 'show', id: cs.id)
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
    if (log.debugEnabled) log.debug "EDIT << ${id}, ${params}"
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

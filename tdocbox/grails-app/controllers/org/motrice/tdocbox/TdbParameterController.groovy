package org.motrice.tdocbox

import org.springframework.dao.DataIntegrityViolationException

class TdbParameterController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [tdbParameterObjList: TdbParameter.list(params), tdbParameterObjTotal: TdbParameter.count()]
  }

  def create() {
    if (log.debugEnabled) log.debug "CREATE << ${params}"
    [tdbParameterObj: new TdbParameter(params), tdbDrillObj: params.tdbDrill]
  }

  def save() {
    def tdbParameterObj = new TdbParameter(params)
    if (!tdbParameterObj.save(flush: true)) {
      render(view: "create", model: [tdbParameterObj: tdbParameterObj])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'tdbParameter.label', default: 'TdbParameter'), tdbParameterObj.id])
    redirect(action: "show", id: tdbParameterObj.id)
  }

  def show(Long id) {
    def tdbParameterObj = TdbParameter.get(id)
    if (!tdbParameterObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbParameter.label', default: 'TdbParameter'), id])
      redirect(action: "list")
      return
    }

    [tdbParameterObj: tdbParameterObj]
  }

  def edit(Long id) {
    if (log.debugEnabled) log.debug "EDIT << ${id}, ${params}"
    def tdbParameterObj = TdbParameter.get(id)
    if (!tdbParameterObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbParameter.label', default: 'TdbParameter'), id])
      redirect(action: "list")
      return
    }

    [tdbParameterObj: tdbParameterObj, tdbDrillObj: tdbParameterObj?.drill]
  }

  def update(Long id, Long version) {
    def tdbParameterObj = TdbParameter.get(id)
    if (!tdbParameterObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbParameter.label', default: 'TdbParameter'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (tdbParameterObj.version > version) {
	tdbParameterObj.errors.rejectValue("version", "default.optimistic.locking.failure",
					   [message(code: 'tdbParameter.label', default: 'TdbParameter')] as Object[],
					   "Another user has updated this TdbParameter while you were editing")
	render(view: "edit", model: [tdbParameterObj: tdbParameterObj])
	return
      }
    }

    tdbParameterObj.properties = params

    if (!tdbParameterObj.save(flush: true)) {
      render(view: "edit", model: [tdbParameterObj: tdbParameterObj])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'tdbParameter.label', default: 'TdbParameter'), tdbParameterObj.id])
    redirect(action: "show", id: tdbParameterObj.id)
  }

  def delete(Long id) {
    def tdbParameterObj = TdbParameter.get(id)
    if (!tdbParameterObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbParameter.label', default: 'TdbParameter'), id])
      redirect(action: "list")
      return
    }

    try {
      tdbParameterObj.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'tdbParameter.label', default: 'TdbParameter'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tdbParameter.label', default: 'TdbParameter'), id])
      redirect(action: "show", id: id)
    }
  }
}

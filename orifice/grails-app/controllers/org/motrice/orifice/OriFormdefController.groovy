package org.motrice.orifice

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.XML

class OriFormdefController {
  // Injection magic
  def packageService

  static allowedMethods = [export: "POST", save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [oriFormdefInstList: OriFormdef.list(params), oriFormdefInstTotal: OriFormdef.count()]
  }

  def create() {
    [oriFormdefInst: new OriFormdef(params)]
  }

  def save() {
    def oriFormdefInst = new OriFormdef(params)
    if (!oriFormdefInst.save(flush: true)) {
      render(view: "create", model: [oriFormdefInst: oriFormdefInst])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'oriFormdef.label', default: 'OriFormdef'), oriFormdefInst.id])
    redirect(action: "show", id: oriFormdefInst.id)
  }

  def show(Long id) {
    def oriFormdefInst = OriFormdef.get(id)
    if (!oriFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriFormdef.label', default: 'OriFormdef'), id])
      redirect(action: "list")
      return
    }

    [oriFormdefInst: oriFormdefInst]
  }

  def edit(Long id) {
    def oriFormdefInst = OriFormdef.get(id)
    if (!oriFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriFormdef.label', default: 'OriFormdef'), id])
      redirect(action: "list")
      return
    }

    [oriFormdefInst: oriFormdefInst]
  }

  def update(Long id, Long version) {
    def oriFormdefInst = OriFormdef.get(id)
    if (!oriFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriFormdef.label', default: 'OriFormdef'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (oriFormdefInst.version > version) {
	oriFormdefInst.errors.rejectValue("version", "default.optimistic.locking.failure",
					      [message(code: 'oriFormdef.label', default: 'OriFormdef')] as Object[],
					      "Another user has updated this OriFormdef while you were editing")
	render(view: "edit", model: [oriFormdefInst: oriFormdefInst])
	return
      }
    }

    oriFormdefInst.properties = params

    if (!oriFormdefInst.save(flush: true)) {
      render(view: "edit", model: [oriFormdefInst: oriFormdefInst])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'oriFormdef.label', default: 'OriFormdef'), oriFormdefInst.id])
    redirect(action: "show", id: oriFormdefInst.id)
  }

  def delete(Long id) {
    def oriFormdefInst = OriFormdef.get(id)
    if (!oriFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriFormdef.label', default: 'OriFormdef'), id])
      redirect(action: "list")
      return
    }

    try {
      oriFormdefInst.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'oriFormdef.label', default: 'OriFormdef'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'oriFormdef.label', default: 'OriFormdef'), id])
      redirect(action: "show", id: id)
    }
  }
}

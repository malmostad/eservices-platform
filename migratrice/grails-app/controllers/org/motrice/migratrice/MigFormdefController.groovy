package org.motrice.migratrice

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.XML

class MigFormdefController {
  // Injection magic
  def packageService

  static allowedMethods = [export: "POST", save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [migFormdefInstList: MigFormdef.list(params), migFormdefInstTotal: MigFormdef.count()]
  }

  def create() {
    [migFormdefInst: new MigFormdef(params)]
  }

  def save() {
    def migFormdefInst = new MigFormdef(params)
    if (!migFormdefInst.save(flush: true)) {
      render(view: "create", model: [migFormdefInst: migFormdefInst])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'migFormdef.label', default: 'MigFormdef'), migFormdefInst.id])
    redirect(action: "show", id: migFormdefInst.id)
  }

  def show(Long id) {
    def migFormdefInst = MigFormdef.get(id)
    if (!migFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migFormdef.label', default: 'MigFormdef'), id])
      redirect(action: "list")
      return
    }

    [migFormdefInst: migFormdefInst]
  }

  def edit(Long id) {
    def migFormdefInst = MigFormdef.get(id)
    if (!migFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migFormdef.label', default: 'MigFormdef'), id])
      redirect(action: "list")
      return
    }

    [migFormdefInst: migFormdefInst]
  }

  def update(Long id, Long version) {
    def migFormdefInst = MigFormdef.get(id)
    if (!migFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migFormdef.label', default: 'MigFormdef'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (migFormdefInst.version > version) {
	migFormdefInst.errors.rejectValue("version", "default.optimistic.locking.failure",
					      [message(code: 'migFormdef.label', default: 'MigFormdef')] as Object[],
					      "Another user has updated this MigFormdef while you were editing")
	render(view: "edit", model: [migFormdefInst: migFormdefInst])
	return
      }
    }

    migFormdefInst.properties = params

    if (!migFormdefInst.save(flush: true)) {
      render(view: "edit", model: [migFormdefInst: migFormdefInst])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'migFormdef.label', default: 'MigFormdef'), migFormdefInst.id])
    redirect(action: "show", id: migFormdefInst.id)
  }

  def delete(Long id) {
    def migFormdefInst = MigFormdef.get(id)
    if (!migFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migFormdef.label', default: 'MigFormdef'), id])
      redirect(action: "list")
      return
    }

    try {
      migFormdefInst.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'migFormdef.label', default: 'MigFormdef'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'migFormdef.label', default: 'MigFormdef'), id])
      redirect(action: "show", id: id)
    }
  }
}

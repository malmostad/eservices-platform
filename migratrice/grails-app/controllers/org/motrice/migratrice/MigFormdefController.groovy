package org.motrice.migratrice

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.XML

class MigFormdefController {

  static allowedMethods = [export: "POST", save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [migFormdefInstList: MigFormdef.list(params), migFormdefInstTotal: MigFormdef.count()]
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

}

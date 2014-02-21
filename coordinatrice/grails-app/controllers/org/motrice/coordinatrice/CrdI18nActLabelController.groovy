package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException

class CrdI18nActLabelController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    if (log.debugEnabled) log.debug "LIST ${params}"
    params.max = Math.min(max ?: 30, 100)
    [actLabelInstList: CrdI18nActLabel.list(params), actLabelInstTotal: CrdI18nActLabel.count()]
  }

  // id must be defined and limits the list to that process definition key
  def listkey(Integer max) {
    if (log.debugEnabled) log.debug "LISTKEY ${params}"
    params.max = Math.min(max ?: 30, 100)
    def key = params.id
    if (key) {
      def instList = CrdI18nActLabel.findAllByProcdefKey(key, params)
      def instTotal = CrdI18nActLabel.countByProcdefKey(key)
      [procdefKey: key, actLabelInstList: instList, actLabelInstTotal: instTotal]
    } else {
      redirect(action: "list")
    }
  }

  def create() {
    [actLabelInst: new CrdI18nActLabel(params)]
  }

  def save() {
    def actLabelInst = new CrdI18nActLabel(params)
    if (!actLabelInst.save(flush: true)) {
      render(view: "create", model: [actLabelInst: actLabelInst])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel'), actLabelInst.id])
    redirect(action: "show", id: actLabelInst.id)
  }

  def createlocale() {
    if (log.debugEnabled) log.debug "CREATE LOCALE ${params}"
    [actLabelInst: new CrdI18nActLabel(params)]
  }

  def show(Long id) {
    def actLabelInst = CrdI18nActLabel.get(id)
    if (!actLabelInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel'), id])
      redirect(action: "list")
      return
    }

    [actLabelInst: actLabelInst]
  }

  def edit(Long id) {
    def actLabelInst = CrdI18nActLabel.get(id)
    if (!actLabelInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel'), id])
      redirect(action: "list")
      return
    }

    [actLabelInst: actLabelInst]
  }

  def update(Long id, Long version) {
    def actLabelInst = CrdI18nActLabel.get(id)
    if (!actLabelInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (actLabelInst.version > version) {
	actLabelInst.errors.rejectValue("version", "default.optimistic.locking.failure",
					       [message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel')] as Object[],
					       "Another user has updated this CrdI18nActLabel while you were editing")
	render(view: "edit", model: [actLabelInst: actLabelInst])
	return
      }
    }

    actLabelInst.properties = params

    if (!actLabelInst.save(flush: true)) {
      render(view: "edit", model: [actLabelInst: actLabelInst])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel'), actLabelInst.id])
    redirect(action: "show", id: actLabelInst.id)
  }

  def delete(Long id) {
    def actLabelInst = CrdI18nActLabel.get(id)
    if (!actLabelInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel'), id])
      redirect(action: "list")
      return
    }

    try {
      actLabelInst.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel'), id])
      redirect(action: "show", id: id)
    }
  }
}

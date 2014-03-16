package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException
import org.motrice.coordinatrice.pxd.PxdFormdef

class CrdI18nFormLabelController {

  def formLabelService

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    if (log.debugEnabled) log.debug "LIST ${params}"
    params.max = Math.min(max ?: 30, 100)
    [crdI18nFormLabelInstList: CrdI18nFormLabel.list(params), crdI18nFormLabelInstTotal: CrdI18nFormLabel.count()]
  }

  /**
   * List start form labels for a given form definition
   * id must be defined and must be the form definition id
   */
  def listkey(Integer max) {
    if (log.debugEnabled) log.debug "LISTKEY ${params}"
    params.max = Math.min(max ?: 30, 100)
    def key = params.id
    if (key) {
      def instSet = new TreeSet(CrdI18nFormLabel.findAllByFormdefId(key, params))
      def instTotal = CrdI18nFormLabel.countByFormdefId(key)
      def formdefInst = PxdFormdef.get(key)
      [formdefInst: formdefInst, formLabelInstList: instSet, formLabelInstTotal: instTotal]
    } else {
      redirect(action: "list")
    }
  }

  def create() {
    [crdI18nFormLabelInst: new CrdI18nFormLabel(params)]
  }

  def save() {
    def crdI18nFormLabelInst = new CrdI18nFormLabel(params)
    if (!crdI18nFormLabelInst.save(flush: true)) {
      render(view: "create", model: [crdI18nFormLabelInst: crdI18nFormLabelInst])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), crdI18nFormLabelInst.id])
    redirect(action: "show", id: crdI18nFormLabelInst.id)
  }

  /**
   * Create a new form label with a new form definition version
   */
  def createversion(Long id) {
    if (log.debugEnabled) log.debug "CREATE VERSION ${params}"
    def formLabelInstProto = CrdI18nFormLabel.get(id)
    if (!formLabelInstProto) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
      redirect(action: "list")
      return
    }

    def formLabelInst = formLabelService.createVersion(formLabelInstProto)
    flash.message = message(code: 'default.created.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), formLabelInst.id])
    redirect(action: 'listkey', id: formLabelInst?.formdefId)
  }

  // Create i18n labels for a locale.
  // The process definition key must be given as params.id.
  def createlocale(Long id) {
    if (log.debugEnabled) log.debug "CREATE LOCALE ${params}"
    if (id) {
      [formdefInst: PxdFormdef.get(id)]
    } else {
      redirect(action: 'list')
    }
  }

  def savelocale() {
    if (log.debugEnabled) log.debug "SAVE LOCALE ${params}"
    def key = params.formdefId
    def localeStr = params.locale
    def labelCount = formLabelService.createLabels(key, localeStr)
    flash.message = message(code: 'crdI18nFormLabel.generated.count', args: [labelCount])
    redirect(action: 'listkey', id: key)
  }

  def show(Long id) {
    if (log.debugEnabled) log.debug "SHOW ${params}"
    def crdI18nFormLabelInst = CrdI18nFormLabel.get(id)
    if (!crdI18nFormLabelInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
      redirect(action: "list")
      return
    }

    [crdI18nFormLabelInst: crdI18nFormLabelInst]
  }

  def edit(Long id) {
    if (log.debugEnabled) log.debug "EDIT ${params}"
    def formLabelInst = CrdI18nFormLabel.get(id)
    if (!formLabelInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
      redirect(action: "list")
      return
    }

    [formLabelInst: formLabelInst, formdefInst: PxdFormdef.get(formLabelInst?.formdefId)]
  }

  def update(Long id, Long version) {
    if (log.debugEnabled) log.debug "UPDATE ${params}"
    def formLabelInst = CrdI18nFormLabel.get(id)
    if (!formLabelInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
      redirect(action: "list")
      return
    }

    def formdefInst = PxdFormdef.get(formLabelInst?.formdefId)

    if (version != null) {
      if (formLabelInst.version > version) {
	formLabelInst.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel')] as Object[],
						"Another user has updated this CrdI18nFormLabel while you were editing")
	redirect(action: 'listkey', id: formdefInst?.id)
	return
      }
    }

    formLabelInst.properties = params

    if (!formLabelInst.save(flush: true)) {
      render(view: "edit", model: [formLabelInst: formLabelInst])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), formLabelInst.id])
    redirect(action: 'listkey', id: formdefInst?.id)
  }

  def delete(Long id) {
    def crdI18nFormLabelInst = CrdI18nFormLabel.get(id)
    if (!crdI18nFormLabelInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
      redirect(action: "list")
      return
    }

    try {
      crdI18nFormLabelInst.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
      redirect(action: "show", id: id)
    }
  }
}

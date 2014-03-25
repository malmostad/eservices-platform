package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException

class CrdI18nGuideUrlController {

  def guideUrlService

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: 'list', params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [crdI18nGuideUrlInstList: CrdI18nGuideUrl.list(params), crdI18nGuideUrlInstTotal: CrdI18nGuideUrl.count()]
  }

  /**
   * List guide URLs given a process key
   * id must be defined and must be a process key
   */
  def listkey(Integer max) {
    if (log.debugEnabled) log.debug "LISTKEY ${params}"
    params.max = Math.min(max ?: 10, 100)
    def key = params.id
    if (key) {
      def instSet = new TreeSet(CrdI18nGuideUrl.findAllByProcdefKey(key, params))
      def instTotal = CrdI18nGuideUrl.countByProcdefKey(key)
      [procdefKey: key, guideUrlInstList: instSet, guideUrlInstTotal: instTotal]
    } else {
      redirect(action: 'list')
    }
  }

  def create() {
    if (log.debugEnabled) log.debug "CREATE ${params}"
    if (params.id) params.procdefKey = params.id
    if (!params.procdefVer) params.procdefVer = '0'
    [crdI18nGuideUrlInst: new CrdI18nGuideUrl(params)]
  }

  def save() {
    def crdI18nGuideUrlInst = new CrdI18nGuideUrl(params)
    if (!crdI18nGuideUrlInst.save(flush: true)) {
      render(view: 'create', model: [crdI18nGuideUrlInst: crdI18nGuideUrlInst])
      return
    }

    flash.message = message(code: 'default.created.message',
    args: [message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl'), crdI18nGuideUrlInst.id])
    redirect(action: 'listkey', id: crdI18nGuideUrlInst.procdefKey)
  }

  def createduplicate(Long id) {
    def crdI18nGuideUrlInst = CrdI18nGuideUrl.get(id)
    if (!crdI18nGuideUrlInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl'), id])
      redirect(action: 'list')
      return
    }

    def crdI18nGuideUrlDupl = guideUrlService.duplicatePattern(crdI18nGuideUrlInst)
    redirect(action: 'listkey', id: crdI18nGuideUrlDupl.procdefKey)
  }

  def show(Long id) {
    def crdI18nGuideUrlInst = CrdI18nGuideUrl.get(id)
    if (!crdI18nGuideUrlInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl'), id])
      redirect(action: 'list')
      return
    }

    [crdI18nGuideUrlInst: crdI18nGuideUrlInst]
  }

  def edit(Long id) {
    def crdI18nGuideUrlInst = CrdI18nGuideUrl.get(id)
    if (!crdI18nGuideUrlInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl'), id])
      redirect(action: 'list')
      return
    }

    [crdI18nGuideUrlInst: crdI18nGuideUrlInst]
  }

  def update(Long id, Long version) {
    def crdI18nGuideUrlInst = CrdI18nGuideUrl.get(id)
    if (!crdI18nGuideUrlInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl'), id])
      redirect(action: 'list')
      return
    }

    if (version != null) {
      if (crdI18nGuideUrlInst.version > version) {
	crdI18nGuideUrlInst.errors.rejectValue('version', 'default.optimistic.locking.failure',
					       [message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl')] as Object[],
					       'Another user has updated this CrdI18nGuideUrl while you were editing')
	render(view: 'edit', model: [crdI18nGuideUrlInst: crdI18nGuideUrlInst])
	return
      }
    }

    crdI18nGuideUrlInst.properties = params

    if (!crdI18nGuideUrlInst.save(flush: true)) {
      render(view: 'edit', model: [crdI18nGuideUrlInst: crdI18nGuideUrlInst])
      return
    }

    def procdefKey = crdI18nGuideUrlInst.procdefKey
    flash.message = message(code: 'default.updated.message', args: [message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl'), crdI18nGuideUrlInst.id])
    redirect(action: 'listkey', id: procdefKey)
  }

  def delete(Long id) {
    def crdI18nGuideUrlInst = CrdI18nGuideUrl.get(id)
    if (!crdI18nGuideUrlInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl'), id])
      redirect(action: 'list')
      return
    }

    def procdefKey = crdI18nGuideUrlInst.procdefKey
    try {
      crdI18nGuideUrlInst.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl'), id])
      redirect(action: 'listkey', id: procdefKey)
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl'), id])
      redirect(action: 'show', id: crdI18nGuideUrlInst.id)
    }
  }
}

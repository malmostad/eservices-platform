package org.motrice.orifice

import org.springframework.dao.DataIntegrityViolationException

class OriPackageController {
  // Injection magic
  def packageService

  static allowedMethods = [save: "POST", saveexp: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [oriPackageInstList: OriPackage.list(params), oriPackageInstTotal: OriPackage.count()]
  }

  /**
   * Imported package -- no design yet
   */
  def create() {
    def appName = grailsApplication.metadata['app.name']
    def appVersion = grailsApplication.metadata['app.version']
    params.packageFormat = "${appName}-${appVersion}"
    params.siteName = grailsApplication.config.orifice.local.site.name
    params.originLocal = true
    params.siteTstamp = new java.sql.Timestamp(System.currentTimeMillis())
    [oriPackageInst: new OriPackage(params)]
  }

  /**
   * List formdefs for creating an export package
   */
  def listexp(Integer max) {
    if (log.debugEnabled) log.debug "LISTEXP: ${Util.clean(params)}, ${request.forwardURI}"
    def formdefList = packageService.allLocalFormdefs()?.sort()
    [oriFormdefInstList: formdefList]
  }

  /**
   * Create a package for export.
   */
  def saveexp() {
    if (log.debugEnabled) log.debug "SAVEEXP: ${Util.clean(params)}, ${request.forwardURI}"
    def packageName = params.packageName
    if (packageName) {
      try {
	def oriPackageInst = packageService.createExportPackage(packageName, params)
	redirect(action: 'list')
      } catch (Exception exc) {
	log.error "Exception in saveexp: ${exc.message}"
      }
    } else {
      flash.message = message(code: 'oriPackage.package.name.required')
      redirect(action: 'listexp')
    }
  }

  def xxxsaveexp() {
    def oriPackageInst = new OriPackage(params)
    if (!oriPackageInst.save(flush: true)) {
      render(view: "create", model: [oriPackageInst: oriPackageInst])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'oriPackage.label', default: 'OriPackage'), oriPackageInst.id])
    redirect(action: "show", id: oriPackageInst.id)
  }

  def save() {
    if (log.debugEnabled) log.debug "LISTEXP: ${Util.clean(params)}, ${request.forwardURI}"
    def oriPackageInst = new OriPackage(params)
    if (!oriPackageInst.save(flush: true)) {
      render(view: "create", model: [oriPackageInst: oriPackageInst])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'oriPackage.label', default: 'OriPackage'), oriPackageInst.id])
    redirect(action: "show", id: oriPackageInst.id)
  }

  def show(Long id) {
    def oriPackageInst = OriPackage.get(id)
    if (!oriPackageInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriPackage.label', default: 'OriPackage'), id])
      redirect(action: "list")
      return
    }

    [oriPackageInst: oriPackageInst]
  }

  def edit(Long id) {
    def oriPackageInst = OriPackage.get(id)
    if (!oriPackageInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriPackage.label', default: 'OriPackage'), id])
      redirect(action: "list")
      return
    }

    [oriPackageInst: oriPackageInst]
  }

  def update(Long id, Long version) {
    def oriPackageInst = OriPackage.get(id)
    if (!oriPackageInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriPackage.label', default: 'OriPackage'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (oriPackageInst.version > version) {
	oriPackageInst.errors.rejectValue("version", "default.optimistic.locking.failure",
					  [message(code: 'oriPackage.label', default: 'OriPackage')] as Object[],
					  "Another user has updated this OriPackage while you were editing")
	render(view: "edit", model: [oriPackageInst: oriPackageInst])
	return
      }
    }

    oriPackageInst.properties = params

    if (!oriPackageInst.save(flush: true)) {
      render(view: "edit", model: [oriPackageInst: oriPackageInst])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'oriPackage.label', default: 'OriPackage'), oriPackageInst.id])
    redirect(action: "show", id: oriPackageInst.id)
  }

  def delete(Long id) {
    def oriPackageInst = OriPackage.get(id)
    if (!oriPackageInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriPackage.label', default: 'OriPackage'), id])
      redirect(action: "list")
      return
    }

    try {
      oriPackageInst.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'oriPackage.label', default: 'OriPackage'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'oriPackage.label', default: 'OriPackage'), id])
      redirect(action: "show", id: id)
    }
  }
}

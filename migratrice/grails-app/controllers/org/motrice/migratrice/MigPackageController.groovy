package org.motrice.migratrice

import org.springframework.dao.DataIntegrityViolationException
import org.motrice.zip.ZipBuilder

class MigPackageController {
  // Injection magic
  def packageService

  static allowedMethods = [save: "POST", saveexp: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [migPackageInstList: MigPackage.list(params), migPackageInstTotal: MigPackage.count()]
  }

  /**
   * Imported package -- no design yet
   */
  def create() {
    def appName = grailsApplication.metadata['app.name']
    def appVersion = grailsApplication.metadata['app.version']
    params.packageFormat = "${appName}-${appVersion}"
    params.siteName = grailsApplication.config.migratrice.local.site.name
    params.originLocal = true
    params.siteTstamp = new java.sql.Timestamp(System.currentTimeMillis())
    [migPackageInst: new MigPackage(params)]
  }

  /**
   * Export a package as a zip file
   */
  def export(Long id) {
    def migPackageInst = MigPackage.get(id)
    if (!migPackageInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "list")
      return
    }
    /*
    new ZipBuilder(new FileOutputStream('/tmp/package.zip')).zip {
      entry("package#${migPackageInst.id}") {
	(migPackageInst as XML).render(new OutputStreamWriter(it, 'UTF-8'))
      }
    }
    */

    render(contentType: "text/xml") {
      migPackageInst
    }
    //render(view: 'show', model: [migPackageInst: migPackageInst])
  }

  /**
   * Install a package
   */
  def install() {
  }

  /**
   * List formdefs for creating an export package
   */
  def listexp(Integer max) {
    if (log.debugEnabled) log.debug "LISTEXP: ${Util.clean(params)}, ${request.forwardURI}"
    def formdefList = packageService.allLocalFormdefs()?.sort()
    [migFormdefInstList: formdefList]
  }

  /**
   * Create a package for export.
   */
  def saveexp() {
    if (log.debugEnabled) log.debug "SAVEEXP: ${Util.clean(params)}, ${request.forwardURI}"
    def packageName = params.packageName
    if (packageName) {
      try {
	def migPackageInst = packageService.createExportPackage(packageName, params)
	redirect(action: 'list')
      } catch (Exception exc) {
	log.error "Exception in saveexp: ${exc.message}"
      }
    } else {
      flash.message = message(code: 'migPackage.package.name.required')
      redirect(action: 'listexp')
    }
  }

  def save() {
    if (log.debugEnabled) log.debug "LISTEXP: ${Util.clean(params)}, ${request.forwardURI}"
    def migPackageInst = new MigPackage(params)
    if (!migPackageInst.save(flush: true)) {
      render(view: "create", model: [migPackageInst: migPackageInst])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'migPackage.label', default: 'MigPackage'), migPackageInst.id])
    redirect(action: "show", id: migPackageInst.id)
  }

  def show(Long id) {
    def migPackageInst = MigPackage.get(id)
    if (!migPackageInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "list")
      return
    }

    [migPackageInst: migPackageInst]
  }

  def edit(Long id) {
    def migPackageInst = MigPackage.get(id)
    if (!migPackageInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "list")
      return
    }

    [migPackageInst: migPackageInst]
  }

  def update(Long id, Long version) {
    def migPackageInst = MigPackage.get(id)
    if (!migPackageInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (migPackageInst.version > version) {
	migPackageInst.errors.rejectValue("version", "default.optimistic.locking.failure",
					  [message(code: 'migPackage.label', default: 'MigPackage')] as Object[],
					  "Another user has updated this MigPackage while you were editing")
	render(view: "edit", model: [migPackageInst: migPackageInst])
	return
      }
    }

    migPackageInst.properties = params

    if (!migPackageInst.save(flush: true)) {
      render(view: "edit", model: [migPackageInst: migPackageInst])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'migPackage.label', default: 'MigPackage'), migPackageInst.id])
    redirect(action: "show", id: migPackageInst.id)
  }

  def delete(Long id) {
    def migPackageInst = MigPackage.get(id)
    if (!migPackageInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "list")
      return
    }

    try {
      migPackageInst.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "show", id: id)
    }
  }
}

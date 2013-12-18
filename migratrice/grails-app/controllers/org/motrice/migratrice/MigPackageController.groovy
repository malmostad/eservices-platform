package org.motrice.migratrice

import org.springframework.dao.DataIntegrityViolationException

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
   * Upload a package
   */
  def upload() {
    def file = request.getFile('pkgUpload')
    if (file.empty) {
      flash.message = message(code: 'migPackage.upload.file.empty')
    } else {
      try {
	// If you want to save the file there is the transferTo(File) method
	packageService.importPackage(file.inputStream)
      } catch (MigratriceException exc) {
	flash.message = message(code: exc.code)
      } finally {
	if (!log.debugEnabled) tempFile.delete()
      }
    }

    redirect(action: "list")
  }

  /**
   * Download a package as a zip file
   */
  def export(Long id) {
    def migPackageInst = MigPackage.get(id)
    if (!migPackageInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "list")
      return
    }

    def downloadFileName = packageService.downloadFileName(migPackageInst)
    def tempFile = File.createTempFile('migpackage', 'zip')
    try {
      packageService.toZip(migPackageInst, new FileOutputStream(tempFile))
      response.setHeader('Content-Disposition', "attachment;filename=${downloadFileName}")
      response.contentType = 'application/octet-stream'
      response.outputStream << tempFile.bytes
    } catch (MigratriceException exc) {
      flash.message = message(code: exc.code)
    } finally {
      if (!log.debugEnabled) tempFile.delete()
    }

    render(view: 'show', model: [migPackageInst: migPackageInst])
  }

  /**
   * Install a package.
   */
  def install(Long id) {
    if (log.debugEnabled) log.debug "INSTALL: ${Util.clean(params)}, ${request.forwardURI}"
    def obj = packageService.findAndCheckPackageToInstall(id)
    if (obj instanceof String) {
      flash.message = message(code: obj, args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "list")
      return
    }

    try {
      packageService.installPackage(obj)
      flash.message = message(code: 'migPackage.install.complete', args: [obj.packageName])
      redirect(action: 'show', id: obj.id)
    } catch (MigratriceException exc) {
      flash.message = message(code: exc.code)
      redirect(action: "list")
    }
  }

  /**
   * List formdefs for creating an export package
   */
  def listexp(Integer max) {
    if (log.debugEnabled) log.debug "LISTEXP: ${Util.clean(params)}, ${request.forwardURI}"
    def formdefList = null
    try {
      formdefList = packageService.allLocalFormdefs()?.sort()
      [migFormdefInstList: formdefList]
    } catch (MigratriceException exc) {
      flash.message = message(code: exc.code)
      redirect(action: 'list')
    }
  }

  /**
   * Create a package from a list of formdefs defined by the user
   */
  def saveexp() {
    if (log.debugEnabled) log.debug "SAVEEXP: ${Util.clean(params)}, ${request.forwardURI}"
    def packageName = params.packageName
    if (packageName) {
      try {
	def migPackageInst = packageService.createExportPackage(packageName, params)
	redirect(action: 'list')
      } catch (MigratriceException exc) {
	flash.message = message(code: exc.code)
	redirect(action: 'list')
      }
    } else {
      flash.message = message(code: 'migPackage.package.name.required')
      redirect(action: 'listexp')
    }
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

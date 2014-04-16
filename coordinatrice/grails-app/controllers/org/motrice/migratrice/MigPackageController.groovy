package org.motrice.migratrice

import org.springframework.dao.DataIntegrityViolationException

import org.motrice.coordinatrice.ServiceException

class MigPackageController {
  // Injection magic
  def packageService

  static allowedMethods = [save: "POST", install: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [migPackageInstList: MigPackage.list(params), migPackageInstTotal: MigPackage.count()]
  }

  /**
   * Select a migration package to upload.
   */
  def uploadprepare() {
    if (log.debugEnabled) log.debug "PREPARE UPLOAD ${params}"
  }

  /**
   * Upload a package
   */
  def upload() {
    if (log.debugEnabled) log.debug "UPLOAD ${params}"
    def file = request.getFile('pkgUpload')
    if (file == null || file.empty) {
      flash.message = message(code: 'migPackage.upload.file.empty')
    } else {
      try {
	// If you want to save the file there is the transferTo(File) method
	packageService.importPackage(file.inputStream)
      } catch (MigratriceException exc) {
	flash.message = message(code: exc.code)
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
  def installpre(Long id) {
    if (log.debugEnabled) log.debug "INSTALLPRE: ${Util.clean(params)}, ${request.forwardURI}"
    def obj = packageService.findAndCheckPackageToInstall(id)
    if (obj instanceof String) {
      flash.message = message(code: obj, args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "list")
      return
    }

    [migPackageInst: obj]
  }

  /**
   * Install a package. Requires parameter installOptions (String).
   * The options are shown as radio buttons.
   */
  def install(Long id) {
    if (log.debugEnabled) log.debug "INSTALL: ${Util.clean(params)}, ${request.forwardURI}"
    def obj = packageService.findAndCheckPackageToInstall(id)
    if (obj instanceof String) {
      flash.message = message(code: obj, args: [message(code: 'migPackage.label', default: 'MigPackage'), id])
      redirect(action: "list")
      return
    }

    def lw = null
    def installMode = params.installOptions
    def installModeMsg = message(code: installMode)
    try {
      lw = packageService.installPackage(obj, installMode, installModeMsg)
      obj.createReport(lw.toString())
      flash.message = message(code: lw.code, args: lw.args)
      redirect(action: 'show', id: obj.id)
    } catch (MigratriceException exc) {
      if (lw) {
	obj.createReport(lw.body)
	flash.message = message(code: lw.code, args: lw.args)
      } else {
	flash.message = exc.message
      }

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
      [migFormdefInstList: formdefList, packageName: params.packageName ?: '']
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
	return
      } catch (ServiceException exc) {
	flash.message = message(code: exc.key)
	redirect(action: 'listexp', params: [packageName: packageName])
	return
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

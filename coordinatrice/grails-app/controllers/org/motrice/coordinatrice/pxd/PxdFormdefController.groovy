package org.motrice.coordinatrice.pxd

import org.motrice.coordinatrice.MtfStartFormDefinition

class PxdFormdefController {
  def grailsApplication
  def formService
  def procdefService

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    if (!params.sort) params.sort = 'path'
    String orbeonBaseUri = grailsApplication.config.coordinatrice.orbeon.builder.base.uri
    def formdefList = PxdFormdef.list(params)
    // Find the latest published version of each form definition in the list
    def pubMap = [:]
    formdefList.each {formdef ->
      def latest = PxdFormdefVer.latestPublished(formdef)
      if (latest) {
	pubMap[formdef.id] = [id: latest.id, version: String.format('v%03d', latest.fvno)]
      }
    }
    [pxdFormdefInstList: formdefList, pxdFormdefInstTotal: PxdFormdef.count(),
    pubMap: pubMap, orbeonUri: orbeonBaseUri]
  }

  def show(Long id) {
    def pxdFormdefInst = PxdFormdef.get(id)
    if (!pxdFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), id])
      redirect(action: "list")
      return
    }

    // Generate the list of versions
    def formdefVerList = []
    if (pxdFormdefInst.forms) {
      pxdFormdefInst.forms.each {fdv ->
	// Check if this version is used as start form
	def process = null
	def startForm = formService.findAsStartForm(fdv)
	if (startForm) {
	  process = procdefService.findProcessDefinition(startForm.procdefId)
	}

	formdefVerList << [formdefVer: fdv, procdef: process]
      }
    }

    String orbeonBaseUri = grailsApplication.config.coordinatrice.orbeon.builder.base.uri
    [pxdFormdefInst: pxdFormdefInst, formdefVerList: formdefVerList, orbeonUri: orbeonBaseUri]
  }

}

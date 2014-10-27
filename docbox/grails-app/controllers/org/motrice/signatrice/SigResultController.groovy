package org.motrice.signatrice

import org.springframework.dao.DataIntegrityViolationException

class SigResultController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    if (!params.sort) {
      params.sort = 'dateCreated'
      params.order = 'desc'
    }
    [sigResultInstList: SigResult.list(params), sigResultInstTotal: SigResult.count()]
  }

  def show(Long id) {
    def sigResultInst = SigResult.get(id)
    if (!sigResultInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigResult.label', default: 'SigResult'), id])
      redirect(action: "list")
      return
    }

    [sigResultInst: sigResultInst]
  }

  def collect(Long id) {
    def sigResultInst = SigResult.get(id)
    if (!sigResultInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigScheme.label', default: 'SigScheme'), id])
      redirect(action: "list")
      return
    }

    [sigResultInst: sigResultInst, autoStartToken: sigResultInst.autoStartToken, returnUrl: '']
  }

}

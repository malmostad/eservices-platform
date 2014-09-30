package org.motrice.signatrice

import org.springframework.dao.DataIntegrityViolationException

class SigAttributeController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [sigAttributeInstList: SigAttribute.list(params), sigAttributeInstTotal: SigAttribute.count()]
  }

  def show(Long id) {
    def sigAttributeInst = SigAttribute.get(id)
    if (!sigAttributeInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigAttribute.label', default: 'SigAttribute'), id])
      redirect(action: "list")
      return
    }

    [sigAttributeInst: sigAttributeInst]
  }

  /**
   * Like show, but the value is base64-decoded
   */
  def base64Decode(Long id) {
    def sigAttributeInst = SigAttribute.get(id)
    if (!sigAttributeInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigAttribute.label', default: 'SigAttribute'), id])
      redirect(action: "list")
      return
    }

    def decodedValue = new String(sigAttributeInst.value.decodeBase64(), 'UTF-8')
    render(view: 'show', model: [sigAttributeInst: sigAttributeInst, decodedValue: decodedValue])
  }
}

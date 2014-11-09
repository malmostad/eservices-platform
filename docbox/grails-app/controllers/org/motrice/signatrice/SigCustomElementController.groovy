package org.motrice.signatrice

class SigCustomElementController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [sigCustomElementObjList: SigCustomElement.list(params), sigCustomElementObjTotal: SigCustomElement.count()]
  }

  def show(Long id) {
    def sigCustomElementObj = SigCustomElement.get(id)
    if (!sigCustomElementObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigCustomElement.label', default: 'SigCustomElement'), id])
      redirect(action: "list")
      return
    }

    [sigCustomElementObj: sigCustomElementObj]
  }
}

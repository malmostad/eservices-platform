package org.motrice.coordinatrice.pxd

class PxdFormdefController {

  def grailsApplication

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    if (!params.sort) params.sort = 'path'
    String orbeonBaseUri = grailsApplication.config.coordinatrice.orbeon.builder.base.uri
    [pxdFormdefInstList: PxdFormdef.list(params), pxdFormdefInstTotal: PxdFormdef.count(),
    orbeonUri: orbeonBaseUri]
  }

  def show(Long id) {
    def pxdFormdefInst = PxdFormdef.get(id)
    if (!pxdFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), id])
      redirect(action: "list")
      return
    }

    String orbeonBaseUri = grailsApplication.config.coordinatrice.orbeon.builder.base.uri
    [pxdFormdefInst: pxdFormdefInst, orbeonUri: orbeonBaseUri]
  }

}

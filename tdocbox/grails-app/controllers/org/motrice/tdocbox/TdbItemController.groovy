package org.motrice.tdocbox

import org.springframework.dao.DataIntegrityViolationException

class TdbItemController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  private final static CONT_DISP = 'Content-Disposition'

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [tdbItemObjList: TdbItem.list(params), tdbItemObjTotal: TdbItem.count()]
  }

  def show(Long id) {
    def tdbItemObj = TdbItem.get(id)
    if (!tdbItemObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbItem.label', default: 'TdbItem'), id])
      redirect(action: "list")
      return
    }

    [tdbItemObj: tdbItemObj]
  }

  def downloadItem(Long id) {
    def tdbItemObj = TdbItem.get(id)
    if (!tdbItemObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'form.label', default: 'Form'), id])
      redirect(action: "list")
      return
    }

    response.setHeader(CONT_DISP, "attachment;filename=${tdbItemObj.fileName}")
    if (tdbItemObj.binary) {
      response.contentType = 'application/octet-stream'
      response.outputStream << tdbItemObj.bytes
    } else {
      response.contentType = 'text/plain;charset=UTF-8'
      response.outputStream << tdbItemObj.text
    }

    render(view: 'show', model: [tdbItemObj: tdbItemObj])
  }

}

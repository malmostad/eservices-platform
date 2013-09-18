package org.motrice.docbox.doc

import org.springframework.dao.DataIntegrityViolationException

class BoxContentsController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [boxContentsObjList: BoxContents.list(params), boxContentsObjTotal: BoxContents.count()]
  }

  def create() {
    [boxContentsObj: new BoxContents(params)]
  }

  def save() {
    def boxContentsObj = new BoxContents(params)
    if (!boxContentsObj.save(flush: true)) {
      render(view: "create", model: [boxContentsObj: boxContentsObj])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'boxContents.label', default: 'BoxContents'), boxContentsObj.id])
    redirect(action: "show", id: boxContentsObj.id)
  }

  def show(Long id) {
    def boxContentsObj = BoxContents.get(id)
    if (!boxContentsObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxContents.label', default: 'BoxContents'), id])
      redirect(action: "list")
      return
    }

    [boxContentsObj: boxContentsObj]
  }

  def downloadContent(Long id) {
    def boxContentsObj = BoxContents.get(id)
    if (!boxContentsObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'form.label', default: 'Form'), id])
      redirect(action: "list")
      return
    }

    def fname = boxContentsObj.name
    response.setHeader('Content-Disposition', "attachment;filename=${fname}")
    if (boxContentsObj.text != null) {
      response.contentType = 'application/xml;charset=UTF-8'
      response.outputStream << boxContentsObj.text
    } else if (boxContentsObj.stream != null) {
      response.contentType = 'application/octet-stream'
      response.outputStream << boxContentsObj.stream
    }

    render(view: 'show', model: [boxContentsObj: boxContentsObj])
  }

  def edit(Long id) {
    def boxContentsObj = BoxContents.get(id)
    if (!boxContentsObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxContents.label', default: 'BoxContents'), id])
      redirect(action: "list")
      return
    }

    [boxContentsObj: boxContentsObj]
  }

  def update(Long id, Long version) {
    def boxContentsObj = BoxContents.get(id)
    if (!boxContentsObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxContents.label', default: 'BoxContents'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (boxContentsObj.version > version) {
	boxContentsObj.errors.rejectValue("version", "default.optimistic.locking.failure",
					  [message(code: 'boxContents.label', default: 'BoxContents')] as Object[],
					  "Another user has updated this BoxContents while you were editing")
	render(view: "edit", model: [boxContentsObj: boxContentsObj])
	return
      }
    }

    boxContentsObj.properties = params

    if (!boxContentsObj.save(flush: true)) {
      render(view: "edit", model: [boxContentsObj: boxContentsObj])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'boxContents.label', default: 'BoxContents'), boxContentsObj.id])
    redirect(action: "show", id: boxContentsObj.id)
  }

  def delete(Long id) {
    def boxContentsObj = BoxContents.get(id)
    if (!boxContentsObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxContents.label', default: 'BoxContents'), id])
      redirect(action: "list")
      return
    }

    try {
      boxContentsObj.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'boxContents.label', default: 'BoxContents'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'boxContents.label', default: 'BoxContents'), id])
      redirect(action: "show", id: id)
    }
  }
}

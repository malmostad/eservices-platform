/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
package org.motrice.docbox.doc

import org.springframework.dao.DataIntegrityViolationException

class BoxContentsController {
  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
  private final static CONT_DISP = 'Content-Disposition'

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
    response.setHeader(CONT_DISP, "attachment;filename=${boxContentsObj.fileName}")
    response.contentType = boxContentsObj.contentType
    if (boxContentsObj.binary) {
      response.outputStream << boxContentsObj.stream
    } else {
      response.outputStream << boxContentsObj.text
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

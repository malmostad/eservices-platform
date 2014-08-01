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
package org.motrice.postxdb

import org.motrice.postxdb.PxdItem;
import org.springframework.dao.DataIntegrityViolationException

class PxdItemController {

  def itemService

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 15, 100)
    [pxdItemObjList: PxdItem.list(params), pxdItemObjTotal: PxdItem.count()]
  }

  /**
   * A limited list: items where the formDef attribute equals the path of a
   * form definition version.
   * id must be the id of a PxdFormdefVer
   */
  def listVersion(Long id) {
    def pxdFormdefVerObj = PxdFormdefVer.get(id)
    if (!pxdFormdefVerObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer'), id])
      redirect(action: "list")
      return
    }

    def itemList = PxdItem.findAllByFormDef(pxdFormdefVerObj.path)
    def total = itemList.size()
    render(view: 'list', model: [pxdItemObjList: itemList, pxdItemObjTotal:total])
  }

  def create() {
    [pxdItemObj: new PxdItem(params)]
  }

  def save() {
    def pxdItemObj = new PxdItem(params)
    if (!pxdItemObj.save(flush: true)) {
      render(view: "create", model: [pxdItemObj: pxdItemObj])
      return
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), pxdItemObj.id])
    redirect(action: "show", id: pxdItemObj.id)
  }

  def show(Long id) {
    def pxdItemObj = PxdItem.get(id)
    if (!pxdItemObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
      redirect(action: "list")
      return
    }

    [pxdItemObj: pxdItemObj]
  }

  def downloadContent(Long id) {
    def pxdItemObj = PxdItem.get(id)
    if (!pxdItemObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'form.label', default: 'Form'), id])
      redirect(action: "list")
      return
    }

    def fname = pxdItemObj.path.replaceAll('/', '_')
    response.setHeader('Content-Disposition', "attachment;filename=${fname}")
    if (pxdItemObj.text != null) {
      response.contentType = 'application/xml;charset=UTF-8'
      response.outputStream << pxdItemObj.text
    } else if (pxdItemObj.stream != null) {
      response.contentType = 'application/octet-stream'
      response.outputStream << pxdItemObj.stream
    }

    render(view: 'show', model: [pxdItemObj: pxdItemObj])
  }

  def edit(Long id) {
    if (log.debugEnabled) log.debug "EDIT ${params}"
    def pxdItemObj = PxdItem.get(id)
    if (!pxdItemObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
      redirect(action: "list")
      return
    }

    [pxdItemObj: pxdItemObj]
  }

  def update(SearchReplaceCommand cmd, Long version) {
    if (log.debugEnabled) log.debug "UPDATE ${params}: ${cmd}"
    def pxdItemObj = PxdItem.get(cmd?.id)
    if (pxdItemObj) {
      cmd.pxdItemObj = pxdItemObj
    } else {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (pxdItemObj.version > version) {
	pxdItemObj.errors.rejectValue("version", "default.optimistic.locking.failure",
				      [message(code: 'pxdItem.label', default: 'PxdItem')] as Object[],
				      "Another user has updated this PxdItem while you were editing")
	render(view: "edit", model: [pxdItemObj: pxdItemObj])
	return
      }
    }

    String editedText = itemService.searchReplace(cmd)
    if (editedText != null) {
      pxdItemObj.text = editedText
      
      if (!pxdItemObj.save(flush: true)) {
	render(view: "edit", model: [pxdItemObj: pxdItemObj])
	return
      } else {
	flash.message = message(code: 'default.updated.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), pxdItemObj.id])
      }
    } else {
      flash.message = message(code: 'pxdItem.no.update')
    }

    redirect(action: "show", id: pxdItemObj.id)
  }

  def delete(Long id) {
    def pxdItemObj = PxdItem.get(id)
    if (!pxdItemObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
      redirect(action: "list")
      return
    }

    try {
      pxdItemObj.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
      redirect(action: "show", id: id)
    }
  }

}

class SearchReplaceCommand {
  Long id
  PxdItem pxdItemObj
  String search
  String replace
  Boolean globalFlag

  String toString() {
    "[SearchReplaceCmd: ${id}, ${pxdItemObj}, '${search}'/'${replace}', global: ${globalFlag}]"
  }
}

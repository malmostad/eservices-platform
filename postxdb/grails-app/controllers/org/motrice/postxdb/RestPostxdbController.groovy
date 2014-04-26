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

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.*

/**
 * REST interface for reading the database according to the postxdb data model.
 * URL mappings begin with /postxdb/...
 * See BootStrap for XML conversion init.
 */
class RestPostxdbController {
  // PostxdbService injection
  def postxdbService

  static CONT_DISP = 'Content-Disposition'

  /**
   * Get form definition metadata (PxdFormdef).
   * Null id lists all form definitions.
   */
  def formdefget(Long id) {
    if (log.debugEnabled) log.debug "DEFGET(${id}): ${Util.clean(params)}, ${request.forwardURI}"
    def formdefList = postxdbService.formdefGet(id)
    if (formdefList) {
      response.status = 200
      render(text: formdefList as XML, encoding:'UTF-8')
    } else {
      render(status: 404, text: 'No formdef was found', contentType: 'text/plain')
    }
  }

  /**
   * Get form definition version metadata (PxdFormdefVer).
   * If id is null exactly one of uuid and formdef parameters is required.
   */
  def versionget(Long id) {
    if (log.debugEnabled) log.debug "VERGET(${id}): ${Util.clean(params)}, ${request.forwardURI}"
    def map = postxdbService.formdefVerGet(id, params.uuid, params.formdef)
    response.status = map.status
    if (map.status == 200) {
      render(text: map.list as XML, encoding:'UTF-8')
    } else {
      render(contentType: 'text/plain', text: map.message)
    }
  }

  /**
   * Get a list of form definition items (a list of item ids).
   * Exactly one of uuid and formdef parameters is required.
   */
  def defitemget() {
    if (log.debugEnabled) log.debug "DEFITEM: ${Util.clean(params)}, ${request.forwardURI}"
    def map = postxdbService.defItemGet(params.uuid, params.formdef)
    response.status = map.status
    if (map.status == 200) {
      map.list.each {item -> item.formref = map.formref}
      render(text: map.list as XML, encoding:'UTF-8')
    } else {
      render(contentType: 'text/plain', text: map.message)
    }
  }

  /**
   * Get a list of form instance items (a list of item ids).
   * The formdefver parameter is required.
   */
  def institemget() {
    if (log.debugEnabled) log.debug "INSTITEM: ${Util.clean(params)}, ${request.forwardURI}"
    def itemList = postxdbService.instItemGet(params.formdefver)
    if (itemList) {
      response.status = 200
      render(text: itemList as XML, encoding:'UTF-8')
    } else {
      render(status: 404, text: 'No item found', contentType: 'text/plain')
    }
  }

  /**
   * Get a an item, i.e. its contents.
   */
  def itemgetbyid(Long id) {
    if (log.debugEnabled) log.debug "ITEMBYID(${id}): ${Util.clean(params)}, ${request.forwardURI}"
    def item = postxdbService.itemGet(id)
    if (item) {
      def fileName = item.path.replace('/', '#')
      String contDisp = "attachment;filename=${fileName}"
      if (item.xmlFormat()) {
	response.setHeader(CONT_DISP, contDisp)
	render(status: 200, text: item.text, contentType: 'application/xml', encoding: 'UTF-8')
      } else {
	response.setHeader(CONT_DISP, contDisp)
	response.status = 200
	response.contentType = 'application/octet-stream'
	response.getOutputStream().withStream {stream ->
	  stream.bytes = item.stream
	}
      }
    } else {
      response.status = 404
    }
  }

}

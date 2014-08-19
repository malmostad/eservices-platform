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

import org.motrice.postxdb.Util;

/**
 * URL mapping:
 * "/rest/db/orbeon-pe/fr/$app/$form/data/$uuid/$resource"(controller: 'RestFormdata') {
 *  action = [GET: 'getop', PUT: 'putop', DELETE: 'delete']
 * }
 */
class RestFormdataController {
  // RestService injection
  def restService

  /**
   * Get a resource for a form instance
   */
  def getop() {
    if (log.debugEnabled) log.debug "GETOP: ${Util.clean(params)}, ${request.forwardURI}"
    def itemObj = null
    if (params?.resource?.startsWith('data')) {
      itemObj = restService.findInstanceItem(params.uuid)
    } else {
      itemObj = restService.findInstanceResource(params.uuid, params.resource)
    }

    // Return the contents
    if (itemObj) {
      if (log.debugEnabled) log.debug "getop FOUND: ${itemObj}"
      if (itemObj.xmlFormat()) {
	render(text: itemObj.text, contentType: 'application/xml;charset=UTF-8', encoding: 'UTF-8')
      } else {
	response.status = 200
	response.contentType = 'application/octet-stream'
	// response.contentLength = itemObj.size
	response.getOutputStream().withStream {stream ->
	  stream.bytes = itemObj.stream
	}
      }
    } else {
      if (log.infoEnabled) log.info "getop >> 404"
      render(status: 404, text: 'Item was not found', contentType: 'text/plain')
    }
  }

  /**
   * Save a form instance resource
   * As a rule, Orbeon saves binary stuff (logo etc) before saving the form definition.
   */
  def putop() {
    if (log.debugEnabled) log.debug "PUTOP: ${Util.clean(params)}, ${request.forwardURI}"
    def itemObj = null
    def status = null
    // Two distinct flows depending on the input
    try {
      if (request.format == 'xml') {
	itemObj = restService.createInstanceItem(params.app, params.form, params.uuid,
						 params.resource, request.reader.text)
      } else {
	itemObj = restService.createInstanceResource(params.app, params.form, params.uuid,
						     params.resource, request)
      }
    } catch (PostxdbException exc) {
      status = exc.http
      if (log.debugEnabled) log.debug "PUTOP >> ${status} ${message(code: exc?.code)}"
    }

    if (status) {
      render(status: status)
    } else if (itemObj) {
      // The response must be empty.
      render(status: 201)
    } else {
      String msg = 'CONFLICT PxdItem not found'
      log.warn "putop ${msg}"
      render(status: 409, text: msg, contentType: 'text/plain')
    }
  }

  /**
   * Delete a form definition resource.
   */
  def delete() {
    if (log.debugEnabled) log.debug "DELETE (no-op): ${params}, ${request.forwardURI}"
  }
}

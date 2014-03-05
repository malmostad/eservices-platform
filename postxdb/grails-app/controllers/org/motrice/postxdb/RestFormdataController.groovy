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
    // Two distinct flows depending on the input
    if (request.format == 'xml') {
      if (log.debugEnabled) log.debug "putop XML >> createInstanceItem"
      itemObj = restService.createInstanceItem(params.app, params.form, params.uuid,
					       params.resource, request.reader.text)
      if (log.debugEnabled) log.debug "putop XML << ${itemObj}"
    } else {
      if (log.debugEnabled) log.debug "putop BIN >> createFormdefResource"
      itemObj = restService.createInstanceResource(params.app, params.form, params.uuid,
						   params.resource, request)
      if (log.debugEnabled) log.debug "putop BIN << ${itemObj}"
    }

    if (itemObj) {
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

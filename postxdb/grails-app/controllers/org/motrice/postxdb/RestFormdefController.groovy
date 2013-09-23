package org.motrice.postxdb

import org.motrice.postxdb.PxdItem;
import org.motrice.postxdb.Util;
import org.springframework.dao.DataIntegrityViolationException

import org.motrice.postxdb.FormdefPath;

/**
 * REST interface for published form definitions.
 * URL mappings:
 * "/rest/db/orbeon-pe/fr/$app/$form/form/$resource"(controller: 'RestFormdef') {
 *   action = [GET: 'getop', PUT: 'putop', DELETE: 'delete']
 * }
 *"/rest/db/orbeon-pe/fr/orbeon/builder/data"(controller: 'RestFormdef', action: 'list')
 */
class RestFormdefController {
  // RestService injection
  def restService

  /**
   * Generate a list of forms available for editing.
   * For every form only the current draft is editable.
   * RETURNS an XML document.
   */
  def list() {
    if (log.debugEnabled) log.debug "LIST: ${Util.clean(params)}, ${request.forwardURI}"
    Integer max = params?.'page-size' as Integer
    if (!max || max <= 0 || max > 100) max = 15
    Integer page = params?.'page-number' as Integer
    if (!page || page < 1 || page > 999) page = 1
    Integer offset = (page - 1) * max

    // The request may be a plain list or a search
    def formInfo = null
    if (params?.value) {
      // The request comes with search parameters
      def map = restService.extractSearchParameters(params.value, params?.path)
      formInfo = restService.doSearch(map)
    } else {
      // This is a plain list
      formInfo = restService.findEditableForms(max, offset)
    }
    def list = formInfo.list
    def total = formInfo.total
    render(status: 200, contentType: 'text/xml;charset=UTF-8') {
      "exist:result"("xmlns:exist": "http://exist.sourceforge.net/NS/exist", "exist:hits": "1",
      "exist:start": "1", "exist:count": "1") {
	documents(total: total, 'search-total': total, 'page-size': max, 'page-number': page, query: "")
	{
	  for (doc in list) {
	    def path = new FormdefPath(doc.path)
	    // The value of the 'name' attribute must be the uuid
	    document(created: doc.createdf(), 'last-modified': doc.updatedf(), offline: "",
	    name: doc?.formdef?.uuid)
	    {
	      details {
		detail(doc.appName)
		detail(path.toString(false))
		detail(doc.title ?: '--')
		detail(doc.description ?: '--')
	      }
	    }
	  }
	}
      }
    }

  }

  /**
   * Get a resource for a published form definition version.
   * Currently only form definition itself, an xml document.
   */
  def getop() {
    if (log.debugEnabled) log.debug "GETOP: ${Util.clean(params)}, ${request.forwardURI}"
    def path = new FormdefPath("${params?.app}/${params?.form}")
    def itemObj = null
    if (params?.resource == 'form.xhtml') {
      String itemPath = "${path}/form.xhtml"
      itemObj = PxdItem.findByPath(itemPath)
    } else {
      itemObj = PxdItem.findByPath(params?.resource)
    }

    if (itemObj) {
      if (log.debugEnabled) log.debug "getop FOUND: ${itemObj}"
      if (itemObj.xmlFormat()) {
	render(text: itemObj.text, contentType: 'application/xml;charset=UTF-8', encoding: 'UTF-8')
      } else {
	response.status = 200
	response.contentType = 'application/octet-stream'
	//response.contentLength = itemObj.size
	response.getOutputStream().withStream {stream ->
	  stream.bytes = itemObj.stream
	}
      }
    } else {
      if (log.debugEnabled) log.debug "getop item NOT FOUND: ${params?.app}/${params?.form}"
      render(status: 404, text: 'Item was not found', contentType: 'text/plain')
    }
  }

  /**
   * Publish and store a form definition resource.
   * Usually called by the form editor.
   * Currently only the form definition itself, an xml document, is stored this way.
   * If this is the first form definition version a new Formdef is created.
   */
  def putop() {
    if (log.debugEnabled) log.debug "PUTOP: ${Util.clean(params)}, ${request.forwardURI}"
    def itemObj = null
    // Two distinct flows depending on the input
    if (request.format == 'xml') {
      if (log.debugEnabled) log.debug "putop XML >> createPublishedItem"
      itemObj = restService.createPublishedItem(params.resource, request.reader.text)
      if (log.debugEnabled) log.debug "putop XML << ${itemObj}"
    } else {
      // Happens only in Orbeon 4
      if (log.debugEnabled) log.debug "putop BIN >> createPublishedResource"
      itemObj = restService.createPublishedResource(params.app, params.form, params.resource, request)
      if (log.debugEnabled) log.debug "putop BIN << ${itemObj}"
    }

    if (itemObj) {
      // The response must be empty, or Orbeon chokes
      render(status: 201)
    } else {
      def msg = 'CONFLICT PxdItem not found'
      log.warn "putop ${msg}"
      render(status: 409, text: msg, contentType: 'text/plain')
    }
  }

  /**
   * Delete a form definition resource.
   */
  def delete() {
    if (log.debugEnabled) log.debug "DELETE (no-op): ${params}"
  }
}

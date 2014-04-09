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

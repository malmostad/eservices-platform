package org.motrice.postxdb

import org.springframework.dao.DataIntegrityViolationException

/**
 * REST interface for reading the database according to the postxdb data model.
 * URL mappings begin with /postxdb/...
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
      render(status: 200, contentType: 'text/xml', encoding: 'UTF-8') {
	formdefs {
	  for (f in formdefList) {
	    formdef(dbid: f.id, version: f.version, created: f.createdr(), updated: f.updatedr()) {
	      app(f.appName)
	      form(f.formName)
	      uuid(f.uuid)
	      currentDraft(f.currentDraft)
	    }
	  }
	}
      }
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
      render(contentType: 'text/xml', encoding: 'UTF-8') {
	formdefvers {
	  for (v in map.list) {
	    formdefver(dbid: v.id, formdef: v.formdef.id, created: v.createdr()) {
	      app(v.appName)
	      form(v.formName)
	      path(v.path)
	      title(v.title)
	      description(v.description)
	      language(v.language)
	    }
	  }
	}
      }
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
      render(contentType: 'text/xml', encoding: 'UTF-8') {
	items {
	  for (i in map.list) {
	    item(dbid: i.id, instance: i.instance, created: i.createdr(), updated: i.updatedr()) {
	      path(i.path)
	      uuid(i.uuid)
	      formDef(i.formDef)
	      format(i.format)
	      size(i.size)
	      sha1(i.sha1)
	    }
	  }
	}
      }
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
      render(status: 200, contentType: 'text/xml', encoding: 'UTF-8') {
	items {
	  for (i in itemList) {
	    item(dbid: i.id, instance: i.instance, created: i.createdr(), updated: i.updatedr()) {
	      path(i.path)
	      uuid(i.uuid)
	      formDef(i.formDef)
	      format(i.format)
	      size(i.size)
	      sha1(i.sha1)
	    }
	  }
	}
      }
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

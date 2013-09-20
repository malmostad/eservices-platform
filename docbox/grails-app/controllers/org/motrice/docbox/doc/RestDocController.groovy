package org.motrice.docbox.doc

import org.motrice.docbox.DocData
import org.motrice.docbox.Util
import org.motrice.docbox.form.PxdFormdefVer
import org.motrice.docbox.form.PxdItem

class RestDocController {
  def docService
  def pdfService

  /**
   * Create a PDF/A document given a formDataUuid and return metadata.
   * Do not create anything in case the formDataUuid has already been created.
   * Returns status 201 if the document was created at this time,
   * 200 if it already existed, 404 if the form was not found.
   */
  def formDataPut(String uuid) {
    if (log.debugEnabled) log.debug "FORM PUT: ${Util.clean(params)}, ${request.forwardURI}"
    // The document that carries the PDF
    def docStep = null
    // Contents of the PDF
    def contents = null
    Integer status = 404

    // Does the document exist?
    docStep = docService.findStepByUuid(uuid)
    if (docStep) {
      // The document exists, get PDF contents.
      contents = docService.findContents(docStep)
      status = 200
    } else {
      // The document has to be created. Begin by retrieving form data.
      def docData = pdfService.retrieveDocData(uuid)
      if (docData) docStep = docService.createBoxDocStep(uuid)
      if (docData && docStep) {
	contents = pdfService.generatePdfa(docData, docStep, log.debugEnabled)
	status = 201
      }
    }

    if (docStep && contents) {
	render(status: status, contentType: 'text/json') {
	  formDataUuid = uuid
	  docboxRef = docStep.docboxRef
	  docNo = docStep.docNo
	  signCount = docStep.signCount
	  checkSum = contents.checksum
	}
    } else {
      render(status: 404)
    }
  }

  /**
   * Get a document given a document number
   * DISABLED
   */
  private def docNoGet(String docno) {
    if (log.debugEnabled) log.debug "BY DOCNO: ${Util.clean(params)}, ${request.forwardURI}"
    def docStep = docService.findStepByDocNo(docno)
    return contentsResponse(docStep, params.item)
  }

  /**
   * Get a document given a docboxRef
   */
  def docboxRefGet(String docboxref) {
    if (log.debugEnabled) log.debug "BY REF: ${Util.clean(params)}, ${request.forwardURI}"
    def docStep = docService.findStepByRef(docboxref)
    return contentsResponse(docStep, params.item)
  }

  /**
   * Get a document given a formDataUuid and optionally a step parameter.
   * Without a step parameter the method returns the latest step of the
   * document with the given formDataUuid.
   */
  def formDataGet(String uuid) {
    if (log.debugEnabled) log.debug "BY UUID: ${Util.clean(params)}, ${request.forwardURI}"
    def docStep = null
    if (params.step) {
      def stepNumber = params.step as Integer
      docStep = docService.findStepByUuid(uuid, stepNumber)
    } else {
      docStep = docService.findStepByUuid(uuid)
    }

    return contentsResponse(docStep, params.item)
  }

  private contentsResponse(BoxDocStep docStep, String itemName) {
    def contents = null
    if (docStep) contents = docService.findContents(docStep, itemName)

    if (contents) {
      if (log.debugEnabled) log.debug "FOUND: ${contents}"
      if (contents.binary) {
	response.status = 200
	response.contentType = contents.contentType
	response.getOutputStream().withStream {stream ->
	  stream.bytes = contents.stream
	}
      } else {
	render(status: 200, text: contents.text, contentType: contents.contentType,
	encoding: 'UTF-8')
      }
    } else {
      response.status = 404
    }
  }

}

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

import grails.converters.*

import org.motrice.docbox.DocData
import org.motrice.docbox.Util
import org.motrice.docbox.DocBoxException
import org.motrice.docbox.form.PxdFormdefVer
import org.motrice.docbox.form.PxdItem

class RestDocController {
  private final static Integer CONFLICT_STATUS = 409
  private final static CONT_DISP = 'Content-Disposition'
  def docService
  def pdfService
  def signdocService

  /**
   * Create a PDF/A document given a formDataUuid and return metadata.
   * Do not create anything in case the formDataUuid has already been created.
   * Returns status 201 if the document was created at this time,
   * 200 if it already existed, 404 if the form was not found.
   */
  def formDataPut(String uuid) {
    if (log.debugEnabled) log.debug "FORM PUT: ${Util.clean(params)}, ${request.forwardURI}"
    Integer status = 404

    // Does the document exist?
    def docStep = docService.findStepByUuid(uuid)
    def pdfContents = docStep? docStep.pdfContents() : null
    if (docStep && pdfContents) {
      status = 200
    } else {
      // The document has to be created. Begin by retrieving form data.
      def docData = pdfService.retrieveDocData(uuid)
      if (docData && !docStep) docStep = docService.createBoxDocStep(uuid)
      if (docData && docStep) {
	try {
	  pdfContents = pdfService.generatePdfa(docData, docStep, log.debugEnabled)
	  status = 201
	} catch (DocBoxException exc) {
	  pdfContents = exc.message
	  status = CONFLICT_STATUS
	}
      }
    }

    if (docStep && pdfContents && status < 300) {
      render(status: status, contentType: 'text/json') {
	formDataUuid = uuid
	docboxRef = docStep.docboxRef
	docNo = docStep.docNo
	signCount = docStep.signCount
	checkSum = pdfContents.checksum
      }
    } else if (status == CONFLICT_STATUS) {
      render(status: status, contentType: 'text/json') {
	formDataUuid = uuid
	conflictMessage = pdfContents
      }
    } else {
      render(status: 404)
    }
  }

  /**
   * Get original form data given an Orbeon form data uuid.
   */
  def docboxOrbeonData(String uuid) {
    if (log.debugEnabled) log.debug "FORMDATA: ${Util.clean(params)}, ${request.forwardURI}"
    def pdfContents = null
    String msg = null
    Integer status = 404

    def docboxref = uuid
    def docStep = docService.findStepByRef(docboxref)
    if (docStep) {
	pdfContents = docService.findPdfContents(docStep)
	if (!pdfContents) msg = "PDF contents not found: ${docboxref}"
    } else {
      msg = "Document step not found: ${docboxref}"
    }

    if (msg) {
      render(status: status, contentType: 'text/plain', text: msg)
    } else {
      def xmlInput = signdocService.findFormdata(pdfContents)
      if (xmlInput) {
	render(status: 200, contentType: 'text/json') {
	  formData = xmlInput.formData
	  formXref = xmlInput.formXref
	  timestamp = xmlInput.timestamp
	}
      } else {
	render(status: 409, contentType: 'text/plain', text: "Form data not found in ${docboxref}")
      }
    }
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

  /**
   * Get document metadata given a docboxRef
   */
  def metaDataGet(String docboxref) {
    if (log.debugEnabled) log.debug "META: ${Util.clean(params)}, ${request.forwardURI}"
    def docStep = docService.findStepByRef(docboxref)
    if (docStep) {
      response.status = 200
      def meta = docStep.meta
      if (log.debugEnabled) log.debug "metaDataGet: ${meta}"
      render meta as JSON
    } else {
      response.status = 404
    }
  }

  /**
   * Get document metadata given a docboxRef
   */
  def metaByFormData(String uuid) {
    if (log.debugEnabled) log.debug "META BY FORMDATA: ${Util.clean(params)}, ${request.forwardURI}"
    def docStep = null
    if (params.step) {
      def stepNumber = params.step as Integer
      docStep = docService.findStepByUuid(uuid, stepNumber)
    } else {
      docStep = docService.findStepByUuid(uuid)
    }

    if (docStep) {
      response.status = 200
      def meta = docStep.meta
      if (log.debugEnabled) log.debug "metaDataGet: ${meta}"
      render meta as JSON
    } else {
      response.status = 404
    }
  }

  private contentsResponse(BoxDocStep docStep, String itemName) {
    def contents = null
    if (docStep) contents = docService.findContents(docStep, itemName)

    if (contents) {
      if (log.debugEnabled) log.debug "FOUND: ${contents}"
      String contDisp = "attachment;filename=${contents.fileName}"
      response.setHeader(CONT_DISP, contDisp)
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

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

import org.motrice.docbox.DocBoxException
import org.motrice.docbox.Util
import org.motrice.docbox.sign.XmlDsig

class RestSigController {
  // Path to the XMLDSIG schema
  static final XMLDSIG_SCHEMA = '/xsd/xmldsig-core-schema.xsd'

  def docService
  def signdocService

  /**
   * Add a XML-DSIG signature to an existing document.
   * @param docboxref identifies the document step where the sig is added
   * Returns 200 on success, 404 if the document step was not found,
   * 409 on concurrent update conflict.
   * XXX TBD: This is a dummy
   */
  def docboxSigPut(String docboxref) {
    if (log.debugEnabled) log.debug "SIGPUT: ${Util.clean(params)}, ${request.forwardURI}"
    Integer status = 404
    def docStep = null
    def pdfContents = null
    def msg = null
    def sigB64 = request.reader.text

    try {
      status = 400
      signdocService.basicSignatureCheck(sigB64, servletContext.getResourceAsStream(XMLDSIG_SCHEMA))
      status = 409
      docStep = docService.findAndCheckByRef(docboxref)
      status = 404
      if (docStep) {
	pdfContents = docService.findPdfContents(docStep)
	if (pdfContents) {
	  try {
	    status = 200
	    signdocService.signedTextCheck(sigB64, docStep, pdfContents)
	  } catch (DocBoxException exc) {
	    status = 403
	    msg = exc.message
	  }
	} else {
	  msg = "PDF contents not found for ${docStep?.docNo}"
	}
      } else {
	msg = "DocStep not found for ${docboxref}"
      }
    } catch (DocBoxException exc) {
      msg = exc.message
    }

    if (msg) {
      render(status: status, contentType: 'text/plain', text: msg)
    } else {
      def sig = new XmlDsig(sigB64, log)
      def result = signdocService.addSignature(docStep, pdfContents, sig)
      def nextStep = result.step
      def nextContents = result.contents

      render(status: 200, contentType: 'text/json') {
	docboxRef = nextStep.docboxRef
	docNo = nextStep.docNo
	signCount = nextStep.signCount
	checkSum = nextContents.checksum
      }
    }
  }

  /**
   * Get all signatures of a document
   */
  def docboxSigGet(String docboxref) {
    if (log.debugEnabled) log.debug "SIGGET: ${Util.clean(params)}, ${request.forwardURI}"
    def pdfContents = null
    String msg = null
    Integer status = 404

    def docStep = docService.findStepByRef(docboxref)
    if (docStep) {
      if (docStep.signCount > 0) {
	pdfContents = docService.findPdfContents(docStep)
      } else {
	status = 409
	msg = "Document has no signature: ${docboxref}"
      }
    } else {
      msg = "Document step not found: ${docboxref}"
    }

    def sigList = null
    if (pdfContents) {
      sigList = signdocService.findAllSignatures(pdfContents)
      if (sigList.empty) {
	status = 409
	msg = "No signature found: ${docboxref}"
      }
    } else {
      msg = "No PDF found for ${docboxref}"
    }

    if (msg) {
      render(status: status, contentType: 'text/plain', text: msg)
    } else {
      status = 200
      def sigContentList = sigList.collect {sigDict ->
	def xmlSig = new XmlDsig(sigDict.signature, log)
	[signedDoc: sigDict.docNo,
	signedChecksum: sigDict.checksum,
	signedText: xmlSig.signedText,
	signedBy: xmlSig.firstCert.subjectX500Principal.toString()]
      }
      render(status: status, contentType: 'text/json') {
	docboxRef = docStep.docboxRef
	docNo = docStep.docNo
	signCount = docStep.signCount
	signExtracted = sigList.size()
	signatures = sigContentList
      }
    }
  }

  /**
   * Validate the last signature of a document.
   */
  def sigValidate(String docboxref) {
    if (log.debugEnabled) log.debug "VALIDATE: ${Util.clean(params)}, ${request.forwardURI}"
    def pdfContents = null
    def sigBase64 = null
    String msg = null
    Integer status = 404

    def docStep = docService.findStepByRef(docboxref)
    def prevChecksum
    if (docStep) {
      if (docStep.signCount > 0) {
	pdfContents = docService.findPdfContents(docStep)
	def prevStep = docService.findPredecessor(docStep)
	if (prevStep) {
	  def prevContents = docService.findPdfContents(prevStep)
	  prevChecksum = prevContents.checksum
	}
      } else {
	status = 400
	msg = "Document has no signature: ${docboxref}"
      }
    } else {
      msg = "Document step not found: ${docboxref}"
    }

    Map outcome = null
    if (pdfContents) {
      def sigList = signdocService.findAllSignatures(pdfContents)
      if (sigList.empty) {
	status = 500
	msg = "No signature found: ${docboxref}"
      } else {
	outcome = signdocService.validateSignature(sigList[-1])
      }
    } else {
      msg = "No PDF found for ${docboxref}"
    }

    if (msg) {
      render(status: status, contentType: 'text/plain', text: msg)
    } else {
      XmlDsig dsig = outcome.sigData
      def coreSigValidation = outcome.coreValid
      def certsValidation = outcome.certValid
      status = (coreSigValidation && certsValidation)? 200 : 409
      render(status: status, contentType: 'text/json') {
	docboxRef = docStep.docboxRef
	docNo = docStep.docNo
	signCount = docStep.signCount
	signedChecksum = prevChecksum
	signedText = dsig.signedText
	coreSigValid = coreSigValidation
	certValid = certsValidation
	validationReport = dsig.report.join('|')
      }
    }
  }

}

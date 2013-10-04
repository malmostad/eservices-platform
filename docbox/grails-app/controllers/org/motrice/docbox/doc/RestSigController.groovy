package org.motrice.docbox.doc

import org.motrice.docbox.DocBoxException
import org.motrice.docbox.Util
import org.motrice.docbox.sign.XmlDsig

class RestSigController {
  // Path to the XMLDSIG schema
  static final XMLDSIG_SCHEMA = '/xsd/xmldsig-core-schema.xsd'

  def docService
  def sigService

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
      sigService.basicSignatureCheck(sigB64, servletContext.getResourceAsStream(XMLDSIG_SCHEMA))
      status = 409
      docStep = docService.findAndCheckByRef(docboxref)
      status = 404
      if (docStep) {
	pdfContents = docService.findPdfContents(docStep)
	if (pdfContents) {
	  try {
	    status = 200
	    sigService.signedTextCheck(sigB64, docStep, pdfContents)
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
      def result = sigService.addSignature(docStep, pdfContents, sig)
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
      def sigList = sigService.findAllSignatures(pdfContents)
      if (sigList.empty) {
	status = 500
	msg = "No signature found: ${docboxref}"
      } else {
	outcome = sigService.validateSignature(sigList[-1])
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
      render(status: 409, contentType: 'text/json') {
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
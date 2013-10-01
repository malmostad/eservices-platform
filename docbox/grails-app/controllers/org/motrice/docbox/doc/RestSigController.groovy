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
      sigService.validateSignature(sigB64, servletContext.getResourceAsStream(XMLDSIG_SCHEMA))
      status = 409
      docStep = docService.findAndCheckByRef(docboxref)
      status = 404
      if (docStep) {
	pdfContents = docService.findPdfContents(docStep)
	if (pdfContents) {
	  status = 200
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

}
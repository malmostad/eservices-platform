package org.motrice.docbox.doc

import org.motrice.docbox.DocBoxException
import org.motrice.docbox.Util
import org.motrice.docbox.sign.XmlDsig

class RestSigController {
  private final static Integer CONFLICT_STATUS = 409

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
    // TBD: Validate the signature
    def sig = new XmlDsig(request.reader.text, log)
    Integer status = 404
    def docStep = null
    def pdfContents
    def msg = null

    try {
      docStep = docService.findAndCheckByRef(docboxref)
      if (docStep) {
	pdfContents = docService.findPdfContents(docStep)
	if (!docStep) msg = "PDF contents not found for ${docStep?.docNo}"
      } else {
	msg = "DocStep not found for ${docboxref}"
      }
    } catch (DocBoxException exc) {
      msg = exc.message
      status = CONFLICT_STATUS
    }

    if (msg) {
      render(status: status, contentType: 'text/plain', text: msg)
    } else {
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
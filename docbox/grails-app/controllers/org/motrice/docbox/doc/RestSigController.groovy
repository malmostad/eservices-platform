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

import org.motrice.docbox.Util
import org.motrice.docbox.sign.XmlDsig

import org.motrice.docbox.doc.BoxContents

import org.motrice.signatrice.ServiceException
import org.motrice.signatrice.SigDefaultScheme
import org.motrice.signatrice.SigResult
import org.motrice.signatrice.SigScheme
import org.motrice.signatrice.SigTestcase

class RestSigController {

  def docService
  def signdocService
  def signService

  /**
   * Add a XML-DSIG signature to an existing document.
   * @param docboxref identifies the document step where the sig is added
   * Returns 200 on success, 404 if the document step was not found,
   * 409 on concurrent update conflict.
   */
  def docboxSigPut(String docboxref) {
    if (log.debugEnabled) log.debug "SIGPUT: ${Util.clean(params)}, ${request.forwardURI}"
    def docStep = null
    def pdfContents = null
    def msg = null
    // The body must contain the Base64-encoded signature to add.
    def sigB64 = request.reader.text

    // Check input before proceeding.
    try {
      signdocService.basicSignatureCheck(sigB64)
      def docs = docService.findPdfByRef(docboxref, true)
      docStep = docs.docStep
      pdfContents = docs.pdfContents
      signdocService.signatureSignedTextCheck(sigB64, docStep, pdfContents)
    } catch (ServiceException exc) {
      msg = exc.canonical
    }

    if (msg) {
      // Any conflict leads to status 409
      render(status: 409, contentType: 'text/plain', text: msg)
    } else {
      def sig = new XmlDsig(sigB64, log)
      def addition = signdocService.addSignature(docStep, pdfContents, sig)
      def nextStep = addition.step
      def pdf = addition.step
      try {
	pdf.save(failOnError: true)
	nextStep.save(failOnError: true)
      } catch (Exception exc) {
	msg = "DOCBOX.115|${exc.message}"
	render(status: 409, contentType: 'text/plain', text: msg)
	return
      }

      render(status: 200, contentType: 'text/json') {
	docboxRef = nextStep.docboxRef
	docNo = nextStep.docNo
	signCount = nextStep.signCount
	checkSum = addition.checksum
      }
    }
  }

  /**
   * Get all signatures of a document
   */
  def docboxSigGet(String docboxref) {
    if (log.debugEnabled) log.debug "SIGGET: ${Util.clean(params)}, ${request.forwardURI}"
    def docStep = null
    def pdfContents = null
    String msg = null

    try {
      def docs = docService.findPdfByRef(docboxref, true)
      docStep = docs.docStep
      pdfContents = docs.pdfContents
      if (docStep.signCount == 0) msg = "DOCBOX.112|Document has no signature: ${docboxref}"
    } catch (ServiceException exc) {
      msg = exc.canonical
    }

    if (msg) {
      render(status: 409, contentType: 'text/plain', text: msg)
    } else {
      def sigList = signdocService.findAllSignatures(pdfContents)
      def sigContentList = sigList.collect {sigDict ->
	def xmlSig = new XmlDsig(sigDict.signature, log)
	[signedDoc: sigDict.docNo,
	signedChecksum: sigDict.checksum,
	signedText: xmlSig.signedText,
	signedBy: xmlSig.firstCert.subjectX500Principal.toString()]
      }
      render(status: 200, contentType: 'text/json') {
	docboxRef = docStep.docboxRef
	docNo = docStep.docNo
	signCount = docStep.signCount
	signExtracted = sigList.size()
	signatures = sigContentList
      }
    }
  }

  /**
   * Collect the outcome of a signature request.
   * Required parameters: txid=transactionId.
   */
  def docSigCollect(SignCollectCommand cmd) {
    if (log.debugEnabled) log.debug "SIG COLLECT: ${cmd}, ${request.forwardURI}"

    try {
      cmd.checkParams(request)
    } catch (ServiceException exc) {
      render(status: 409, contentType: 'text/plain', text: exc.canonical)
      return
    }

    response.status = 200
    render cmd?.result?.toMap() as JSON
  }

  /**
   * Request a document to be signed.
   * Parameters: scheme=schemeName[&personalIdNo=personnummer]
   * The body must contain the text to be signed, Base64-encoded.
   */
  def docSigRequest(SignRequestCommand cmd) {
    if (log.debugEnabled) log.debug "SIG REQUEST: ${cmd}, ${request.forwardURI}"

    try {
      cmd.checkParams(request)
    } catch (ServiceException exc) {
      render(status: 409, contentType: 'text/plain', text: exc.canonical)
      return
    }

    def result = signService.signatureRequest(cmd)
    response.status = 200
    render result.toMap() as JSON
  }

  /**
   * Test making a signature request.
   * No document is involved.
   * NOTE: This is an interactive version of docSigRequest, NOT a REST method.
   */
  def testSigRequest(Long id) {
    if (log.debugEnabled) log.debug "TEST SIG REQUEST: ${Util.clean(params)}, ${request.forwardURI}"
    def sigTestcaseObj = SigTestcase.get(id)
    if (!sigTestcaseObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'sigTestcase.label', default: 'SigTestcase'), id])
      redirect(action: "list")
      return
    }

    def cmd = SignRequestCommand.createCommand(sigTestcaseObj)

    try {
      cmd.checkParams(request)
    } catch (ServiceException exc) {
      flash.message = exc.canonical
      redirect(controller: 'sigTestcase', action: 'show', id: sigTestcaseObj.id)
      return
    }

    def result = signService.signatureRequest(cmd)
    redirect(controller: 'sigResult', action: 'show', id: result.id)
  }

  /**
   * Validate the last signature of a document.
   */
  def sigValidate(String docboxref) {
    if (log.debugEnabled) log.debug "VALIDATE: ${Util.clean(params)}, ${request.forwardURI}"
    def docStep = null
    def pdfContents = null
    def sigBase64 = null
    def prevChecksum = null
    String msg = null

    try {
      def docs = docService.findPdfByRef(docboxref, true)
      docStep = docs.docStep
      pdfContents = docs.pdfContents

      if (docStep.signCount > 0) {
	def prevStep = docService.findPredecessor(docStep)
	if (prevStep) {
	  def prevContents = docService.findPdfContentsExc(prevStep)
	  prevChecksum = prevContents.checksum
	}
      } else {
	msg = "DOCBOX.112|Document has no signature: ${docboxref}"
      }
    } catch (ServiceException exc) {
      msg = exc.canonical
    }

    Map outcome = null
    if (!msg) {
      def sigList = signdocService.findAllSignatures(pdfContents)
      if (sigList.empty) {
	msg = "DOCBOX.112|No signature found: ${docboxref}"
      } else {
	outcome = signdocService.validateSignature(sigList[-1])
      }
    }

    if (msg) {
      render(status: 409, contentType: 'text/plain', text: msg)
    } else {
      XmlDsig dsig = outcome.sigData
      def coreSigValidation = outcome.coreValid
      def certsValidation = outcome.certValid
      def status = (coreSigValidation && certsValidation)? 200 : 409
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

/**
 * Command object for collecting the result of a signature request.
 * Some logic added to allow using it for testing.
 */
class SignCollectCommand {
  // Injected service
  def docService

  // --- PARAMETERS (SPELLING CANNOT BE CHANGED)

  // DocboxRef identifying the document to sign.
  // The document itself is not needed, included as a check.
  String docboxref

  // Transaction id identifying the original request
  String txid

  // --- END PARAMETERS

  // The HTTP request
  def httpRequest

  SigResult result

  /**
   * Evaluate the parameters, get the objects where applicable.
   */
  def checkParams(request) {
    httpRequest = request
    if (!txid) throw new ServiceException("DOCBOX.113|Transaction id (txid) required but missing")
    result = SigResult.findByTransactionIdAndDocboxRefIn(txid, docboxref)
    if (!result) {
      def msg = "DOCBOX.114|Transaction not found (${txid}) or does not match docboxRef (${docboxref})"
      throw new ServiceException(msg)
    }
  }

  def getRemoteAddr() {
    httpRequest?.remoteAddr
  }

  String toString() {
    "[SignCollect: ${txid}, ${docboxref}]"
  }
}

/**
 * Command object for a signature request.
 * Some logic added to allow using it for testing.
 */
class SignRequestCommand {
  // Injected service
  def docService
  def signdocService

  // --- PARAMETERS (SPELLING CANNOT BE CHANGED)

  // DocboxRef identifying the document to sign
  String docboxref

  // Name of scheme to use for the request
  String scheme

  // Personal id number (personnummer), optional request parameter
  String personalId

  // --- END PARAMETERS

  // Flag to indicate testing.
  // The text to be signed is taken from the test case, not from the request.
  // Data binding prevented by making it private.
  private Boolean testWithoutDocument

  // Document object
  BoxDocStep docStep

  // PDF contents
  BoxContents pdfContents

  // Scheme object
  SigScheme schemeObj

  // Request body, the text to be signed, Base64-encoded
  String bodyB64

  // The HTTP request
  def httpRequest

  // Transaction id
  String transactionId

  /**
   * Create from a test case (without data binding).
   */
  static SignRequestCommand createCommand(SigTestcase testCase) {
    new SignRequestCommand(scheme: testCase.scheme.name,
    personalId: testCase.personalIdNo,
    schemeObj: testCase.scheme,
    bodyB64: testCase.encodedVisibleText,
    testWithoutDocument: true)	     
  }

  /**
   * Evaluate the parameters, get the objects where applicable.
   * Several checks are made now and omitted in the Collect call.
   * Throws ServiceException on conflict.
   */
  def checkParams(request) {
    httpRequest = request
    if (testWithoutDocument) {
      docStep = null
    } else {
      bodyB64 = request.reader.text
      // Include flag for checking that this is the latest document step
      def docs = docService.findPdfByRef(docboxref, true)
      docStep = docs.docStep
      pdfContents = docs.pdfContents
      def signedText = new String(bodyB64.decodeBase64(), 'UTF-8')
      signdocService.literalSignedTextCheck(signedText, docStep, pdfContents)
    }

    if (scheme) scheme = scheme.trim()
    schemeObj = scheme? SigScheme.findByName(scheme) : SigDefaultScheme.current()
    if (!schemeObj) throw new ServiceException('DOCBOX.118', "Scheme not found: ${scheme}")
  }

  def getRemoteAddr() {
    httpRequest?.remoteAddr
  }

  boolean isTestOnly() {
    testWithoutDocument
  }

  String toString() {
    "[SignRequest: ${docStep ?: docboxref}, scheme ${schemeObj ?: scheme}, pers id ${personalId}]"
  }

}

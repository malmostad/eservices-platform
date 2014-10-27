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

import java.sql.Timestamp

import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH

import org.motrice.docbox.DocData
import org.motrice.docbox.pdf.PdfFormdataDict
import org.motrice.docbox.sign.PdfSignatureDict
import org.motrice.docbox.sign.XmlDsig
import org.motrice.docbox.util.Exxtractor
import org.motrice.signatrice.ServiceException
import org.motrice.signatrice.SigResult

import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfAConformanceLevel
import com.itextpdf.text.pdf.PdfAStamper
import com.itextpdf.text.pdf.PdfDate
import com.itextpdf.text.pdf.PdfDictionary
import com.itextpdf.text.pdf.PdfFormXObject
import com.itextpdf.text.pdf.PdfName
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfStamper
import com.itextpdf.text.pdf.PdfString

import net.glxn.qrgen.QRCode
import net.glxn.qrgen.image.ImageType

import java.util.UUID
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator
import org.xml.sax.SAXParseException

// The only way to create a logger with a predictable name?
import org.apache.commons.logging.LogFactory

import org.springframework.transaction.annotation.Transactional

/**
 * Services related to signing and storing the signature in a PDF/A document.
 * A page is added to the document to carry signature data.
 * The class had to be renamed after name clash with Signatrice domain SigService.
 */
class SigndocService {
  // Path to the XMLDSIG schema
  // In the project this file lives in docbox/web-app/xsd/
  static final XMLDSIG_SCHEMA = '/xsd/xmldsig-core-schema.xsd'

  private static final log = LogFactory.getLog(this)
  static final String PDF_FORMAT_PAT = 'http://motrice.org/spec/docbox/%s/pdf'

  static final HEADER_FONT = 'headerFont'
  static final TEXT_FONT = 'textFont'
  static final BOLD_TEXT_FONT = 'textFontBold'
  // Left margin in points
  static final LEFT = 79.3f

  static final SIGNATURE_KEY = new PdfName('Signature')

  def grailsApplication
  def docService

  /**
   * Add a signature to a document step.
   * Return a map with the following entries:
   * step: a new BoxDocStep object
   * pdf: the new PDF contents
   * checksum: the checksum of the new BoxContents object
   */
  @Transactional
  Map addSignature(BoxDocStep docStep, BoxContents pdfContents, XmlDsig sig) {
    // Create the docboxRef for the next step
    def nextRef = UUID.randomUUID().toString()
    def bytes = addPage(docStep, pdfContents, sig, nextRef)
    def nextStep = docService.createBoxDocStep(docStep.doc, docStep.signCount + 1, nextRef)
    def nextContents = docService.createPdfContents(nextStep)
    nextContents.assignStream(bytes, true)
    if (log.debugEnabled) log.debug "addSignature: ${nextContents}"
    return [step: nextStep, pdf: nextContents, checksum: nextContents.checksum]
  }

  /**
   * Add a page to the document step
   * It contains visible info about the signature
   * It also contains the signature (XML-DSIG) as an invisible resource
   * @param nextRef must be the docboxRef of the new step to be created
   * Return a byte array containing the new PDF
   */
  private byte[] addPage(BoxDocStep docStep, BoxContents pdfContents, XmlDsig sig,
			 String nextRef)
  {
    // Set up for reading the existing PDF and writing a new one
    def reader = new PdfReader(pdfContents.stream)
    def pages = reader.numberOfPages
    def output = new ByteArrayOutputStream()
    def stamper = new PdfAStamper(reader, output, 0 as char, true, PdfAConformanceLevel.PDF_A_1A)
    def addedPage = pages + 1
    if (log.debugEnabled) log.debug "${docStep?.docNo} pages: ${pages}, add page ${addedPage}"
    // Add a new page
    stamper.insertPage(addedPage, reader.getPageSize(pages))
    def canvas = stamper.getOverContent(addedPage)
    // Text for the new page
    def fontMap = createFontMap()
    def infoTable = sigInfoTable(fontMap, sig, docStep.docNo, pdfContents.checksum)
    // Place text on the new page
    def heading = new Phrase('Underskrift ' + now(), fontMap[HEADER_FONT])
    def text = new Phrase('\nUnderskriften omfattar innehållet före denna sida.',
			  fontMap[TEXT_FONT])
    def coltext = new ColumnText(canvas)
    coltext.setSimpleColumn(heading, LEFT, 120.0f, 500.0f, 760.0f, 6.0f, Element.ALIGN_LEFT)
    coltext.setExtraParagraphSpace(20.0f)
    coltext.addText(text)
    coltext.go()
    coltext.addElement(infoTable)
    coltext.go()

    // Add a template where the signature itself is stored as "additional information"
    def templ = canvas.createTemplate(20.0f, 20.0f)
    templ.rectangle(templ.boundingBox)
    def info = new PdfSignatureDict(pdfContents.checksum, docStep.docNo, sig.signatureB64)
    def additional = new PdfDictionary()
    def docboxKey = grailsApplication.config.docbox.dictionary.key
    additional.put(new PdfName(docboxKey), info.toDictionary(pdfFormatName()))
    templ.additional = additional
    canvas.addTemplate(templ, 200.0f, 200.0f)

    // Experimental: generate QR code
    def baseurl = grailsApplication.config.docbox.signed.doc.base.url
    if (baseurl) {
      def url = "${baseurl}/${nextRef}"
      def qr = QRCode.from(url).to(ImageType.GIF).stream()
      def qrimg = Image.getInstance(qr.toByteArray())
      qrimg.setAbsolutePosition(100.0f, 60.0f)
      canvas.addImage(qrimg)
    }

    // Write the output
    stamper.close()
    reader.close()
    return output.toByteArray()
  }

  /**
   * Create the fonts to be used for added text (3 in all)
   * Based on the config file for FOP in Processor
   * All fonts must be embedded in PDF/A
   * @return a map
   * Key: Our local alias for the font (String)
   * Value: com.itextpdf.text.Font
   */
  private Map createFontMap() {
    def fontMap = org.motrice.docbox.pdf.Processor.fopConfigMap()
    def map = [:]

    // Header font
    def fontPath = fontMap['LiberationSans:normal:bold']
    FontFactory.register(fontPath, HEADER_FONT)
    map[HEADER_FONT] = FontFactory.getFont(HEADER_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20.0f)

    // Running text
    fontPath = fontMap['LiberationSerif:normal:normal']
    FontFactory.register(fontPath, TEXT_FONT)
    map[TEXT_FONT] = FontFactory.getFont(TEXT_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10.0f)

    // Bold running text
    fontPath = fontMap['LiberationSerif:normal:bold']
    FontFactory.register(fontPath, BOLD_TEXT_FONT)
    map[BOLD_TEXT_FONT] = FontFactory.getFont(BOLD_TEXT_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10.0f)

    return map
  }

  /**
   * Pick up the Motrice site name, or a default text.
   */
  private String localSiteName() {
    grailsApplication.config.motrice.site.name ?: UNKNOWN_MOTRICE_SITE_NAME
  }

  /**
   * Generate a formatted timestamp
   */
  private String now() {
    new Date().format('yyyy-MM-dd HH:mm:ss')
  }

  /**
   * Extract the signer from a signature
   */
  private String signer(XmlDsig sig) {
    def cert = sig.firstCert
    return String.valueOf(cert.subjectX500Principal)
  }

  /**
   * Create a PdfPTable containing signature information
   */
  private sigInfoTable(fontMap, XmlDsig sig, String docNo, String checkSum) {
    def table = new PdfPTable(2)
    table.totalWidth = 420.0f
    table.lockedWidth = true
    table.widths = [1.0f, 2.0f] as float[]
    table.headerRows = 1
    table.footerRows = 0
    def phrase = new Phrase('Egenskap', fontMap[BOLD_TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase('Värde', fontMap[BOLD_TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase('Dokumentnummer', fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase(docNo, fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase('Fingeravtryck', fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase(checkSum, fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase('Underskriven text', fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase(sig.signedText, fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase('Underskrift av', fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase(signer(sig), fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase('IT-system', fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase(localSiteName(), fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    return table
  }

  /**
   * Post-process PDF generated from form data: Insert form data in its first page.
   * @param formXref must be a form data cross-reference (XML as String)
   * @return a new PDF document as a byte array
   */
  def byte[] pdfPostProcess(File pdfFile, DocData docData, String formXref) {
    if (log.debugEnabled) log.debug "pdfPostProcess << ${pdfFile}"
    def reader = new PdfReader(pdfFile.bytes)
    def output = new ByteArrayOutputStream()
    // This stamper does not create a new document revision
    def stamper = new PdfAStamper(reader, output, 0 as char, false,
				  PdfAConformanceLevel.PDF_A_1A)
    def canvas = stamper.getOverContent(1)
    // Add a template where the signature itself is stored as "additional information"
    def templ = canvas.createTemplate(20.0f, 20.0f)
    templ.rectangle(templ.boundingBox)
    def info = new PdfFormdataDict(docData.dataItem.text, formXref)
    def additional = new PdfDictionary()
    def docboxKey = grailsApplication.config.docbox.dictionary.key
    additional.put(new PdfName(docboxKey), info.toDictionary(pdfFormatName()))
    templ.additional = additional
    canvas.addTemplate(templ, 25.0f, 25.0f)

    // Write the output
    stamper.close()
    reader.close()
    def outputBytes = output.toByteArray()
    if (log.debugEnabled) log.debug "pdfPostProcess >> ${outputBytes?.length}"
    return outputBytes
  }

  /**
   * Find a signature in pdf contents
   * @param pdfContents must contain a pdf document
   * @return a List of PdfSignatureDict where each element is a Base64-encoded signature
   * The list is empty if there are no signatures in the document
   */
  def findAllSignatures(BoxContents pdfContents) {
    findAllSignatures(pdfContents.stream)
  }

  def findAllSignatures(byte[] pdf) {
    if (log.debugEnabled) log.debug "findAllSignatures << ${pdf.length} bytes"
    def reader = new PdfReader(pdf)
    def pageCount = reader.numberOfPages
    // The first page that may contain a signature is page 2
    if (pageCount < 2) return null
    def sigList = []
    def docboxKey = new PdfName(grailsApplication.config.docbox.dictionary.key)
    (2..pageCount).each {pageNo ->
      def page = reader.getPageN(pageNo)
      def resources = page.getDirectObject(PdfName.RESOURCES)
      if (resources) {
	def xobjDict = resources.getDirectObject(PdfName.XOBJECT)
	xobjDict.keys.each {key1 ->
	  def xobj = xobjDict.getDirectObject(key1)
	  if (xobj.stream) {
	    xobj.keys.each {key2 ->
	      if (docboxKey.equals(key2)) {
		def docboxDict = xobj.get(key2)
		def sig = PdfSignatureDict.create(docboxDict)
		if (sig) sigList.add(sig)
	      }
	    }
	  }
	}
      }
    }

    //if (log.debugEnabled) log.debug "findAllSignatures >> ${sigList.collect {it.size()}} chars"
    if (log.debugEnabled) log.debug "findAllSignatures >> ${sigList}"
    return sigList
  }

  /**
   * Find a signature in pdf contents
   * @param pdfContents must contain a pdf document
   * @return a PdfFormdataDict or null if not found
   */
  PdfFormdataDict findFormdata(BoxContents pdfContents) {
    findFormdata(pdfContents.stream)
  }

  PdfFormdataDict findFormdata(byte[] pdf) {
    if (log.debugEnabled) log.debug "findFormdata << ${pdf.length} bytes"
    def reader = new PdfReader(pdf)
    def pageCount = reader.numberOfPages
    def docboxKey = new PdfName(grailsApplication.config.docbox.dictionary.key)
    // Formdata is always on page 1
    def page = reader.getPageN(1)
    def formData = null
    def resources = page.getDirectObject(PdfName.RESOURCES)
    if (resources) {
      def xobjDict = resources.getDirectObject(PdfName.XOBJECT)
      xobjDict.keys.each {key1 ->
	def xobj = xobjDict.getDirectObject(key1)
	if (xobj.stream) {
	  xobj.keys.each {key2 ->
	    if (docboxKey.equals(key2)) {
	      def docboxDict = xobj.get(key2)
	      if (!formData) formData = PdfFormdataDict.create(docboxDict)
	    }
	  }
	}
      }
    }

    //if (log.debugEnabled) log.debug "findAllSignatures >> ${sigList.collect {it.size()}} chars"
    if (log.debugEnabled) log.debug "findFormdata >> ${formData}"
    return formData
  }

  /**
   * Get a name identifying the application creating this Pdf format.
   * In this case "format" means the way docbox inserts data in a Pdf document.
   */
  String pdfFormatName() {
    String.format(PDF_FORMAT_PAT, grailsApplication.metadata['app.version'])
  }

  /**
   * Check signature syntax and that the signed text matches this document.
   * Throw ServiceException on failure, silent otherwise.
   */
  def basicSignatureCheck(String sigBase64) {
    if (log.debugEnabled) log.debug "basicSignatureCheck << (${sigBase64?.length()} chars)"
    def factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema")
    // We need the signature XML schema. It is an app resource.
    def schemaStream = SCH.servletContext.getResourceAsStream(XMLDSIG_SCHEMA)
    def schemaSource = new StreamSource(schemaStream)
    def schema = factory.newSchema(schemaSource)
    def validator = schema.newValidator()
    def sigSource = new StreamSource(new ByteArrayInputStream(sigBase64.decodeBase64()))
    try { 
      validator.validate(sigSource)
    } catch (SAXParseException exc) {
      if (log.debugEnabled) log.debug "basicSignatureCheck >> FAIL ${exc}"
      throw new ServiceException('DOCBOX.101', exc.message)
    }

    if (log.debugEnabled) log.debug "basicSignatureCheck >> OK"
  }

  /**
   * Given a signature (a Base64-encoded text), check that its signed text
   * contains two fields, a document number and a checksum.
   * The check is not done if the config property
   * 'docbox.signed.text.strict.check' is 'false'.
   * Throws ServiceException if conflict.
   * sigBase64 must be a Base64-encoded XML DSIG signature.
   * It should have passed basic checking before calling this method.
   */
  def signatureSignedTextCheck(String sigBase64, BoxDocStep docStep,
			       BoxContents pdfContents)
  {
    if (isLenient()) {
      if (log.debugEnabled) log.debug "signatureSignedTextCheck: LENIENT"
    } else {
      def sig = null
      try {
	sig = new XmlDsig(sigBase64, log)
	doStrictCheckSignedText(sig.signedText, docStep, pdfContents)
      } catch (javax.xml.crypto.dsig.XMLSignatureException exc) {
	exc('DOCBOX.109', exc?.cause?.message)
      }
    }

    if (log.debugEnabled) log.debug "signatureSignedTextCheck >> VALIDATED"
  }

  /**
   * Given a text, check that it contains two fields, a document number and a checksum.
   * Otherwise as above.
   * The signed text must be plain text, NOT Base64-encoded.
   */
  def literalSignedTextCheck(String signedText, BoxDocStep docStep,
			     BoxContents pdfContents)
  {
    if (isLenient()) {
      if (log.debugEnabled) log.debug "literalSignedTextCheck: LENIENT"
    } else {
      doStrictCheckSignedText(signedText, docStep, pdfContents)
    }

    if (log.debugEnabled) log.debug "literalSignedTextCheck >> VALIDATED"
  }

  private exc(String docboxCode, String message) {
    if (log.debugEnabled) log.debug "EXCEPTION(${docboxCode}): ${message}"
    throw new ServiceException(docboxCode, message)
  }

  /**
   * Is DocBox configured for lenient checking?
   */
  private boolean isLenient() {
    def strictProp = grailsApplication.config.docbox.signed.text.check.strict
    return strictProp == 'false'
  }

  private doStrictCheckSignedText(String signedText, BoxDocStep docStep,
				BoxContents pdfContents)
  {
    def extr = new Exxtractor(signedText)
    def fieldList = extr.extract()
    if (log.debugEnabled) log.debug "doStrictCheckSignedText: STRICT ${fieldList}"
    if (fieldList.size() != 2) {
      exc('DOCBOX.102', "2 fields expected in signed text, got ${fieldList.size()}")
    }

    boolean docNumberFound = fieldList.find {it == docStep.docNo}
    boolean checksumFound = fieldList.find {it == pdfContents.checksum}
    if (!docNumberFound) exc('DOCBOX.103', 'Document number not found in signed text')
    if (!checksumFound) exc('DOCBOX.104', 'Checksum not found in signed text')
  }

  /**
   * Validate a signature
   * @param sigBase64 must contain the signature Base64-encoded
   * @return a map containing:
   * sigValid: (boolean) the outcome of signature validation
   * certValid: (boolean) the outcome of certificate validation
   * sigData: XmlDsig instance
   */
  def validateSignature(String sigBase64) {
    def sig = new XmlDsig(sigBase64, log)
    def map = [:]
    map.coreValid = sig.validateSignature()
    map.certValid = sig.validateCertificates()
    map.sigData = sig
    return map
  }

  def validateSignature(PdfSignatureDict dict) {
    validateSignature(dict.signature)
  }

  // Query to pick up SigResults to be post-processed and finished.
  // The query must agree with the SigResult.readyForFinish condition.
  private static final String PP_Q = 'from SigResult r where ' +
    'r.finishConflict is null and docboxRefOut is null and ' +
    'r.sigTstamp between ? and ?'

  /**
   * Post-process all signature requests limited by a time window.
   * Executed periodically by quartz through PostprocessJob.
   */
  def sigPostProcessAll() {
    def now = new Date()
    // Age window for post-processing
    def beg = new Timestamp(now.time - SigResult.MAX_FINISH_AGE_MILLIS)
    def end = new Timestamp(now.time - SigResult.MIN_FINISH_AGE_MILLIS)
    //if (log.debugEnabled) log.debug "post-process interval: ${beg}..${end}"
    def candidates = SigResult.findAll(PP_Q, [beg, end])
    if (log.debugEnabled && candidates?.size() > 0) log.debug "collect candidates: ${candidates?.size()}"
    candidates.each {candidate ->
      sigPostProcess(candidate)
    }
  }

  /**
   * Periodically executed by the PostprocessJob.
   * Inserts the signature obtained previously and creates a new document step.
   */
  def sigPostProcess(SigResult item) {
    if (log.debugEnabled) log.debug "postProcess << ${item}"
    try {
      basicSignatureCheck(item.signature)
      def docs = docService.findPdfByRef(item.docboxRefIn, true)
      def docStep = docs.docStep
      def pdfContents = docs.pdfContents
      signatureSignedTextCheck(item.signature, docStep, pdfContents)
      def sig = new XmlDsig(item.signature, log)
      def addition = addSignature(docStep, pdfContents, sig)
      item.docboxRefOut = addition.step.docboxRef
      if (log.debugEnabled) log.debug "postProcess >> ${addition?.step}"
    } catch (ServiceException exc) {
      item.docboxRefOut = null
      item.finishConflict = exc.canonical
       if (log.debugEnabled) log.debug "postProcess EXC: ${exc.canonical}"
    } catch (Exception exc) {
      item.finishConflict = exc.message
      if (log.debugEnabled) log.debug "postProcess EXC: ${exc.message}"
    }

    if (!item.save()) log.error "post-process ${item} save: ${item.errors.allErrors.join(', ')}"
  }

}

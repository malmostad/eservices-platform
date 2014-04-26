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
import org.motrice.docbox.DocData
import org.motrice.docbox.pdf.PdfFormdataDict
import org.motrice.docbox.sign.PdfSignatureDict
import org.motrice.docbox.sign.XmlDsig
import org.motrice.docbox.util.Exxtractor

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
 * Services related to signing and storing the signature in a PDF/A document
 * A page is added to the document to carry signature data
 */
class SigService {
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

  @Transactional
  def addSignature(BoxDocStep docStep, BoxContents pdfContents, XmlDsig sig) {
    // Create the docboxRef for the next step
    def nextRef = UUID.randomUUID().toString()
    def bytes = addPage(docStep, pdfContents, sig, nextRef)
    def nextStep = docService.createBoxDocStep(docStep.doc, docStep.signCount + 1, nextRef)
    def nextContents = docService.createPdfContents(nextStep)
    nextContents.assignStream(bytes, true)
    if (log.debugEnabled) log.debug "addSignature: ${nextContents}"
    return [step: nextStep, contents: nextContents]
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
   * Throw DocBoxException on failure, silent otherwise.
   */
  def basicSignatureCheck(String sigBase64, InputStream schemaStream) {
    if (log.debugEnabled) log.debug "basicSignatureCheck << (${sigBase64?.length()} chars)"
    def factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema")
    def schemaSource = new StreamSource(schemaStream)
    def schema = factory.newSchema(schemaSource)
    def validator = schema.newValidator()
    def sigSource = new StreamSource(new ByteArrayInputStream(sigBase64.decodeBase64()))
    try { 
      validator.validate(sigSource)
    } catch (SAXParseException exc) {
      if (log.debugEnabled) log.debug "basicSignatureCheck >> FAIL ${exc}"
      throw new DocBoxException(exc.message)
    }

    if (log.debugEnabled) log.debug "basicSignatureCheck >> OK"
  }

  /**
   * Check that the signed text contains two fields, a document number and
   * a checksum.
   * The check is not done if the config property
   * 'docbox.signed.text.strict.check' is 'false'.
   */
  def signedTextCheck(String sigBase64, BoxDocStep docStep, BoxContents pdfContents) {
    def strictProp = grailsApplication.config.docbox.signed.text.check.strict
    boolean lenientFlag = strictProp == 'false'
    if (lenientFlag) {
      if (log.debugEnabled) log.debug "signedTextCheck: LENIENT"
    } else {
      def sig = new XmlDsig(sigBase64, log)
      def extr = new Exxtractor(sig.signedText)
      def fieldList = extr.extract()
      if (log.debugEnabled) log.debug "signedTextCheck: STRICT ${fieldList}"
      if (fieldList.size() != 2) {
	exc("2 fields expected in signed text, got ${fieldList.size()}")
      }

      boolean docNumberFound = fieldList.find {it == docStep.docNo}
      boolean checksumFound = fieldList.find {it == pdfContents.checksum}
      if (!docNumberFound) exc('Document number not found in signed text')
      if (!checksumFound) exc('Checksum not found in signed text')
    }

    if (log.debugEnabled) log.debug "signedTextCheck >> VALIDATED"
  }

  private exc(String message) {
    if (log.debugEnabled) log.debug "EXCEPTION: ${message}"
    throw new DocBoxException(message)
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

}

package org.motrice.docbox.doc

import org.motrice.docbox.sign.XmlDsig

import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
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

// The only way to create a logger with a predictable name?
import org.apache.commons.logging.LogFactory

/**
 * Services related to signing and storing the signature in a PDF/A document
 * A page is added to the document to carry signature data
 */
class SigService {
  private static final log = LogFactory.getLog(this)
  static transactional = true

  static final HEADER_FONT = 'headerFont'
  static final TEXT_FONT = 'textFont'
  static final BOLD_TEXT_FONT = 'textFontBold'
  // Left margin in points
  static final LEFT = 79.3f

  static final RESOURCE_NAME = 'org.motrice.docbox.info'

  def docService

  def addSignature(BoxDocStep docStep, BoxContents pdfContents, XmlDsig sig) {
    def bytes = addPage(docStep, pdfContents, sig)
    def nextStep = docService.createBoxDocStep(docStep.doc, docStep.signCount + 1)
    def nextContents = docService.createPdfContents(nextStep)
    nextContents.assignStream(bytes, true)
    if (log.debugEnabled) log.debug "addSignature: ${nextContents}"
    return [step: nextStep, contents: nextContents]
  }

  /**
   * Add a page to the document step
   * It contains visible info about the signature
   * It also contains the signature (XML-DSIG) as an invisible resource
   * Return a byte array containing the new PDF
   */
  private byte[] addPage(BoxDocStep docStep, BoxContents pdfContents, XmlDsig sig) {
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
    def info = new PdfDictionary()
    info.put(new PdfName('DocNo'), new PdfString(docStep.docNo))
    info.put(new PdfName('Signature'), new PdfString(sig.signatureB64))
    info.put(new PdfName('Timestamp'), new PdfDate())
    def additional = new PdfDictionary()
    additional.put(new PdfName(RESOURCE_NAME), info)
    templ.additional = additional
    canvas.addTemplate(templ, 200.0f, 200.0f)

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
    phrase = new Phrase('Kontrollsumma', fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase(checkSum, fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase('Underskrift av', fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    phrase = new Phrase(signer(sig), fontMap[TEXT_FONT])
    table.addCell(new PdfPCell(phrase))
    return table
  }

}

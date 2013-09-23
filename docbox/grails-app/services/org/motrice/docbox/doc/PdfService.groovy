package org.motrice.docbox.doc

import org.motrice.docbox.DocData
import org.motrice.docbox.form.PxdFormdefVer
import org.motrice.docbox.form.PxdItem
import org.motrice.docbox.pdf.Attachment
import org.motrice.docbox.pdf.Control
import org.motrice.docbox.pdf.FormData
import org.motrice.docbox.pdf.FormDef
import org.motrice.docbox.pdf.FormMeta
import org.motrice.docbox.pdf.Options
import org.motrice.docbox.pdf.Processor
import org.motrice.docbox.pdf.Section
import org.motrice.docbox.pdf.Tag

// The only way to create a logger with a predictable name?
import org.apache.commons.logging.LogFactory

/**
 * Logic for converting an Orbeon form to PDF/A.
 */
class PdfService {
  private static final log = LogFactory.getLog(this)
  static transactional = true

  def docService
  def signService

  /**
   * Retrieve all database objects relevant for a form instance.
   * @param uuid must be the uuid of the form instance.
   * @return a DocData object, or null if form data is not found.
   */
  DocData retrieveDocData(String uuid) {
    def docData = null
    def dataItem = PxdItem.findWhere(uuid: uuid, format: 'xml', instance: true)
    if (dataItem) {
      docData = new DocData(uuid, dataItem)
      String formDef = docData.dataItem.formDef
      // Find attachments
      docData.auxItems = PxdItem.findAllWhere(uuid: docData.uuid, format: 'binary',
      instance: true)
      docData.formDef = PxdItem.findWhere(formDef: formDef, instance: false)
      docData.formMeta = PxdFormdefVer.findWhere(path: formDef)
      // Find fixed items, part of the form definition
      docData.fixedItems =
	PxdItem.findAllWhere(uuid: docData.formMeta.formdef.uuid, instance: false)
    }

    if (log.debugEnabled) log.debug "retrieveDocData: ${docData}"
    return docData
  }

  /**
   * Convert a form instance to PDF/A
   * @param docData must contain all database objects relevant for a
   * form instance
   * @return a BoxContents object
   */
  BoxContents generatePdfa(DocData docData, BoxDocStep docStep, boolean debug) {
    def formData = new FormData(docData.dataItem.text)
    // if (debug) println "FORM DATA: ${formData}"
    def formDef = new FormDef(docData.formDef.text)
    formDef.build(log)
    if (log.debugEnabled) log.debug formDef.dump()
    createPreview(docStep, formDef, formData)
    def docbook = createDocBook(docStep, formDef, formData)
    return docbookXmlToPdf(docStep, docData, docbook, debug)
  }

  /**
   * Create a DocBook equivalent of the form
   * SIDE EFFECT: A new PxdItem containing DocBook XML is attached
   * to form data
   * Also copies any external pxdItems into this form data
   * (Main example is form logo)
   * RETURN BoxContents containing DocBook XML
   */
  private BoxContents createDocBook(BoxDocStep docStep, formDef, formData) {
    def map = formDef.generateDocBook(formData, log)
    def docbook = docService.createContents(docStep, 'docbook.xml', 'xml')
    docbook.assignText(map.xml)
    if (!docbook.save(insert: true)) {
      log.error "BoxContents save: ${docbook.errors.allErrors.join(',')}"
    }

    return docbook
  }

  /**
   * Create an all-text form preview for debugging
   * SIDE EFFECT: A new PxdItem containing the text is attached to
   * form data
   */
  private createPreview(BoxDocStep docStep, FormDef formDef, FormData formData) {
    def pv = formDef.generateTextPreview(formData)
    def preview = docService.createContents(docStep, 'preview', 'text')
    preview.assignText(pv?.toString())
    if (log.debugEnabled) "createPreview: ${preview}"
    if (!preview.save(insert: true)) {
      log.error "BoxContents save: ${preview.errors.allErrors.join(',')}"
    }

    return preview
  }

  /**
   * Convert a DocBook XML PxdItem, generating a PDF PxdItem
   * Add the new Pdf PxdItem to the data
   * Return the new contents
   */
  private BoxContents docbookXmlToPdf(BoxDocStep docStep, DocData docData, BoxContents docbook,
			  boolean debug)
  {
    def processor = new Processor(debug)
    if (debug) println "docbookXmlToPdf ${docStep} << ${processor.tempDir.absolutePath}"
    // Store DocBook xml
    storeBoxContents(docbook, processor.tempDir)

    // Copy all pxdItems to the temp directory defined by the processor
    // Attachments
    docData.auxItems.each {pxdItem ->
      storePxdItem(pxdItem, processor.tempDir, 'attachment')
    }
    // Fixed items
    docData.fixedItems.each {pxdItem ->
      storePxdItem(pxdItem, processor.tempDir, 'fixed item')
    }

    // Run the conversion pipeline
    def result = processor.toPdf()
    def pdfPath = result.pdf
    def logPath = result.log

    // Store the newly generated pdf
    def pdfFile = new File(pdfPath)
    def pdf = docService.createContents(docStep, 'pdf', 'binary')
    pdf.checksum = signService.computeChecksum(pdf)
    pdf.assignStream(pdfFile.bytes)
    if (!pdf.save(insert: true)) log.error "BoxContents save: ${pdf.errors.allErrors.join(',')}"

    // Store the log file
    def logFile = new File(logPath)
    def conversionLog = docService.createContents(docStep, 'convlog', 'text')
    conversionLog.assignText(logFile.text)
    if (!conversionLog.save(insert: true)) {
      log.error "BoxContents save: ${conversionLog.errors.allErrors.join(',')}"
    }

    // Clean up temp files
    processor.cleanUp()
    if (log.debugEnabled) log.debug "xmlToPdf >> ${pdf}"
    return pdf
  }

  /**
   * Store BoxContents in the temp directory
   */
  private storeBoxContents(BoxContents boxContents, File tempDir) {
    // Do not store anything with a slash in the path
    if (boxContents.name.indexOf('/') > 0) return
    def tgtFile = new File(tempDir, boxContents.name)
    if (boxContents.text) {
      tgtFile.text = boxContents.text
    } else if (boxContents.stream) {
      tgtFile.bytes = boxContents.stream
    }
  }

  /**
   * Store pxdItem content in the temp directory
   */
  private storePxdItem(PxdItem pxdItem, File tempDir, String comment) {
    // Do not store anything with a slash in the path
    if (pxdItem.path.indexOf('/') > 0) return
    if (log.debugEnabled) log.debug "${comment}: ${pxdItem}"
    def tgtFile = new File(tempDir, pxdItem.path)
    if (pxdItem.text) {
      tgtFile.text = pxdItem.text
    } else if (pxdItem.stream) {
      tgtFile.bytes = pxdItem.stream
    }
  }

}

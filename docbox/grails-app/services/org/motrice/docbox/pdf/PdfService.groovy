package org.motrice.docbox.doc

import org.motrice.docbox.form.PxdItem

/**
 * Logic for converting an Orbeon form to PDF/A.
 */
class PdfService {
  static transactional = true

  static final String rQuery = 'from PxdItem r where r.data.uuid=?'
  static final String fQuery = 'from PxdItem r where r.data.uuid=? and r.name=?'

  // Name prefix for files taking part in PDF generation
  // PDF generation begins by removing all pxdItems matching this prefix
  static final PDFPREFIX = 'publish'

  // PxdItems not to copy to temp dir when preparing for PDF conversion
  static final EXCLUDELIST = ['data.xml']

  /**
   *
   */
  def generatePdfa(Data dataObj, boolean debug) {
    def pxdItemMap = createPxdItemMap(dataObj)
    removePdfPxdItems(dataObj, pxdItemMap)
    def formData = new FormData(pxdItemMap['data.xml'].text)
    // if (debug) println "FORM DATA: ${formData}"
    def formDef = new FormDef(dataObj.form.formDef)
    formDef.build()
    // if (debug) println "FORM DEF: ${formDef}"
    createPreview(dataObj, formDef, formData)
    createDocBook(dataObj, formDef, formData)
    docbookXmlToPdf(formDef, dataObj, debug)
    return Processor.PDFFILE
  }

  private createPxdItemMap(Data dataObj) {
    def map = [:]
    PxdItem.executeQuery(rQuery, dataObj.uuid).each {pxdItem ->
      map[pxdItem.name] = pxdItem
    }

    return map
  }

  /**
   * Create a DocBook equivalent of the form
   * SIDE EFFECT: A new PxdItem containing DocBook XML is attached
   * to form data
   * Also copies any external pxdItems into this form data
   * (Main example is form logo)
   * RETURN a new PxdItem containing DocBook XML
   */
  private createDocBook(dataObj, formDef, formData) {
    def map = formDef.generateDocBook(formData)
    def docbook = new PxdItem(name: 'publish.xml', tstamp: new Date())
    docbook.assignText(map.xml)
    dataObj.addToPxdItems(docbook)
    docbook.save()
    return docbook
  }

  /**
   * Create an all-text form preview for debugging
   * SIDE EFFECT: A new PxdItem containing the text is attached to
   * form data
   */
  private createPreview(dataObj, formDef, formData) {
    def pv = formDef.generateTextPreview(formData)
    def preview = new PxdItem(name: 'publish.txt', tstamp: new Date())
    preview.assignText(pv?.toString())
    dataObj.addToPxdItems(preview)
    preview.save()
  }

  /**
   * Convert a DocBook XML PxdItem, generating a PDF PxdItem
   * Add the new Pdf PxdItem to the data
   */
  private docbookXmlToPdf(FormDef formDef, Data dataObj, boolean debug) {
    def processor = new Processor(debug)
    if (debug) println "docbookXmlToPdf temp dir: ${processor.tempDir.absolutePath}"
    // Copy all pxdItems to the temp directory defined by the processor
    PxdItem.executeQuery(rQuery, [dataObj.uuid]).each {pxdItem ->
      if (EXCLUDELIST.contains(pxdItem.name)) return
      storePxdItem(pxdItem, processor.tempDir)
    }

    // Copy pxdItems not stored in the form definition
    // Fixed pxdItems are stored as form data
    formDef.pxdItemList.each {attachment ->
      // DEBUG
      // println "ALL pxdItemList: ${attachment} (data=${dataObj.uuid})"
      if (attachment.path && attachment.dataname() != dataObj.uuid) {
	def crit = [attachment.dataname(), attachment.basename()]
	def pxdItem = PxdItem.find(fQuery, crit)
	if (pxdItem) storePxdItem(pxdItem, processor.tempDir)
      }
    }

    // Run the conversion pipeline
    def result = processor.toPdf()
    def pdfPath = result.pdf
    def logPath = result.log

    // Store the newly generated pdf
    def pdfFile = new File(pdfPath)
    def pdf = new PxdItem(name: 'publish.pdf', tstamp: new Date())
    pdf.assignStream(pdfFile.bytes)
    dataObj.addToPxdItems(pdf)
    pdf.save()

    // Store the log file
    def logFile = new File(logPath)
    def log = new PxdItem(name: 'publish.log', tstamp: new Date())
    log.assignText(logFile.text)
    dataObj.addToPxdItems(log)
    log.save()

    // Clean up temp files
    processor.cleanUp()
  }

  /**
   * Remove any existing PDF pxdItems from the given data directory
   * SIDE EFFECT: Also removes the corresponding entries from the pxdItem map
   */
  private removePdfPxdItems(Data dataObj, Map pxdItemMap) {
    def deletedKeys = []
    pxdItemMap.keySet().each {key ->
      def idx = key.indexOf('.')
      if (idx >= 0 && key.substring(0, idx) == PDFPREFIX) {
	deletedKeys.add(key)
	def pxdItem = pxdItemMap[key]
	pxdItem.delete(flush: true)
      }
    }

    deletedKeys.each {key ->
      pxdItemMap.remove(key)
    }
  }

  /**
   * Store pxdItem content in the temp directory
   */
  private storePxdItem(PxdItem pxdItem, File tempDir) {
    def tgtFile = new File(tempDir, pxdItem.name)
    if (pxdItem.text) {
      tgtFile.text = pxdItem.text
    } else if (pxdItem.stream) {
      tgtFile.bytes = pxdItem.stream
    }
  }

}

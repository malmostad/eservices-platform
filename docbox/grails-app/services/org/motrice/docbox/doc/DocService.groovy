package org.motrice.docbox.doc

import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.UUID
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.motrice.docbox.util.CrockfordBase32

// The only way to create a logger with a predictable name?
import org.apache.commons.logging.LogFactory

/**
 * Services for documents
 */
class DocService {
  static RNG = new SecureRandom()
  private static final log = LogFactory.getLog(this)
  static transactional = true

  private static final DOCNO_PAT = ~/([A-Za-z0-9-]+?)(?:-(\d{1,2}))?/

  /**
   * Create and save a BoxDoc document
   * Mainly for picking a random document number
   */
  BoxDoc createBoxDoc(String formDataUuid) {
    def idMap = generateDocNo()
    def doc = new BoxDoc(formDataUuid: formDataUuid)
    doc.id = idMap.docId
    doc.docNo = idMap.docNo
    if (!doc.save(insert: true)) log.error "BoxDoc save: ${doc.errors.allErrors.join(',')}"
    if (log.debugEnabled) log.debug "createBoxDoc: ${doc}"
    return doc
  }

  /**
   * Create and save a BoxDocStep
   * Create its parent BoxDoc if necessary
   */
  BoxDocStep createBoxDocStep(String formDataUuid) {
    def parent = BoxDoc.findByFormDataUuid(formDataUuid)
    if (!parent) parent = createBoxDoc(formDataUuid)
    return createBoxDocStep(parent)
  }

  /**
   * Create and save a BoxDocStep
   */
  BoxDocStep createBoxDocStep(BoxDoc parent) {
    def q = 'select count(id) from BoxDocStep s where s.doc.id=?'
    def stepCountList = BoxDocStep.executeQuery(q, [parent.id])
    def stepCount = stepCountList[0]
    String docNo = "${parent.docNo}-${stepCount}"
    def uuid = UUID.randomUUID().toString()
    def step = new BoxDocStep(step: stepCount, docNo: docNo, docboxRef: uuid, signCount: 0)
    parent.addToSteps(step)
    if (!step.save(insert: true)) log.error "BoxDocStep save: ${step.errors.allErrors.join(',')}"
    if (log.debugEnabled) log.debug "createBoxDocStep: ${step}"
    return step
  }

  /**
   * Create contents for a BoxDocStep, but does not save
   * @param name must be the name of the new contents
   */
  BoxContents createContents(BoxDocStep step, String name, String format) {
    def contents = new BoxContents(name: name, format: format)
    step.addToContents(contents)
    return contents
  }

  /**
   * Find a BoxDocStep given a document number
   * The document number may or may not contain a step number
   */
  BoxDocStep findStepByDocNo(String docNo) {
    def m =  DOCNO_PAT.matcher(docNo)
    if (!m.matches()) return null
    def docNumber = m.group(1).replaceAll('[^A-Za-z0-9]', '')
    def stepStr = m.group(2)
    def docId = decodeDocNo(docNumber)

    Integer stepNumber = null
    if (stepStr) {
      try {
	stepNumber = stepStr as Integer
      } catch (NumberFormatException exc) {
	stepNumber = null
      }
    }

    if (stepNumber == null) {
      // No step number, find latest
      def q = 'select max(step) from BoxDocStep s where s.doc.id=?'
      def list = BoxDocStep.executeQuery(q, [docId as Long])
      stepNumber = list[0]
    }

    if (log.debugEnabled) log.debug "findStepByDocNo << ${docNo} (${docId}, ${stepNumber})"
    def q = 'from BoxDocStep s where s.doc.id=? and step=?'
    def docStepList = BoxDocStep.executeQuery(q, [docId as Long, stepNumber])
    def docStep = docStepList[0]
    if (log.debugEnabled) log.debug "findStepByDocNo >> ${docStep}"
    return docStep
  }

  BoxDocStep findStepByRef(String docboxRef) {
    if (log.debugEnabled) log.debug "findStepByRef << ${docboxRef}"
    def docStep = BoxDocStep.findByDocboxRef(docboxRef)
    if (log.debugEnabled) log.debug "findStepByRef >> ${docStep}"
    return docStep
  }

  BoxDocStep findStepByUuid(String uuid) {
    findStepByUuid(uuid, null)
  }

  BoxDocStep findStepByUuid(String uuid, Integer stepNumber) {
    if (stepNumber == null) {
      def q = 'select max(step) from BoxDocStep s where s.doc.formDataUuid=?'
      def list = BoxDocStep.executeQuery(q, [uuid])
      stepNumber = list[0]
    }

    if (log.debugEnabled) log.debug "findStepByUuid << ${uuid}, ${stepNumber}"
    def q = 'from BoxDocStep s where doc.formDataUuid=? and step=?'
    def docStepList = BoxDocStep.executeQuery(q, [uuid, stepNumber])
    def docStep = (docStepList?.size() > 0)? docStepList[0] : null
    if (log.debugEnabled) log.debug "findStepByUuid >> ${docStep}"
    return docStep
  }

  BoxContents findContents(BoxDocStep docStep) {
    findContents(docStep, null)
  }

  BoxContents findContents(BoxDocStep docStep, String itemName) {
    if (log.debugEnabled) log.debug "findContents << ${docStep}, ${itemName}"
    def item = itemName ?: 'pdf'
    def q = 'from BoxContents c where c.step.id=? and name=?'
    def contList = BoxContents.executeQuery(q, [docStep.id, item])
    def contents = (contList?.size() > 0)? contList[0] : null
    if (log.debugEnabled) log.debug "findContents >> ${contents}"
    return contents
  }

  /**
   * Generate a document number, check for uniqueness
   * @return a map with the following components:
   * docId - the document number as an int,
   * docNo - the document number as string encoded with
   * Crockford Base32.
   */
  private Map generateDocNo() {
    byte[] bytes = nextId()
    int id = toInt(bytes)

    // Find an unused id
    while (BoxDoc.exists(id)) {
      bytes = nextId()
      id = toInt(bytes)
    }

    def cb32 = new CrockfordBase32()
    def docNo = cb32.encodeToString(bytes)
    if (docNo.startsWith('0')) docNo = docNo.substring(1)
    return [docNo: docNo, docId: id]
  }

  /**
   * Decode a document number from string to int
   * @param docNo must be the string number without any separators
   */
  private int decodeDocNo(String docNo) {
    def cb32 = new CrockfordBase32()
    if (docNo.length() < 7) docNo = '0' + docNo
    def bytes = cb32.decode(docNo)
    return toInt(bytes)
  }

  /**
   * Get random 4 bytes (for a doc id)
   */
  private byte[] nextId() {
    def bytes = new byte[4]
    synchronized(RNG) {
      RNG.nextBytes(bytes)
    }

    // Limit to 30 bits
    bytes[0] &= 0x03
    return bytes
  }

  /**
   * Convert a byte array to a 30-bit int
   */
  private int toInt(byte[] bytes) {
    def buf = ByteBuffer.wrap(bytes)
    return buf.getInt()
  }

}

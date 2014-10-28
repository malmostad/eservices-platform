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

import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.UUID
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.motrice.docbox.util.CrockfordBase32
import org.motrice.signatrice.ServiceException

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
    createBoxDocStep(parent, null, null)
  }

  BoxDocStep createBoxDocStep(BoxDoc parent, Integer signCount) {
    createBoxDocStep(parent, signCount, null)
  }

  /**
   * Create and save a BoxDocStep
   * @param parent must be the BoxDoc to which the new step belongs
   * @param signCount must be the number of signatures in the new step or null
   * Null is taken as zero.
   */
  BoxDocStep createBoxDocStep(BoxDoc parent, Integer signCount, String uuid) {
    def q = 'select count(id) from BoxDocStep s where s.doc.id=?'
    def stepCountList = BoxDocStep.executeQuery(q, [parent.id])
    def stepCount = stepCountList[0]
    String docNo = "${parent.docNo}-${stepCount}"
    def docboxRef = uuid ?: UUID.randomUUID().toString()
    def step = new BoxDocStep(step: stepCount, docNo: docNo, docboxRef: docboxRef,
    signCount: signCount ?: 0)
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
    step.addToContents(contents).save()
    return contents
  }

  BoxContents createPdfContents(BoxDocStep step) {
    createContents(step, 'pdf', 'binary')
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

  BoxDocStep findStepByRefExc(String docboxRef) {
    def docStep = findStepByRef(docboxRef)
    if (!docStep) {
      def msg = "Document step not found with docboxRef ${docboxRef}"
      throw new ServiceException('DOCBOX.110', msg)
    }

    return docStep
  }

  /**
   * Given a form data uuid, find the latest document step.
   */
  BoxDocStep findStepByUuid(String uuid) {
    findStepByUuid(uuid, null)
  }

  BoxDocStep findStepByUuid(String uuid, Integer stepNumber) {
    if (log.debugEnabled) log.debug "findStepByUuid << ${uuid}, ${stepNumber}"
    if (stepNumber == null) {
      def q = 'select max(step) from BoxDocStep s where s.doc.formDataUuid=?'
      def list = BoxDocStep.executeQuery(q, [uuid])
      stepNumber = list[0]
    }

    if (log.debugEnabled) log.debug "findStepByUuid.stepNumber: ${stepNumber}"
    def docStep = null
    // stepNumber is null if there are no steps with the given uuid
    if (stepNumber != null) {
      def q = 'from BoxDocStep s where s.doc.formDataUuid=? and s.step=?'
      def docStepList = BoxDocStep.executeQuery(q, [uuid, stepNumber])
      docStep = (docStepList?.size() > 0)? docStepList[0] : null
    }

    if (log.debugEnabled) log.debug "findStepByUuid >> ${docStep}"
    return docStep
  }

  BoxDocStep findStepByUuidExc(String uuid) {
    findStepByUuidExc(uuid, null)
  }

  BoxDocStep findStepByUuidExc(String uuid, Integer stepNumber) {
    def docStep = findStepByUuid(uuid, stepNumber)
    if (!docStep) {
      def msg = "Document step not found with formDataUuid ${uuid}"
      if (stepNumber) msg += " and step number ${stepNumber}"
      throw new ServiceException('DOCBOX.116', msg)
    }

    return docStep
  }

  BoxDocStep findPredecessor(BoxDocStep docStep) {
    if (log.debugEnabled) log.debug "findPredecessor << ${docStep}"
    def q = 'from BoxDocStep s where s.doc.id=? and s.step=?'
    def docStepList = BoxDocStep.executeQuery(q, [docStep.doc.id, docStep.step - 1])
    def prevStep = (docStepList?.size() > 0)? docStepList[0] : null
    if (log.debugEnabled) log.debug "findPredecessor >> ${prevStep}"
    return prevStep
  }

  BoxContents findPdfContents(BoxDocStep docStep) {
    findContents(docStep, null)
  }

  BoxContents findPdfContentsExc(BoxDocStep docStep) {
    def pdfContents = findPdfContents(docStep)
    if (!pdfContents) {
      def msg = "PDF contents not found for document ref ${docStep.docboxRef}"
      throw new ServiceException('DOCBOX.111', msg)
    }

    return pdfContents
  }

  /**
   * Find a document step and its PDF contents.
   * Throw ServiceException if not found.
   * Return a map with the following entries:
   * docStep: the document step (BoxDocStep)
   * pdfContents: the PDF contents (BoxContents)
   */
  Map findPdfByRef(String docboxRef, Boolean requireLatest) {
    if (log.debugEnabled) log.debug "findPdfByRef << ${docboxRef}, ${requireLatest}"
    def docStep = findStepByRef(docboxRef)
    if (!docStep) {
      def msg = "Document not found with ref ${docboxRef}"
      throw new ServiceException('DOCBOX.110', msg)
    } else if (requireLatest) {
      requireLatestStep(docStep)
    }

    def pdfContents = findPdfContentsExc(docStep)
    def result = [docStep: docStep, pdfContents: pdfContents]
    if (log.debugEnabled) log.debug "findPdfByRef >> ${result}"
    return result
  }

  /**
   * Find without requiring that the document step is the latest.
   */
  Map findPdfByRef(String docboxRef) {
    findPdfByRef(docboxRef, false)
  }

  /**
   * Check that a given document step is currently the latest step.
   * docStep must be the document step to examine and must not be null.
   * Throws ServiceException on conflict.
   */
  private requireLatestStep(BoxDocStep docStep) {
    def q = 'select count(id) from BoxDocStep s where s.doc.id=? and s.step > ?'
    def list = BoxDocStep.executeQuery(q, [docStep.doc.id, docStep.step])
    Integer count = list[0]
    if (count > 0) {
      def msg = "Doc step ${docStep.docNo} has ${count} later steps"
      if (log.debugEnabled) log.debug "requireLatestStep EXC: ${msg}"
      throw new ServiceException('DOCBOX.105', msg)
    }
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
    RNG.nextBytes(bytes)
    // Limit to 30 bits
    bytes[0] &= 0x3F
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

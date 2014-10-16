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

/**
 * A form converted to PDF
 */
class BoxDocStep implements Comparable {
  // Date format for metadata
  private static final DFMT = 'yyyy-MM-dd HH:mm'

  // Step number
  Integer step

  // The document number of the BoxDoc with step number added
  String docNo

  // Auto timestamping
  Date dateCreated
  Date lastUpdated

  // A unique reference to this document
  String docboxRef

  // Number of signatures present in the document
  Integer signCount

  static mapping = {
    docboxRef index: 'DocboxRef_Idx'
  }
  static belongsTo = [doc: BoxDoc]
  static hasMany = [contents: BoxContents]
  static constraints = {
    step range: 0..999
    docNo maxSize: 16
    docboxRef maxSize: 200
    signCount range: 0..12
    dateCreated nullable: true
    lastUpdated nullable: true
  }

  /**
   * Get metadata as a Map
   */
  Map getMeta() {
    def map = [docboxRef: docboxRef, docNo: docNo, stepNo: step,
    created: dateCreated.format(DFMT), signCount: signCount]
    map.putAll(doc.meta)
    return map
  }

  /**
   * Get pdf contents for this doc step
   */
  BoxContents pdfContents() {
    BoxContents.find('from BoxContents c where c.step.id=? and c.name=?', [this.id, 'pdf'])
  }

  // To show in the gui
  String display() {
    docNo
  }

  String toString() {
    "[DocStep ${docNo}: ${docboxRef}, ${signCount}, ${contents?.size()}]"
  }
  
  //-------------------- Comparable --------------------

  int hashCode() {
    docNo.hashCode()
  }

  boolean equals(Object obj) {
    (obj instanceof BoxDocStep) && ((BoxDocStep)obj).docNo == docNo
  }

  // Comparison based on document number and step
  int compareTo(Object obj) {
    def other = (BoxDocStep)obj
    int outcome = docNo.compareTo(other.docNo)
    if (outcome == 0) outcome = step.compareTo(other.step)
    return outcome
  }

}

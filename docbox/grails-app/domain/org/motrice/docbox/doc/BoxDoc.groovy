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
 * A document: a form converted to PDF
 * A document may evolve in several steps
 * The primary key is a 30-bit integer, randomly chosen
 * When shown to a user it is converted to a readable format
 */
class BoxDoc implements Comparable {
  // The id on display format, called the document number
  String docNo

  // Auto timestamping
  Date dateCreated
  Date lastUpdated

  // The uuid identifying the original form
  // There may be several versions of the document so the uuid does not
  // uniquely identify a document
  String formDataUuid

  static mapping = {
    id generator: 'assigned'
    formDataUuid index: 'FormDataUuid_Idx'
  }
  SortedSet steps
  static hasMany = [steps: BoxDocStep]
  static constraints = {
    docNo maxSize: 16
    formDataUuid maxSize: 200
    dateCreated nullable: true
    lastUpdated nullable: true
  }

  /**
   * Get metadata as a Map
   */
  Map getMeta() {
    def map = [formDataUuid: formDataUuid]
    steps.each {step ->
      map[String.format('step_%02d', step.step)] = step.docboxRef
    }

    return map
  }

  String display() {
    "${docNo} (${formDataUuid})"
  }

  String toString() {
    "[Doc ${docNo}: ${formDataUuid}, ${steps?.size()}]"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    docNo.hashCode()
  }

  boolean equals(Object obj) {
    (obj instanceof BoxDoc) && ((BoxDoc)obj).docNo == docNo
  }

  // Comparison based on the document number
  int compareTo(Object obj) {
    def other = (BoxDoc)obj
    return docNo.compareTo(other.docNo)
  }

}

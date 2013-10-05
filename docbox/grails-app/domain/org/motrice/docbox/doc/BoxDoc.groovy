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
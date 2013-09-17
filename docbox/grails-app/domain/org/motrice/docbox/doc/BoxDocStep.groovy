package org.motrice.docbox.doc

/**
 * A form converted to PDF
 */
class BoxDocStep {
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
    signCount range: 0..12
    dateCreated nullable: true
    lastUpdated nullable: true
  }

  String toString() {
    "[DocStep ${docNo}: ${docboxRef}, ${signCount}, ${contents?.size()}]"
  }
  
}
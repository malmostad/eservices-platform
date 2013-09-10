package org.motrice.docbox.pdf

/**
 * A form converted to PDF
 */
class BoxDoc {
  // The uuid identifying the original form
  // There may be several versions of the document so the uuid does not
  // uniquely identify a document
  String formUuid

  // Number of signatures present in the document
  Integer signCount

  // Auto timestamping, must be nullable
  Date dateCreated

  static constraints = {
    uuid maxSize: 200
    signCount range: 0..12
    dateCreated nullable: true
  }
  
}
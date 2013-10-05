package org.motrice.docbox.doc

/**
 * A form converted to PDF
 */
class BoxDocStep implements Comparable {
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
  SortedSet contents
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

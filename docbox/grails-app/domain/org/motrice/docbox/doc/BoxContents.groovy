package org.motrice.docbox.doc

/**
 * Contents of a DocBox document
 */
class BoxContents {
  // A name distinguishing this from other contents of the same step
  String name

  // Format: xml, text or binary
  String format

  // Auto timestamping
  Date dateCreated
  Date lastUpdated

  // Size: number of characters in text, number of bytes in stream
  Integer size

  // Content is either text or binary
  // Text content
  String text
  // Binary content (PostgreSQL bytea limited to 1 GB)
  byte[] stream

  static belongsTo = [step: BoxDocStep]
  static transients = ['binary', 'contentType']
  static mapping = {
    text type: 'text'
  }
  static constraints = {
    name maxSize: 80, unique: 'step'
    dateCreated nullable: true
    lastUpdated nullable: true
    format nullable: false, maxSize: 80
    size range: 0..Integer.MAX_VALUE-1
    text nullable: true
    stream nullable: true
  }

  // Assign stream
  def assignStream(byte[] stream) {
    this.size = stream.length
    this.stream = stream
    this.text = null
    return this
  }

  // Assign text
  def assignText(String text) {
    this.size = text.length()
    this.text = text
    this.stream = null
    return this
  }

  /**
   * Is this binary contents?
   */
  Boolean isBinary() {
    format == 'binary'
  }

  /**
   * Our simplified content type
   */
  String getContentType() {
    binary? 'application/octet-stream' :
    ((format == 'xml')? 'application/xml;charset=UTF-8' : 'text/plain;charset=UTF-8')
  }

  String toString() {
    "[Contents ${id}: ${format}/${size}]"
  }

}

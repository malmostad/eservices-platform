package org.motrice.docbox.doc

import java.security.MessageDigest

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

  // Checksum for signing purposes
  String checksum

  // Size: number of characters in text, number of bytes in stream
  Integer size

  // Content is either text or binary
  // Text content
  String text
  // Binary content (PostgreSQL bytea limited to 1 GB)
  byte[] stream

  static belongsTo = [step: BoxDocStep]
  static transients = ['binary', 'contentType', 'fileName']
  static mapping = {
    text type: 'text'
  }
  static constraints = {
    name maxSize: 80, unique: 'step'
    dateCreated nullable: true
    lastUpdated nullable: true
    format nullable: false, maxSize: 80
    checksum nullable: true, maxSize: 200
    size range: 0..Integer.MAX_VALUE-1
    text nullable: true
    stream nullable: true
  }

  // Assign stream, conditionally compute checksum
  // Return this BoxContents
  def assignStream(byte[] stream, boolean createChecksum) {
    this.size = stream.length
    this.stream = stream
    this.text = null
    if (createChecksum) {
      def digest = MessageDigest.getInstance('SHA-256').digest(stream)
      def sw = new StringWriter()
      digest.encodeBase64().writeTo(sw)
      checksum = sw.toString()
    }
    return this
  }

  // Assign stream, no checksum
  def assignStream(byte[] stream) {
    return assignStream(stream, false)
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

  /**
   * Create a credible file name for downloading
   */
  String getFileName() {
    def docNo = step.docNo
    def fileName
    if (name.endsWith('.xml')) {
      fileName = "${docNo}-${name}"
    } else if (name == 'pdf') {
      fileName = "${docNo}.pdf"
    } else if (binary) {
      fileName = "${docNo}-${name}.bin"
    } else {
      fileName = "${docNo}-${name}.txt"
    }

    return fileName
  }

  // To show in the gui
  String display() {
    "${name} (size ${size})"
  }

  String toString() {
    "[Contents ${id}/${name}: ${format}/${size}]"
  }

}

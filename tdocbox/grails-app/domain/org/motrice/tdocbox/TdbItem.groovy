package org.motrice.tdocbox

import java.security.MessageDigest

/**
 * An item obtained from running a drill.
 * Mainly name-value.
 */
class TdbItem implements Comparable {
  // Item name
  String name

  // Item type, binary or text.
  // Binary if true, otherwise text (including null).
  Boolean binaryFlag

  // Text value
  String text

  // Binary value
  byte[] bytes

  // SHA-256 checksum, currently only used for binary contents
  String checksum

  static mapping = {
    text type: 'text'
  }
  static belongsTo = [tcase: TdbCase]
  static constraints = {
    binaryFlag nullable: true
    text nullable: true
    bytes nullable: true
    checksum nullable: true, maxSize: 200
  }

  /**
   * Create a text item without saving it.
   */
  static createTextItem(String name, String text) {
    new TdbItem(name: name, text: text, checksum: null)
  }

  /**
   * Create a binary item without saving it.
   */
  static createBinaryItem(String name, byte[] bytes) {
    def digest = MessageDigest.getInstance('SHA-256').digest(bytes)
    def sw = new StringWriter()
    digest.encodeBase64().writeTo(sw)
    new TdbItem(name: name, bytes: bytes, binaryFlag: true, checksum: sw.toString())
  }

  boolean isBinary() {
    binaryFlag
  }

  /**
   * The file name of this item when downloaded
   */
  String getFileName() {
    def fileExt = binary? 'bin' : 'txt'
    if (binary && bytes.length > 9) {
      def magic = new String(bytes, 0, 5, 'UTF-8')
      if (magic == '%PDF-') fileExt = 'pdf'
    }
    "${tcase?.timeStamp?.format('yyyyMMdd-HHmmss')}-${name}.${fileExt}"
  }

  def getValue() {
    binaryFlag? bytes : text
  }

  String toString() {
    binaryFlag? "${name}: ${bytes?.size()} bytes" : "${name}: ${text}"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    text? (name + text).hashCode() : (bytes? bytes.hashCode() : name.hashCode())
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof TdbItem) {
      def other = (TdbItem)obj
      result = this.is(other)
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (TdbItem)obj
    name.compareTo(other.name)
  }

}

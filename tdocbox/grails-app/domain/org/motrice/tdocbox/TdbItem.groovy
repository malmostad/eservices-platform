package org.motrice.tdocbox

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

  static mapping = {
    text type: 'text'
  }
  static belongsTo = [case: TdbCase]
  static constraints = {
    binaryFlag nullable: true
    text nullable: true
    bytes nullable: true
  }

  /**
   * Create a text item without saving it.
   */
  static createTextItem(String name, String text) {
    new TdbItem(name: name, text: text)
  }

  /**
   * Create a binary item without saving it.
   */
  static createBinaryItem(String name, byte[] bytes) {
    new TdbItem(name: name, bytes: bytes, binaryFlag: true)
  }

  boolean isBinary() {
    binaryFlag
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

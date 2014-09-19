package org.motrice.signatrice

/**
 * A name-value pair returned by a CGI call.
 * Also known as a property in their terminology.
 */
class SigAttribute implements Comparable {
  // Name (usually dot notation)
  String name

  // Value
  String value

  static belongsTo = [result: SigResult]
  static constraints = {
    name maxSize: 80
  }
  static mapping = {
    value type: 'text'
  }
  static transients = ['valueSize']

  Integer getValueSize() {
    value?.size() ?: 0
  }

  String toString() {
    def sb = new StringBuilder()
    sb.append(name).append(': ')
    if (value.size() <= 40) {
      sb.append("'").append(value).append("'")
    } else {
      sb.append("'").append(value.substring(0, 38)).append("...'")
    }

    return sb.toString()
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    (result.orderRef + name).hashCode()
  }

  boolean equals(Object obj) {
    def outcome = false
    if (obj instanceof SigAttribute) {
      def other = (SigAttribute)obj
      outcome = result?.equals(other?.result) && name.equals(other?.name)
    }

    return outcome
  }

  /**
   * Date-based comparison, latest first.
   */
  int compareTo(Object obj) {
    def other = (SigAttribute)obj
    def outcome = result?.compareTo(other?.result)
    if (outcome == null || outcome == 0) outcome = name.toUpperCase().compareTo(other.name.toUpperCase())
    return outcome
  }

}

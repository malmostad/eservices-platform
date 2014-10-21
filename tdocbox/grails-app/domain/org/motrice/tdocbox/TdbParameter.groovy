package org.motrice.tdocbox

/**
 * A name-value pair used by a drill.
 */
class TdbParameter implements Comparable {
  // Parameter name
  String name

  // Parameter value
  String value

  // Should the parameter value be Base64-encoded?
  Boolean b64Encode

  // Parameter description
  String description

  static belongsTo = [drill: TdbDrill]
  static constraints = {
    name nullable: false, maxSize: 64
    value nullable: true
    b64Encode nullable: true
    description nullable: true, maxSize: 400
  }
  static mapping = {
    value type: 'text'
  }

  String getValueEncoded() {
    b64Encode? value.getBytes('UTF-8').encodeBase64().toString() : value
  }

  String toString() {
    String valDisplay = value
    if (valDisplay?.size() > 40) {
      valDisplay = valDisplay.substring(0, 37) + '...'
    }

    "${name}: ${valDisplay}"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    (name + value + description).hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof TdbParameter) {
      def other = (TdbParameter)obj
      result = this.is(other)
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (TdbParameter)obj
    name.compareTo(other.name)
  }

}

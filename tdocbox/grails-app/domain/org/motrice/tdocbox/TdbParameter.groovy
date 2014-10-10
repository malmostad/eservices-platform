package org.motrice.tdocbox

/**
 * A name-value pair used by a drill.
 */
class TdbParameter {
  // Parameter name
  String name

  // Parameter value
  String value

  // Parameter description
  String description

  static belongsTo = [drill: TdbDrill]
  static constraints = {
    name nullable: false, maxSize: 64
    value nullable: true, maxSize: 400
    description nullable: true, maxSize: 400
  }

  String toString() {
    "${name}: ${value}"
  }

}

package org.motrice.tdocbox

/**
 * A sequence of Drills.
 */
class TdbSuite implements Comparable {
  // The name of the suite.
  String name

  // A free text description of the suite.
  String description

  SortedSet drills
  static hasMany = [drills: TdbDrill]
  static constraints = {
    name nullable: false, maxSize: 40, unique: true
    description nullable: true, maxSize: 400
  }

  TdbCase createCase() {
    TdbCase.createCase(this)
  }

  TdbCase createCase(TdbCase cs) {
    cs ?: TdbCase.createCase(this)
  }

  String toString() {
    name
  }

  String toTitle() {
    def shortDescr = description
    if (shortDescr?.size() > 47) shortDescr = shortDescr.substring(0, 44) + '..'
    return "${name}: ${shortDescr}"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    name.hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof TdbSuite) {
      def other = (TdbSuite)obj
      result = name == other.name
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (TdbSuite)obj
    name.compareTo(other.name)
  }

}

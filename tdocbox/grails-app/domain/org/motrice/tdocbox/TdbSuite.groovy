package org.motrice.tdocbox

/**
 * A sequence of Drills.
 */
class TdbSuite {
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

  String toString() {
    name
  }

}

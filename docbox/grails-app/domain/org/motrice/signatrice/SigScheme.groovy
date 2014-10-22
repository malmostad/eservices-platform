package org.motrice.signatrice

/**
 * A scheme provides the details for invoking a SigService.
 */
class SigScheme implements Comparable {
  // Name of this test case
  String name

  // Creation timestamp
  Date dateCreated

  // Display name (see CGI doco)
  SigDisplayname displayName

  // Policy, also known as service ID
  // Predefined policies for test only
  SigPolicy policy

  SortedSet results
  static belongsTo = [service: SigService]
  static hasMany = [results: SigResult, testCases: SigTestcase]
  static constraints = {
    name size: 1..120, unique: true
    dateCreated nullable: true
    results nullable: true
  }

  boolean isDefaultScheme() {
    SigDefaultScheme.isCurrentDefault(this)
  }

  /**
   * The flagged name get an asterisk at the end if the scheme is the current
   * default scheme.
   */
  String getFlaggedName() {
    defaultScheme? "${name} *" : name
  }

  /**
   * uname is a unique name composed of the common name and the creation timestamp
   */
  String getUname() {
    name + (dateCreated? String.format('%019d', dateCreated.time) : '')
  }

  String toString() {
    "${name} [${dateCreated.format('yyyy-MM-dd HH:mm')}]"
  }

  //-------------------- Comparable --------------------
  // Comparisons are based on uname (name and timestamp)

  int hashCode() {
    uname.hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof SigScheme) {
      def other = (SigScheme)obj
      result = uname == other.uname
    }

    return result
  }

  /**
   * Date-based comparison, latest first.
   */
  int compareTo(Object obj) {
    def other = (SigScheme)obj
    int outcome = name.compareTo(other.name)
    println "outcome 1: ${outcome}"
    if (outcome == 0) outcome = -dateCreated.compareTo(other.dateCreated)
    println "outcome 2: ${outcome}"
    return outcome
  }

}

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

  // Swedish personal identity number (12 digits)
  String personalIdNo

  // Text to be signed
  String userVisibleText

  // Transient abbreviation of the visible text
  String abbrVisibleText

  SortedSet results
  static belongsTo = [service: SigService]
  static hasMany = [results: SigResult]
  static mapping = {
    userVisibleText type: 'text'
  }
  static constraints = {
    name size: 1..120
    dateCreated nullable: true, unique: true
    personalIdNo maxSize: 12, nullable: true
    results nullable: true
  }
  static transients = ['abbrVisibleText', 'encodedVisibleText', 'uname']

  String getAbbrVisibleText() {
    (userVisibleText.length() > 24)? userVisibleText.substring(0, 22) + '...' : userVisibleText
  }

  /**
   * Encode the visible text as required by the GPR API
   */
  String getEncodedVisibleText() {
    userVisibleText.getBytes('UTF-8').encodeBase64()
  }

  /**
   * uname is a unique name composed of the common name and the creation timestamp
   */
  String getUname() {
    name + (dateCreated? String.format('%019d', dateCreated.time) : '')
  }

  String toString() {
    name + ' ' + dateCreated.format('yyyy-MM-dd HH:mm')
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

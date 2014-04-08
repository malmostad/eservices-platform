package org.motrice.migratrice

/**
 * A report from a package installation attempt.
 * Consists mainly of a chunk of plain text.
 * Use MigPackage.createReport in most cases.
 */
class MigReport implements Comparable {
  // The point in time when the package was created.
  // Used as the basis for sorting.
  Date tstamp

  // The report body
  // In PostgreSQL there is practically no upper size limit
  String body

  static belongsTo = [pkg: MigPackage]
  static mapping = {
    body type: 'text'
  }
  static constraints = {
    body nullable: false, blank: true
  }

  static MigReport createReport(String content) {
    new MigReport(tstamp: new Date(), body: content)
  }

  String display() {
    tstamp.format('yyyy-MM-dd HH:mm:ss')
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    tstamp.hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof MigPackage) {
      def other = (MigReport)obj
      result = tstamp == other.tstamp
    }

    return result
  }

  /**
   * Date-based comparison, latest first.
   */
  int compareTo(Object obj) {
    def other = (MigReport)obj
    -tstamp.compareTo(other.tstamp)
  }
}

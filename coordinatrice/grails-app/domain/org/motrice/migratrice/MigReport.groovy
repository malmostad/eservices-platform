/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
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

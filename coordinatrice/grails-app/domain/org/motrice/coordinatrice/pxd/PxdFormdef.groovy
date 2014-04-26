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
package org.motrice.coordinatrice.pxd

/**
 * Read-only access to Postxdb forms
 */
class PxdFormdef implements Comparable {

  String path
  String uuid
  String appName
  String formName
  Date dateCreated
  Date lastUpdated
  
  SortedSet forms
  static hasMany = [forms: PxdFormdefVer]
  static mapping = {
    cache usage: 'read-only'
    version false
  }
  static constraints = {
    path size: 3..256, unique: true
    uuid maxSize: 200, unique: true
    appName size: 1..120
    formName size: 1..120
    forms nullable: true
    dateCreated nullable: true
    lastUpdated nullable: true
  }

  String toString() {
    path
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    path.hashCode()
  }

  boolean equals(Object obj) {
    (obj instanceof PxdFormdef) && ((PxdFormdef)obj).path == path
  }

  int compareTo(Object obj) {
    def other = (PxdFormdef)obj
    // Path cannot be null
    path.compareTo(other.path)
  }
}

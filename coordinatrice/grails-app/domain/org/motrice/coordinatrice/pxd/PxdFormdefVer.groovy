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

import org.motrice.coordinatrice.MtfStartFormDefinition
import org.motrice.coordinatrice.Procdef

class PxdFormdefVer implements Comparable {
  // The magic draft number that means "published"
  static Integer PUBLISHED = 9999

  String path
  String appName
  String formName
  Integer fvno
  Integer draft
  String title
  String description
  String language
  Date dateCreated
  Date lastUpdated

  static belongsTo = [formdef: PxdFormdef]
  static mapping = {
    cache usage: 'read-only'
    version false
  }
  static constraints = {
    path nullable: false, size: 3..400, unique: true
    appName size: 1..120
    formName size: 1..120
    fvno range: 1..9999
    draft range: 1..10000
    title nullable: true, maxSize: 120
    description nullable: true, maxSize: 800
    language nullable: true, maxSize: 16
    formdef nullable: false
    dateCreated nullable: true
    lastUpdated nullable: true
  }

  static allPublishedForms() {
    PxdFormdefVer.findAllByDraft(PUBLISHED, [sort: 'path'])
  }

  static PxdFormdefVer latestPublished(PxdFormdef parent) {
    def list = PxdFormdefVer.findAllByFormdefAndDraft(parent, PUBLISHED, [sort: 'fvno', order: 'desc'])
    return list? list[0] : null
  }

  String toString() {
    path
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    path.hashCode()
  }

  boolean equals(Object obj) {
    (obj instanceof PxdFormdefVer) && ((PxdFormdefVer)obj).path == path
  }

  // This comparison is different from comparing paths.
  // Highest version and highest draft number comes first.
  int compareTo(Object obj) {
    def other = (PxdFormdefVer)obj
    int outcome = appName.compareTo(other.appName)
    if (outcome == 0) outcome = formName.compareTo(other.formName)
    if (outcome == 0) outcome = -fvno.compareTo(other.fvno)
    if (outcome == 0) outcome = -draft.compareTo(other.draft)
    return outcome
  }

}

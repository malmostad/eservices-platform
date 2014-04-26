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
package org.motrice.docbox.form

/**
 * A read-only form version as defined by the postxdb domain
 * org.motrice.postxdb.PxdFormdefVer
 * See org.motrice.postxdb.PxdFormdefVer for documentation
 */
class PxdFormdefVer {
  String path
  String appName
  String formName
  Integer fvno
  Integer draft
  String title
  String description
  String language

  static belongsTo = [formdef: PxdFormdef]
  static mapping = {
    datasource 'forms'
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
  }

  String toString() {
    "[FormdefVer ${path}: ${title}]"
  }
}

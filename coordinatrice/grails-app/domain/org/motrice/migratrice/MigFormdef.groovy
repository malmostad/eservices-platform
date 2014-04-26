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
 * A form definition, part of a porting work package.
 * A form definition is an abstraction, the collection of all versions
 * of a form.
 * Most of the fields refer to data from the originating Motrice site.
 */
class MigFormdef implements Comparable {
  // Ref: the database id the form definition had in the originating system.
  // A new database id is generated for the package, but the ref is vital
  // for resolving relationships within a package.
  Long ref

  // Original creation timestamp
  Date created

  // Original last updated timestamp
  Date updated

  // App name (an app may have any number of forms)
  String app

  // Form name
  String form

  // Uuid keeping together all parts of the form definition
  String uuid

  // The current draft at the time the package was generated
  String currentDraft

  SortedSet versions
  SortedSet items
  static belongsTo = [pack: MigPackage]
  static hasMany = [versions: MigFormdefVer, items: MigItem]
  static transients = ['path']
  static constraints = {
    app size: 1..120
    form size: 1..120
    uuid maxSize: 200
    currentDraft size: 3..400
    versions nullable: true
    items nullable: true
  }

  String getPath() {"${app}/${form}"}

  String display() {
    path
  }

  String toString() {
    "[Formdef ${path}, ${created}, ${currentDraft}]"
  }

  /**
   * Bootstrap init causes this method to be used for rendering as XML
   */
  def toXML(xml) {
    xml.build {
      ref(gen: version, id)
      created(created)
      updated(updated)
      app(app)
      form(form)
      uuid(uuid)
      currentDraft(currentDraft)
      pack(pack.id)
    }
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    path.hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof MigFormdef) {
      def other = (MigFormdef)obj
      result = path == other.path
    }

    return result
  }

  /**
   * Date-based comparison, latest first.
   */
  int compareTo(Object obj) {
    def other = (MigFormdef)obj
    return path.compareTo(other.path)
  }

}

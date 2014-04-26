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
 * A specific version of a form definition.
 * The version is available as an integer code (verno) and also as part
 * of the path (a String).
 */
class MigFormdefVer implements Comparable {
  // Ref: the database id the form definition had in the originating system.
  // A new database id is generated for the snapshot, but the ref is vital
  // for resolving relationships within a package.
  Long ref

  // Identify the owning Formdef
  Long formref

  // Original creation timestamp. Updated omitted, always same as created.
  Date created

  // App name (an app many have any number of forms)
  String app

  // Form name
  String form

  // Path identifying this form definition version
  String path

  // Form version number
  Integer verno

  // Draft number, null if the version is published
  Integer draft

  // Is this version published? An explicit flag in addition to the version number.
  Boolean published

  // Form title
  String title

  // Form description
  String description

  // Form language
  String language

  static belongsTo = [formdef: MigFormdef, pack: MigPackage]
  static constraints = {
    app size: 1..120
    form size: 1..120
    path size: 3..400
    draft nullable: true
    published nullable: true
    title nullable: true, maxSize: 120
    description nullable: true, maxSize: 800
    language nullable: true, maxSize: 80
  }

  String display() {
    path
  }

  /**
   * Bootstrap init causes this method to be used for rendering as XML
   */
  def toXML(xml) {
    xml.build {
      ref(id)
      created(created)
      app(app)
      form(form)
      path(path)
      verno(published: published, verno)
      published? draft() : draft(draft)
      title(title)
      description(description)
      language(language)
      formref(formdef.id)
      pack(pack.id)
    }
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    return (ref ^ formref).hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof MigFormdefVer) {
      def other = (MigFormdefVer)obj
      result = formref == other.formref && path == other.path
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (MigFormdefVer)obj
    def result = formref.compareTo(other.formref)
    if (result == 0) result = path.compareTo(other.path)
    return result
  }

}

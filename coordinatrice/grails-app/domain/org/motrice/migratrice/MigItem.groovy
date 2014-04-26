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
 * Contents of a form definition: XML or image files are the most common cases.
 */
class MigItem implements Comparable {
  // Ref: the database id the form definition had in the originating system.
  // A new database id is generated for the snapshot, but the ref is vital
  // for resolving relationships within a package.
  Long ref

  // Pointer to the owning Formdef, but not always available
  Long formref

  // Original creation timestamp. Updated omitted, always same as created.
  Date created

  // Path identifying this item
  String path

  // Uuid of the form definition to which this item belongs
  String uuid

  // Path of form definition to which this item belongs, if available
  String formpath

  // The format is either 'xml' or 'binary'
  String format

  // Size: number of characters in text, number of bytes in stream
  Integer size

  // A locally created export package only contains metadata to
  // avoid duplicate storage of contents.
  // So 'text' and 'stream' may be both null.
  // Content is either text or binary
  // Text content
  String text
  // Binary content (PostgreSQL bytea limited to 1 GB)
  byte[] stream

  // SHA1 hash sum of the contents
  String sha1

  // Transient attribute for flagging XML items to be installed
  boolean install

  // Transient attribute for flagging published XML items
  boolean published

  // Transient form def version number
  Integer verno

  static transients = ['install','published','verno']
  static belongsTo = [formdef: MigFormdef, pack: MigPackage]
  static mapping = {
    text type: 'text'
  }
  static constraints = {
    formref nullable: true
    path size: 3..400
    uuid maxSize: 200
    formpath nullable: true, maxSize: 400
    format maxSize: 80
    size range: 0..Integer.MAX_VALUE-1
    text nullable: true
    stream nullable: true
    sha1 maxSize: 400
  }

  // Assign stream
  def assignStream(byte[] stream) {
    this.size = stream.length
    this.stream = stream
    this.text = null
    return this
  }

  // Assign text
  def assignText(String text) {
    this.size = text.length()
    this.text = text
    this.stream = null
    return this
  }

  // Is this item XML?
  boolean xmlFormat() {
    format == 'xml'
  }

  String display() {
    "${path} (size ${size})"
  }

  String toString() {
    "[Item ${path}: ${id}/${ref}, ${size}]"
  }

  String toDump() {
    "[Item ${path}: ${id}/${ref}, ${published?'published':'--'}, ${install?'install':'--'}]"
  }

  /**
   * Bootstrap init causes this method to be used for rendering as XML
   */
  def toXML(xml) {
    xml.build {
      ref(id)
      created(created)
      path(path)
      uuid(uuid)
      formpath(formpath)
      format(format)
      size(size)
      sha1(sha1)
      if (formref) formref(formdef.id)
      pack(pack.id)
    }
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    return (ref ^ formref).hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof MigItem) {
      def other = (MigItem)obj
      result = formref == other.formref && path == other.path
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (MigItem)obj
    def result = formref.compareTo(other.formref)
    if (result == 0) result = path.compareTo(other.path)
    return result
  }

}

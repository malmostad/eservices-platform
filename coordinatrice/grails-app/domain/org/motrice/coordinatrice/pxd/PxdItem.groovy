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
 * A form resource, form definition or form data, text or binary.
 * Definition copied from postxdb.
 * In Coordinatrice only used to verify that it and postxdb are connected
 * to the same database.
 * A record is inserted over postxdb and then read and deleted directly
 * from the database.
 */
class PxdItem {
  // Unique path of this item
  String path

  // Orbeon "directory" uuid, if any.
  String uuid

  // Path of form definition version this item belongs to, if known, otherwise null.
  String formDef

  // Does this item belong to a form instance?
  Boolean instance

  // Format (after MIME conversion defined in config): xml or binary
  String format

  // Size: number of characters in text, number of bytes in stream
  Integer size

  // Content is either text or binary
  // Text content
  String text
  // Binary content (PostgreSQL bytea limited to 1 GB)
  byte[] stream

  Date dateCreated
  Date lastUpdated

  static constraints = {
    path nullable: false, unique: true
    uuid nullable: true, maxSize: 200
    formDef nullable: true, maxSize: 400
    dateCreated nullable: true
    lastUpdated nullable: true
    format nullable: false, maxSize: 80
    size range: 0..Integer.MAX_VALUE-1
    text nullable: true
    stream nullable: true
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

}

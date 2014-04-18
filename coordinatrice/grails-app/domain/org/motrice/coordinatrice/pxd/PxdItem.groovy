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

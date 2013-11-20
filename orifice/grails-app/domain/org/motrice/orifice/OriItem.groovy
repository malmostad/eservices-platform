package org.motrice.orifice

/**
 * Contents of a form definition: XML or image files are the most common cases.
 */
class OriItem {
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
  String formDef

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

  static belongsTo = [formdef: OriFormdef, pkg: OriPackage]
  static mapping = {
    text type: 'text'
  }
  static constraints = {
    formref nullable: true
    path size: 3..400
    uuid maxSize: 200
    formDef nullable: true, maxSize: 400
    format maxSize: 80
    size range: 0..Integer.MAX_VALUE-1
    text nullable: true
    stream nullable: true
    sha1 maxSize: 400
  }
}

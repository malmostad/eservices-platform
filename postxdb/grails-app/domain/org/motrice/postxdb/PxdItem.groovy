package org.motrice.postxdb

/**
 * A form resource, form definition or form data, text or binary.
 * Even if the path is unique, a Grails conventional id is used for primary key.
 * The reason is complications for the non-REST parts of the application
 * when deviating from Grails common conventions.
 */
class PxdItem {
  // Unique path of this item
  // Form definition draft:     app/form--v002_03/form.xml
  // Published form definition: app/form--v002/form.xhtml
  // Form definition resource:  {uuid}.png
  // Form data:                 {uuid}/data.xml
  // Form instance resource:    {uuid}.png
  String path

  // Orbeon "directory" uuid, if any
  // Used by form definition drafts, form definition resources, and
  // form instance resources
  String uuid

  // Form definition version this item belongs to, if known, otherwise null.
  // Examples: app/form--v002_03, app/form--v002
  // Form definition resources are stored before the form definition.
  // In such case the form definition cannot be known and is left as null, except
  // that, indirectly, the uuid would point to a form definition.
  String formDef

  // Does this item belong to a form instance?
  // If not it belongs to a form definition.
  // A purist would do this by subclassing.
  Boolean instance

  // Auto timestamping
  Date dateCreated
  Date lastUpdated

  // Format (after MIME conversion defined in config): xml or binary
  String format

  // Size: number of characters in text, number of bytes in stream
  Integer size

  // Content is either text or binary
  // Text content
  String text
  // Binary content (PostgreSQL bytea limited to 1 GB)
  byte[] stream

  static mapping = {
    name defaultValue: 'Cash'
    text type: 'text'
    uuid index: 'Uuid_Idx'
    formDef index: 'Formdef_Idx'
  }
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

  /**
   * Make a copy, almost, of an existing item except that it sets a different path
   * and formDef.
   * @param path must be the new path
   * Does NOT copy text/stream
   */
  static PxdItem almostCopy(PxdItem otherItem, String path, String formDef) {
    def item = new PxdItem(path: path, formDef: formDef, uuid: otherItem.uuid,
    instance: otherItem.instance, format: otherItem.format)
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

  String toString() {
    "[Item ${path}: ${formDef}, ${format}/${size}]"
  }
}
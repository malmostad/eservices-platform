package org.motrice.postxdb

import java.text.SimpleDateFormat
import java.security.MessageDigest

/**
 * A form resource, form definition or form data, text or binary.
 * Even if the path is unique, a Grails conventional id is used for primary key.
 * The reason is complications for the non-REST parts of the application
 * when deviating from Grails common conventions.
 */
class PxdItem {
  // Injection magic
  def grailsApplication

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

  // Path of form definition version this item belongs to, if known, otherwise null.
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

  // Id of PxdFormdef to which this item belongs, if known. Null otherwise.
  // Private means not persisted.
  private Long formref

  // Content is either text or binary
  // Text content
  String text
  // Binary content (PostgreSQL bytea limited to 1 GB)
  byte[] stream

  static mapping = {
    text type: 'text'
    uuid index: 'Uuid_Idx'
    formDef index: 'Formdef_Idx'
  }
  static transients = ['formref', 'sha1']
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

  /**
   * Compute the SHA1 hash of the content.
   * Returns the string "EMPTY" if there is no content.
   */
  String getSha1() {
    def result
    if (size == 0) {
      result = 'EMPTY'
    } else {
      result = xmlFormat()? sha1Hash(text.bytes) : sha1Hash(stream)
    }

    return result
  }

  private static String sha1Hash(byte[] bytes) {
    MessageDigest.getInstance('SHA1').digest(bytes)
    .collect {String.format('%02x', it)}.join('')
  }

  /**
   * Creation timestamp regular format
   */
  String createdr() {
    def fmt = new SimpleDateFormat(grailsApplication.config.postxdb.regular.fmt)
    return fmt.format(dateCreated)
  }

  /**
   * Updated timestamp regular format
   */
  String updatedr() {
    def fmt = new SimpleDateFormat(grailsApplication.config.postxdb.regular.fmt)
    return fmt.format(lastUpdated)
  }

  String toString() {
    "[Item ${id}/${path}: ${formDef}, ${format}/${size}]"
  }

  /**
   * Bootstrap init causes this method to be used for rendering as XML
   */
  def toXML(xml) {
    xml.build {
      ref(id)
      if (formref) formref(formref)
      created(dateCreated)
      path(path)
      uuid(uuid)
      formpath(formDef)
      format(format)
      size(size)
      sha1(sha1)
    }
  }

}

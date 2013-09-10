package org.motrice.docbox.form

/**
 * A read-only form resource as defined by the postxdb domain
 * org.motrice.postxdb.PxdItem
 * See org.motrice.postxdb.PxdItem for documentation
 */
class PxdItem {
  String path
  String uuid
  String formDef
  Boolean instance
  String format
  Integer size
  String text
  byte[] stream

  static mapping = {
    datasource 'forms'
    text type: 'text'
  }
  static constraints = {
    path nullable: false, unique: true
    uuid nullable: true, maxSize: 200
    formDef nullable: true, maxSize: 400
    format nullable: false, maxSize: 80
    size range: 0..Integer.MAX_VALUE-1
    text nullable: true
    stream nullable: true
  }

  String toString() {
    "[Item ${path}: ${formDef}, ${format}/${size}]"
  }
}

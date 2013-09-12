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
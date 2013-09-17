package org.motrice.docbox.form

/**
 * A read-only form definition as defined by the postxdb domain
 * org.motrice.postxdb.PxdFormdef
 * See org.motrice.postxdb.PxdFormdef for documentation
 */
class PxdFormdef {
  String path
  String uuid

  static hasMany = [forms: PxdFormdefVer]
  static mapping = {
    datasource 'forms'
  }
  static constraints = {
    path size: 3..256
    uuid maxSize: 200
  }
}

package org.motrice.coordinatrice.pxd

/**
 * Read-only access to Postxdb forms
 */
class PxdFormdef implements Comparable {

  String path
  String uuid
  String appName
  String formName
  Date dateCreated
  Date lastUpdated
  
  SortedSet forms
  static hasMany = [forms: PxdFormdefVer]
  static mapping = {
    cache usage: 'read-only'
    version false
  }
  static constraints = {
    path size: 3..256, unique: true
    uuid maxSize: 200, unique: true
    appName size: 1..120
    formName size: 1..120
    forms nullable: true
    dateCreated nullable: true
    lastUpdated nullable: true
  }

  String toString() {
    path
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    path.hashCode()
  }

  boolean equals(Object obj) {
    (obj instanceof PxdFormdef) && ((PxdFormdef)obj).path == path
  }

  int compareTo(Object obj) {
    def other = (PxdFormdef)obj
    // Path cannot be null
    path.compareTo(other.path)
  }
}

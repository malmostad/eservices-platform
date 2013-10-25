package org.motrice.coordinatrice.pxd

class PxdFormdefVer implements Comparable {

  String path
  String appName
  String formName
  Integer fvno
  Integer draft
  String title
  String description
  String language
  Date dateCreated
  Date lastUpdated

  static belongsTo = [formdef: PxdFormdef]
  static mapping = {
    cache usage: 'read-only'
    version false
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
    formdef nullable: false
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
    (obj instanceof PxdFormdefVer) && ((PxdFormdefVer)obj).path == path
  }

  // This comparison is different from comparing paths.
  // Highest version and highest draft number comes first.
  int compareTo(Object obj) {
    def other = (PxdFormdefVer)obj
    int outcome = appName.compareTo(other.appName)
    if (outcome == 0) outcome = formName.compareTo(other.formName)
    if (outcome == 0) outcome = -fvno.compareTo(other.fvno)
    if (outcome == 0) outcome = -draft.compareTo(other.draft)
    return outcome
  }

}

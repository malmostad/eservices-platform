package org.motrice.orifice

/**
 * A form definition, part of a porting work package.
 * A form definition is an abstraction, the collection of all versions
 * of a form.
 * Most of the fields refer to data from the originating Motrice site.
 */
class OriFormdef implements Comparable {
  // Ref: the database id the form definition had in the originating system.
  // A new database id is generated for the package, but the ref is vital
  // for resolving relationships within a package.
  Long ref

  // Original creation timestamp
  Date created

  // Original last updated timestamp
  Date updated

  // App name (an app may have any number of forms)
  String app

  // Form name
  String form

  // Uuid keeping together all parts of the form definition
  String uuid

  // The current draft at the time the package was generated
  String currentDraft

  SortedSet versions
  SortedSet items
  static belongsTo = [pkg: OriPackage]
  static hasMany = [versions: OriFormdefVer, items: OriItem]
  static transients = ['path']
  static constraints = {
    app size: 1..120
    form size: 1..120
    uuid maxSize: 200
    currentDraft size: 3..400
    versions nullable: true
    items nullable: true
  }

  String getPath() {"${app}/${form}"}

  String display() {
    path
  }

  String toString() {
    "[Formdef ${path}, ${created}, ${currentDraft}]"
  }

  /**
   * Bootstrap init causes this method to be used for rendering as XML
   */
  def toXML(xml) {
    xml.build {
      ref(gen: version, id)
      created(created)
      updated(updated)
      app(app)
      form(form)
      uuid(uuid)
      currentDraft(currentDraft)
      pkg(pkg.id)
    }
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    def hash = path.hashCode()
    if (pkg) hash ^= pkg.hashCode()
    return hash
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof OriFormdef) {
      def other = (OriFormdef)obj
      result = pkg? pkg.equals(other.pkg) : other.pkg == null
      if (result) result = path == other.path
    }

    return result
  }

  /**
   * Date-based comparison, latest first.
   */
  int compareTo(Object obj) {
    def other = (OriFormdef)obj
    def result = pkg? pkg.compareTo(other.pkg) : null
    // The condition can be written (result) but this is more readable
    if (result == null || result == 0) result = path.compareTo(other.path)
    return result
  }

}

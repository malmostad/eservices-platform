package org.motrice.migratrice

/**
 * A package is a container for transferring form definitions between
 * Motrice sites.
 * A package contains one or more form definitions, all versions of those
 * form definitions, and all items (i.e. data) making up the form definitions.
 * Form definitions and versions really are metadata.
 * The substance of a form definition is the items.
 * A package may be gererated "here" (the local Motrice site), or in some
 * other site and unpacked here.
 * The same structure is used for both cases.
 */
class MigPackage implements Comparable {
  // Name of the site where the package was generated
  String siteName

  // Name of the package.
  // The name need not be unique in itself, only with site name and timestamp.
  String packageName

  // Format of the package
  String packageFormat

  // Site timestamp, the point in time when the package was generated
  Date siteTstamp

  // Was the package generated here?
  Boolean originLocal

  // Auto timestamping, the point in time when the package was created here.
  Date dateCreated

  SortedSet formdefs
  static hasMany = [formdefs: MigFormdef, versions: MigFormdefVer, items: MigItem]
  static constraints = {
    siteName size: 1..120
    packageName size: 1..120
    originLocal nullable: true
    dateCreated nullable: true
    formdefs nullable: true
    versions nullable: true
    items nullable: true
  }

  String toString() {
    "[Package ${siteName}-${packageName} ${originLocal?'Local':'Remote'}]"
  }

  /**
   * Bootstrap init causes this method to be used for rendering as XML
   * @param xml must be a grails.converters.XML
   */
  def toXML(xml) {
    xml.build {
      siteName(siteName)
      packageName(packageName)
      ref(id)
      siteTstamp(siteTstamp)
      packageFormat(packageFormat)
      created(dateCreated)
    }
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    (siteName + packageName).hashCode() ^ siteTstamp.hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof MigPackage) {
      def other = (MigPackage)obj
      result = siteName == other.siteName && packageName == other.packageName &&
      siteTstamp == other.siteTstamp
    }

    return result
  }

  /**
   * Date-based comparison, latest first.
   */
  int compareTo(Object obj) {
    def other = (MigPackage)obj
    def result = -siteTstamp.compareTo(other.siteTstamp)
    if (result == 0) {
      result = siteName.compareTo(other.siteName)
      if (result == 0) {
	result == packageName.compareTo(other.packageName)
      }
    }

    return result
  }

}

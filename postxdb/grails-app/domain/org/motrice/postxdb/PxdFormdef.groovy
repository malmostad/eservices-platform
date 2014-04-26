/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
package org.motrice.postxdb

import java.text.SimpleDateFormat

import org.motrice.postxdb.FormdefPath;

/**
 * The common element uniting a number of form definition versions.
 * This class is mainly an app/form name without a version.
 * NOTE: Tried to use path as primary key, but got problems with uuid uniqueness constraint
 */
class PxdFormdef implements Comparable {
  def grailsApplication

  /**
   * The path appName/formName without a version.
   * Its components are stored with intentional redundancy.
   * Use the assignPath methods.
   */
  String path

  // Orbeon uuid for saved form definitions
  // There is a one-to-one mapping between uuid and PxdFormdef
  String uuid

  // Application name. Part of primary key, may not be modified.
  String appName

  // Form name. Part of primary key, may not be modified.
  String formName

  // Auto timestamping
  Date dateCreated
  Date lastUpdated

  // The current draft form definition, the only editable version.
  // The field must contain the full path of the currentDraft.
  // Example: alpha/beta--v001_02
  // Whenever a draft is saved this field must be set.
  String currentDraft

  // A form definition may have any number of versions
  // If there are versions, one of them is the current draft (see currentDraft)
  SortedSet forms
  static hasMany = [forms: PxdFormdefVer]

  static constraints = {
    path size: 3..256, unique: true
    uuid maxSize: 200, unique: true
    appName size: 1..120
    formName size: 1..120
    dateCreated nullable: true
    lastUpdated nullable: true
    currentDraft nullable: true, size: 3..400
    forms nullable: true
  }

  /**
   * Set all components of the primary key
   */
  def assignPath(String path) {
    def fdpath = new FormdefPath(path)
    assignPath(fdpath)
  }

  def assignPath(FormdefPath fdpath) {
    this.path = fdpath.toString()
    appName = fdpath.appName
    formName = fdpath.formName
    return this
  }

  /**
   * Creation timestamp Orbeon format
   */
  String createdf() {
    def fmt = new SimpleDateFormat(grailsApplication.config.postxdb.tstamp.fmt)
    return fmt.format(dateCreated)
  }

  /**
   * Updated timestamp Orbeon format
   */
  String updatedf() {
    def fmt = new SimpleDateFormat(grailsApplication.config.postxdb.tstamp.fmt)
    return fmt.format(lastUpdated)
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

  String display() {
    "${path} (${currentDraft})"
  }

  String toString() {
    "[Formdef ${path}, ${currentDraft}]"
  }

  /**
   * Bootstrap init causes this method to be used for rendering as XML
   */
  def toXML(xml) {
    xml.build {
      ref(gen: version, id)
      created(dateCreated)
      updated(lastUpdated)
      app(appName)
      form(formName)
      uuid(uuid)
      currentDraft(currentDraft)
    }
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

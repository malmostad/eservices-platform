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
package org.motrice.coordinatrice

import org.motrice.coordinatrice.pxd.PxdFormdef

/**
 * A locale-dependent label for a form (used as a start form).
 */
class CrdI18nFormLabel implements Comparable {

  // Form definition id, really a foreign key to PxdFormdef.
  // Motrice conventions is not to declare foreign keys over module boundaries.
  Long formdefId

  // Form definition version number.
  // Makes it possible to have more than one label for a given form.
  // The version number indicates the first version for which this label
  // should be used.
  // For instance, if formdefVer is 4 it means that the definition is valid for
  // form definitions version 4 and higher.
  // The first process version is 1 but the default value here is 0.
  Integer formdefVer

  // Locale string
  String locale

  // The form definition label
  String label

  static mapping = {
    table 'crd_i18n_form_label'
    // This seems to have no effect in Grails 2.2.4
    formdefVer defaultValue: 0
  }
  static transients = ['formdef']
  // A lot of indexes are justified because this is a read-mostly structure.
  // Simplify read at the expense of inserts and updates.
  static constraints = {
    formdefVer min: 0, unique: ['formdefId', 'locale']
    locale maxSize: 24, unique: ['formdefId', 'formdefVer']
    label maxSize: 255, nullable: true, unique: ['formdefId', 'formdefVer', 'locale']
  }

  /**
   * The equivalent of a copy constructor.
   */
  static CrdI18nFormLabel copyLabel(CrdI18nFormLabel srcLabel) {
    new CrdI18nFormLabel(formdefId: srcLabel.formdefId, formdefVer: srcLabel.formdefVer,
    locale: srcLabel.locale, label: srcLabel.label)
  }

  /**
   * Compensates for not treating this as a relationship (which would cause
   * cross-module database constraints).
   */
  PxdFormdef getFormdef() {
    PxdFormdef.get(formdefId)
  }

  def setFormdef(PxdFormdef formdefInst) {
    formdefId = formdefInst.id
  }

  String toString() {
    "${formdefId}|${formdefVer} '${label}'"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    def sb = new StringBuilder()
    sb.append(formdefId).append(formdefVer).append(locale).append(label).toString().hashCode()
  }

  boolean equals(Object obj) {
    boolean result = obj instanceof CrdI18nFormLabel
    if (result) {
      def other = (CrdI18nFormLabel)obj
      result = (formdefId == other.formdefId) && (formdefVer == other.formdefVer) &&
      (locale == other.locale)
      if (result) result = (label == null && other.label == null) ||
      (label && other.label && label == other.label)
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (CrdI18nFormLabel)obj
    int result = formdefId.compareTo(other.formdefId)
    // Version in descending order
    if (result == 0) result = -formdefVer.compareTo(other.formdefVer)
    if (result == 0) result = locale.compareTo(other.locale)
    if (result == 0 && label) result = label.compareTo(other.label)
    return result
  }

}

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
package org.motrice.docbox.pdf

/**
 * An enumeration of possible values for a control, either exclusively
 * or multi-choice
 * Each value has a locale-dependent presentation string (label)
 * and a key string (value) that is stored in form data
 */
class Options {
  // List of key strings
  List keyList

  // Map key string to value
  Map kvMap

  Options() {
    keyList = []
    kvMap = [:]
  }

  /**
   * Add a key-value pair
   * Used when building options
   */
  def addKeyValue(String key, String value) {
    keyList << key
    kvMap[key] = value
  }

  boolean isEmpty() {
    keyList.isEmpty()
  }

  String display(String key) {
    kvMap[key]
  }

  String toString() {
    "{Options keys=${keyList} map=${kvMap}}"
  }

}

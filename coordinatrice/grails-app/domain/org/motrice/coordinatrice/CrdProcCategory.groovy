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

/**
 * The "category" attribute in Activiti process definitions.
 * Coordinatrice does not assume any meaning in category names.
 */
class CrdProcCategory {
  // Category name
  String name

  // Free text description of the category
  String description

  // Automatic timestamping
  Date dateCreated
  Date lastUpdated

  static mapping = {
    description type: 'text'
  }
  static constraints = {
    name blank: false, maxSize: 200, unique: true
    description nullable: true
    dateCreated nullable: true
    lastUpdated nullable: true
  }

  static final String DEFAULT_CATEGORY_NAME = 'http://www.activiti.org/test'
  static final String DEFAULT_CATEGORY_DESCRIPTION = 'Activiti built-in category'

  static CrdProcCategory createCategory(String name, String description) {
    if (!CrdProcCategory.findByName(name)) {
      def categ = new CrdProcCategory(name: name, description: description)
      categ.save(failOnError: true)
    }
  }

  /**
   * Return the default category
   */
  static CrdProcCategory defaultEntry() {
    CrdProcCategory.findByName(DEFAULT_CATEGORY_NAME)
  }

  String toString() {
    name
  }

}

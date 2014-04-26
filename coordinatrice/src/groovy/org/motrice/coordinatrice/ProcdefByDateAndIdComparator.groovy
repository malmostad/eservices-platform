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
 * Comparator for sorting process definitions first by descending
 * deployment date, then by ascending id.
 */
class ProcdefByDateAndIdComparator implements Comparator {
  /**
   * Construct
   */
  ProcdefByDateAndIdComparator() {
  }

  int compare(o1, o2) {
    def obj1 = o1 as Procdef
    def obj2 = o2 as Procdef
    def result = obj2.deployedTime.compareTo(obj1.deployedTime)
    if (result == 0) result = obj1.uuid.compareTo(obj2.uuid)
    return result
  }

  boolean equals(Object obj) {
    obj instanceof ProcdefByDateAndIdComparator
  }
  
}


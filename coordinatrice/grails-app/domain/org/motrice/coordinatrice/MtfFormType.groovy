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
 * Form types for MtfActivityFormDefinition and MtfStartFormDefinition
 */
class MtfFormType {

  // Descriptive label
  String label

  // Spring bean managing this form type. Not used by Coordinatrice.
  String formHandlerBean

  static mapping = {
    id column: 'formtypeid'
    version false
    formHandlerBean column: 'formhandlerbean'
  }
  static constraints = {
    label nullable: true, maxSize: 255
    formHandlerBean nullable: true, maxSize: 255
  }

  // Pre-defined form types...
  static final ORBEON_TYPE_ID = 1
  static final SIGN_START_FORM_ID = 2
  static final SIGN_TASK_FORM_ID = 3
  static final PAY_TASK_ID = 4
  static final NOTIFY_ACT_ID = 5
  static final NO_FORM_ID = 6

  String toString() {
    "[FormType ${id}: ${label}]"
  }

}

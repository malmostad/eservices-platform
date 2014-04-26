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

import org.motrice.coordinatrice.pxd.PxdFormdefVer

/**
 * This helper class is introduced because of Hibernate problems.
 * It just cannot manage MtfStartFormDefinition.
 * This is a workaround.
 */
class StartFormView {
  // Id of this start form definition
  Long id

  // Id of a MtfFormType
  Long formHandlerId

  // A form definition path
  String formConnectionKey

  // If there is a form definition path this is the id
  Long formdefId

  // BPMN process definition id
  String procdefId

  String toString() {
    "[StartFormView(${id}): type=${formHandlerId}, form=(${formConnectionKey}|${formdefId}, process=${procdefId}]"
  }
}

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

class RestProcdefController {
  private final static Integer CONFLICT_STATUS = 409
  private final static CONT_DISP = 'Content-Disposition'

  /**
   * Get the state of a process definition.
   * $PREFIX/procdef/state/$id
   * where $id must be a process definition id.
   */
  def procdefStateGet(String id) {
    if (log.debugEnabled) log.debug "PROCDEF STATE GET: ${params}"
    def procdef = CrdProcdef.findByActid(id)
    if (procdef) {
      render(status: 200, contentType: 'text/json') {
	procdefId = id
	procdefVer = procdef.actver
	stateCode = procdef.state.id
	stateName = procdef.state.defaultMessage
	editable = procdef.state.editable
	startableCode = procdef.state.startable
	startableName = procdef.state.startableToString()
      }
    } else {
      if (log.debugEnabled) log.debug "PROCDEF STATE GET >> 404: ${id} not found"
      render(status: 404)
    }
  }

}

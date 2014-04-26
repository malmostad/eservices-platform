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
package org.motrice.migratrice

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.XML

class MigFormdefController {

  static allowedMethods = [export: "POST", save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    [migFormdefInstList: MigFormdef.list(params), migFormdefInstTotal: MigFormdef.count()]
  }

  def show(Long id) {
    def migFormdefInst = MigFormdef.get(id)
    if (!migFormdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'migFormdef.label', default: 'MigFormdef'), id])
      redirect(action: "list")
      return
    }

    [migFormdefInst: migFormdefInst]
  }

}

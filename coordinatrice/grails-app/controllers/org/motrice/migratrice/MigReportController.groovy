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

class MigReportController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [migReportInstList: MigReport.list(params), migReportInstTotal: MigReport.count()]
    }

    def show(Long id) {
        def migReportInst = MigReport.get(id)
        if (!migReportInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'migReport.label', default: 'MigReport'), id])
            redirect(action: "list")
            return
        }

        [migReportInst: migReportInst]
    }

    def delete(Long id) {
        def migReportInst = MigReport.get(id)
        if (!migReportInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'migReport.label', default: 'MigReport'), id])
            redirect(controller: 'migPackage', action: 'list')
            return
        }

        try {
            migReportInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'migReport.label', default: 'MigReport'), id])
            redirect(controller: 'migPackage', action: 'show', id: migReportInst.pkg.id)
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'migReport.label', default: 'MigReport'), id])
            redirect(action: 'show', id: id)
        }
    }
}

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
            redirect(action: "list")
            return
        }

        try {
            migReportInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'migReport.label', default: 'MigReport'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'migReport.label', default: 'MigReport'), id])
            redirect(action: "show", id: id)
        }
    }
}

package org.motrice.migratrice

import org.springframework.dao.DataIntegrityViolationException

class MigFormdefVerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [migFormdefVerInstList: MigFormdefVer.list(params), migFormdefVerInstTotal: MigFormdefVer.count()]
    }

    def show(Long id) {
        def migFormdefVerInst = MigFormdefVer.get(id)
        if (!migFormdefVerInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'migFormdefVer.label', default: 'MigFormdefVer'), id])
            redirect(action: "list")
            return
        }

        [migFormdefVerInst: migFormdefVerInst]
    }

}

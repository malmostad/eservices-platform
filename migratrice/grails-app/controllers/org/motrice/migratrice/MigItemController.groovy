package org.motrice.migratrice

import org.springframework.dao.DataIntegrityViolationException

class MigItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [migItemInstList: MigItem.list(params), migItemInstTotal: MigItem.count()]
    }

    def show(Long id) {
        def migItemInst = MigItem.get(id)
        if (!migItemInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'migItem.label', default: 'MigItem'), id])
            redirect(action: "list")
            return
        }

        [migItemInst: migItemInst]
    }

}


package org.motrice.coordinatrice.bonita

import org.springframework.dao.DataIntegrityViolationException

class BnProcDefController {

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
	if (!params.sort) params.sort = 'uuid'
        [bnProcDefInstList: BnProcDef.list(params), bnProcDefInstTotal: BnProcDef.count()]
    }

    def show(Long id) {
        def bnProcDefInst = BnProcDef.get(id)
        if (!bnProcDefInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnProcDef.label', default: 'BnProcDef'), id])
            redirect(action: "list")
            return
        }

        [bnProcDefInst: bnProcDefInst]
    }

}

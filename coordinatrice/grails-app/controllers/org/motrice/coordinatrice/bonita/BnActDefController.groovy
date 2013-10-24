package org.motrice.coordinatrice.bonita

import org.springframework.dao.DataIntegrityViolationException

class BnActDefController {

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [bnActDefInstList: BnActDef.list(params), bnActDefInstTotal: BnActDef.count()]
    }

    def show(Long id) {
        def bnActDefInst = BnActDef.get(id)
        if (!bnActDefInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), id])
            redirect(action: "list")
            return
        }

        [bnActDefInst: bnActDefInst]
    }

}

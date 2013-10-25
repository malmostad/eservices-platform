package org.motrice.coordinatrice.pxd

import org.springframework.dao.DataIntegrityViolationException

class PxdFormdefController {

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
	if (!params.sort) params.sort = 'path'
        [pxdFormdefInstList: PxdFormdef.list(params), pxdFormdefInstTotal: PxdFormdef.count()]
    }

    def show(Long id) {
        def pxdFormdefInst = PxdFormdef.get(id)
        if (!pxdFormdefInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), id])
            redirect(action: "list")
            return
        }

        [pxdFormdefInst: pxdFormdefInst]
    }

}

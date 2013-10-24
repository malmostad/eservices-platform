package org.motrice.coordinatrice.pxd

import org.springframework.dao.DataIntegrityViolationException

class PxdFormdefVerController {

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [pxdFormdefVerInstList: PxdFormdefVer.list(params), pxdFormdefVerInstTotal: PxdFormdefVer.count()]
    }

    def show(Long id) {
        def pxdFormdefVerInst = PxdFormdefVer.get(id)
        if (!pxdFormdefVerInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer'), id])
            redirect(action: "list")
            return
        }

        [pxdFormdefVerInst: pxdFormdefVerInst]
    }

}

package org.motrice.coordinatrice.bonita

import org.springframework.dao.DataIntegrityViolationException

class BnProcDefController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [bnProcDefInstList: BnProcDef.list(params), bnProcDefInstTotal: BnProcDef.count()]
    }

    def create() {
        [bnProcDefInst: new BnProcDef(params)]
    }

    def save() {
        def bnProcDefInst = new BnProcDef(params)
        if (!bnProcDefInst.save(flush: true)) {
            render(view: "create", model: [bnProcDefInst: bnProcDefInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'bnProcDef.label', default: 'BnProcDef'), bnProcDefInst.id])
        redirect(action: "show", id: bnProcDefInst.id)
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

    def edit(Long id) {
        def bnProcDefInst = BnProcDef.get(id)
        if (!bnProcDefInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnProcDef.label', default: 'BnProcDef'), id])
            redirect(action: "list")
            return
        }

        [bnProcDefInst: bnProcDefInst]
    }

    def update(Long id, Long version) {
        def bnProcDefInst = BnProcDef.get(id)
        if (!bnProcDefInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnProcDef.label', default: 'BnProcDef'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (bnProcDefInst.version > version) {
                bnProcDefInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'bnProcDef.label', default: 'BnProcDef')] as Object[],
                          "Another user has updated this BnProcDef while you were editing")
                render(view: "edit", model: [bnProcDefInst: bnProcDefInst])
                return
            }
        }

        bnProcDefInst.properties = params

        if (!bnProcDefInst.save(flush: true)) {
            render(view: "edit", model: [bnProcDefInst: bnProcDefInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'bnProcDef.label', default: 'BnProcDef'), bnProcDefInst.id])
        redirect(action: "show", id: bnProcDefInst.id)
    }

    def delete(Long id) {
        def bnProcDefInst = BnProcDef.get(id)
        if (!bnProcDefInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnProcDef.label', default: 'BnProcDef'), id])
            redirect(action: "list")
            return
        }

        try {
            bnProcDefInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'bnProcDef.label', default: 'BnProcDef'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'bnProcDef.label', default: 'BnProcDef'), id])
            redirect(action: "show", id: id)
        }
    }
}

package org.motrice.coordinatrice.bonita

import org.springframework.dao.DataIntegrityViolationException

class BnActDefController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [bnActDefInstList: BnActDef.list(params), bnActDefInstTotal: BnActDef.count()]
    }

    def create() {
        [bnActDefInst: new BnActDef(params)]
    }

    def save() {
        def bnActDefInst = new BnActDef(params)
        if (!bnActDefInst.save(flush: true)) {
            render(view: "create", model: [bnActDefInst: bnActDefInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), bnActDefInst.id])
        redirect(action: "show", id: bnActDefInst.id)
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

    def edit(Long id) {
        def bnActDefInst = BnActDef.get(id)
        if (!bnActDefInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), id])
            redirect(action: "list")
            return
        }

        [bnActDefInst: bnActDefInst]
    }

    def update(Long id, Long version) {
        def bnActDefInst = BnActDef.get(id)
        if (!bnActDefInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (bnActDefInst.version > version) {
                bnActDefInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'bnActDef.label', default: 'BnActDef')] as Object[],
                          "Another user has updated this BnActDef while you were editing")
                render(view: "edit", model: [bnActDefInst: bnActDefInst])
                return
            }
        }

        bnActDefInst.properties = params

        if (!bnActDefInst.save(flush: true)) {
            render(view: "edit", model: [bnActDefInst: bnActDefInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), bnActDefInst.id])
        redirect(action: "show", id: bnActDefInst.id)
    }

    def delete(Long id) {
        def bnActDefInst = BnActDef.get(id)
        if (!bnActDefInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), id])
            redirect(action: "list")
            return
        }

        try {
            bnActDefInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'bnActDef.label', default: 'BnActDef'), id])
            redirect(action: "show", id: id)
        }
    }
}

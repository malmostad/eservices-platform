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

    def create() {
        [migItemInst: new MigItem(params)]
    }

    def save() {
        def migItemInst = new MigItem(params)
        if (!migItemInst.save(flush: true)) {
            render(view: "create", model: [migItemInst: migItemInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'migItem.label', default: 'MigItem'), migItemInst.id])
        redirect(action: "show", id: migItemInst.id)
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

    def edit(Long id) {
        def migItemInst = MigItem.get(id)
        if (!migItemInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'migItem.label', default: 'MigItem'), id])
            redirect(action: "list")
            return
        }

        [migItemInst: migItemInst]
    }

    def update(Long id, Long version) {
        def migItemInst = MigItem.get(id)
        if (!migItemInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'migItem.label', default: 'MigItem'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (migItemInst.version > version) {
                migItemInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'migItem.label', default: 'MigItem')] as Object[],
                          "Another user has updated this MigItem while you were editing")
                render(view: "edit", model: [migItemInst: migItemInst])
                return
            }
        }

        migItemInst.properties = params

        if (!migItemInst.save(flush: true)) {
            render(view: "edit", model: [migItemInst: migItemInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'migItem.label', default: 'MigItem'), migItemInst.id])
        redirect(action: "show", id: migItemInst.id)
    }

    def delete(Long id) {
        def migItemInst = MigItem.get(id)
        if (!migItemInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'migItem.label', default: 'MigItem'), id])
            redirect(action: "list")
            return
        }

        try {
            migItemInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'migItem.label', default: 'MigItem'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'migItem.label', default: 'MigItem'), id])
            redirect(action: "show", id: id)
        }
    }
}

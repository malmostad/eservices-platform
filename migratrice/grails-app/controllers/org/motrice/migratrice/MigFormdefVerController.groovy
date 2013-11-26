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

    def create() {
        [migFormdefVerInst: new MigFormdefVer(params)]
    }

    def save() {
        def migFormdefVerInst = new MigFormdefVer(params)
        if (!migFormdefVerInst.save(flush: true)) {
            render(view: "create", model: [migFormdefVerInst: migFormdefVerInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'migFormdefVer.label', default: 'MigFormdefVer'), migFormdefVerInst.id])
        redirect(action: "show", id: migFormdefVerInst.id)
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

    def edit(Long id) {
        def migFormdefVerInst = MigFormdefVer.get(id)
        if (!migFormdefVerInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'migFormdefVer.label', default: 'MigFormdefVer'), id])
            redirect(action: "list")
            return
        }

        [migFormdefVerInst: migFormdefVerInst]
    }

    def update(Long id, Long version) {
        def migFormdefVerInst = MigFormdefVer.get(id)
        if (!migFormdefVerInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'migFormdefVer.label', default: 'MigFormdefVer'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (migFormdefVerInst.version > version) {
                migFormdefVerInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'migFormdefVer.label', default: 'MigFormdefVer')] as Object[],
                          "Another user has updated this MigFormdefVer while you were editing")
                render(view: "edit", model: [migFormdefVerInst: migFormdefVerInst])
                return
            }
        }

        migFormdefVerInst.properties = params

        if (!migFormdefVerInst.save(flush: true)) {
            render(view: "edit", model: [migFormdefVerInst: migFormdefVerInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'migFormdefVer.label', default: 'MigFormdefVer'), migFormdefVerInst.id])
        redirect(action: "show", id: migFormdefVerInst.id)
    }

    def delete(Long id) {
        def migFormdefVerInst = MigFormdefVer.get(id)
        if (!migFormdefVerInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'migFormdefVer.label', default: 'MigFormdefVer'), id])
            redirect(action: "list")
            return
        }

        try {
            migFormdefVerInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'migFormdefVer.label', default: 'MigFormdefVer'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'migFormdefVer.label', default: 'MigFormdefVer'), id])
            redirect(action: "show", id: id)
        }
    }
}

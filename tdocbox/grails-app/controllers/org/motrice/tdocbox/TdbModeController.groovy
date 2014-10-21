package org.motrice.tdocbox

import org.springframework.dao.DataIntegrityViolationException

class TdbModeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [tdbModeObjList: TdbMode.list(params), tdbModeObjTotal: TdbMode.count()]
    }

    def create() {
        [tdbModeObj: new TdbMode(params)]
    }

    def save() {
        def tdbModeObj = new TdbMode(params)
        if (!tdbModeObj.save(flush: true)) {
            render(view: "create", model: [tdbModeObj: tdbModeObj])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'tdbMode.label', default: 'TdbMode'), tdbModeObj.id])
        redirect(action: "show", id: tdbModeObj.id)
    }

    def show(Long id) {
        def tdbModeObj = TdbMode.get(id)
        if (!tdbModeObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbMode.label', default: 'TdbMode'), id])
            redirect(action: "list")
            return
        }

        [tdbModeObj: tdbModeObj]
    }

    def edit(Long id) {
        def tdbModeObj = TdbMode.get(id)
        if (!tdbModeObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbMode.label', default: 'TdbMode'), id])
            redirect(action: "list")
            return
        }

        [tdbModeObj: tdbModeObj]
    }

    def update(Long id, Long version) {
        def tdbModeObj = TdbMode.get(id)
        if (!tdbModeObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbMode.label', default: 'TdbMode'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (tdbModeObj.version > version) {
                tdbModeObj.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'tdbMode.label', default: 'TdbMode')] as Object[],
                          "Another user has updated this TdbMode while you were editing")
                render(view: "edit", model: [tdbModeObj: tdbModeObj])
                return
            }
        }

        tdbModeObj.properties = params

        if (!tdbModeObj.save(flush: true)) {
            render(view: "edit", model: [tdbModeObj: tdbModeObj])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'tdbMode.label', default: 'TdbMode'), tdbModeObj.id])
        redirect(action: "show", id: tdbModeObj.id)
    }

    def delete(Long id) {
        def tdbModeObj = TdbMode.get(id)
        if (!tdbModeObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbMode.label', default: 'TdbMode'), id])
            redirect(action: "list")
            return
        }

        try {
            tdbModeObj.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'tdbMode.label', default: 'TdbMode'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tdbMode.label', default: 'TdbMode'), id])
            redirect(action: "show", id: id)
        }
    }
}

package org.motrice.tdocbox

import org.springframework.dao.DataIntegrityViolationException

class TdbHttpVerbController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [tdbHttpVerbObjList: TdbHttpVerb.list(params), tdbHttpVerbObjTotal: TdbHttpVerb.count()]
    }

    def create() {
        [tdbHttpVerbObj: new TdbHttpVerb(params)]
    }

    def save() {
        def tdbHttpVerbObj = new TdbHttpVerb(params)
        if (!tdbHttpVerbObj.save(flush: true)) {
            render(view: "create", model: [tdbHttpVerbObj: tdbHttpVerbObj])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'tdbHttpVerb.label', default: 'TdbHttpVerb'), tdbHttpVerbObj.id])
        redirect(action: "show", id: tdbHttpVerbObj.id)
    }

    def show(Long id) {
        def tdbHttpVerbObj = TdbHttpVerb.get(id)
        if (!tdbHttpVerbObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbHttpVerb.label', default: 'TdbHttpVerb'), id])
            redirect(action: "list")
            return
        }

        [tdbHttpVerbObj: tdbHttpVerbObj]
    }

    def edit(Long id) {
        def tdbHttpVerbObj = TdbHttpVerb.get(id)
        if (!tdbHttpVerbObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbHttpVerb.label', default: 'TdbHttpVerb'), id])
            redirect(action: "list")
            return
        }

        [tdbHttpVerbObj: tdbHttpVerbObj]
    }

    def update(Long id, Long version) {
        def tdbHttpVerbObj = TdbHttpVerb.get(id)
        if (!tdbHttpVerbObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbHttpVerb.label', default: 'TdbHttpVerb'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (tdbHttpVerbObj.version > version) {
                tdbHttpVerbObj.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'tdbHttpVerb.label', default: 'TdbHttpVerb')] as Object[],
                          "Another user has updated this TdbHttpVerb while you were editing")
                render(view: "edit", model: [tdbHttpVerbObj: tdbHttpVerbObj])
                return
            }
        }

        tdbHttpVerbObj.properties = params

        if (!tdbHttpVerbObj.save(flush: true)) {
            render(view: "edit", model: [tdbHttpVerbObj: tdbHttpVerbObj])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'tdbHttpVerb.label', default: 'TdbHttpVerb'), tdbHttpVerbObj.id])
        redirect(action: "show", id: tdbHttpVerbObj.id)
    }

    def delete(Long id) {
        def tdbHttpVerbObj = TdbHttpVerb.get(id)
        if (!tdbHttpVerbObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbHttpVerb.label', default: 'TdbHttpVerb'), id])
            redirect(action: "list")
            return
        }

        try {
            tdbHttpVerbObj.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'tdbHttpVerb.label', default: 'TdbHttpVerb'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tdbHttpVerb.label', default: 'TdbHttpVerb'), id])
            redirect(action: "show", id: id)
        }
    }
}

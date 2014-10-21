package org.motrice.tdocbox

import org.springframework.dao.DataIntegrityViolationException

class TdbPropertyController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [tdbPropertyObjList: TdbProperty.list(params), tdbPropertyObjTotal: TdbProperty.count()]
    }

    def create() {
        [tdbPropertyObj: new TdbProperty(params)]
    }

    def save() {
        def tdbPropertyObj = new TdbProperty(params)
        if (!tdbPropertyObj.save(flush: true)) {
            render(view: "create", model: [tdbPropertyObj: tdbPropertyObj])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'tdbProperty.label', default: 'TdbProperty'), tdbPropertyObj.id])
        redirect(action: "show", id: tdbPropertyObj.id)
    }

    def show(Long id) {
        def tdbPropertyObj = TdbProperty.get(id)
        if (!tdbPropertyObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbProperty.label', default: 'TdbProperty'), id])
            redirect(action: "list")
            return
        }

        [tdbPropertyObj: tdbPropertyObj]
    }

    def edit(Long id) {
        def tdbPropertyObj = TdbProperty.get(id)
        if (!tdbPropertyObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbProperty.label', default: 'TdbProperty'), id])
            redirect(action: "list")
            return
        }

        [tdbPropertyObj: tdbPropertyObj]
    }

    def update(Long id, Long version) {
        def tdbPropertyObj = TdbProperty.get(id)
        if (!tdbPropertyObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbProperty.label', default: 'TdbProperty'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (tdbPropertyObj.version > version) {
                tdbPropertyObj.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'tdbProperty.label', default: 'TdbProperty')] as Object[],
                          "Another user has updated this TdbProperty while you were editing")
                render(view: "edit", model: [tdbPropertyObj: tdbPropertyObj])
                return
            }
        }

        tdbPropertyObj.properties = params

        if (!tdbPropertyObj.save(flush: true)) {
            render(view: "edit", model: [tdbPropertyObj: tdbPropertyObj])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'tdbProperty.label', default: 'TdbProperty'), tdbPropertyObj.id])
        redirect(action: "show", id: tdbPropertyObj.id)
    }

    def delete(Long id) {
        def tdbPropertyObj = TdbProperty.get(id)
        if (!tdbPropertyObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbProperty.label', default: 'TdbProperty'), id])
            redirect(action: "list")
            return
        }

        try {
            tdbPropertyObj.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'tdbProperty.label', default: 'TdbProperty'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tdbProperty.label', default: 'TdbProperty'), id])
            redirect(action: "show", id: id)
        }
    }
}

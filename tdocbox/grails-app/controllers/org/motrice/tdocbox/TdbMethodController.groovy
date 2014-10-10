package org.motrice.tdocbox

import org.springframework.dao.DataIntegrityViolationException

class TdbMethodController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [tdbMethodObjList: TdbMethod.list(params), tdbMethodObjTotal: TdbMethod.count()]
    }

    def create() {
        [tdbMethodObj: new TdbMethod(params)]
    }

    def save() {
        def tdbMethodObj = new TdbMethod(params)
        if (!tdbMethodObj.save(flush: true)) {
            render(view: "create", model: [tdbMethodObj: tdbMethodObj])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'tdbMethod.label', default: 'TdbMethod'), tdbMethodObj.id])
        redirect(action: "show", id: tdbMethodObj.id)
    }

    def show(Long id) {
        def tdbMethodObj = TdbMethod.get(id)
        if (!tdbMethodObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbMethod.label', default: 'TdbMethod'), id])
            redirect(action: "list")
            return
        }

        [tdbMethodObj: tdbMethodObj]
    }

    def edit(Long id) {
        def tdbMethodObj = TdbMethod.get(id)
        if (!tdbMethodObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbMethod.label', default: 'TdbMethod'), id])
            redirect(action: "list")
            return
        }

        [tdbMethodObj: tdbMethodObj]
    }

    def update(Long id, Long version) {
        def tdbMethodObj = TdbMethod.get(id)
        if (!tdbMethodObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbMethod.label', default: 'TdbMethod'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (tdbMethodObj.version > version) {
                tdbMethodObj.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'tdbMethod.label', default: 'TdbMethod')] as Object[],
                          "Another user has updated this TdbMethod while you were editing")
                render(view: "edit", model: [tdbMethodObj: tdbMethodObj])
                return
            }
        }

        tdbMethodObj.properties = params

        if (!tdbMethodObj.save(flush: true)) {
            render(view: "edit", model: [tdbMethodObj: tdbMethodObj])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'tdbMethod.label', default: 'TdbMethod'), tdbMethodObj.id])
        redirect(action: "show", id: tdbMethodObj.id)
    }

    def delete(Long id) {
        def tdbMethodObj = TdbMethod.get(id)
        if (!tdbMethodObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbMethod.label', default: 'TdbMethod'), id])
            redirect(action: "list")
            return
        }

        try {
            tdbMethodObj.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'tdbMethod.label', default: 'TdbMethod'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tdbMethod.label', default: 'TdbMethod'), id])
            redirect(action: "show", id: id)
        }
    }
}

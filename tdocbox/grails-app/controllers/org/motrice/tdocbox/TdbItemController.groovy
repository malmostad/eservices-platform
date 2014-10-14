package org.motrice.tdocbox

import org.springframework.dao.DataIntegrityViolationException

class TdbItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [tdbItemObjList: TdbItem.list(params), tdbItemObjTotal: TdbItem.count()]
    }

    def create() {
        [tdbItemObj: new TdbItem(params)]
    }

    def save() {
        def tdbItemObj = new TdbItem(params)
        if (!tdbItemObj.save(flush: true)) {
            render(view: "create", model: [tdbItemObj: tdbItemObj])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'tdbItem.label', default: 'TdbItem'), tdbItemObj.id])
        redirect(action: "show", id: tdbItemObj.id)
    }

    def show(Long id) {
        def tdbItemObj = TdbItem.get(id)
        if (!tdbItemObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbItem.label', default: 'TdbItem'), id])
            redirect(action: "list")
            return
        }

        [tdbItemObj: tdbItemObj]
    }

    def edit(Long id) {
        def tdbItemObj = TdbItem.get(id)
        if (!tdbItemObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbItem.label', default: 'TdbItem'), id])
            redirect(action: "list")
            return
        }

        [tdbItemObj: tdbItemObj]
    }

    def update(Long id, Long version) {
        def tdbItemObj = TdbItem.get(id)
        if (!tdbItemObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbItem.label', default: 'TdbItem'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (tdbItemObj.version > version) {
                tdbItemObj.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'tdbItem.label', default: 'TdbItem')] as Object[],
                          "Another user has updated this TdbItem while you were editing")
                render(view: "edit", model: [tdbItemObj: tdbItemObj])
                return
            }
        }

        tdbItemObj.properties = params

        if (!tdbItemObj.save(flush: true)) {
            render(view: "edit", model: [tdbItemObj: tdbItemObj])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'tdbItem.label', default: 'TdbItem'), tdbItemObj.id])
        redirect(action: "show", id: tdbItemObj.id)
    }

    def delete(Long id) {
        def tdbItemObj = TdbItem.get(id)
        if (!tdbItemObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tdbItem.label', default: 'TdbItem'), id])
            redirect(action: "list")
            return
        }

        try {
            tdbItemObj.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'tdbItem.label', default: 'TdbItem'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tdbItem.label', default: 'TdbItem'), id])
            redirect(action: "show", id: id)
        }
    }
}

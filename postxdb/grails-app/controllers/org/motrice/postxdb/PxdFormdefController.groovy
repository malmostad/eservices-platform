package org.motrice.postxdb

import org.motrice.postxdb.PxdFormdef;
import org.springframework.dao.DataIntegrityViolationException

class PxdFormdefController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [pxdFormdefObjList: PxdFormdef.list(params), pxdFormdefObjTotal: PxdFormdef.count()]
    }

    def create() {
        [pxdFormdefObj: new PxdFormdef(params)]
    }

    def save() {
        def pxdFormdefObj = new PxdFormdef(params)
        if (!pxdFormdefObj.save(flush: true)) {
            render(view: "create", model: [pxdFormdefObj: pxdFormdefObj])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), pxdFormdefObj.id])
        redirect(action: "show", id: pxdFormdefObj.id)
    }

    def show(Long id) {
        def pxdFormdefObj = PxdFormdef.get(id)
        if (!pxdFormdefObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), id])
            redirect(action: "list")
            return
        }

        [pxdFormdefObj: pxdFormdefObj]
    }

    def edit(Long id) {
        def pxdFormdefObj = PxdFormdef.get(id)
        if (!pxdFormdefObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), id])
            redirect(action: "list")
            return
        }

        [pxdFormdefObj: pxdFormdefObj]
    }

    def update(Long id, Long version) {
        def pxdFormdefObj = PxdFormdef.get(id)
        if (!pxdFormdefObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (pxdFormdefObj.version > version) {
                pxdFormdefObj.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'pxdFormdef.label', default: 'PxdFormdef')] as Object[],
                          "Another user has updated this PxdFormdef while you were editing")
                render(view: "edit", model: [pxdFormdefObj: pxdFormdefObj])
                return
            }
        }

        pxdFormdefObj.properties = params

        if (!pxdFormdefObj.save(flush: true)) {
            render(view: "edit", model: [pxdFormdefObj: pxdFormdefObj])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), pxdFormdefObj.id])
        redirect(action: "show", id: pxdFormdefObj.id)
    }

    def delete(Long id) {
        def pxdFormdefObj = PxdFormdef.get(id)
        if (!pxdFormdefObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), id])
            redirect(action: "list")
            return
        }

        try {
            pxdFormdefObj.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'pxdFormdef.label', default: 'PxdFormdef'), id])
            redirect(action: "show", id: id)
        }
    }
}

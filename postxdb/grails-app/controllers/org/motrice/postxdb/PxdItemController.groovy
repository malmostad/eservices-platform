package org.motrice.postxdb

import org.motrice.postxdb.PxdItem;
import org.springframework.dao.DataIntegrityViolationException

class PxdItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [pxdItemObjList: PxdItem.list(params), pxdItemObjTotal: PxdItem.count()]
    }

    def create() {
        [pxdItemObj: new PxdItem(params)]
    }

    def save() {
        def pxdItemObj = new PxdItem(params)
        if (!pxdItemObj.save(flush: true)) {
            render(view: "create", model: [pxdItemObj: pxdItemObj])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), pxdItemObj.id])
        redirect(action: "show", id: pxdItemObj.id)
    }

    def show(Long id) {
        def pxdItemObj = PxdItem.get(id)
        if (!pxdItemObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
            redirect(action: "list")
            return
        }

        [pxdItemObj: pxdItemObj]
    }

    def edit(Long id) {
        def pxdItemObj = PxdItem.get(id)
        if (!pxdItemObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
            redirect(action: "list")
            return
        }

        [pxdItemObj: pxdItemObj]
    }

    def update(Long id, Long version) {
        def pxdItemObj = PxdItem.get(id)
        if (!pxdItemObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (pxdItemObj.version > version) {
                pxdItemObj.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'pxdItem.label', default: 'PxdItem')] as Object[],
                          "Another user has updated this PxdItem while you were editing")
                render(view: "edit", model: [pxdItemObj: pxdItemObj])
                return
            }
        }

        pxdItemObj.properties = params

        if (!pxdItemObj.save(flush: true)) {
            render(view: "edit", model: [pxdItemObj: pxdItemObj])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), pxdItemObj.id])
        redirect(action: "show", id: pxdItemObj.id)
    }

    def delete(Long id) {
        def pxdItemObj = PxdItem.get(id)
        if (!pxdItemObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
            redirect(action: "list")
            return
        }

        try {
            pxdItemObj.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'pxdItem.label', default: 'PxdItem'), id])
            redirect(action: "show", id: id)
        }
    }
}

package org.motrice.postxdb

import org.motrice.postxdb.PxdFormdefVer;
import org.springframework.dao.DataIntegrityViolationException

class PxdFormdefVerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [pxdFormdefVerObjList: PxdFormdefVer.list(params), pxdFormdefVerObjTotal: PxdFormdefVer.count()]
    }

    def create() {
        [pxdFormdefVerObj: new PxdFormdefVer(params)]
    }

    def save() {
        def pxdFormdefVerObj = new PxdFormdefVer(params)
        if (!pxdFormdefVerObj.save(flush: true)) {
            render(view: "create", model: [pxdFormdefVerObj: pxdFormdefVerObj])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer'), pxdFormdefVerObj.id])
        redirect(action: "show", id: pxdFormdefVerObj.id)
    }

    def show(Long id) {
        def pxdFormdefVerObj = PxdFormdefVer.get(id)
        if (!pxdFormdefVerObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer'), id])
            redirect(action: "list")
            return
        }

        [pxdFormdefVerObj: pxdFormdefVerObj]
    }

    def edit(Long id) {
        def pxdFormdefVerObj = PxdFormdefVer.get(id)
        if (!pxdFormdefVerObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer'), id])
            redirect(action: "list")
            return
        }

        [pxdFormdefVerObj: pxdFormdefVerObj]
    }

    def update(Long id, Long version) {
        def pxdFormdefVerObj = PxdFormdefVer.get(id)
        if (!pxdFormdefVerObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (pxdFormdefVerObj.version > version) {
                pxdFormdefVerObj.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer')] as Object[],
                          "Another user has updated this PxdFormdefVer while you were editing")
                render(view: "edit", model: [pxdFormdefVerObj: pxdFormdefVerObj])
                return
            }
        }

        pxdFormdefVerObj.properties = params

        if (!pxdFormdefVerObj.save(flush: true)) {
            render(view: "edit", model: [pxdFormdefVerObj: pxdFormdefVerObj])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer'), pxdFormdefVerObj.id])
        redirect(action: "show", id: pxdFormdefVerObj.id)
    }

    def delete(Long id) {
        def pxdFormdefVerObj = PxdFormdefVer.get(id)
        if (!pxdFormdefVerObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer'), id])
            redirect(action: "list")
            return
        }

        try {
            pxdFormdefVerObj.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer'), id])
            redirect(action: "show", id: id)
        }
    }
}

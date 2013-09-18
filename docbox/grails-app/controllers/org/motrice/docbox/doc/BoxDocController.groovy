package org.motrice.docbox.doc

import org.springframework.dao.DataIntegrityViolationException

class BoxDocController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [boxDocObjList: BoxDoc.list(params), boxDocObjTotal: BoxDoc.count()]
    }

    def create() {
        [boxDocObj: new BoxDoc(params)]
    }

    def save() {
        def boxDocObj = new BoxDoc(params)
        if (!boxDocObj.save(flush: true)) {
            render(view: "create", model: [boxDocObj: boxDocObj])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'boxDoc.label', default: 'BoxDoc'), boxDocObj.id])
        redirect(action: "show", id: boxDocObj.id)
    }

    def show(Long id) {
        def boxDocObj = BoxDoc.get(id)
        if (!boxDocObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxDoc.label', default: 'BoxDoc'), id])
            redirect(action: "list")
            return
        }

        [boxDocObj: boxDocObj]
    }

    def edit(Long id) {
        def boxDocObj = BoxDoc.get(id)
        if (!boxDocObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxDoc.label', default: 'BoxDoc'), id])
            redirect(action: "list")
            return
        }

        [boxDocObj: boxDocObj]
    }

    def update(Long id, Long version) {
        def boxDocObj = BoxDoc.get(id)
        if (!boxDocObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxDoc.label', default: 'BoxDoc'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (boxDocObj.version > version) {
                boxDocObj.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'boxDoc.label', default: 'BoxDoc')] as Object[],
                          "Another user has updated this BoxDoc while you were editing")
                render(view: "edit", model: [boxDocObj: boxDocObj])
                return
            }
        }

        boxDocObj.properties = params

        if (!boxDocObj.save(flush: true)) {
            render(view: "edit", model: [boxDocObj: boxDocObj])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'boxDoc.label', default: 'BoxDoc'), boxDocObj.id])
        redirect(action: "show", id: boxDocObj.id)
    }

    def delete(Long id) {
        def boxDocObj = BoxDoc.get(id)
        if (!boxDocObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxDoc.label', default: 'BoxDoc'), id])
            redirect(action: "list")
            return
        }

        try {
            boxDocObj.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'boxDoc.label', default: 'BoxDoc'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'boxDoc.label', default: 'BoxDoc'), id])
            redirect(action: "show", id: id)
        }
    }
}

package org.motrice.orifice

import org.springframework.dao.DataIntegrityViolationException

class OriItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [oriItemInstList: OriItem.list(params), oriItemInstTotal: OriItem.count()]
    }

    def create() {
        [oriItemInst: new OriItem(params)]
    }

    def save() {
        def oriItemInst = new OriItem(params)
        if (!oriItemInst.save(flush: true)) {
            render(view: "create", model: [oriItemInst: oriItemInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'oriItem.label', default: 'OriItem'), oriItemInst.id])
        redirect(action: "show", id: oriItemInst.id)
    }

    def show(Long id) {
        def oriItemInst = OriItem.get(id)
        if (!oriItemInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriItem.label', default: 'OriItem'), id])
            redirect(action: "list")
            return
        }

        [oriItemInst: oriItemInst]
    }

    def edit(Long id) {
        def oriItemInst = OriItem.get(id)
        if (!oriItemInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriItem.label', default: 'OriItem'), id])
            redirect(action: "list")
            return
        }

        [oriItemInst: oriItemInst]
    }

    def update(Long id, Long version) {
        def oriItemInst = OriItem.get(id)
        if (!oriItemInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriItem.label', default: 'OriItem'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (oriItemInst.version > version) {
                oriItemInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'oriItem.label', default: 'OriItem')] as Object[],
                          "Another user has updated this OriItem while you were editing")
                render(view: "edit", model: [oriItemInst: oriItemInst])
                return
            }
        }

        oriItemInst.properties = params

        if (!oriItemInst.save(flush: true)) {
            render(view: "edit", model: [oriItemInst: oriItemInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'oriItem.label', default: 'OriItem'), oriItemInst.id])
        redirect(action: "show", id: oriItemInst.id)
    }

    def delete(Long id) {
        def oriItemInst = OriItem.get(id)
        if (!oriItemInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriItem.label', default: 'OriItem'), id])
            redirect(action: "list")
            return
        }

        try {
            oriItemInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'oriItem.label', default: 'OriItem'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'oriItem.label', default: 'OriItem'), id])
            redirect(action: "show", id: id)
        }
    }
}

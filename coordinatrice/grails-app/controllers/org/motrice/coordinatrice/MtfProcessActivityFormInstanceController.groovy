package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException

class MtfProcessActivityFormInstanceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [mtfProcessActivityFormInstanceInstList: MtfProcessActivityFormInstance.list(params), mtfProcessActivityFormInstanceInstTotal: MtfProcessActivityFormInstance.count()]
    }

    def create() {
        [mtfProcessActivityFormInstanceInst: new MtfProcessActivityFormInstance(params)]
    }

    def save() {
        def mtfProcessActivityFormInstanceInst = new MtfProcessActivityFormInstance(params)
        if (!mtfProcessActivityFormInstanceInst.save(flush: true)) {
            render(view: "create", model: [mtfProcessActivityFormInstanceInst: mtfProcessActivityFormInstanceInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance'), mtfProcessActivityFormInstanceInst.id])
        redirect(action: "show", id: mtfProcessActivityFormInstanceInst.id)
    }

    def show(Long id) {
        def mtfProcessActivityFormInstanceInst = MtfProcessActivityFormInstance.get(id)
        if (!mtfProcessActivityFormInstanceInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance'), id])
            redirect(action: "list")
            return
        }

        [mtfProcessActivityFormInstanceInst: mtfProcessActivityFormInstanceInst]
    }

    def edit(Long id) {
        def mtfProcessActivityFormInstanceInst = MtfProcessActivityFormInstance.get(id)
        if (!mtfProcessActivityFormInstanceInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance'), id])
            redirect(action: "list")
            return
        }

        [mtfProcessActivityFormInstanceInst: mtfProcessActivityFormInstanceInst]
    }

    def update(Long id, Long version) {
        def mtfProcessActivityFormInstanceInst = MtfProcessActivityFormInstance.get(id)
        if (!mtfProcessActivityFormInstanceInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (mtfProcessActivityFormInstanceInst.version > version) {
                mtfProcessActivityFormInstanceInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance')] as Object[],
                          "Another user has updated this MtfProcessActivityFormInstance while you were editing")
                render(view: "edit", model: [mtfProcessActivityFormInstanceInst: mtfProcessActivityFormInstanceInst])
                return
            }
        }

        mtfProcessActivityFormInstanceInst.properties = params

        if (!mtfProcessActivityFormInstanceInst.save(flush: true)) {
            render(view: "edit", model: [mtfProcessActivityFormInstanceInst: mtfProcessActivityFormInstanceInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance'), mtfProcessActivityFormInstanceInst.id])
        redirect(action: "show", id: mtfProcessActivityFormInstanceInst.id)
    }

    def delete(Long id) {
        def mtfProcessActivityFormInstanceInst = MtfProcessActivityFormInstance.get(id)
        if (!mtfProcessActivityFormInstanceInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance'), id])
            redirect(action: "list")
            return
        }

        try {
            mtfProcessActivityFormInstanceInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance'), id])
            redirect(action: "show", id: id)
        }
    }
}

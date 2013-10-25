package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException

class MtfActivityFormDefinitionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [mtfActivityFormDefinitionInstList: MtfActivityFormDefinition.list(params), mtfActivityFormDefinitionInstTotal: MtfActivityFormDefinition.count()]
    }

    def create() {
        [mtfActivityFormDefinitionInst: new MtfActivityFormDefinition(params)]
    }

    def save() {
        def mtfActivityFormDefinitionInst = new MtfActivityFormDefinition(params)
        if (!mtfActivityFormDefinitionInst.save(flush: true)) {
            render(view: "create", model: [mtfActivityFormDefinitionInst: mtfActivityFormDefinitionInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition'), mtfActivityFormDefinitionInst.id])
        redirect(action: "show", id: mtfActivityFormDefinitionInst.id)
    }

    def show(Long id) {
        def mtfActivityFormDefinitionInst = MtfActivityFormDefinition.get(id)
        if (!mtfActivityFormDefinitionInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition'), id])
            redirect(action: "list")
            return
        }

        [mtfActivityFormDefinitionInst: mtfActivityFormDefinitionInst]
    }

    def edit(Long id) {
        def mtfActivityFormDefinitionInst = MtfActivityFormDefinition.get(id)
        if (!mtfActivityFormDefinitionInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition'), id])
            redirect(action: "list")
            return
        }

        [mtfActivityFormDefinitionInst: mtfActivityFormDefinitionInst]
    }

    def update(Long id, Long version) {
        def mtfActivityFormDefinitionInst = MtfActivityFormDefinition.get(id)
        if (!mtfActivityFormDefinitionInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (mtfActivityFormDefinitionInst.version > version) {
                mtfActivityFormDefinitionInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition')] as Object[],
                          "Another user has updated this MtfActivityFormDefinition while you were editing")
                render(view: "edit", model: [mtfActivityFormDefinitionInst: mtfActivityFormDefinitionInst])
                return
            }
        }

        mtfActivityFormDefinitionInst.properties = params

        if (!mtfActivityFormDefinitionInst.save(flush: true)) {
            render(view: "edit", model: [mtfActivityFormDefinitionInst: mtfActivityFormDefinitionInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition'), mtfActivityFormDefinitionInst.id])
        redirect(action: "show", id: mtfActivityFormDefinitionInst.id)
    }

    def delete(Long id) {
        def mtfActivityFormDefinitionInst = MtfActivityFormDefinition.get(id)
        if (!mtfActivityFormDefinitionInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition'), id])
            redirect(action: "list")
            return
        }

        try {
            mtfActivityFormDefinitionInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition'), id])
            redirect(action: "show", id: id)
        }
    }
}

package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException

class MtfStartFormDefinitionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [mtfStartFormDefinitionInstList: MtfStartFormDefinition.list(params), mtfStartFormDefinitionInstTotal: MtfStartFormDefinition.count()]
    }

    def create() {
        [mtfStartFormDefinitionInst: new MtfStartFormDefinition(params)]
    }

    def save() {
        def mtfStartFormDefinitionInst = new MtfStartFormDefinition(params)
        if (!mtfStartFormDefinitionInst.save(flush: true)) {
            render(view: "create", model: [mtfStartFormDefinitionInst: mtfStartFormDefinitionInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), mtfStartFormDefinitionInst.id])
        redirect(action: "show", id: mtfStartFormDefinitionInst.id)
    }

    def show(Long id) {
        def mtfStartFormDefinitionInst = MtfStartFormDefinition.get(id)
        if (!mtfStartFormDefinitionInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
            redirect(action: "list")
            return
        }

        [mtfStartFormDefinitionInst: mtfStartFormDefinitionInst]
    }

    def edit(Long id) {
        def mtfStartFormDefinitionInst = MtfStartFormDefinition.get(id)
        if (!mtfStartFormDefinitionInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
            redirect(action: "list")
            return
        }

        [mtfStartFormDefinitionInst: mtfStartFormDefinitionInst]
    }

    def update(Long id, Long version) {
        def mtfStartFormDefinitionInst = MtfStartFormDefinition.get(id)
        if (!mtfStartFormDefinitionInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (mtfStartFormDefinitionInst.version > version) {
                mtfStartFormDefinitionInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition')] as Object[],
                          "Another user has updated this MtfStartFormDefinition while you were editing")
                render(view: "edit", model: [mtfStartFormDefinitionInst: mtfStartFormDefinitionInst])
                return
            }
        }

        mtfStartFormDefinitionInst.properties = params

        if (!mtfStartFormDefinitionInst.save(flush: true)) {
            render(view: "edit", model: [mtfStartFormDefinitionInst: mtfStartFormDefinitionInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), mtfStartFormDefinitionInst.id])
        redirect(action: "show", id: mtfStartFormDefinitionInst.id)
    }

    def delete(Long id) {
        def mtfStartFormDefinitionInst = MtfStartFormDefinition.get(id)
        if (!mtfStartFormDefinitionInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
            redirect(action: "list")
            return
        }

        try {
            mtfStartFormDefinitionInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition'), id])
            redirect(action: "show", id: id)
        }
    }
}

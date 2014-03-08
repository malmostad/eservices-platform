package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException

class CrdI18nFormLabelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [crdI18nFormLabelInstList: CrdI18nFormLabel.list(params), crdI18nFormLabelInstTotal: CrdI18nFormLabel.count()]
    }

    def create() {
        [crdI18nFormLabelInst: new CrdI18nFormLabel(params)]
    }

    def save() {
        def crdI18nFormLabelInst = new CrdI18nFormLabel(params)
        if (!crdI18nFormLabelInst.save(flush: true)) {
            render(view: "create", model: [crdI18nFormLabelInst: crdI18nFormLabelInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), crdI18nFormLabelInst.id])
        redirect(action: "show", id: crdI18nFormLabelInst.id)
    }

    def show(Long id) {
        def crdI18nFormLabelInst = CrdI18nFormLabel.get(id)
        if (!crdI18nFormLabelInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
            redirect(action: "list")
            return
        }

        [crdI18nFormLabelInst: crdI18nFormLabelInst]
    }

    def edit(Long id) {
        def crdI18nFormLabelInst = CrdI18nFormLabel.get(id)
        if (!crdI18nFormLabelInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
            redirect(action: "list")
            return
        }

        [crdI18nFormLabelInst: crdI18nFormLabelInst]
    }

    def update(Long id, Long version) {
        def crdI18nFormLabelInst = CrdI18nFormLabel.get(id)
        if (!crdI18nFormLabelInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (crdI18nFormLabelInst.version > version) {
                crdI18nFormLabelInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel')] as Object[],
                          "Another user has updated this CrdI18nFormLabel while you were editing")
                render(view: "edit", model: [crdI18nFormLabelInst: crdI18nFormLabelInst])
                return
            }
        }

        crdI18nFormLabelInst.properties = params

        if (!crdI18nFormLabelInst.save(flush: true)) {
            render(view: "edit", model: [crdI18nFormLabelInst: crdI18nFormLabelInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), crdI18nFormLabelInst.id])
        redirect(action: "show", id: crdI18nFormLabelInst.id)
    }

    def delete(Long id) {
        def crdI18nFormLabelInst = CrdI18nFormLabel.get(id)
        if (!crdI18nFormLabelInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
            redirect(action: "list")
            return
        }

        try {
            crdI18nFormLabelInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel'), id])
            redirect(action: "show", id: id)
        }
    }
}

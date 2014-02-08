package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException

class CrdProcCategoryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [crdProcCategoryInstList: CrdProcCategory.list(params), crdProcCategoryInstTotal: CrdProcCategory.count()]
    }

    def create() {
        [crdProcCategoryInst: new CrdProcCategory(params)]
    }

    def save() {
        def crdProcCategoryInst = new CrdProcCategory(params)
        if (!crdProcCategoryInst.save(flush: true)) {
            render(view: "create", model: [crdProcCategoryInst: crdProcCategoryInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'crdProcCategory.label', default: 'CrdProcCategory'), crdProcCategoryInst.id])
        redirect(action: "show", id: crdProcCategoryInst.id)
    }

    def show(Long id) {
        def crdProcCategoryInst = CrdProcCategory.get(id)
        if (!crdProcCategoryInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdProcCategory.label', default: 'CrdProcCategory'), id])
            redirect(action: "list")
            return
        }

        [crdProcCategoryInst: crdProcCategoryInst]
    }

    def edit(Long id) {
        def crdProcCategoryInst = CrdProcCategory.get(id)
        if (!crdProcCategoryInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdProcCategory.label', default: 'CrdProcCategory'), id])
            redirect(action: "list")
            return
        }

        [crdProcCategoryInst: crdProcCategoryInst]
    }

    def update(Long id, Long version) {
        def crdProcCategoryInst = CrdProcCategory.get(id)
        if (!crdProcCategoryInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdProcCategory.label', default: 'CrdProcCategory'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (crdProcCategoryInst.version > version) {
                crdProcCategoryInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'crdProcCategory.label', default: 'CrdProcCategory')] as Object[],
                          "Another user has updated this CrdProcCategory while you were editing")
                render(view: "edit", model: [crdProcCategoryInst: crdProcCategoryInst])
                return
            }
        }

        crdProcCategoryInst.properties = params

        if (!crdProcCategoryInst.save(flush: true)) {
            render(view: "edit", model: [crdProcCategoryInst: crdProcCategoryInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'crdProcCategory.label', default: 'CrdProcCategory'), crdProcCategoryInst.id])
        redirect(action: "show", id: crdProcCategoryInst.id)
    }

    def delete(Long id) {
        def crdProcCategoryInst = CrdProcCategory.get(id)
        if (!crdProcCategoryInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'crdProcCategory.label', default: 'CrdProcCategory'), id])
            redirect(action: "list")
            return
        }

        try {
            crdProcCategoryInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'crdProcCategory.label', default: 'CrdProcCategory'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'crdProcCategory.label', default: 'CrdProcCategory'), id])
            redirect(action: "show", id: id)
        }
    }
}

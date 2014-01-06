package org.motrice.orifice

import org.springframework.dao.DataIntegrityViolationException

class OriFormdefVerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [oriFormdefVerInstList: OriFormdefVer.list(params), oriFormdefVerInstTotal: OriFormdefVer.count()]
    }

    def create() {
        [oriFormdefVerInst: new OriFormdefVer(params)]
    }

    def save() {
        def oriFormdefVerInst = new OriFormdefVer(params)
        if (!oriFormdefVerInst.save(flush: true)) {
            render(view: "create", model: [oriFormdefVerInst: oriFormdefVerInst])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'oriFormdefVer.label', default: 'OriFormdefVer'), oriFormdefVerInst.id])
        redirect(action: "show", id: oriFormdefVerInst.id)
    }

    def show(Long id) {
        def oriFormdefVerInst = OriFormdefVer.get(id)
        if (!oriFormdefVerInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriFormdefVer.label', default: 'OriFormdefVer'), id])
            redirect(action: "list")
            return
        }

        [oriFormdefVerInst: oriFormdefVerInst]
    }

    def edit(Long id) {
        def oriFormdefVerInst = OriFormdefVer.get(id)
        if (!oriFormdefVerInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriFormdefVer.label', default: 'OriFormdefVer'), id])
            redirect(action: "list")
            return
        }

        [oriFormdefVerInst: oriFormdefVerInst]
    }

    def update(Long id, Long version) {
        def oriFormdefVerInst = OriFormdefVer.get(id)
        if (!oriFormdefVerInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriFormdefVer.label', default: 'OriFormdefVer'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (oriFormdefVerInst.version > version) {
                oriFormdefVerInst.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'oriFormdefVer.label', default: 'OriFormdefVer')] as Object[],
                          "Another user has updated this OriFormdefVer while you were editing")
                render(view: "edit", model: [oriFormdefVerInst: oriFormdefVerInst])
                return
            }
        }

        oriFormdefVerInst.properties = params

        if (!oriFormdefVerInst.save(flush: true)) {
            render(view: "edit", model: [oriFormdefVerInst: oriFormdefVerInst])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'oriFormdefVer.label', default: 'OriFormdefVer'), oriFormdefVerInst.id])
        redirect(action: "show", id: oriFormdefVerInst.id)
    }

    def delete(Long id) {
        def oriFormdefVerInst = OriFormdefVer.get(id)
        if (!oriFormdefVerInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'oriFormdefVer.label', default: 'OriFormdefVer'), id])
            redirect(action: "list")
            return
        }

        try {
            oriFormdefVerInst.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'oriFormdefVer.label', default: 'OriFormdefVer'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'oriFormdefVer.label', default: 'OriFormdefVer'), id])
            redirect(action: "show", id: id)
        }
    }
}

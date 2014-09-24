package org.motrice.signatrice.audit

import org.springframework.dao.DataIntegrityViolationException

class AuditRecordController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [auditRecordInstList: AuditRecord.list(params), auditRecordInstTotal: AuditRecord.count()]
    }

    def show(Long id) {
        def auditRecordInst = AuditRecord.get(id)
        if (!auditRecordInst) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'auditRecord.label', default: 'AuditRecord'), id])
            redirect(action: "list")
            return
        }

        [auditRecordInst: auditRecordInst]
    }

}

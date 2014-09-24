package org.motrice.cgisigntest.audit



import org.junit.*
import grails.test.mixin.*

@TestFor(AuditRecordController)
@Mock(AuditRecord)
class AuditRecordControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/auditRecord/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.auditRecordInstanceList.size() == 0
        assert model.auditRecordInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.auditRecordInstance != null
    }

    void testSave() {
        controller.save()

        assert model.auditRecordInstance != null
        assert view == '/auditRecord/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/auditRecord/show/1'
        assert controller.flash.message != null
        assert AuditRecord.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/auditRecord/list'

        populateValidParams(params)
        def auditRecord = new AuditRecord(params)

        assert auditRecord.save() != null

        params.id = auditRecord.id

        def model = controller.show()

        assert model.auditRecordInstance == auditRecord
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/auditRecord/list'

        populateValidParams(params)
        def auditRecord = new AuditRecord(params)

        assert auditRecord.save() != null

        params.id = auditRecord.id

        def model = controller.edit()

        assert model.auditRecordInstance == auditRecord
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/auditRecord/list'

        response.reset()

        populateValidParams(params)
        def auditRecord = new AuditRecord(params)

        assert auditRecord.save() != null

        // test invalid parameters in update
        params.id = auditRecord.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/auditRecord/edit"
        assert model.auditRecordInstance != null

        auditRecord.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/auditRecord/show/$auditRecord.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        auditRecord.clearErrors()

        populateValidParams(params)
        params.id = auditRecord.id
        params.version = -1
        controller.update()

        assert view == "/auditRecord/edit"
        assert model.auditRecordInstance != null
        assert model.auditRecordInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/auditRecord/list'

        response.reset()

        populateValidParams(params)
        def auditRecord = new AuditRecord(params)

        assert auditRecord.save() != null
        assert AuditRecord.count() == 1

        params.id = auditRecord.id

        controller.delete()

        assert AuditRecord.count() == 0
        assert AuditRecord.get(auditRecord.id) == null
        assert response.redirectedUrl == '/auditRecord/list'
    }
}

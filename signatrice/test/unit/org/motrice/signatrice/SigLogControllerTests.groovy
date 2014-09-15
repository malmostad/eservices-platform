package org.motrice.signatrice



import org.junit.*
import grails.test.mixin.*

@TestFor(SigLogController)
@Mock(SigLog)
class SigLogControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/sigLog/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.sigLogInstanceList.size() == 0
        assert model.sigLogInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.sigLogInstance != null
    }

    void testSave() {
        controller.save()

        assert model.sigLogInstance != null
        assert view == '/sigLog/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/sigLog/show/1'
        assert controller.flash.message != null
        assert SigLog.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/sigLog/list'

        populateValidParams(params)
        def sigLog = new SigLog(params)

        assert sigLog.save() != null

        params.id = sigLog.id

        def model = controller.show()

        assert model.sigLogInstance == sigLog
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/sigLog/list'

        populateValidParams(params)
        def sigLog = new SigLog(params)

        assert sigLog.save() != null

        params.id = sigLog.id

        def model = controller.edit()

        assert model.sigLogInstance == sigLog
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/sigLog/list'

        response.reset()

        populateValidParams(params)
        def sigLog = new SigLog(params)

        assert sigLog.save() != null

        // test invalid parameters in update
        params.id = sigLog.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/sigLog/edit"
        assert model.sigLogInstance != null

        sigLog.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/sigLog/show/$sigLog.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        sigLog.clearErrors()

        populateValidParams(params)
        params.id = sigLog.id
        params.version = -1
        controller.update()

        assert view == "/sigLog/edit"
        assert model.sigLogInstance != null
        assert model.sigLogInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/sigLog/list'

        response.reset()

        populateValidParams(params)
        def sigLog = new SigLog(params)

        assert sigLog.save() != null
        assert SigLog.count() == 1

        params.id = sigLog.id

        controller.delete()

        assert SigLog.count() == 0
        assert SigLog.get(sigLog.id) == null
        assert response.redirectedUrl == '/sigLog/list'
    }
}

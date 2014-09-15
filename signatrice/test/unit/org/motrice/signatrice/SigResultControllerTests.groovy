package org.motrice.signatrice



import org.junit.*
import grails.test.mixin.*

@TestFor(SigResultController)
@Mock(SigResult)
class SigResultControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/sigResult/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.sigResultInstanceList.size() == 0
        assert model.sigResultInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.sigResultInstance != null
    }

    void testSave() {
        controller.save()

        assert model.sigResultInstance != null
        assert view == '/sigResult/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/sigResult/show/1'
        assert controller.flash.message != null
        assert SigResult.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/sigResult/list'

        populateValidParams(params)
        def sigResult = new SigResult(params)

        assert sigResult.save() != null

        params.id = sigResult.id

        def model = controller.show()

        assert model.sigResultInstance == sigResult
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/sigResult/list'

        populateValidParams(params)
        def sigResult = new SigResult(params)

        assert sigResult.save() != null

        params.id = sigResult.id

        def model = controller.edit()

        assert model.sigResultInstance == sigResult
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/sigResult/list'

        response.reset()

        populateValidParams(params)
        def sigResult = new SigResult(params)

        assert sigResult.save() != null

        // test invalid parameters in update
        params.id = sigResult.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/sigResult/edit"
        assert model.sigResultInstance != null

        sigResult.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/sigResult/show/$sigResult.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        sigResult.clearErrors()

        populateValidParams(params)
        params.id = sigResult.id
        params.version = -1
        controller.update()

        assert view == "/sigResult/edit"
        assert model.sigResultInstance != null
        assert model.sigResultInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/sigResult/list'

        response.reset()

        populateValidParams(params)
        def sigResult = new SigResult(params)

        assert sigResult.save() != null
        assert SigResult.count() == 1

        params.id = sigResult.id

        controller.delete()

        assert SigResult.count() == 0
        assert SigResult.get(sigResult.id) == null
        assert response.redirectedUrl == '/sigResult/list'
    }
}

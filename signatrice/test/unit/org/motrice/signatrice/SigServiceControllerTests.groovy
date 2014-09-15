package org.motrice.signatrice



import org.junit.*
import grails.test.mixin.*

@TestFor(SigServiceController)
@Mock(SigService)
class SigServiceControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/sigService/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.sigServiceInstanceList.size() == 0
        assert model.sigServiceInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.sigServiceInstance != null
    }

    void testSave() {
        controller.save()

        assert model.sigServiceInstance != null
        assert view == '/sigService/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/sigService/show/1'
        assert controller.flash.message != null
        assert SigService.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/sigService/list'

        populateValidParams(params)
        def sigService = new SigService(params)

        assert sigService.save() != null

        params.id = sigService.id

        def model = controller.show()

        assert model.sigServiceInstance == sigService
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/sigService/list'

        populateValidParams(params)
        def sigService = new SigService(params)

        assert sigService.save() != null

        params.id = sigService.id

        def model = controller.edit()

        assert model.sigServiceInstance == sigService
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/sigService/list'

        response.reset()

        populateValidParams(params)
        def sigService = new SigService(params)

        assert sigService.save() != null

        // test invalid parameters in update
        params.id = sigService.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/sigService/edit"
        assert model.sigServiceInstance != null

        sigService.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/sigService/show/$sigService.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        sigService.clearErrors()

        populateValidParams(params)
        params.id = sigService.id
        params.version = -1
        controller.update()

        assert view == "/sigService/edit"
        assert model.sigServiceInstance != null
        assert model.sigServiceInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/sigService/list'

        response.reset()

        populateValidParams(params)
        def sigService = new SigService(params)

        assert sigService.save() != null
        assert SigService.count() == 1

        params.id = sigService.id

        controller.delete()

        assert SigService.count() == 0
        assert SigService.get(sigService.id) == null
        assert response.redirectedUrl == '/sigService/list'
    }
}

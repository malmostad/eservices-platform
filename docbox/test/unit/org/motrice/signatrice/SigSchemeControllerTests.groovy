package org.motrice.signatrice



import org.junit.*
import grails.test.mixin.*

@TestFor(SigSchemeController)
@Mock(SigScheme)
class SigSchemeControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/sigScheme/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.sigSchemeInstanceList.size() == 0
        assert model.sigSchemeInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.sigSchemeInstance != null
    }

    void testSave() {
        controller.save()

        assert model.sigSchemeInstance != null
        assert view == '/sigScheme/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/sigScheme/show/1'
        assert controller.flash.message != null
        assert SigScheme.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/sigScheme/list'

        populateValidParams(params)
        def sigScheme = new SigScheme(params)

        assert sigScheme.save() != null

        params.id = sigScheme.id

        def model = controller.show()

        assert model.sigSchemeInstance == sigScheme
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/sigScheme/list'

        populateValidParams(params)
        def sigScheme = new SigScheme(params)

        assert sigScheme.save() != null

        params.id = sigScheme.id

        def model = controller.edit()

        assert model.sigSchemeInstance == sigScheme
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/sigScheme/list'

        response.reset()

        populateValidParams(params)
        def sigScheme = new SigScheme(params)

        assert sigScheme.save() != null

        // test invalid parameters in update
        params.id = sigScheme.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/sigScheme/edit"
        assert model.sigSchemeInstance != null

        sigScheme.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/sigScheme/show/$sigScheme.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        sigScheme.clearErrors()

        populateValidParams(params)
        params.id = sigScheme.id
        params.version = -1
        controller.update()

        assert view == "/sigScheme/edit"
        assert model.sigSchemeInstance != null
        assert model.sigSchemeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/sigScheme/list'

        response.reset()

        populateValidParams(params)
        def sigScheme = new SigScheme(params)

        assert sigScheme.save() != null
        assert SigScheme.count() == 1

        params.id = sigScheme.id

        controller.delete()

        assert SigScheme.count() == 0
        assert SigScheme.get(sigScheme.id) == null
        assert response.redirectedUrl == '/sigScheme/list'
    }
}

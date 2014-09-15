package org.motrice.signatrice



import org.junit.*
import grails.test.mixin.*

@TestFor(SigTestcaseController)
@Mock(SigTestcase)
class SigTestcaseControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/sigTestcase/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.sigTestcaseInstanceList.size() == 0
        assert model.sigTestcaseInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.sigTestcaseInstance != null
    }

    void testSave() {
        controller.save()

        assert model.sigTestcaseInstance != null
        assert view == '/sigTestcase/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/sigTestcase/show/1'
        assert controller.flash.message != null
        assert SigTestcase.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/sigTestcase/list'

        populateValidParams(params)
        def sigTestcase = new SigTestcase(params)

        assert sigTestcase.save() != null

        params.id = sigTestcase.id

        def model = controller.show()

        assert model.sigTestcaseInstance == sigTestcase
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/sigTestcase/list'

        populateValidParams(params)
        def sigTestcase = new SigTestcase(params)

        assert sigTestcase.save() != null

        params.id = sigTestcase.id

        def model = controller.edit()

        assert model.sigTestcaseInstance == sigTestcase
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/sigTestcase/list'

        response.reset()

        populateValidParams(params)
        def sigTestcase = new SigTestcase(params)

        assert sigTestcase.save() != null

        // test invalid parameters in update
        params.id = sigTestcase.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/sigTestcase/edit"
        assert model.sigTestcaseInstance != null

        sigTestcase.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/sigTestcase/show/$sigTestcase.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        sigTestcase.clearErrors()

        populateValidParams(params)
        params.id = sigTestcase.id
        params.version = -1
        controller.update()

        assert view == "/sigTestcase/edit"
        assert model.sigTestcaseInstance != null
        assert model.sigTestcaseInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/sigTestcase/list'

        response.reset()

        populateValidParams(params)
        def sigTestcase = new SigTestcase(params)

        assert sigTestcase.save() != null
        assert SigTestcase.count() == 1

        params.id = sigTestcase.id

        controller.delete()

        assert SigTestcase.count() == 0
        assert SigTestcase.get(sigTestcase.id) == null
        assert response.redirectedUrl == '/sigTestcase/list'
    }
}

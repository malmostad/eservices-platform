package org.motrice.docbox.doc



import org.junit.*
import grails.test.mixin.*

@TestFor(BoxDocStepController)
@Mock(BoxDocStep)
class BoxDocStepControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/boxDocStep/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.boxDocStepInstanceList.size() == 0
        assert model.boxDocStepInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.boxDocStepInstance != null
    }

    void testSave() {
        controller.save()

        assert model.boxDocStepInstance != null
        assert view == '/boxDocStep/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/boxDocStep/show/1'
        assert controller.flash.message != null
        assert BoxDocStep.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/boxDocStep/list'

        populateValidParams(params)
        def boxDocStep = new BoxDocStep(params)

        assert boxDocStep.save() != null

        params.id = boxDocStep.id

        def model = controller.show()

        assert model.boxDocStepInstance == boxDocStep
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/boxDocStep/list'

        populateValidParams(params)
        def boxDocStep = new BoxDocStep(params)

        assert boxDocStep.save() != null

        params.id = boxDocStep.id

        def model = controller.edit()

        assert model.boxDocStepInstance == boxDocStep
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/boxDocStep/list'

        response.reset()

        populateValidParams(params)
        def boxDocStep = new BoxDocStep(params)

        assert boxDocStep.save() != null

        // test invalid parameters in update
        params.id = boxDocStep.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/boxDocStep/edit"
        assert model.boxDocStepInstance != null

        boxDocStep.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/boxDocStep/show/$boxDocStep.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        boxDocStep.clearErrors()

        populateValidParams(params)
        params.id = boxDocStep.id
        params.version = -1
        controller.update()

        assert view == "/boxDocStep/edit"
        assert model.boxDocStepInstance != null
        assert model.boxDocStepInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/boxDocStep/list'

        response.reset()

        populateValidParams(params)
        def boxDocStep = new BoxDocStep(params)

        assert boxDocStep.save() != null
        assert BoxDocStep.count() == 1

        params.id = boxDocStep.id

        controller.delete()

        assert BoxDocStep.count() == 0
        assert BoxDocStep.get(boxDocStep.id) == null
        assert response.redirectedUrl == '/boxDocStep/list'
    }
}

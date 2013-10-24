package org.motrice.coordinatrice



import org.junit.*
import grails.test.mixin.*

@TestFor(MtfActivityFormDefinitionController)
@Mock(MtfActivityFormDefinition)
class MtfActivityFormDefinitionControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/mtfActivityFormDefinition/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.mtfActivityFormDefinitionInstanceList.size() == 0
        assert model.mtfActivityFormDefinitionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.mtfActivityFormDefinitionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.mtfActivityFormDefinitionInstance != null
        assert view == '/mtfActivityFormDefinition/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/mtfActivityFormDefinition/show/1'
        assert controller.flash.message != null
        assert MtfActivityFormDefinition.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/mtfActivityFormDefinition/list'

        populateValidParams(params)
        def mtfActivityFormDefinition = new MtfActivityFormDefinition(params)

        assert mtfActivityFormDefinition.save() != null

        params.id = mtfActivityFormDefinition.id

        def model = controller.show()

        assert model.mtfActivityFormDefinitionInstance == mtfActivityFormDefinition
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/mtfActivityFormDefinition/list'

        populateValidParams(params)
        def mtfActivityFormDefinition = new MtfActivityFormDefinition(params)

        assert mtfActivityFormDefinition.save() != null

        params.id = mtfActivityFormDefinition.id

        def model = controller.edit()

        assert model.mtfActivityFormDefinitionInstance == mtfActivityFormDefinition
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/mtfActivityFormDefinition/list'

        response.reset()

        populateValidParams(params)
        def mtfActivityFormDefinition = new MtfActivityFormDefinition(params)

        assert mtfActivityFormDefinition.save() != null

        // test invalid parameters in update
        params.id = mtfActivityFormDefinition.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/mtfActivityFormDefinition/edit"
        assert model.mtfActivityFormDefinitionInstance != null

        mtfActivityFormDefinition.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/mtfActivityFormDefinition/show/$mtfActivityFormDefinition.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        mtfActivityFormDefinition.clearErrors()

        populateValidParams(params)
        params.id = mtfActivityFormDefinition.id
        params.version = -1
        controller.update()

        assert view == "/mtfActivityFormDefinition/edit"
        assert model.mtfActivityFormDefinitionInstance != null
        assert model.mtfActivityFormDefinitionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/mtfActivityFormDefinition/list'

        response.reset()

        populateValidParams(params)
        def mtfActivityFormDefinition = new MtfActivityFormDefinition(params)

        assert mtfActivityFormDefinition.save() != null
        assert MtfActivityFormDefinition.count() == 1

        params.id = mtfActivityFormDefinition.id

        controller.delete()

        assert MtfActivityFormDefinition.count() == 0
        assert MtfActivityFormDefinition.get(mtfActivityFormDefinition.id) == null
        assert response.redirectedUrl == '/mtfActivityFormDefinition/list'
    }
}

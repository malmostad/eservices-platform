package org.motrice.coordinatrice



import org.junit.*
import grails.test.mixin.*

@TestFor(MtfStartFormDefinitionController)
@Mock(MtfStartFormDefinition)
class MtfStartFormDefinitionControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/mtfStartFormDefinition/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.mtfStartFormDefinitionInstanceList.size() == 0
        assert model.mtfStartFormDefinitionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.mtfStartFormDefinitionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.mtfStartFormDefinitionInstance != null
        assert view == '/mtfStartFormDefinition/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/mtfStartFormDefinition/show/1'
        assert controller.flash.message != null
        assert MtfStartFormDefinition.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/mtfStartFormDefinition/list'

        populateValidParams(params)
        def mtfStartFormDefinition = new MtfStartFormDefinition(params)

        assert mtfStartFormDefinition.save() != null

        params.id = mtfStartFormDefinition.id

        def model = controller.show()

        assert model.mtfStartFormDefinitionInstance == mtfStartFormDefinition
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/mtfStartFormDefinition/list'

        populateValidParams(params)
        def mtfStartFormDefinition = new MtfStartFormDefinition(params)

        assert mtfStartFormDefinition.save() != null

        params.id = mtfStartFormDefinition.id

        def model = controller.edit()

        assert model.mtfStartFormDefinitionInstance == mtfStartFormDefinition
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/mtfStartFormDefinition/list'

        response.reset()

        populateValidParams(params)
        def mtfStartFormDefinition = new MtfStartFormDefinition(params)

        assert mtfStartFormDefinition.save() != null

        // test invalid parameters in update
        params.id = mtfStartFormDefinition.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/mtfStartFormDefinition/edit"
        assert model.mtfStartFormDefinitionInstance != null

        mtfStartFormDefinition.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/mtfStartFormDefinition/show/$mtfStartFormDefinition.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        mtfStartFormDefinition.clearErrors()

        populateValidParams(params)
        params.id = mtfStartFormDefinition.id
        params.version = -1
        controller.update()

        assert view == "/mtfStartFormDefinition/edit"
        assert model.mtfStartFormDefinitionInstance != null
        assert model.mtfStartFormDefinitionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/mtfStartFormDefinition/list'

        response.reset()

        populateValidParams(params)
        def mtfStartFormDefinition = new MtfStartFormDefinition(params)

        assert mtfStartFormDefinition.save() != null
        assert MtfStartFormDefinition.count() == 1

        params.id = mtfStartFormDefinition.id

        controller.delete()

        assert MtfStartFormDefinition.count() == 0
        assert MtfStartFormDefinition.get(mtfStartFormDefinition.id) == null
        assert response.redirectedUrl == '/mtfStartFormDefinition/list'
    }
}

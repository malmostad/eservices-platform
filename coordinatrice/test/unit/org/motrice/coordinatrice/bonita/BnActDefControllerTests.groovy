package org.motrice.coordinatrice.bonita



import org.junit.*
import grails.test.mixin.*

@TestFor(BnActDefController)
@Mock(BnActDef)
class BnActDefControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/bnActDef/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.bnActDefInstanceList.size() == 0
        assert model.bnActDefInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.bnActDefInstance != null
    }

    void testSave() {
        controller.save()

        assert model.bnActDefInstance != null
        assert view == '/bnActDef/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/bnActDef/show/1'
        assert controller.flash.message != null
        assert BnActDef.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/bnActDef/list'

        populateValidParams(params)
        def bnActDef = new BnActDef(params)

        assert bnActDef.save() != null

        params.id = bnActDef.id

        def model = controller.show()

        assert model.bnActDefInstance == bnActDef
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/bnActDef/list'

        populateValidParams(params)
        def bnActDef = new BnActDef(params)

        assert bnActDef.save() != null

        params.id = bnActDef.id

        def model = controller.edit()

        assert model.bnActDefInstance == bnActDef
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/bnActDef/list'

        response.reset()

        populateValidParams(params)
        def bnActDef = new BnActDef(params)

        assert bnActDef.save() != null

        // test invalid parameters in update
        params.id = bnActDef.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/bnActDef/edit"
        assert model.bnActDefInstance != null

        bnActDef.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/bnActDef/show/$bnActDef.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        bnActDef.clearErrors()

        populateValidParams(params)
        params.id = bnActDef.id
        params.version = -1
        controller.update()

        assert view == "/bnActDef/edit"
        assert model.bnActDefInstance != null
        assert model.bnActDefInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/bnActDef/list'

        response.reset()

        populateValidParams(params)
        def bnActDef = new BnActDef(params)

        assert bnActDef.save() != null
        assert BnActDef.count() == 1

        params.id = bnActDef.id

        controller.delete()

        assert BnActDef.count() == 0
        assert BnActDef.get(bnActDef.id) == null
        assert response.redirectedUrl == '/bnActDef/list'
    }
}

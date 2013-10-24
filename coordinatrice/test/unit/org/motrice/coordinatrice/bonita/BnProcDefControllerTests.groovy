package org.motrice.coordinatrice.bonita



import org.junit.*
import grails.test.mixin.*

@TestFor(BnProcDefController)
@Mock(BnProcDef)
class BnProcDefControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/bnProcDef/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.bnProcDefInstanceList.size() == 0
        assert model.bnProcDefInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.bnProcDefInstance != null
    }

    void testSave() {
        controller.save()

        assert model.bnProcDefInstance != null
        assert view == '/bnProcDef/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/bnProcDef/show/1'
        assert controller.flash.message != null
        assert BnProcDef.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/bnProcDef/list'

        populateValidParams(params)
        def bnProcDef = new BnProcDef(params)

        assert bnProcDef.save() != null

        params.id = bnProcDef.id

        def model = controller.show()

        assert model.bnProcDefInstance == bnProcDef
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/bnProcDef/list'

        populateValidParams(params)
        def bnProcDef = new BnProcDef(params)

        assert bnProcDef.save() != null

        params.id = bnProcDef.id

        def model = controller.edit()

        assert model.bnProcDefInstance == bnProcDef
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/bnProcDef/list'

        response.reset()

        populateValidParams(params)
        def bnProcDef = new BnProcDef(params)

        assert bnProcDef.save() != null

        // test invalid parameters in update
        params.id = bnProcDef.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/bnProcDef/edit"
        assert model.bnProcDefInstance != null

        bnProcDef.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/bnProcDef/show/$bnProcDef.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        bnProcDef.clearErrors()

        populateValidParams(params)
        params.id = bnProcDef.id
        params.version = -1
        controller.update()

        assert view == "/bnProcDef/edit"
        assert model.bnProcDefInstance != null
        assert model.bnProcDefInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/bnProcDef/list'

        response.reset()

        populateValidParams(params)
        def bnProcDef = new BnProcDef(params)

        assert bnProcDef.save() != null
        assert BnProcDef.count() == 1

        params.id = bnProcDef.id

        controller.delete()

        assert BnProcDef.count() == 0
        assert BnProcDef.get(bnProcDef.id) == null
        assert response.redirectedUrl == '/bnProcDef/list'
    }
}

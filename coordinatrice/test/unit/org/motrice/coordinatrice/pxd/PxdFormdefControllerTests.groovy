package org.motrice.coordinatrice.pxd



import org.junit.*
import grails.test.mixin.*

@TestFor(PxdFormdefController)
@Mock(PxdFormdef)
class PxdFormdefControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/pxdFormdef/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.pxdFormdefInstanceList.size() == 0
        assert model.pxdFormdefInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.pxdFormdefInstance != null
    }

    void testSave() {
        controller.save()

        assert model.pxdFormdefInstance != null
        assert view == '/pxdFormdef/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/pxdFormdef/show/1'
        assert controller.flash.message != null
        assert PxdFormdef.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/pxdFormdef/list'

        populateValidParams(params)
        def pxdFormdef = new PxdFormdef(params)

        assert pxdFormdef.save() != null

        params.id = pxdFormdef.id

        def model = controller.show()

        assert model.pxdFormdefInstance == pxdFormdef
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/pxdFormdef/list'

        populateValidParams(params)
        def pxdFormdef = new PxdFormdef(params)

        assert pxdFormdef.save() != null

        params.id = pxdFormdef.id

        def model = controller.edit()

        assert model.pxdFormdefInstance == pxdFormdef
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/pxdFormdef/list'

        response.reset()

        populateValidParams(params)
        def pxdFormdef = new PxdFormdef(params)

        assert pxdFormdef.save() != null

        // test invalid parameters in update
        params.id = pxdFormdef.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/pxdFormdef/edit"
        assert model.pxdFormdefInstance != null

        pxdFormdef.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/pxdFormdef/show/$pxdFormdef.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        pxdFormdef.clearErrors()

        populateValidParams(params)
        params.id = pxdFormdef.id
        params.version = -1
        controller.update()

        assert view == "/pxdFormdef/edit"
        assert model.pxdFormdefInstance != null
        assert model.pxdFormdefInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/pxdFormdef/list'

        response.reset()

        populateValidParams(params)
        def pxdFormdef = new PxdFormdef(params)

        assert pxdFormdef.save() != null
        assert PxdFormdef.count() == 1

        params.id = pxdFormdef.id

        controller.delete()

        assert PxdFormdef.count() == 0
        assert PxdFormdef.get(pxdFormdef.id) == null
        assert response.redirectedUrl == '/pxdFormdef/list'
    }
}

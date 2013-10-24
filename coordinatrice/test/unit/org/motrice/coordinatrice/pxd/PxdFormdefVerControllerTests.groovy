package org.motrice.coordinatrice.pxd



import org.junit.*
import grails.test.mixin.*

@TestFor(PxdFormdefVerController)
@Mock(PxdFormdefVer)
class PxdFormdefVerControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/pxdFormdefVer/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.pxdFormdefVerInstanceList.size() == 0
        assert model.pxdFormdefVerInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.pxdFormdefVerInstance != null
    }

    void testSave() {
        controller.save()

        assert model.pxdFormdefVerInstance != null
        assert view == '/pxdFormdefVer/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/pxdFormdefVer/show/1'
        assert controller.flash.message != null
        assert PxdFormdefVer.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/pxdFormdefVer/list'

        populateValidParams(params)
        def pxdFormdefVer = new PxdFormdefVer(params)

        assert pxdFormdefVer.save() != null

        params.id = pxdFormdefVer.id

        def model = controller.show()

        assert model.pxdFormdefVerInstance == pxdFormdefVer
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/pxdFormdefVer/list'

        populateValidParams(params)
        def pxdFormdefVer = new PxdFormdefVer(params)

        assert pxdFormdefVer.save() != null

        params.id = pxdFormdefVer.id

        def model = controller.edit()

        assert model.pxdFormdefVerInstance == pxdFormdefVer
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/pxdFormdefVer/list'

        response.reset()

        populateValidParams(params)
        def pxdFormdefVer = new PxdFormdefVer(params)

        assert pxdFormdefVer.save() != null

        // test invalid parameters in update
        params.id = pxdFormdefVer.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/pxdFormdefVer/edit"
        assert model.pxdFormdefVerInstance != null

        pxdFormdefVer.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/pxdFormdefVer/show/$pxdFormdefVer.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        pxdFormdefVer.clearErrors()

        populateValidParams(params)
        params.id = pxdFormdefVer.id
        params.version = -1
        controller.update()

        assert view == "/pxdFormdefVer/edit"
        assert model.pxdFormdefVerInstance != null
        assert model.pxdFormdefVerInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/pxdFormdefVer/list'

        response.reset()

        populateValidParams(params)
        def pxdFormdefVer = new PxdFormdefVer(params)

        assert pxdFormdefVer.save() != null
        assert PxdFormdefVer.count() == 1

        params.id = pxdFormdefVer.id

        controller.delete()

        assert PxdFormdefVer.count() == 0
        assert PxdFormdefVer.get(pxdFormdefVer.id) == null
        assert response.redirectedUrl == '/pxdFormdefVer/list'
    }
}

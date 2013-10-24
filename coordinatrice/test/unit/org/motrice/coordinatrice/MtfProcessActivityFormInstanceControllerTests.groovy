package org.motrice.coordinatrice



import org.junit.*
import grails.test.mixin.*

@TestFor(MtfProcessActivityFormInstanceController)
@Mock(MtfProcessActivityFormInstance)
class MtfProcessActivityFormInstanceControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/mtfProcessActivityFormInstance/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.mtfProcessActivityFormInstanceInstanceList.size() == 0
        assert model.mtfProcessActivityFormInstanceInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.mtfProcessActivityFormInstanceInstance != null
    }

    void testSave() {
        controller.save()

        assert model.mtfProcessActivityFormInstanceInstance != null
        assert view == '/mtfProcessActivityFormInstance/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/mtfProcessActivityFormInstance/show/1'
        assert controller.flash.message != null
        assert MtfProcessActivityFormInstance.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/mtfProcessActivityFormInstance/list'

        populateValidParams(params)
        def mtfProcessActivityFormInstance = new MtfProcessActivityFormInstance(params)

        assert mtfProcessActivityFormInstance.save() != null

        params.id = mtfProcessActivityFormInstance.id

        def model = controller.show()

        assert model.mtfProcessActivityFormInstanceInstance == mtfProcessActivityFormInstance
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/mtfProcessActivityFormInstance/list'

        populateValidParams(params)
        def mtfProcessActivityFormInstance = new MtfProcessActivityFormInstance(params)

        assert mtfProcessActivityFormInstance.save() != null

        params.id = mtfProcessActivityFormInstance.id

        def model = controller.edit()

        assert model.mtfProcessActivityFormInstanceInstance == mtfProcessActivityFormInstance
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/mtfProcessActivityFormInstance/list'

        response.reset()

        populateValidParams(params)
        def mtfProcessActivityFormInstance = new MtfProcessActivityFormInstance(params)

        assert mtfProcessActivityFormInstance.save() != null

        // test invalid parameters in update
        params.id = mtfProcessActivityFormInstance.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/mtfProcessActivityFormInstance/edit"
        assert model.mtfProcessActivityFormInstanceInstance != null

        mtfProcessActivityFormInstance.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/mtfProcessActivityFormInstance/show/$mtfProcessActivityFormInstance.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        mtfProcessActivityFormInstance.clearErrors()

        populateValidParams(params)
        params.id = mtfProcessActivityFormInstance.id
        params.version = -1
        controller.update()

        assert view == "/mtfProcessActivityFormInstance/edit"
        assert model.mtfProcessActivityFormInstanceInstance != null
        assert model.mtfProcessActivityFormInstanceInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/mtfProcessActivityFormInstance/list'

        response.reset()

        populateValidParams(params)
        def mtfProcessActivityFormInstance = new MtfProcessActivityFormInstance(params)

        assert mtfProcessActivityFormInstance.save() != null
        assert MtfProcessActivityFormInstance.count() == 1

        params.id = mtfProcessActivityFormInstance.id

        controller.delete()

        assert MtfProcessActivityFormInstance.count() == 0
        assert MtfProcessActivityFormInstance.get(mtfProcessActivityFormInstance.id) == null
        assert response.redirectedUrl == '/mtfProcessActivityFormInstance/list'
    }
}

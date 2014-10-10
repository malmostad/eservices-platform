package org.motrice.tdocbox



import org.junit.*
import grails.test.mixin.*

@TestFor(TdbMethodController)
@Mock(TdbMethod)
class TdbMethodControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/tdbMethod/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.tdbMethodInstanceList.size() == 0
        assert model.tdbMethodInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.tdbMethodInstance != null
    }

    void testSave() {
        controller.save()

        assert model.tdbMethodInstance != null
        assert view == '/tdbMethod/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/tdbMethod/show/1'
        assert controller.flash.message != null
        assert TdbMethod.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbMethod/list'

        populateValidParams(params)
        def tdbMethod = new TdbMethod(params)

        assert tdbMethod.save() != null

        params.id = tdbMethod.id

        def model = controller.show()

        assert model.tdbMethodInstance == tdbMethod
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbMethod/list'

        populateValidParams(params)
        def tdbMethod = new TdbMethod(params)

        assert tdbMethod.save() != null

        params.id = tdbMethod.id

        def model = controller.edit()

        assert model.tdbMethodInstance == tdbMethod
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbMethod/list'

        response.reset()

        populateValidParams(params)
        def tdbMethod = new TdbMethod(params)

        assert tdbMethod.save() != null

        // test invalid parameters in update
        params.id = tdbMethod.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/tdbMethod/edit"
        assert model.tdbMethodInstance != null

        tdbMethod.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/tdbMethod/show/$tdbMethod.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        tdbMethod.clearErrors()

        populateValidParams(params)
        params.id = tdbMethod.id
        params.version = -1
        controller.update()

        assert view == "/tdbMethod/edit"
        assert model.tdbMethodInstance != null
        assert model.tdbMethodInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/tdbMethod/list'

        response.reset()

        populateValidParams(params)
        def tdbMethod = new TdbMethod(params)

        assert tdbMethod.save() != null
        assert TdbMethod.count() == 1

        params.id = tdbMethod.id

        controller.delete()

        assert TdbMethod.count() == 0
        assert TdbMethod.get(tdbMethod.id) == null
        assert response.redirectedUrl == '/tdbMethod/list'
    }
}

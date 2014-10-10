package org.motrice.tdocbox



import org.junit.*
import grails.test.mixin.*

@TestFor(TdbPropertyController)
@Mock(TdbProperty)
class TdbPropertyControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/tdbProperty/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.tdbPropertyInstanceList.size() == 0
        assert model.tdbPropertyInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.tdbPropertyInstance != null
    }

    void testSave() {
        controller.save()

        assert model.tdbPropertyInstance != null
        assert view == '/tdbProperty/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/tdbProperty/show/1'
        assert controller.flash.message != null
        assert TdbProperty.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbProperty/list'

        populateValidParams(params)
        def tdbProperty = new TdbProperty(params)

        assert tdbProperty.save() != null

        params.id = tdbProperty.id

        def model = controller.show()

        assert model.tdbPropertyInstance == tdbProperty
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbProperty/list'

        populateValidParams(params)
        def tdbProperty = new TdbProperty(params)

        assert tdbProperty.save() != null

        params.id = tdbProperty.id

        def model = controller.edit()

        assert model.tdbPropertyInstance == tdbProperty
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbProperty/list'

        response.reset()

        populateValidParams(params)
        def tdbProperty = new TdbProperty(params)

        assert tdbProperty.save() != null

        // test invalid parameters in update
        params.id = tdbProperty.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/tdbProperty/edit"
        assert model.tdbPropertyInstance != null

        tdbProperty.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/tdbProperty/show/$tdbProperty.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        tdbProperty.clearErrors()

        populateValidParams(params)
        params.id = tdbProperty.id
        params.version = -1
        controller.update()

        assert view == "/tdbProperty/edit"
        assert model.tdbPropertyInstance != null
        assert model.tdbPropertyInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/tdbProperty/list'

        response.reset()

        populateValidParams(params)
        def tdbProperty = new TdbProperty(params)

        assert tdbProperty.save() != null
        assert TdbProperty.count() == 1

        params.id = tdbProperty.id

        controller.delete()

        assert TdbProperty.count() == 0
        assert TdbProperty.get(tdbProperty.id) == null
        assert response.redirectedUrl == '/tdbProperty/list'
    }
}

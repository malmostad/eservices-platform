package org.motrice.tdocbox



import org.junit.*
import grails.test.mixin.*

@TestFor(TdbDrillController)
@Mock(TdbDrill)
class TdbDrillControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/tdbDrill/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.tdbDrillInstanceList.size() == 0
        assert model.tdbDrillInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.tdbDrillInstance != null
    }

    void testSave() {
        controller.save()

        assert model.tdbDrillInstance != null
        assert view == '/tdbDrill/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/tdbDrill/show/1'
        assert controller.flash.message != null
        assert TdbDrill.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbDrill/list'

        populateValidParams(params)
        def tdbDrill = new TdbDrill(params)

        assert tdbDrill.save() != null

        params.id = tdbDrill.id

        def model = controller.show()

        assert model.tdbDrillInstance == tdbDrill
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbDrill/list'

        populateValidParams(params)
        def tdbDrill = new TdbDrill(params)

        assert tdbDrill.save() != null

        params.id = tdbDrill.id

        def model = controller.edit()

        assert model.tdbDrillInstance == tdbDrill
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbDrill/list'

        response.reset()

        populateValidParams(params)
        def tdbDrill = new TdbDrill(params)

        assert tdbDrill.save() != null

        // test invalid parameters in update
        params.id = tdbDrill.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/tdbDrill/edit"
        assert model.tdbDrillInstance != null

        tdbDrill.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/tdbDrill/show/$tdbDrill.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        tdbDrill.clearErrors()

        populateValidParams(params)
        params.id = tdbDrill.id
        params.version = -1
        controller.update()

        assert view == "/tdbDrill/edit"
        assert model.tdbDrillInstance != null
        assert model.tdbDrillInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/tdbDrill/list'

        response.reset()

        populateValidParams(params)
        def tdbDrill = new TdbDrill(params)

        assert tdbDrill.save() != null
        assert TdbDrill.count() == 1

        params.id = tdbDrill.id

        controller.delete()

        assert TdbDrill.count() == 0
        assert TdbDrill.get(tdbDrill.id) == null
        assert response.redirectedUrl == '/tdbDrill/list'
    }
}

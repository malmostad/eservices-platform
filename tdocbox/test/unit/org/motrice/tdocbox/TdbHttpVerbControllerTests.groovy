package org.motrice.tdocbox



import org.junit.*
import grails.test.mixin.*

@TestFor(TdbHttpVerbController)
@Mock(TdbHttpVerb)
class TdbHttpVerbControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/tdbHttpVerb/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.tdbHttpVerbInstanceList.size() == 0
        assert model.tdbHttpVerbInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.tdbHttpVerbInstance != null
    }

    void testSave() {
        controller.save()

        assert model.tdbHttpVerbInstance != null
        assert view == '/tdbHttpVerb/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/tdbHttpVerb/show/1'
        assert controller.flash.message != null
        assert TdbHttpVerb.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbHttpVerb/list'

        populateValidParams(params)
        def tdbHttpVerb = new TdbHttpVerb(params)

        assert tdbHttpVerb.save() != null

        params.id = tdbHttpVerb.id

        def model = controller.show()

        assert model.tdbHttpVerbInstance == tdbHttpVerb
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbHttpVerb/list'

        populateValidParams(params)
        def tdbHttpVerb = new TdbHttpVerb(params)

        assert tdbHttpVerb.save() != null

        params.id = tdbHttpVerb.id

        def model = controller.edit()

        assert model.tdbHttpVerbInstance == tdbHttpVerb
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbHttpVerb/list'

        response.reset()

        populateValidParams(params)
        def tdbHttpVerb = new TdbHttpVerb(params)

        assert tdbHttpVerb.save() != null

        // test invalid parameters in update
        params.id = tdbHttpVerb.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/tdbHttpVerb/edit"
        assert model.tdbHttpVerbInstance != null

        tdbHttpVerb.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/tdbHttpVerb/show/$tdbHttpVerb.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        tdbHttpVerb.clearErrors()

        populateValidParams(params)
        params.id = tdbHttpVerb.id
        params.version = -1
        controller.update()

        assert view == "/tdbHttpVerb/edit"
        assert model.tdbHttpVerbInstance != null
        assert model.tdbHttpVerbInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/tdbHttpVerb/list'

        response.reset()

        populateValidParams(params)
        def tdbHttpVerb = new TdbHttpVerb(params)

        assert tdbHttpVerb.save() != null
        assert TdbHttpVerb.count() == 1

        params.id = tdbHttpVerb.id

        controller.delete()

        assert TdbHttpVerb.count() == 0
        assert TdbHttpVerb.get(tdbHttpVerb.id) == null
        assert response.redirectedUrl == '/tdbHttpVerb/list'
    }
}

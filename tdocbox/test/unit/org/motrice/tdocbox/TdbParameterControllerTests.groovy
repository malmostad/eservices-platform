package org.motrice.tdocbox



import org.junit.*
import grails.test.mixin.*

@TestFor(TdbParameterController)
@Mock(TdbParameter)
class TdbParameterControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/tdbParameter/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.tdbParameterInstanceList.size() == 0
        assert model.tdbParameterInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.tdbParameterInstance != null
    }

    void testSave() {
        controller.save()

        assert model.tdbParameterInstance != null
        assert view == '/tdbParameter/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/tdbParameter/show/1'
        assert controller.flash.message != null
        assert TdbParameter.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbParameter/list'

        populateValidParams(params)
        def tdbParameter = new TdbParameter(params)

        assert tdbParameter.save() != null

        params.id = tdbParameter.id

        def model = controller.show()

        assert model.tdbParameterInstance == tdbParameter
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbParameter/list'

        populateValidParams(params)
        def tdbParameter = new TdbParameter(params)

        assert tdbParameter.save() != null

        params.id = tdbParameter.id

        def model = controller.edit()

        assert model.tdbParameterInstance == tdbParameter
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/tdbParameter/list'

        response.reset()

        populateValidParams(params)
        def tdbParameter = new TdbParameter(params)

        assert tdbParameter.save() != null

        // test invalid parameters in update
        params.id = tdbParameter.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/tdbParameter/edit"
        assert model.tdbParameterInstance != null

        tdbParameter.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/tdbParameter/show/$tdbParameter.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        tdbParameter.clearErrors()

        populateValidParams(params)
        params.id = tdbParameter.id
        params.version = -1
        controller.update()

        assert view == "/tdbParameter/edit"
        assert model.tdbParameterInstance != null
        assert model.tdbParameterInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/tdbParameter/list'

        response.reset()

        populateValidParams(params)
        def tdbParameter = new TdbParameter(params)

        assert tdbParameter.save() != null
        assert TdbParameter.count() == 1

        params.id = tdbParameter.id

        controller.delete()

        assert TdbParameter.count() == 0
        assert TdbParameter.get(tdbParameter.id) == null
        assert response.redirectedUrl == '/tdbParameter/list'
    }
}

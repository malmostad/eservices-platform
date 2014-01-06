package org.motrice.orifice



import org.junit.*
import grails.test.mixin.*

@TestFor(OriFormdefController)
@Mock(OriFormdef)
class OriFormdefControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/oriFormdef/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.oriFormdefInstanceList.size() == 0
        assert model.oriFormdefInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.oriFormdefInstance != null
    }

    void testSave() {
        controller.save()

        assert model.oriFormdefInstance != null
        assert view == '/oriFormdef/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/oriFormdef/show/1'
        assert controller.flash.message != null
        assert OriFormdef.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/oriFormdef/list'

        populateValidParams(params)
        def oriFormdef = new OriFormdef(params)

        assert oriFormdef.save() != null

        params.id = oriFormdef.id

        def model = controller.show()

        assert model.oriFormdefInstance == oriFormdef
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/oriFormdef/list'

        populateValidParams(params)
        def oriFormdef = new OriFormdef(params)

        assert oriFormdef.save() != null

        params.id = oriFormdef.id

        def model = controller.edit()

        assert model.oriFormdefInstance == oriFormdef
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/oriFormdef/list'

        response.reset()

        populateValidParams(params)
        def oriFormdef = new OriFormdef(params)

        assert oriFormdef.save() != null

        // test invalid parameters in update
        params.id = oriFormdef.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/oriFormdef/edit"
        assert model.oriFormdefInstance != null

        oriFormdef.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/oriFormdef/show/$oriFormdef.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        oriFormdef.clearErrors()

        populateValidParams(params)
        params.id = oriFormdef.id
        params.version = -1
        controller.update()

        assert view == "/oriFormdef/edit"
        assert model.oriFormdefInstance != null
        assert model.oriFormdefInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/oriFormdef/list'

        response.reset()

        populateValidParams(params)
        def oriFormdef = new OriFormdef(params)

        assert oriFormdef.save() != null
        assert OriFormdef.count() == 1

        params.id = oriFormdef.id

        controller.delete()

        assert OriFormdef.count() == 0
        assert OriFormdef.get(oriFormdef.id) == null
        assert response.redirectedUrl == '/oriFormdef/list'
    }
}

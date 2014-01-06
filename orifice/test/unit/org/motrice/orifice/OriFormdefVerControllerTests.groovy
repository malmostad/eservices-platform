package org.motrice.orifice



import org.junit.*
import grails.test.mixin.*

@TestFor(OriFormdefVerController)
@Mock(OriFormdefVer)
class OriFormdefVerControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/oriFormdefVer/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.oriFormdefVerInstanceList.size() == 0
        assert model.oriFormdefVerInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.oriFormdefVerInstance != null
    }

    void testSave() {
        controller.save()

        assert model.oriFormdefVerInstance != null
        assert view == '/oriFormdefVer/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/oriFormdefVer/show/1'
        assert controller.flash.message != null
        assert OriFormdefVer.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/oriFormdefVer/list'

        populateValidParams(params)
        def oriFormdefVer = new OriFormdefVer(params)

        assert oriFormdefVer.save() != null

        params.id = oriFormdefVer.id

        def model = controller.show()

        assert model.oriFormdefVerInstance == oriFormdefVer
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/oriFormdefVer/list'

        populateValidParams(params)
        def oriFormdefVer = new OriFormdefVer(params)

        assert oriFormdefVer.save() != null

        params.id = oriFormdefVer.id

        def model = controller.edit()

        assert model.oriFormdefVerInstance == oriFormdefVer
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/oriFormdefVer/list'

        response.reset()

        populateValidParams(params)
        def oriFormdefVer = new OriFormdefVer(params)

        assert oriFormdefVer.save() != null

        // test invalid parameters in update
        params.id = oriFormdefVer.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/oriFormdefVer/edit"
        assert model.oriFormdefVerInstance != null

        oriFormdefVer.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/oriFormdefVer/show/$oriFormdefVer.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        oriFormdefVer.clearErrors()

        populateValidParams(params)
        params.id = oriFormdefVer.id
        params.version = -1
        controller.update()

        assert view == "/oriFormdefVer/edit"
        assert model.oriFormdefVerInstance != null
        assert model.oriFormdefVerInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/oriFormdefVer/list'

        response.reset()

        populateValidParams(params)
        def oriFormdefVer = new OriFormdefVer(params)

        assert oriFormdefVer.save() != null
        assert OriFormdefVer.count() == 1

        params.id = oriFormdefVer.id

        controller.delete()

        assert OriFormdefVer.count() == 0
        assert OriFormdefVer.get(oriFormdefVer.id) == null
        assert response.redirectedUrl == '/oriFormdefVer/list'
    }
}

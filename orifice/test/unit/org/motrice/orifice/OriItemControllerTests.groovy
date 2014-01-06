package org.motrice.orifice



import org.junit.*
import grails.test.mixin.*

@TestFor(OriItemController)
@Mock(OriItem)
class OriItemControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/oriItem/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.oriItemInstanceList.size() == 0
        assert model.oriItemInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.oriItemInstance != null
    }

    void testSave() {
        controller.save()

        assert model.oriItemInstance != null
        assert view == '/oriItem/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/oriItem/show/1'
        assert controller.flash.message != null
        assert OriItem.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/oriItem/list'

        populateValidParams(params)
        def oriItem = new OriItem(params)

        assert oriItem.save() != null

        params.id = oriItem.id

        def model = controller.show()

        assert model.oriItemInstance == oriItem
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/oriItem/list'

        populateValidParams(params)
        def oriItem = new OriItem(params)

        assert oriItem.save() != null

        params.id = oriItem.id

        def model = controller.edit()

        assert model.oriItemInstance == oriItem
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/oriItem/list'

        response.reset()

        populateValidParams(params)
        def oriItem = new OriItem(params)

        assert oriItem.save() != null

        // test invalid parameters in update
        params.id = oriItem.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/oriItem/edit"
        assert model.oriItemInstance != null

        oriItem.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/oriItem/show/$oriItem.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        oriItem.clearErrors()

        populateValidParams(params)
        params.id = oriItem.id
        params.version = -1
        controller.update()

        assert view == "/oriItem/edit"
        assert model.oriItemInstance != null
        assert model.oriItemInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/oriItem/list'

        response.reset()

        populateValidParams(params)
        def oriItem = new OriItem(params)

        assert oriItem.save() != null
        assert OriItem.count() == 1

        params.id = oriItem.id

        controller.delete()

        assert OriItem.count() == 0
        assert OriItem.get(oriItem.id) == null
        assert response.redirectedUrl == '/oriItem/list'
    }
}

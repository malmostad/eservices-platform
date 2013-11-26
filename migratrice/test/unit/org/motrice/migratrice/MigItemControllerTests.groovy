package org.motrice.migratrice



import org.junit.*
import grails.test.mixin.*

@TestFor(MigItemController)
@Mock(MigItem)
class MigItemControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/migItem/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.migItemInstanceList.size() == 0
        assert model.migItemInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.migItemInstance != null
    }

    void testSave() {
        controller.save()

        assert model.migItemInstance != null
        assert view == '/migItem/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/migItem/show/1'
        assert controller.flash.message != null
        assert MigItem.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/migItem/list'

        populateValidParams(params)
        def migItem = new MigItem(params)

        assert migItem.save() != null

        params.id = migItem.id

        def model = controller.show()

        assert model.migItemInstance == migItem
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/migItem/list'

        populateValidParams(params)
        def migItem = new MigItem(params)

        assert migItem.save() != null

        params.id = migItem.id

        def model = controller.edit()

        assert model.migItemInstance == migItem
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/migItem/list'

        response.reset()

        populateValidParams(params)
        def migItem = new MigItem(params)

        assert migItem.save() != null

        // test invalid parameters in update
        params.id = migItem.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/migItem/edit"
        assert model.migItemInstance != null

        migItem.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/migItem/show/$migItem.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        migItem.clearErrors()

        populateValidParams(params)
        params.id = migItem.id
        params.version = -1
        controller.update()

        assert view == "/migItem/edit"
        assert model.migItemInstance != null
        assert model.migItemInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/migItem/list'

        response.reset()

        populateValidParams(params)
        def migItem = new MigItem(params)

        assert migItem.save() != null
        assert MigItem.count() == 1

        params.id = migItem.id

        controller.delete()

        assert MigItem.count() == 0
        assert MigItem.get(migItem.id) == null
        assert response.redirectedUrl == '/migItem/list'
    }
}

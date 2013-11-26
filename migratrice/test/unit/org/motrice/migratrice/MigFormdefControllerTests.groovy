package org.motrice.migratrice



import org.junit.*
import grails.test.mixin.*

@TestFor(MigFormdefController)
@Mock(MigFormdef)
class MigFormdefControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/migFormdef/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.migFormdefInstanceList.size() == 0
        assert model.migFormdefInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.migFormdefInstance != null
    }

    void testSave() {
        controller.save()

        assert model.migFormdefInstance != null
        assert view == '/migFormdef/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/migFormdef/show/1'
        assert controller.flash.message != null
        assert MigFormdef.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/migFormdef/list'

        populateValidParams(params)
        def migFormdef = new MigFormdef(params)

        assert migFormdef.save() != null

        params.id = migFormdef.id

        def model = controller.show()

        assert model.migFormdefInstance == migFormdef
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/migFormdef/list'

        populateValidParams(params)
        def migFormdef = new MigFormdef(params)

        assert migFormdef.save() != null

        params.id = migFormdef.id

        def model = controller.edit()

        assert model.migFormdefInstance == migFormdef
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/migFormdef/list'

        response.reset()

        populateValidParams(params)
        def migFormdef = new MigFormdef(params)

        assert migFormdef.save() != null

        // test invalid parameters in update
        params.id = migFormdef.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/migFormdef/edit"
        assert model.migFormdefInstance != null

        migFormdef.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/migFormdef/show/$migFormdef.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        migFormdef.clearErrors()

        populateValidParams(params)
        params.id = migFormdef.id
        params.version = -1
        controller.update()

        assert view == "/migFormdef/edit"
        assert model.migFormdefInstance != null
        assert model.migFormdefInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/migFormdef/list'

        response.reset()

        populateValidParams(params)
        def migFormdef = new MigFormdef(params)

        assert migFormdef.save() != null
        assert MigFormdef.count() == 1

        params.id = migFormdef.id

        controller.delete()

        assert MigFormdef.count() == 0
        assert MigFormdef.get(migFormdef.id) == null
        assert response.redirectedUrl == '/migFormdef/list'
    }
}

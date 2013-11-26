package org.motrice.migratrice



import org.junit.*
import grails.test.mixin.*

@TestFor(MigFormdefVerController)
@Mock(MigFormdefVer)
class MigFormdefVerControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/migFormdefVer/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.migFormdefVerInstanceList.size() == 0
        assert model.migFormdefVerInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.migFormdefVerInstance != null
    }

    void testSave() {
        controller.save()

        assert model.migFormdefVerInstance != null
        assert view == '/migFormdefVer/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/migFormdefVer/show/1'
        assert controller.flash.message != null
        assert MigFormdefVer.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/migFormdefVer/list'

        populateValidParams(params)
        def migFormdefVer = new MigFormdefVer(params)

        assert migFormdefVer.save() != null

        params.id = migFormdefVer.id

        def model = controller.show()

        assert model.migFormdefVerInstance == migFormdefVer
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/migFormdefVer/list'

        populateValidParams(params)
        def migFormdefVer = new MigFormdefVer(params)

        assert migFormdefVer.save() != null

        params.id = migFormdefVer.id

        def model = controller.edit()

        assert model.migFormdefVerInstance == migFormdefVer
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/migFormdefVer/list'

        response.reset()

        populateValidParams(params)
        def migFormdefVer = new MigFormdefVer(params)

        assert migFormdefVer.save() != null

        // test invalid parameters in update
        params.id = migFormdefVer.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/migFormdefVer/edit"
        assert model.migFormdefVerInstance != null

        migFormdefVer.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/migFormdefVer/show/$migFormdefVer.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        migFormdefVer.clearErrors()

        populateValidParams(params)
        params.id = migFormdefVer.id
        params.version = -1
        controller.update()

        assert view == "/migFormdefVer/edit"
        assert model.migFormdefVerInstance != null
        assert model.migFormdefVerInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/migFormdefVer/list'

        response.reset()

        populateValidParams(params)
        def migFormdefVer = new MigFormdefVer(params)

        assert migFormdefVer.save() != null
        assert MigFormdefVer.count() == 1

        params.id = migFormdefVer.id

        controller.delete()

        assert MigFormdefVer.count() == 0
        assert MigFormdefVer.get(migFormdefVer.id) == null
        assert response.redirectedUrl == '/migFormdefVer/list'
    }
}

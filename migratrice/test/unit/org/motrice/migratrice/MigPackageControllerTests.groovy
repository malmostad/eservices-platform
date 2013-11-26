package org.motrice.migratrice



import org.junit.*
import grails.test.mixin.*

@TestFor(MigPackageController)
@Mock(MigPackage)
class MigPackageControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/migPackage/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.migPackageInstanceList.size() == 0
        assert model.migPackageInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.migPackageInstance != null
    }

    void testSave() {
        controller.save()

        assert model.migPackageInstance != null
        assert view == '/migPackage/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/migPackage/show/1'
        assert controller.flash.message != null
        assert MigPackage.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/migPackage/list'

        populateValidParams(params)
        def migPackage = new MigPackage(params)

        assert migPackage.save() != null

        params.id = migPackage.id

        def model = controller.show()

        assert model.migPackageInstance == migPackage
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/migPackage/list'

        populateValidParams(params)
        def migPackage = new MigPackage(params)

        assert migPackage.save() != null

        params.id = migPackage.id

        def model = controller.edit()

        assert model.migPackageInstance == migPackage
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/migPackage/list'

        response.reset()

        populateValidParams(params)
        def migPackage = new MigPackage(params)

        assert migPackage.save() != null

        // test invalid parameters in update
        params.id = migPackage.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/migPackage/edit"
        assert model.migPackageInstance != null

        migPackage.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/migPackage/show/$migPackage.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        migPackage.clearErrors()

        populateValidParams(params)
        params.id = migPackage.id
        params.version = -1
        controller.update()

        assert view == "/migPackage/edit"
        assert model.migPackageInstance != null
        assert model.migPackageInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/migPackage/list'

        response.reset()

        populateValidParams(params)
        def migPackage = new MigPackage(params)

        assert migPackage.save() != null
        assert MigPackage.count() == 1

        params.id = migPackage.id

        controller.delete()

        assert MigPackage.count() == 0
        assert MigPackage.get(migPackage.id) == null
        assert response.redirectedUrl == '/migPackage/list'
    }
}

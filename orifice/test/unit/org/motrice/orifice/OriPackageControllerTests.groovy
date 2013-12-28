package org.motrice.orifice



import org.junit.*
import grails.test.mixin.*

@TestFor(OriPackageController)
@Mock(OriPackage)
class OriPackageControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/oriPackage/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.oriPackageInstanceList.size() == 0
        assert model.oriPackageInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.oriPackageInstance != null
    }

    void testSave() {
        controller.save()

        assert model.oriPackageInstance != null
        assert view == '/oriPackage/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/oriPackage/show/1'
        assert controller.flash.message != null
        assert OriPackage.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/oriPackage/list'

        populateValidParams(params)
        def oriPackage = new OriPackage(params)

        assert oriPackage.save() != null

        params.id = oriPackage.id

        def model = controller.show()

        assert model.oriPackageInstance == oriPackage
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/oriPackage/list'

        populateValidParams(params)
        def oriPackage = new OriPackage(params)

        assert oriPackage.save() != null

        params.id = oriPackage.id

        def model = controller.edit()

        assert model.oriPackageInstance == oriPackage
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/oriPackage/list'

        response.reset()

        populateValidParams(params)
        def oriPackage = new OriPackage(params)

        assert oriPackage.save() != null

        // test invalid parameters in update
        params.id = oriPackage.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/oriPackage/edit"
        assert model.oriPackageInstance != null

        oriPackage.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/oriPackage/show/$oriPackage.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        oriPackage.clearErrors()

        populateValidParams(params)
        params.id = oriPackage.id
        params.version = -1
        controller.update()

        assert view == "/oriPackage/edit"
        assert model.oriPackageInstance != null
        assert model.oriPackageInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/oriPackage/list'

        response.reset()

        populateValidParams(params)
        def oriPackage = new OriPackage(params)

        assert oriPackage.save() != null
        assert OriPackage.count() == 1

        params.id = oriPackage.id

        controller.delete()

        assert OriPackage.count() == 0
        assert OriPackage.get(oriPackage.id) == null
        assert response.redirectedUrl == '/oriPackage/list'
    }
}

package org.motrice.coordinatrice



import org.junit.*
import grails.test.mixin.*

@TestFor(ProcCategoryController)
@Mock(ProcCategory)
class ProcCategoryControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/procCategory/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.procCategoryInstanceList.size() == 0
        assert model.procCategoryInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.procCategoryInstance != null
    }

    void testSave() {
        controller.save()

        assert model.procCategoryInstance != null
        assert view == '/procCategory/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/procCategory/show/1'
        assert controller.flash.message != null
        assert ProcCategory.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/procCategory/list'

        populateValidParams(params)
        def procCategory = new ProcCategory(params)

        assert procCategory.save() != null

        params.id = procCategory.id

        def model = controller.show()

        assert model.procCategoryInstance == procCategory
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/procCategory/list'

        populateValidParams(params)
        def procCategory = new ProcCategory(params)

        assert procCategory.save() != null

        params.id = procCategory.id

        def model = controller.edit()

        assert model.procCategoryInstance == procCategory
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/procCategory/list'

        response.reset()

        populateValidParams(params)
        def procCategory = new ProcCategory(params)

        assert procCategory.save() != null

        // test invalid parameters in update
        params.id = procCategory.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/procCategory/edit"
        assert model.procCategoryInstance != null

        procCategory.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/procCategory/show/$procCategory.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        procCategory.clearErrors()

        populateValidParams(params)
        params.id = procCategory.id
        params.version = -1
        controller.update()

        assert view == "/procCategory/edit"
        assert model.procCategoryInstance != null
        assert model.procCategoryInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/procCategory/list'

        response.reset()

        populateValidParams(params)
        def procCategory = new ProcCategory(params)

        assert procCategory.save() != null
        assert ProcCategory.count() == 1

        params.id = procCategory.id

        controller.delete()

        assert ProcCategory.count() == 0
        assert ProcCategory.get(procCategory.id) == null
        assert response.redirectedUrl == '/procCategory/list'
    }
}

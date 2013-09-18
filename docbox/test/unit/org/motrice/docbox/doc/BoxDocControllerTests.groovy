package org.motrice.docbox.doc



import org.junit.*
import grails.test.mixin.*

@TestFor(BoxDocController)
@Mock(BoxDoc)
class BoxDocControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/boxDoc/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.boxDocInstanceList.size() == 0
        assert model.boxDocInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.boxDocInstance != null
    }

    void testSave() {
        controller.save()

        assert model.boxDocInstance != null
        assert view == '/boxDoc/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/boxDoc/show/1'
        assert controller.flash.message != null
        assert BoxDoc.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/boxDoc/list'

        populateValidParams(params)
        def boxDoc = new BoxDoc(params)

        assert boxDoc.save() != null

        params.id = boxDoc.id

        def model = controller.show()

        assert model.boxDocInstance == boxDoc
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/boxDoc/list'

        populateValidParams(params)
        def boxDoc = new BoxDoc(params)

        assert boxDoc.save() != null

        params.id = boxDoc.id

        def model = controller.edit()

        assert model.boxDocInstance == boxDoc
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/boxDoc/list'

        response.reset()

        populateValidParams(params)
        def boxDoc = new BoxDoc(params)

        assert boxDoc.save() != null

        // test invalid parameters in update
        params.id = boxDoc.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/boxDoc/edit"
        assert model.boxDocInstance != null

        boxDoc.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/boxDoc/show/$boxDoc.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        boxDoc.clearErrors()

        populateValidParams(params)
        params.id = boxDoc.id
        params.version = -1
        controller.update()

        assert view == "/boxDoc/edit"
        assert model.boxDocInstance != null
        assert model.boxDocInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/boxDoc/list'

        response.reset()

        populateValidParams(params)
        def boxDoc = new BoxDoc(params)

        assert boxDoc.save() != null
        assert BoxDoc.count() == 1

        params.id = boxDoc.id

        controller.delete()

        assert BoxDoc.count() == 0
        assert BoxDoc.get(boxDoc.id) == null
        assert response.redirectedUrl == '/boxDoc/list'
    }
}

package org.motrice.docbox.doc



import org.junit.*
import grails.test.mixin.*

@TestFor(BoxContentsController)
@Mock(BoxContents)
class BoxContentsControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/boxContents/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.boxContentsInstanceList.size() == 0
        assert model.boxContentsInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.boxContentsInstance != null
    }

    void testSave() {
        controller.save()

        assert model.boxContentsInstance != null
        assert view == '/boxContents/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/boxContents/show/1'
        assert controller.flash.message != null
        assert BoxContents.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/boxContents/list'

        populateValidParams(params)
        def boxContents = new BoxContents(params)

        assert boxContents.save() != null

        params.id = boxContents.id

        def model = controller.show()

        assert model.boxContentsInstance == boxContents
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/boxContents/list'

        populateValidParams(params)
        def boxContents = new BoxContents(params)

        assert boxContents.save() != null

        params.id = boxContents.id

        def model = controller.edit()

        assert model.boxContentsInstance == boxContents
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/boxContents/list'

        response.reset()

        populateValidParams(params)
        def boxContents = new BoxContents(params)

        assert boxContents.save() != null

        // test invalid parameters in update
        params.id = boxContents.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/boxContents/edit"
        assert model.boxContentsInstance != null

        boxContents.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/boxContents/show/$boxContents.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        boxContents.clearErrors()

        populateValidParams(params)
        params.id = boxContents.id
        params.version = -1
        controller.update()

        assert view == "/boxContents/edit"
        assert model.boxContentsInstance != null
        assert model.boxContentsInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/boxContents/list'

        response.reset()

        populateValidParams(params)
        def boxContents = new BoxContents(params)

        assert boxContents.save() != null
        assert BoxContents.count() == 1

        params.id = boxContents.id

        controller.delete()

        assert BoxContents.count() == 0
        assert BoxContents.get(boxContents.id) == null
        assert response.redirectedUrl == '/boxContents/list'
    }
}

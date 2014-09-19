package org.motrice.signatrice



import org.junit.*
import grails.test.mixin.*

@TestFor(SigAttributeController)
@Mock(SigAttribute)
class SigAttributeControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/sigAttribute/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.sigAttributeInstanceList.size() == 0
        assert model.sigAttributeInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.sigAttributeInstance != null
    }

    void testSave() {
        controller.save()

        assert model.sigAttributeInstance != null
        assert view == '/sigAttribute/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/sigAttribute/show/1'
        assert controller.flash.message != null
        assert SigAttribute.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/sigAttribute/list'

        populateValidParams(params)
        def sigAttribute = new SigAttribute(params)

        assert sigAttribute.save() != null

        params.id = sigAttribute.id

        def model = controller.show()

        assert model.sigAttributeInstance == sigAttribute
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/sigAttribute/list'

        populateValidParams(params)
        def sigAttribute = new SigAttribute(params)

        assert sigAttribute.save() != null

        params.id = sigAttribute.id

        def model = controller.edit()

        assert model.sigAttributeInstance == sigAttribute
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/sigAttribute/list'

        response.reset()

        populateValidParams(params)
        def sigAttribute = new SigAttribute(params)

        assert sigAttribute.save() != null

        // test invalid parameters in update
        params.id = sigAttribute.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/sigAttribute/edit"
        assert model.sigAttributeInstance != null

        sigAttribute.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/sigAttribute/show/$sigAttribute.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        sigAttribute.clearErrors()

        populateValidParams(params)
        params.id = sigAttribute.id
        params.version = -1
        controller.update()

        assert view == "/sigAttribute/edit"
        assert model.sigAttributeInstance != null
        assert model.sigAttributeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/sigAttribute/list'

        response.reset()

        populateValidParams(params)
        def sigAttribute = new SigAttribute(params)

        assert sigAttribute.save() != null
        assert SigAttribute.count() == 1

        params.id = sigAttribute.id

        controller.delete()

        assert SigAttribute.count() == 0
        assert SigAttribute.get(sigAttribute.id) == null
        assert response.redirectedUrl == '/sigAttribute/list'
    }
}

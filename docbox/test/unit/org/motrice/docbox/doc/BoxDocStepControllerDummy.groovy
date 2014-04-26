/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
package org.motrice.docbox.doc



import org.junit.*
import grails.test.mixin.*

@TestFor(BoxDocStepController)
@Mock(BoxDocStep)
class BoxDocStepControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/boxDocStep/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.boxDocStepInstanceList.size() == 0
        assert model.boxDocStepInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.boxDocStepInstance != null
    }

    void testSave() {
        controller.save()

        assert model.boxDocStepInstance != null
        assert view == '/boxDocStep/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/boxDocStep/show/1'
        assert controller.flash.message != null
        assert BoxDocStep.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/boxDocStep/list'

        populateValidParams(params)
        def boxDocStep = new BoxDocStep(params)

        assert boxDocStep.save() != null

        params.id = boxDocStep.id

        def model = controller.show()

        assert model.boxDocStepInstance == boxDocStep
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/boxDocStep/list'

        populateValidParams(params)
        def boxDocStep = new BoxDocStep(params)

        assert boxDocStep.save() != null

        params.id = boxDocStep.id

        def model = controller.edit()

        assert model.boxDocStepInstance == boxDocStep
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/boxDocStep/list'

        response.reset()

        populateValidParams(params)
        def boxDocStep = new BoxDocStep(params)

        assert boxDocStep.save() != null

        // test invalid parameters in update
        params.id = boxDocStep.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/boxDocStep/edit"
        assert model.boxDocStepInstance != null

        boxDocStep.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/boxDocStep/show/$boxDocStep.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        boxDocStep.clearErrors()

        populateValidParams(params)
        params.id = boxDocStep.id
        params.version = -1
        controller.update()

        assert view == "/boxDocStep/edit"
        assert model.boxDocStepInstance != null
        assert model.boxDocStepInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/boxDocStep/list'

        response.reset()

        populateValidParams(params)
        def boxDocStep = new BoxDocStep(params)

        assert boxDocStep.save() != null
        assert BoxDocStep.count() == 1

        params.id = boxDocStep.id

        controller.delete()

        assert BoxDocStep.count() == 0
        assert BoxDocStep.get(boxDocStep.id) == null
        assert response.redirectedUrl == '/boxDocStep/list'
    }
}

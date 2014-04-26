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

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

import org.springframework.dao.DataIntegrityViolationException

class BoxDocStepController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [boxDocStepObjList: BoxDocStep.list(params), boxDocStepObjTotal: BoxDocStep.count()]
    }

    def create() {
        [boxDocStepObj: new BoxDocStep(params)]
    }

    def save() {
        def boxDocStepObj = new BoxDocStep(params)
        if (!boxDocStepObj.save(flush: true)) {
            render(view: "create", model: [boxDocStepObj: boxDocStepObj])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'boxDocStep.label', default: 'BoxDocStep'), boxDocStepObj.id])
        redirect(action: "show", id: boxDocStepObj.id)
    }

    def show(Long id) {
        def boxDocStepObj = BoxDocStep.get(id)
        if (!boxDocStepObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxDocStep.label', default: 'BoxDocStep'), id])
            redirect(action: "list")
            return
        }

        [boxDocStepObj: boxDocStepObj]
    }

    def edit(Long id) {
        def boxDocStepObj = BoxDocStep.get(id)
        if (!boxDocStepObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxDocStep.label', default: 'BoxDocStep'), id])
            redirect(action: "list")
            return
        }

        [boxDocStepObj: boxDocStepObj]
    }

    def update(Long id, Long version) {
        def boxDocStepObj = BoxDocStep.get(id)
        if (!boxDocStepObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxDocStep.label', default: 'BoxDocStep'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (boxDocStepObj.version > version) {
                boxDocStepObj.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'boxDocStep.label', default: 'BoxDocStep')] as Object[],
                          "Another user has updated this BoxDocStep while you were editing")
                render(view: "edit", model: [boxDocStepObj: boxDocStepObj])
                return
            }
        }

        boxDocStepObj.properties = params

        if (!boxDocStepObj.save(flush: true)) {
            render(view: "edit", model: [boxDocStepObj: boxDocStepObj])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'boxDocStep.label', default: 'BoxDocStep'), boxDocStepObj.id])
        redirect(action: "show", id: boxDocStepObj.id)
    }

    def delete(Long id) {
        def boxDocStepObj = BoxDocStep.get(id)
        if (!boxDocStepObj) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'boxDocStep.label', default: 'BoxDocStep'), id])
            redirect(action: "list")
            return
        }

        try {
            boxDocStepObj.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'boxDocStep.label', default: 'BoxDocStep'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'boxDocStep.label', default: 'BoxDocStep'), id])
            redirect(action: "show", id: id)
        }
    }
}

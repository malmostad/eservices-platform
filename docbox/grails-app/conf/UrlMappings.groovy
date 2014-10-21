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
class UrlMappings {

  static mappings = {
    "/rest/env/validate"(controller: 'Env') {
      action = [GET: 'validate']
    }
    "/rest/doc/orbeon/$uuid"(controller: 'RestDoc') {
      action = [GET: 'docboxOrbeonData', PUT: 'formDataPut']
    }
    "/rest/doc/byformdata/$uuid"(controller: 'RestDoc') {
      action = [GET: 'formDataGet']
    }
    "/rest/meta/byformdata/$uuid"(controller: 'RestDoc') {
      action = [GET: 'metaByFormData']
    }
    "/rest/doc/core/$docboxref"(controller: 'RestDoc') {
      action = [GET: 'docboxRefGet']
    }
    "/rest/doc/meta/$docboxref"(controller: 'RestDoc') {
      action = [GET: 'metaDataGet']
    }
    "/rest/sig/core/$docboxref"(controller: 'RestSig') {
      action = [GET: 'docboxSigGet', POST: 'docboxSigPut']
    }
    "/rest/sig/request/$docboxref"(controller: 'RestSig') {
      action = [GET: 'docSigCollect', POST: 'docSigRequest']
    }
    "/rest/sig/validation/$docboxref"(controller: 'RestSig') {
      action = [GET: 'sigValidate']
    }

    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }

    "/"(controller: 'BoxDoc', action: '/index')
    "500"(controller: 'errors', action: 'internalServerError')
  }
}

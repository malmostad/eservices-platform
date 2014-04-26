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
    "/rest/db/orbeon-pe/fr/$app/$form/form/$resource"(controller: 'RestFormdef') {
      action = [GET: 'getop', PUT: 'putop', DELETE: 'delete']
    }
    "/rest/db/orbeon-pe/fr/orbeon/builder/data/$uuid/$resource"(controller: 'RestResource') {
      action = [GET: 'getop', PUT: 'putop', DELETE: 'delete']
    }
    "/rest/db/orbeon-pe/fr/$app/$form/data/$uuid/$resource"(controller: 'RestFormdata') {
      action = [GET: 'getop', PUT: 'putop', DELETE: 'delete']
    }
    "/rest/db/orbeon-pe/fr/orbeon/builder/data"(controller: 'RestFormdef', action: 'list')
    "/postxdb/formdef/$id?"(controller: 'RestPostxdb') {
      action = [GET: 'formdefget']
    }
    "/postxdb/formdefver/$id?"(controller: 'RestPostxdb') {
      action = [GET: 'versionget']
    }
    "/postxdb/defitem"(controller: 'RestPostxdb') {
      action = [GET: 'defitemget']
    }
    "/postxdb/institem"(controller: 'RestPostxdb') {
      action = [GET: 'institemget']
    }
    "/postxdb/item/$id"(controller: 'RestPostxdb') {
      action = [GET: 'itemgetbyid']
    }
    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }

    "/"(controller: 'PxdFormdef', action: 'index')
    "500"(controller: 'errors', action: 'internalServerError')
  }
}

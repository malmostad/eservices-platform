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
package org.motrice.docbox

import org.motrice.docbox.form.PxdItem
import org.motrice.docbox.doc.BoxDoc

/**
 * Controller for checking the environment of this application
 */
class EnvController {

  def envService

  /**
   * Validate the setup
   * The id is not used
   * Besides the HTTP status the method returns diagnostics as JSON data
   */
  def validate(Long id) {
    // Check installed packages
    def map = envService.validate()
    int errorCount = map.errorCount

    // Check the forms datasource
    def formsDsMsg = null
    try {
      def rcount = PxdItem.count()
      formsDsMsg = 'OK/forms datasource'
    } catch (Exception exc) {
      ++errorCount
      formsDsMsg = "ERR/forms datasource (${exc.message})"
    }

    // Check the DOC datasource
    def docDsMsg = null
    try {
      def rcount = BoxDoc.count()
      docDsMsg = 'OK/doc datasource'
    } catch (Exception exc) {
      ++errorCount
      docDsMsg = "ERR/doc datasource (${exc.message})"
    }

    def status = (errorCount > 0)? 409 : 200
    render(status: status, contentType: "text/json") {
      docbox_version = grailsApplication.metadata['app.version']
      error_count = errorCount
      package_status = map.pkgMessage
      docs_datasource = docDsMsg
      forms_datasource = formsDsMsg
    }
  }
}

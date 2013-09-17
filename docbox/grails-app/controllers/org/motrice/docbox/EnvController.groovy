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

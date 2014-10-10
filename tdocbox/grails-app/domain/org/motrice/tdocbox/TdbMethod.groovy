package org.motrice.tdocbox

class TdbMethod {
  // Method URL without prefix and separating slash, but with optional placeholders
  String urlPattern

  // Description of the method
  String description

  static mapping = {
    id generator: 'assigned'
  }
  static constraints = {
    urlPattern nullable: false, maxSize: 512
    description nullable: true, maxSize: 200
  }

  static TdbMethod createMethod(Long identity, String url, String description) {
    if (!TdbMethod.get(identity)) {
      def method = new TdbMethod(urlPattern: url, description: description)
      method.id = identity
      method.save(failOnError: true)
    }
  }

  static createMethods() {
    createMethod(CREATE_PDFA_FROM_ORBEON_ID, CREATE_PDFA_FROM_ORBEON_URL,
		 'Create PDF/A from Orbeon form data')
  }

  String url(Map binding) {
    def prefixProp = TdbProperty.findByName(TdbProperty.DOCBOX_REST_PREFIX)
    if (!prefixProp) {
      def msg = "URL prefix not found"
      log.error "TdbMethod.url: ${msg}"
      throw new ServiceException(msg)
    }

    def prefix = prefixProp.value
    def engine = new groovy.text.SimpleTemplateEngine()
    return prefix + '/' + engine.createTemplate(urlPattern).make(binding).toString()
  }

  String toString() {
    description
  }

  static final CREATE_PDFA_FROM_ORBEON_URL = 'doc/orbeon/${formDataUuid}'
  static final CREATE_PDFA_FROM_ORBEON_ID = 1

}

package org.motrice.tdocbox

/**
 * A DocBox method.
 * All instances of this class are predefined.
 */
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

  static createMethod(Long identity, String url, String description) {
    if (!TdbMethod.get(identity)) {
      def method = new TdbMethod(urlPattern: url, description: description)
      method.id = identity
      method.save(failOnError: true)
    }
  }

  static createMethods() {
    createMethod(CREATE_PDFA_FROM_ORBEON_ID, CREATE_PDFA_FROM_ORBEON_URL,
		 'Create PDF/A from Orbeon form data, get Orbeon form data')
    createMethod(VALIDATE_ENVIRONMENT_ID, VALIDATE_ENVIRONMENT_URL,
		 'Validate application environment')
    createMethod(GET_DOC_BY_FORMDATA_ID, GET_DOC_BY_FORMDATA_URL,
		 'Get document by Orbeon form data uuid')
    createMethod(GET_DOC_BY_DOCBOXREF_ID, GET_DOC_BY_DOCBOXREF_URL,
		 'Get document by docboxRef')
    createMethod(SET_GET_SIGNATURE_ID, SET_GET_SIGNATURE_URL,
		 'Set or get document signature')
    createMethod(VALIDATE_SIG_ID, VALIDATE_SIG_URL,
		 'Validate signature')
  }

  static String createUrl(TdbDrill drill, TdbCase cs) {
    def method = TdbMethod.get(drill.method.id)
    cs? method.url(drill.map, cs.map) : method.url(drill.map, null)
  }

  String url(Map locals, Map globals) {
    def prefixProp = TdbProperty.findByName(TdbProperty.DOCBOX_REST_PREFIX)
    if (!prefixProp) {
      def msg = "URL prefix not found"
      log.error "TdbMethod.url: ${msg}"
      throw new ServiceException(msg)
    }

    def binding = [:]
    if (globals) binding.putAll(globals)
    // Locals take precedence.
    binding.putAll(locals)

    // Run the template engine twice to allow for one level of indirection.
    def engine = new groovy.text.SimpleTemplateEngine()
    def url = engine.createTemplate(urlPattern).make(binding).toString()
    url = engine.createTemplate(url).make(binding).toString()
    return "${prefixProp.value}/${url}"
  }

  String toString() {
    description
  }

  static final CREATE_PDFA_FROM_ORBEON_ID = 1
  static final CREATE_PDFA_FROM_ORBEON_URL = 'doc/orbeon/${formDataUuid}'
  static final VALIDATE_ENVIRONMENT_ID = 2
  static final VALIDATE_ENVIRONMENT_URL = 'env/validate'
  static final GET_DOC_BY_FORMDATA_ID = 3
  static final GET_DOC_BY_FORMDATA_URL = 'doc/byformdata/${formDataUuid}'
  static final GET_DOC_BY_DOCBOXREF_ID = 4
  static final GET_DOC_BY_DOCBOXREF_URL = 'doc/core/${docboxref}'
  static final SET_GET_SIGNATURE_ID = 5
  static final SET_GET_SIGNATURE_URL = 'sig/core/${docboxref}'
  static final VALIDATE_SIG_ID = 5
  static final VALIDATE_SIG_URL = 'sig/validation/${docboxref}'

}

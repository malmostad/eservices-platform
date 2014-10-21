package org.motrice.tdocbox

/**
 * A DocBox method.
 * All instances of this class are predefined.
 * A method is either a REST invocation or display a screen.
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
    createMethod(GET_METADATA_ID, GET_METADATA_URL,
		 'Get document metadata by docboxRef')
    createMethod(GET_METADATA_BY_F_ID, GET_METADATA_BY_F_URL,
		 'Get document metadata by form data uuid')
    createMethod(REQUEST_COLLECT_SIG_ID, REQUEST_COLLECT_SIG_URL,
		 'Request or collect signature')
    createMethod(SHOW_BID_PC_ID, SHOW_BID_PC_URL,
		 'Display and start Bankid for a PC')
    createMethod(SHOW_BID_MOBILE_ID, SHOW_BID_MOBILE_URL,
		 'Display and start Bankid for a mobile device')
  }

  static String createAbsoluteUrl(TdbDrill drill, TdbCase cs) {
    drill?.method?.url(drill.queryString, drill.map, cs?.map, true)
  }

  static String createUrl(TdbDrill drill, TdbCase cs) {
    // DEBUG
    println "TdbMethod.url << ${drill}, ${cs}"
    drill?.method?.url(drill.queryString, drill.map, cs?.map, false)
  }

  boolean isDisplayMethod() {
    id >= 1000
  }

  /**
   * Construct an URL string, resolving all variables.
   * NOTE: Throws MissingPropertyException if a variable cannot be resolved
   * If the absolute flag is set no prefix is added.
   */
  String url(String queryString, Map locals, Map globals, boolean absolute) {
    def prefixStr = null
    if (!absolute) {
      def prefixProp = TdbProperty.findByName(TdbProperty.DOCBOX_REST_PREFIX)
      if (!prefixProp) {
	def msg = "URL prefix not found"
	log.error "TdbMethod.url: ${msg}"
	throw new ServiceException(msg)
      }

      prefixStr = prefixProp.value
    }

    def binding = [:]
    if (globals) binding.putAll(globals)
    // Locals take precedence.
    binding.putAll(locals)

    // Run the template engine twice to allow for one level of indirection.
    def engine = new groovy.text.SimpleTemplateEngine()
    String pattern = queryString? "${urlPattern}?${queryString}" : urlPattern
    def url = engine.createTemplate(pattern).make(binding).toString()
    url = engine.createTemplate(url).make(binding).toString()
    return absolute? url : "${prefixStr}/${url}"
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
  static final GET_DOC_BY_DOCBOXREF_URL = 'doc/core/${docboxRef}'
  static final SET_GET_SIGNATURE_ID = 5
  static final SET_GET_SIGNATURE_URL = 'sig/core/${docboxRef}'
  static final VALIDATE_SIG_ID = 6
  static final VALIDATE_SIG_URL = 'sig/validation/${docboxRef}'
  static final GET_METADATA_ID = 7
  static final GET_METADATA_URL = 'doc/meta/${docboxRef}'
  static final GET_METADATA_BY_F_ID = 8
  static final GET_METADATA_BY_F_URL = 'meta/byformdata/${formDataUuid}'
  static final REQUEST_COLLECT_SIG_ID = 9
  static final REQUEST_COLLECT_SIG_URL = 'sig/request/${docboxRef}'
  // Show methods
  static final SHOW_BID_PC_ID = 1000
  static final SHOW_BID_PC_URL = 'bankid:///?autostarttoken=${autostart}&redirect=${returnurl}'
  static final SHOW_BID_MOBILE_ID = 1001
  static final SHOW_BID_MOBILE_URL = 'bankid://autostarttoken=${autostart}&redirect=${returnurl}'

}

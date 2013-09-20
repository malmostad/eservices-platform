package org.motrice.docbox.doc

// The only way to create a logger with a predictable name?
import org.apache.commons.logging.LogFactory

/**
 * Services related to PDF signing
 */
class SignService {
  private static final log = LogFactory.getLog(this)
  static transactional = true

  /**
   * Generate a checksum for a PDF document
   * XXX TODO: this is only a dummy
   * It should not be transactional
   */
  String computeChecksum(BoxContents pdf) {
    def rng = new Random()
    def bytes = new byte[32]
    rng.nextBytes(bytes)
    def sw = new StringWriter()
    bytes.encodeBase64().writeTo(sw)
    return sw.toString()
  }
}

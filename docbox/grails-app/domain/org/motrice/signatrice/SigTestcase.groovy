package org.motrice.signatrice

/**
 * A prepared test case.
 */
class SigTestcase {
  // Name of this test case
  String name

  // Personal id number (Swedish 12-digit personnummer)
  String personalIdNo

  // Text to be signed
  String userVisibleText

  static belongsTo = [scheme: SigScheme]
  static mapping = {
    userVisibleText type: 'text'
  }
  static constraints = {
    name size: 1..80, unique: true
    personalIdNo nullable: true, maxSize: 24
    userVisibleText nullable: false
    scheme nullable: false
  }

  /**
   * Transient abbreviated visible text.
   */
  String getAbbrVisibleText() {
    (userVisibleText.length() > 24)? userVisibleText.substring(0, 22) + '...' : userVisibleText
  }

  /**
   * Encode the visible text as required by the GPR API
   */
  String getEncodedVisibleText() {
    userVisibleText.getBytes('UTF-8').encodeBase64()
  }

}

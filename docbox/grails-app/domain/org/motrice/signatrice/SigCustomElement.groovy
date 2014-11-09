package org.motrice.signatrice

/**
 * Properties read from the configuration at startup.
 * Used in signature validation.
 * We must declare elements that may occur in a signature with a custom
 * id attribute.
 */
class SigCustomElement {
  // Custom element name
  String elementName

  // Name of the custom id attribute
  String idAttrName

  // The properties are read at startup.
  static mapping = {
    cache usage: 'read-only'
  }
  static constraints = {
    elementName nullable: false, maxSize: 40
    idAttrName nullable: false, maxSize: 180
  }

  /**
   * To be invoked from BootStrap in such a way that the configObject parameter
   * is a map of "idNN" and "nsNN" entries (where NN is 01, 02 ...).
   */
  static createAllCustomElements(configObject) {
    // Must delete because caching prevents update
    SigCustomElement.executeUpdate('delete SigCustomElement')
    configObject.each {key, value ->
      def element = SigCustomElement.findOrCreateByElementName(key)
      element.idAttrName = value
      element.save(flush: true, failOnError: true)
    }
  }

}

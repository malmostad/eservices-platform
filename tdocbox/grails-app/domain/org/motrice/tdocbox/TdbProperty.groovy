package org.motrice.tdocbox

/**
 * A name-value pair controlling the actions of this application.
 * A few basic properties are created by BootStrap.
 */
class TdbProperty {
  // The name of the property
  String name

  // The value of the property
  String value

  static mapping = {
    value type: 'text'
  }
  static constraints = {
    name nullable: false, maxSize: 80, unique: true
    value nullable: true
  }

  static createProperty(String name, String value) {
    if (!TdbProperty.findAllByName(name)) {
      new TdbProperty(name: name, value: value).save(failOnError: true)
    }
  }

  /**
   * Create a property from a property name and a config name taken from
   * the application configuration.
   * The config name is assumed to have the prefix "tdocbox.".
   */
  static createFromConfig(String name, props) {
    def value = props.getProperty(name)
    createProperty(name, value)
  }

  // Create predefined properties
  static createProperties() {
    // Accessing the config according to Burt Beckwith
    def app = new TdbProperty().domainClass.grailsApplication
    def props = app.config.tdocbox.toProperties()
    createFromConfig(DOCBOX_REST_PREFIX, props)
  }

  static final DOCBOX_REST_PREFIX = 'docbox.rest.prefix'

}

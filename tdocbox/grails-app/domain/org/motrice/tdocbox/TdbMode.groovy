package org.motrice.tdocbox

/**
 * The way to perform a method call.
 */
class TdbMode {
  // The name of the mode
  String name

  // A free text description of the mode
  String description

  static mapping = {
    id generator: 'assigned'
  }
  static constraints = {
    name nullable: false, maxSize: 40, unique: true
    description nullable: true, maxSize: 400
  }

  /**
   * Make sure a mode is stored in the database.
   */
  static createMode(Long identity, String name, String description) {
    if (!TdbMode.get(identity)) {
      def mode = new TdbMode(name: name, description: description)
      mode.id = identity
      mode.save(failOnError: true)
    }
  }

  /**
   * Create all modes. Must be invoked by BootStrap.
   */
  static createModes() {
    createMode(PARSE_MODE_ID, PARSE_MODE_NAME, PARSE_MODE_DESC)
    createMode(TEXT_MODE_ID, TEXT_MODE_NAME, TEXT_MODE_DESC)
    createMode(BINARY_MODE_ID, BINARY_MODE_NAME, BINARY_MODE_DESC)
  }

  String toString() {
    name
  }

  static PARSE_MODE_ID = 1
  static PARSE_MODE_NAME = 'Parse'
  static PARSE_MODE_DESC = 'Parse result as JSON or XML'
  static TEXT_MODE_ID = 2
  static TEXT_MODE_NAME = 'Text'
  static TEXT_MODE_DESC = 'Capture result as text'
  static BINARY_MODE_ID = 3
  static BINARY_MODE_NAME = 'Binary'
  static BINARY_MODE_DESC = 'Capture result as binary'
  
}

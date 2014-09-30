package org.motrice.signatrice

/**
 * A name by which the application introduces itself to the user
 */
class SigDisplayname {
  // The display name
  String name

  static mapping = {
    id generator: 'assigned'
    cache usage: 'read-only'
  }
  static constraints = {
    name size: 1..120, unique: true
  }

  static SigDisplayname createDisplayname(Long identity, String text) {
    if (!SigDisplayname.get(identity)) {
      def displayName = new SigDisplayname(name: text)
      displayName.id = identity
      displayName.save(failOnError: true)
    }
  }

  static createDisplaynames() {
    DISPLAY_NAMES.eachWithIndex {text, idx ->
      createDisplayname(idx + 1, text)
    }
  }

  String toString() {
    name
  }

  /**
   * All display names.
   * Their id will be the array index + 1.
   */
  static final DISPLAY_NAMES = [
    'Test av Mobilt BankID',
    'Funktionstjänster Test',
    'Unrecognized display name'
  ]
}

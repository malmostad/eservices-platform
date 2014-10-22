package org.motrice.signatrice

/**
 * A singleton defining the current default scheme or null.
 */
class SigDefaultScheme {
  // The default scheme
  SigScheme defaultScheme

  // Last update. Must be nullable to be automatic.
  Date lastUpdated

  static mapping = {
    id generator: 'assigned'
    cache usage: 'nonstrict-read-write'
  }
  static constraints = {
    defaultScheme nullable: true
    lastUpdated nullable: true
  }

  static createDefaultScheme() {
    if (!SigDefaultScheme.get(DEFAULT_SCHEME_ID)) {
      def ds = new SigDefaultScheme()
      ds.id = DEFAULT_SCHEME_ID
      ds.save(failOnError: true)
    }
  }

  /**
   * Return the current default scheme (may be null).
   */
  static SigScheme current() {
    SigDefaultScheme.get(DEFAULT_SCHEME_ID)?.defaultScheme
  }

  /**
   * Test if a scheme is the current default scheme.
   */
  static boolean isCurrentDefault(SigScheme scheme) {
    current()?.id == scheme.id
  }

  static final DEFAULT_SCHEME_ID = 1
}

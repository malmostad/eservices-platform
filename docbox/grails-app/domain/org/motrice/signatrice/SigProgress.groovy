package org.motrice.signatrice

/**
 * Essentially an enumeration of progress states of a CGI request
 */
class SigProgress {
  // Name of the progress state
  String name

  static mapping = {
    id generator: 'assigned'
    cache usage: 'read-only'
  }
  static constraints = {
    name size: 1..80, unique: true
  }

  static SigProgress createProgress(Long identity, String text) {
    if (!SigProgress.get(identity)) {
      def progress = new SigProgress(name: text)
      progress.id = identity
      progress.save(failOnError: true)
    }
  }

  static createAllProgress() {
    STATE_NAMES.eachWithIndex {text, idx ->
      createProgress(idx + INITIAL_ID, text)
    }

    createProgress(UNDEFINED_ID, UNDEFINED_STR)
  }

  static SigProgress initialState() {
    SigProgress.get(INITIAL_ID)
  }

  static SigProgress undefinedState() {
    SigProgress.get(UNDEFINED_ID)
  }

  /**
   * Given a string, look up the corresponding status.
   * Return the undefined status if the string is not found.
   */
  static SigProgress lookup(String input) {
    def idx = STATE_NAMES.findIndexOf {it == input}
    return (idx >= 0)? SigProgress.get(idx + INITIAL_ID) : undefinedState()
  }

  String toString() {
    name
  }

  /**
   * State names.
   * Their id will be the array index + 1.
   */
  static final STATE_NAMES = [
    'INIT',
    'OUTSTANDING_TRANSACTION',
    'USER_SIGN',
    'NO_CLIENT',
    'USER_REQ',
    'STARTED',
    'COMPLETE'
    ]

    // Database id of the initial state
    static final int INITIAL_ID = 1

    // Define an "undefined" state
    static final int UNDEFINED_ID = 99
    static final String UNDEFINED_STR = '*UNDEFINED*'

}

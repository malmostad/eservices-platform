package org.motrice.tdocbox

/**
 * One of the HTTP verbs.
 */
class TdbHttpVerb {
  // The verb (GET, PUT, etc)
  String verb

  static mapping = {
    id generator: 'assigned'
  }
  static constraints = {
    verb nullable: false, maxSize: 16
  }

  static TdbHttpVerb createVerb(Long identity, String verbStr) {
    if (!TdbHttpVerb.get(identity)) {
      def verb = new TdbHttpVerb(verb: verbStr)
      verb.id = identity
      verb.save(failOnError: true)
    }
  }

  static createVerbs() {
    createVerb(GET_ID, GET_STR)
    createVerb(PUT_ID, PUT_STR)
    createVerb(POST_ID, POST_STR)
    createVerb(DELETE_ID, DELETE_STR)
  }

  String toString() {
    verb
  }

  static final GET_STR = 'GET'
  static final GET_ID = 1
  static final PUT_STR = 'PUT'
  static final PUT_ID = 2
  static final POST_STR = 'POST'
  static final POST_ID = 3
  static final DELETE_STR = 'DELETE'
  static final DELETE_ID = 4
}

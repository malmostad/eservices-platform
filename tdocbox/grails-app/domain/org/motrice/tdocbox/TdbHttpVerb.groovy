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
    createVerb(GET_IDX, GET_STR)
    createVerb(PUT_IDX, PUT_STR)
    createVerb(POST_IDX, POST_STR)
    createVerb(DELETE_IDX, DELETE_STR)
  }

  String toString() {
    verb
  }

  static final GET_STR = 'GET'
  static final GET_IDX = 1
  static final PUT_STR = 'PUT'
  static final PUT_IDX = 2
  static final POST_STR = 'POST'
  static final POST_IDX = 3
  static final DELETE_STR = 'DELETE'
  static final DELETE_IDX = 4
}

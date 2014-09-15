package org.motrice.signatrice

/**
 * Policy is a synonym for serviceId.
 * There is a set of predefined service ids for test.
 */
class SigPolicy implements Comparable {
  // Policy identifier
  String name
  
  static mapping = {
    id generator: 'assigned'
  }
  static constraints = {
    name size: 1..40, unique: true
  }

  static final Long LO_ID = 1
  static final Long HI_ID = 20
  static final Long INVALID_ID = 21

  /**
   * Create a single policy
   */
  static SigPolicy createPolicy(Long identity) {
    def policy = null
    if (!SigPolicy.get(identity)) {
      policy = createPolicy(identity, createName(identity))
    }

    return policy
  }

  static SigPolicy createPolicy(Long identity, String text) {
    def policy = null
    if (!SigPolicy.get(identity)) {
      policy = new SigPolicy(name: text)
      policy.id = identity
      policy.save(failOnError: true)
    }

    return policy
  }

  /**
   * Create all policies
   */
  static createPolicies() {
    (LO_ID..HI_ID).each {createPolicy(it)}
    createPolicy(INVALID_ID, 'invalid/policy')
  }

  private static String createName(Long identity) {
    "logtest${String.format('%03d', identity)}"
  }

  String toString() {
    name
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    name.hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof SigPolicy) {
      def other = (SigPolicy)obj
      result = name == other.name
    }

    return result
  }

  /**
   * Date-based comparison, latest first.
   */
  int compareTo(Object obj) {
    def other = (SigPolicy)obj
    name.compareTo(other.name)
  }

}

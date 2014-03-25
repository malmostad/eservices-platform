package org.motrice.coordinatrice

/**
 * The state of a process definition as defined by Motrice.
 */
class CrdProcdef {
  // The Activiti identity of the process definition
  String actid

  // Activiti process definition version (starting on 1)
  Integer actver

  // Activiti deployment id
  String actdepl

  // The Motrice state of the process definition
  CrdProcdefState state

  static mapping = {
    id name: 'actid', column: 'actid', generator: 'assigned'
    cache usage: 'read-write'
  }
  static constraints = {
    actid blank: false, maxSize: 64
    actver min: 1
    actdepl blank: false, maxSize: 64
  }

  String toString() {
    "${actid}[v${actver}]${state}"
  }

}

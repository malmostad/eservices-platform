package org.motrice.coordinatrice

/**
 * The state of a process definition as defined by Motrice.
 */
class CrdProcdef {
  // The Activiti identity of the process definition
  String actid

  // Process definition version (starting on 1)
  Integer rev

  // The Motrice state of the process definition
  CrdProcdefState state

  static constraints = {
    actid blank: false, maxSize: 64, unique: true
    rev min: 1
  }

  String toString() {
    "${actid}[v${rev}]${state}"
  }

}

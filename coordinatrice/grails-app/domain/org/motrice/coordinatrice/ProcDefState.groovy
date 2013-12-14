package org.motrice.coordinatrice

import org.activiti.engine.impl.persistence.entity.SuspensionState

/**
 * Class matching Activiti suspension state except that the id is Long
 */
class ProcDefState {

  // Message resource
  String res

  // Default message
  String defaultMessage

  static mapping = {
    table 'crd_procdef_state'
    id generator: 'assigned'
    cache usage: 'read-only'
  }
  static constraints = {
    res blank: false, maxSize: 80
    defaultMessage blank: false, maxSize: 80
  }

  static final Long STATE_ACTIVE_ID = SuspensionState.ACTIVE.stateCode as Long
  static final Long STATE_SUSPENDED_ID = SuspensionState.SUSPENDED.stateCode as Long
  static final String STATE_ACTIVE_RES = 'procDefState.active.label'
  static final String STATE_SUSPENDED_RES = 'procDefState.suspended.label'
  static final String STATE_ACTIVE_DEF = 'Active'
  static final String STATE_SUSPENDED_DEF = 'Suspended'

  // Method for making sure the states exist at bootstrap time
  // No other states may be created
  static ProcDefState createState(Long identity, String resource, String defMsg) {
    if (!ProcDefState.get(identity)) {
      def state = new ProcDefState(res: resource, defaultMessage: defMsg)
      state.id = identity
      state.save(failOnError: true)
    }
  }

}

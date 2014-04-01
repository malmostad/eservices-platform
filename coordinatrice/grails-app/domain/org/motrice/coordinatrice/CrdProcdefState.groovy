package org.motrice.coordinatrice

import org.activiti.engine.impl.persistence.entity.SuspensionState

/**
 * Class matching Activiti suspension state except that the id is Long
 */
class CrdProcdefState {

  // Message resource
  String res

  // Default message
  String defaultMessage

  // The following fields are for the sake of other modules.
  // In Coordinatrice they may be computed.

  // State to assign to the process in the Activiti process engine
  Long activitiState

  // Can the process be modified, connections and/or process definition?
  Boolean editable

  // May the process be started (new instance generated)?
  Integer startable

  static mapping = {
    id generator: 'assigned'
    cache usage: 'read-only'
  }
  static transients = ['stateChangeAllowed']
  static constraints = {
    res blank: false, maxSize: 80
    defaultMessage blank: false, maxSize: 80
    activitiState range: 0..200
    startable range: 1..3
  }

  // NOTE: The numeric definition of the Active and Suspended states is defined
  // by Activiti source code.
  static final Long STATE_ACTIVE_ID = SuspensionState.ACTIVE.stateCode as Long
  static final Long STATE_SUSPENDED_ID = SuspensionState.SUSPENDED.stateCode as Long
  static final Long STATE_EDIT_ID = 101;
  static final Long STATE_TRIAL_ID = 102;
  static final Long STATE_APPROVED_ID = 103;
  static final Long STATE_PUBLISHED_ID = 104;
  static final Long STATE_RETIRED_ID = 105;

  static final String STATE_ACTIVE_RES = 'procdefState.active.label'
  static final String STATE_SUSPENDED_RES = 'procdefState.suspended.label'
  static final String STATE_EDIT_RES = 'procdefState.edit.label'
  static final String STATE_TRIAL_RES = 'procdefState.trial.label'
  static final String STATE_APPROVED_RES = 'procdefState.approved.label'
  static final String STATE_PUBLISHED_RES = 'procdefState.published.label'
  static final String STATE_RETIRED_RES = 'procdefState.retired.label'

  static final String STATE_ACTIVE_DEF = 'Active'
  static final String STATE_SUSPENDED_DEF = 'Suspended'
  static final String STATE_EDIT_DEF = 'Edit'
  static final String STATE_TRIAL_DEF = 'Trial'
  static final String STATE_APPROVED_DEF = 'Approved'
  static final String STATE_PUBLISHED_DEF = 'Published'
  static final String STATE_RETIRED_DEF = 'Retired'

  static final Integer STARTABLE_FALSE = 1
  static final Integer STARTABLE_TEST = 2
  static final Integer STARTABLE_PRODUCTION = 3

  // Method for making sure the states exist at bootstrap time
  // No other states may be created
  static CrdProcdefState createState(Long identity, String resource, String defMsg) {
    if (!CrdProcdefState.get(identity)) {
      def state = new CrdProcdefState(res: resource, defaultMessage: defMsg)
      state.id = identity
      state.activitiState = state.computeActivitiState()
      state.editable = state.computeEditable()
      state.startable = state.computeStartable()
      state.save(failOnError: true)
    }
  }

  Long computeActivitiState() {
    Long result = null

    switch (id) {
    case STATE_TRIAL_ID:
    case STATE_APPROVED_ID:
    case STATE_PUBLISHED_ID:
    result = STATE_ACTIVE_ID
    break
    case STATE_EDIT_ID:
    case STATE_RETIRED_ID:
    result = STATE_SUSPENDED_ID
    break
    default:
    result = id
    }

    return result
  }

  /**
   * Is editing allowed?
   * NOTE: This is different from creating a new version which is the
   * start of editing a process definition.
   */
  boolean computeEditable() {
    id == STATE_EDIT_ID
  }

  Integer computeStartable() {
    def result = 0

    switch (id) {
    case STATE_TRIAL_ID:
    case STATE_APPROVED_ID:
    result = STARTABLE_TEST
    break
    case STATE_PUBLISHED_ID:
    case STATE_ACTIVE_ID:
    result = STARTABLE_PRODUCTION
    break
    default:
    result = STARTABLE_FALSE
    }

    return result
  }

  /**
   * Constraints for allowing state change.
   * Now state changes are always allowed.
   */
  boolean isStateChangeAllowed() {
    true
  }

  /**
   * Compute the possible next states.
   * Usually just a pair: promote, demote.
   * Return a list of state ids.
   */
  List nextStates() {
    def result = []

    switch (id) {
    case STATE_ACTIVE_ID:
    case STATE_SUSPENDED_ID:
    result = [STATE_EDIT_ID, STATE_TRIAL_ID]
    break;
    case STATE_EDIT_ID:
    result = [STATE_TRIAL_ID, STATE_APPROVED_ID]
    break
    case STATE_TRIAL_ID:
    result = [STATE_EDIT_ID, STATE_APPROVED_ID, STATE_PUBLISHED_ID]
    break
    case STATE_APPROVED_ID:
    result = [STATE_EDIT_ID, STATE_PUBLISHED_ID]
    break
    case STATE_PUBLISHED_ID:
    result = [STATE_RETIRED_ID]
    break
    case STATE_RETIRED_ID:
    result = [STATE_PUBLISHED_ID]
    break
    }

    return result
  }

  String startableToString() {
    def result = 'NotStartable'
    switch (startable) {
    case STARTABLE_TEST:
    result = 'TestOnly'
    break;
    case STARTABLE_PRODUCTION:
    result = 'Production'
    break;
    }

    return result
  }

  String toString() {
    defaultMessage
  }

}

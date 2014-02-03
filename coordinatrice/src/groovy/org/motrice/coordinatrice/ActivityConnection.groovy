package org.motrice.coordinatrice

/**
 * Class for decoding and encoding formpath in MtfActivityFormDefinition, a string.
 * The value indicates one of several connection states.
 */
class ActivityConnection {
  def messageSource

  // Unconnected
  public static Integer UNCONNECTED_STATE = 0
  public static String UNCONNECTED_MSG = 'activity.connection.state.unconnected'

  // Connect to a form
  public static Integer FORM_STATE = 1
  public static String FORM_MSG = 'activity.connection.state.form'

  // Connect to no form
  public static Integer NO_FORM_STATE = 2
  public static String NO_FORM_KEY = 'none'
  public static String NO_FORM_MSG = 'activity.connection.state.noform'

  // Connect to signing the start form
  public static Integer SIGN_START_FORM_STATE = 3
  public static String SIGN_START_FORM_KEY = 'signstartform'
  public static String SIGN_START_FORM_MSG = 'activity.connection.state.signstartform'

  // Connect to signing an activity
  public static Integer SIGN_ACTIVITY_STATE = 4
  public static String SIGN_ACTIVITY_KEY = 'signactivity'
  public static String SIGN_ACTIVITY_MSG = 'activity.connection.state.signactivity'

  // (Future:) Connect to signing an activity
  public static Integer PAY_ACTIVITY_STATE = 5
  public static String PAY_ACTIVITY_KEY = 'payactivity'
  public static String PAY_ACTIVITY_MSG = 'activity.connection.state.payactivity'

  // State of this connection, one of the STATE constants
  Integer state

  // The path of this connection, or null.
  // The following states have a path:
  // FORM, SIGN_ACTIVITY, PAY_ACTIVITY
  String path

  // The ActDef instance to which this connection belongs
  ActDef activity

  /**
   * Construct from an activity definition and an existing formpath
   */
  ActivityConnection(ActDef activity, String formpath) {
    this.activity = activity
    if (formpath == null) {
      state = UNCONNECTED_STATE
      path = null
    } else {
      def idx = formpath.indexOf('/')
      def head = (idx >= 0)? formpath.substring(0, idx) : formpath
      def tail = (idx >= 0)? formpath.substring(idx + 1) : ''
      switch (head) {
      case NO_FORM_KEY:
      state = NO_FORM_STATE
      path = null
      break
      case SIGN_START_FORM_KEY:
      state = SIGN_START_FORM_STATE
      path = null
      break
      case SIGN_ACTIVITY_KEY:
      state = SIGN_ACTIVITY_STATE
      path = tail
      break
      case PAY_ACTIVITY_STATE:
      state = PAY_ACTIVITY_STATE
      path = tail
      break
      default:
      state = FORM_STATE
      path = formpath
      }
    }
  }

  /**
   * Construct from an activity definition and activity connection edit input
   * Arguments reversed to avoid overloading conflicts.
   */
  ActivityConnection(ActivityConnectionCommand command, ActDef activity) {
    this.activity = activity
    // This happens if no radio button is selected
    if (command.form && command.connectionState == null) {
      command.connectionState = ActivityConnection.FORM_STATE
    }
    state = command.connectionState
    
    switch (state) {
    case FORM_STATE:
    path = command?.form?.path
    break
    case NO_FORM_STATE:
    path = NO_FORM_KEY
    break
    case SIGN_START_FORM_STATE:
    path = SIGN_START_FORM_KEY
    break
    case SIGN_ACTIVITY_STATE:
    case PAY_ACTIVITY_STATE:
    path = activity?.fullId?.toString()
    break
    default:
    // No connection specified
    path = null
    }
  }

  static String unconnected() {
    UNCONNECTED_MSG
  }

  /**
   * Get a list of state label codes and values (used for radio buttons)
   * Return a map with the following entries:
   * codes: List of String containing state label message codes
   * values: List of Integer containing state integer codes
   */
  Map stateInfo() {
    def map = [:]
    map.codes = [FORM_MSG, NO_FORM_MSG, SIGN_START_FORM_MSG, SIGN_ACTIVITY_MSG]
    map.values = [FORM_STATE, NO_FORM_STATE, SIGN_START_FORM_STATE, SIGN_ACTIVITY_STATE]
    return map
  }

  boolean isFormState() {
    state == FORM_STATE
  }

  boolean isNoFormState() {
    state == NO_FORM_STATE
  }

  boolean isSignStartFormState() {
    state == SIGN_START_FORM_STATE
  }

  boolean isSignActivityState() {
    state == SIGN_ACTIVITY_STATE
  }

  boolean isPayActivityState() {
    state == PAY_ACTIVITY_STATE
  }

  /**
   * Convert state to a message key
   */
  String msgKey() {
    switch (state) {
    case UNCONNECTED_STATE:
    UNCONNECTED_MSG
    break
    case FORM_STATE:
    FORM_MSG
    break
    case NO_FORM_STATE:
    NO_FORM_MSG
    break
    case SIGN_START_FORM_STATE:
    SIGN_START_FORM_MSG
    break
    case SIGN_ACTIVITY_STATE:
    SIGN_ACTIVITY_MSG
    break
    case PAY_ACTIVITY_STATE:
    PAY_ACTIVITY_MSG
    default:
    'activity.connection.state.unknown'
    }
  }

  /**
   * Convert to a display string
   */
  String toDisplay() {
    switch (state) {
    case FORM_STATE:
    path
    break
    case SIGN_ACTIVITY_STATE:
    case PAY_ACTIVITY_STATE:
    ActDef.fullId.toString()
    break
    default:
    ''
    }
  }

  /**
   * Convert to a string suitable as formpath value
   */
  String toString() {
    switch (state) {
    case UNCONNECTED_STATE:
    ''
    break
    case FORM_STATE:
    path
    break
    case NO_FORM_STATE:
    NO_FORM_KEY
    break
    case SIGN_START_FORM_STATE:
    SIGN_START_FORM_KEY
    break
    case SIGN_ACTIVITY_STATE:
    "${SIGN_ACTIVITY_KEY}/${path}"
    break
    case PAY_ACTIVITY_STATE:
    "${PAY_ACTIVITY_KEY}/${path}"
    default:
    '***UNKNOWN***'
    }
  }

}
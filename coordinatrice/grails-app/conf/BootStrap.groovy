import org.motrice.coordinatrice.ProcDefState
import org.motrice.coordinatrice.TaskType

class BootStrap {

  def init = { servletContext ->
    // Make sure all process definition states exist
    ProcDefState.createState(ProcDefState.STATE_ACTIVE_ID,
			     ProcDefState.STATE_ACTIVE_RES,
			     ProcDefState.STATE_ACTIVE_DEF)
    ProcDefState.createState(ProcDefState.STATE_SUSPENDED_ID,
			     ProcDefState.STATE_SUSPENDED_RES,
			     ProcDefState.STATE_SUSPENDED_DEF)

    // Make sure all task types exist
    TaskType.createType(TaskType.TYPE_BUSINESS_RULE_ID,
			TaskType.TYPE_BUSINESS_RULE_RES,
			TaskType.TYPE_BUSINESS_RULE_DEF)
    TaskType.createType(TaskType.TYPE_MANUAL_ID,
			TaskType.TYPE_MANUAL_RES,
			TaskType.TYPE_MANUAL_DEF)
    TaskType.createType(TaskType.TYPE_RECEIVE_ID,
			TaskType.TYPE_RECEIVE_RES,
			TaskType.TYPE_RECEIVE_DEF)
    TaskType.createType(TaskType.TYPE_SCRIPT_ID,
			TaskType.TYPE_SCRIPT_RES,
			TaskType.TYPE_SCRIPT_DEF)
    TaskType.createType(TaskType.TYPE_SEND_ID,
			TaskType.TYPE_SEND_RES,
			TaskType.TYPE_SEND_DEF)
    TaskType.createType(TaskType.TYPE_SERVICE_ID,
			TaskType.TYPE_SERVICE_RES,
			TaskType.TYPE_SERVICE_DEF)
    TaskType.createType(TaskType.TYPE_USER_ID,
			TaskType.TYPE_USER_RES,
			TaskType.TYPE_USER_DEF)
    
  }
  def destroy = {
  }
}

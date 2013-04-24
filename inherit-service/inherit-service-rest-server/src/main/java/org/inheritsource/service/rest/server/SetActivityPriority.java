package org.inheritsource.service.rest.server;

import org.inheritsource.service.common.domain.ActivityWorkflowInfo;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.ServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;


public class SetActivityPriority extends ServerResource {
	
	public static final String ACTION_ASSIGN = "assign";
	public static final String ACTION_ADD_CANDIDATE = "addcandidate";
	public static final String ACTION_REMOVE_CANDIDATE = "removecandidate";
	public static final String ACTION_UNASSIGN = "unassign";
	
	TaskFormService engine = new TaskFormService();
	
	@Get
	@Post
	public ActivityWorkflowInfo setActivityPriority() {
		ActivityWorkflowInfo result = null;
		
		String activityInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("activityInstanceUuid"));
		int priority = Integer.parseInt(ParameterEncoder.decode((String)getRequestAttributes().get("priority")));
		
		result = engine.setActivityPriority(activityInstanceUuid, priority);
		
		return result;
	}
}

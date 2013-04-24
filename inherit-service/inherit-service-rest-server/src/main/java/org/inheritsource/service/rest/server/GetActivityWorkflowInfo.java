package org.inheritsource.service.rest.server;

import org.inheritsource.service.common.domain.ActivityWorkflowInfo;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.ServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;


public class GetActivityWorkflowInfo extends ServerResource {
	
	public static final String ACTION_ASSIGN = "assign";
	public static final String ACTION_ADD_CANDIDATE = "addcandidate";
	public static final String ACTION_UNASSIGN = "unassign";
	
	TaskFormService engine = new TaskFormService();
	
	@Get
	@Post
	public ActivityWorkflowInfo getActivityWorkflowInfo() {
		ActivityWorkflowInfo result = null;
		
		String activityInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("activityInstanceUuid"));
		
		result = engine.getActivityWorkflowInfo(activityInstanceUuid);
		
		return result;
	}
}

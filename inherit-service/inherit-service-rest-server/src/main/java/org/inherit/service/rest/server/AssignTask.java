package org.inherit.service.rest.server;

import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.ServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public class AssignTask extends ServerResource {
	
	public static final String ACTION_ASSIGN = "assign";
	public static final String ACTION_ADD_CANDIDATE = "addcandidate";
	public static final String ACTION_UNASSIGN = "unassign";
	
	TaskFormService engine = new TaskFormService();
	
	@Get
	@Post
	public int assignTask() {
		int result = 0;
		
		String activityInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("activityInstanceUuid"));
		String action = ParameterEncoder.decode((String)getRequestAttributes().get("action"));
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userid"));
		
		if (ACTION_ASSIGN.equals(action)) {
			result = engine.assignTask(activityInstanceUuid, userId);
		}
		else if (ACTION_ADD_CANDIDATE.equals(action)) {
			result = engine.addCandidate(activityInstanceUuid, userId);
		}
		else if (ACTION_UNASSIGN.equals(action)) {
			result = engine.unassignTask(activityInstanceUuid);
		}
		
		return result;
	}
}

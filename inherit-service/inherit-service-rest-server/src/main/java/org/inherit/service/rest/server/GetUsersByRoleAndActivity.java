package org.inherit.service.rest.server;

import java.util.Set;

import org.inherit.service.common.domain.ActivityWorkflowInfo;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.ServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public class GetUsersByRoleAndActivity extends ServerResource {
	
	TaskFormService engine = new TaskFormService();
	
	@Get
	@Post
	public Set<String> getUsersByRoleAndActivity() {
		
		String roleName = ParameterEncoder.decode((String)getRequestAttributes().get("roleName"));
		
		String activityInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("activityInstanceUuid"));
		
		Set<String> result = engine.getUsersByRoleAndActivity(roleName, activityInstanceUuid);
		
		return result;
	}
}

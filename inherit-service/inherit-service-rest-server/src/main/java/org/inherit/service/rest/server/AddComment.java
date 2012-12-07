package org.inherit.service.rest.server;

import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class AddComment extends ServerResource {
	TaskFormService engine = new TaskFormService();
	
	@Post
	public int addComment() {
		String activityInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("activityInstanceUuid"));
		String comment = ParameterEncoder.decode((String)getRequestAttributes().get("comment"));
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userid"));
		
		int result = engine.addComment(activityInstanceUuid, comment, userId);
		
		return result;
	}
}

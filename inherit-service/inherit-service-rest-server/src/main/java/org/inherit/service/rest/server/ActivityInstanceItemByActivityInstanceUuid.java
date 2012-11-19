package org.inherit.service.rest.server;

import java.util.logging.Logger;

import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class ActivityInstanceItemByActivityInstanceUuid extends ServerResource {

	public static final Logger log = Logger.getLogger(ActivityInstanceItemByActivityInstanceUuid.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public ActivityInstanceItem getProcessInstanceDetailsByActivityInstance() {
		String activityInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("activityInstanceUuid"));
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userId"));
		
		log.fine("REST getProcessInstanceDetailsByActivityInstance with parameter activityInstanceUuid=[" + activityInstanceUuid + "]" );
		
		return engine.getActivityInstanceItem(activityInstanceUuid, userId);
	}
}

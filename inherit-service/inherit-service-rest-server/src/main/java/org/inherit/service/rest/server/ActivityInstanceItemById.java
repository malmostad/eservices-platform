package org.inherit.service.rest.server;

import java.util.logging.Logger;

import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class ActivityInstanceItemById extends ServerResource {

	public static final Logger log = Logger.getLogger(ActivityInstanceItemById.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public ActivityInstanceItem getActivityInstanceItemByFormPath() {
		String processActivityFormInstanceId = ParameterEncoder.decode((String)getRequestAttributes().get("processActivityFormInstanceId"));
		log.fine("REST call with parameter processActivityFormInstanceId=[" + processActivityFormInstanceId + "]" );
		
		ActivityInstanceItem result = null;
		
		Long id = null;
		try {
			id = Long.decode(processActivityFormInstanceId);
			result = engine.getActivityInstanceItem(id);
		}
		catch (NumberFormatException nfe) {
			log.info("processActivityFormInstanceId=[" + processActivityFormInstanceId + "] is not a valid id");
		}		
		
		return result;
	}
} 

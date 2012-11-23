package org.inherit.service.rest.server;

import java.util.logging.Logger;

import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class StartActivityInstanceItemByFormPath extends ServerResource {

	public static final Logger log = Logger.getLogger(StartActivityInstanceItemByFormPath.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public ActivityInstanceItem getStartActivityInstanceItemByFormPath() {
		String formPath = ParameterEncoder.decode((String)getRequestAttributes().get("formPath"));
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userId"));
		
		log.fine("REST call with parameter formPath=[" + formPath + "]" );
		
		return engine.getStartActivityInstanceItem(formPath, userId);
	}
}
package org.inherit.service.rest.server;

import java.util.logging.Logger;

import org.inherit.service.common.domain.DashOpenActivities;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class DashOpenActivitiesByUserId extends ServerResource {

	public static final Logger log = Logger.getLogger(DashOpenActivitiesByUserId.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Get
	@Post
	public DashOpenActivities getUserInstancesList() {
		String userid = ParameterEncoder.decode((String)getRequestAttributes().get("userid"));
		String remainingDaysStr = ParameterEncoder.decode((String)getRequestAttributes().get("remainingDays"));
		
		int remainingDays = Integer.parseInt(remainingDaysStr);
		
		log.fine("REST getUserInstancesList with parameter userid=[" + userid + "] remainingDays=[" + remainingDaysStr + "]" );
		
		return engine.getDashOpenActivitiesByUserId(userid, remainingDays);
	}
}

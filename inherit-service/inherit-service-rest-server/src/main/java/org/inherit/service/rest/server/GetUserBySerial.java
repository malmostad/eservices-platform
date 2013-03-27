package org.inherit.service.rest.server;

import java.util.logging.Logger;

import org.inherit.service.common.domain.DashOpenActivities;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class GetUserBySerial extends ServerResource {

	public static final Logger log = Logger.getLogger(GetUserBySerial.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Get
	@Post
	public UserInfo getUserBySerial() {
		String serial = ParameterEncoder.decode((String)getRequestAttributes().get("serial"));
		String certificateSubject = ParameterEncoder.decode((String)getRequestAttributes().get("certificateSubject"));
				
		log.fine("REST getUserBySerial with parameter serial=[" + serial + "] certificateSubject=[" + certificateSubject + "]" );
		
		return engine.getUserBySerial(serial, certificateSubject);
	}
}

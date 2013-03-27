package org.inherit.service.rest.server;

import java.util.logging.Logger;

import org.inherit.service.common.domain.DashOpenActivities;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class GetUserByDn extends ServerResource {

	public static final Logger log = Logger.getLogger(GetUserByDn.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Get
	@Post
	public UserInfo getUserByDn() {
		String dn = ParameterEncoder.decode((String)getRequestAttributes().get("dn"));
				
		log.fine("REST getUserByDn with parameter dn=[" + dn + "]" );
		
		return engine.getUserByDn(dn);
	}
}

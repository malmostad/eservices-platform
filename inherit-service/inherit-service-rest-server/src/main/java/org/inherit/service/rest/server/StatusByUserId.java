package org.inherit.service.rest.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import se.inherit.service.bonita.BonitaEngineServiceImpl;

public class StatusByUserId extends ServerResource {

	public static final Logger log = Logger.getLogger(StatusByUserId.class.getName());
	
	BonitaEngineServiceImpl engine = new BonitaEngineServiceImpl();
	
	@Post
	public ArrayList<ProcessInstanceListItem> getUserInstancesList() {
		String userid = (String)getRequestAttributes().get("userid");
		
		log.fine("REST getUserInstancesList with parameter userid=[" + userid + "]" );
		
		return engine.getUserInstancesList(userid);
	}
}

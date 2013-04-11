package org.inherit.service.rest.server;

import java.util.List;
import java.util.logging.Logger;

import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class SearchProcessInstancesStartedByUser extends ServerResource {

	public static final Logger log = Logger.getLogger(SearchProcessInstancesStartedByUser.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public List<ProcessInstanceListItem> searchProcessInstancesStartedByUser() {
		String searchForUserId = ParameterEncoder.decode((String)getRequestAttributes().get("searchForUserId"));
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userId"));
		
		log.fine("REST getUserInstancesList with parameter searchForUserId=[" + searchForUserId + "] userId=[" + userId + "]");
		
		return engine.searchProcessInstancesStartedByUser(searchForUserId, userId);
	}
}

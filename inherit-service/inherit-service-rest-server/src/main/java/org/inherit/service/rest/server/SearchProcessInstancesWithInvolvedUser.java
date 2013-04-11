package org.inherit.service.rest.server;

import java.util.logging.Logger;

import org.inherit.service.common.domain.PagedProcessInstanceSearchResult;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class SearchProcessInstancesWithInvolvedUser extends ServerResource {

	public static final Logger log = Logger.getLogger(SearchProcessInstancesWithInvolvedUser.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public PagedProcessInstanceSearchResult searchProcessInstancesWithInvolvedUser() {
		String searchForUserId = ParameterEncoder.decode((String)getRequestAttributes().get("searchForUserId"));
		String fromIndexStr = ParameterEncoder.decode((String)getRequestAttributes().get("fromIndex"));
		String pageSizeStr = ParameterEncoder.decode((String)getRequestAttributes().get("pageSize"));
		String sortBy = ParameterEncoder.decode((String)getRequestAttributes().get("sortBy"));
		String sortOrder = ParameterEncoder.decode((String)getRequestAttributes().get("sortOrder"));		
		String filter = ParameterEncoder.decode((String)getRequestAttributes().get("filter"));		
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userId"));
		
		int fromIndex = Integer.parseInt(fromIndexStr);
		int pageSize = Integer.parseInt(pageSizeStr);
		
		log.fine("REST searchProcessInstancesWithInvolvedUser with parameter searchForUserId=[" + searchForUserId + "] fromIndex=[" + fromIndex + "] pageSize=[" + pageSize + "] sortBy=["  + sortBy + "] sortOrder=[" + sortOrder + "] filter=[" + filter + "] userId=[" + userId + "]");
		
		return engine.searchProcessInstancesWithInvolvedUser(searchForUserId, fromIndex, pageSize, sortBy, sortOrder, filter, userId);
	}
}

package org.inheritsource.service.rest.server;

import java.util.logging.Logger;

import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;



public class SearchProcessInstancesStartedByUser extends ServerResource {

	public static final Logger log = Logger.getLogger(SearchProcessInstancesStartedByUser.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public PagedProcessInstanceSearchResult searchProcessInstancesStartedByUser() {
		String searchForUserId = ParameterEncoder.decode((String)getRequestAttributes().get("searchForUserId"));
		String fromIndexStr = ParameterEncoder.decode((String)getRequestAttributes().get("fromIndex"));
		String pageSizeStr = ParameterEncoder.decode((String)getRequestAttributes().get("pageSize"));
		String sortBy = ParameterEncoder.decode((String)getRequestAttributes().get("sortBy"));
		String sortOrder = ParameterEncoder.decode((String)getRequestAttributes().get("sortOrder"));		
		String filter = ParameterEncoder.decode((String)getRequestAttributes().get("filter"));		
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userId"));
		
		int fromIndex = Integer.parseInt(fromIndexStr);
		int pageSize = Integer.parseInt(pageSizeStr);
		
		log.fine("REST searchProcessInstancesStartedByUser with parameter searchForUserId=[" + searchForUserId + "] fromIndex=[" + fromIndex + "] pageSize=[" + pageSize + "] sortBy=["  + sortBy + "] sortOrder=[" + sortOrder + "] filter=[" + filter + "] userId=[" + userId + "]");
		
		return engine.searchProcessInstancesStartedByUser(searchForUserId, fromIndex, pageSize, sortBy, sortOrder, filter, userId);

	}
}

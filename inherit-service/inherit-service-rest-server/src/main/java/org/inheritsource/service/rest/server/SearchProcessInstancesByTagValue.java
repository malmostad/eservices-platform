package org.inheritsource.service.rest.server;

import java.util.logging.Logger;

import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;



public class SearchProcessInstancesByTagValue extends ServerResource {

	public static final Logger log = Logger.getLogger(SearchProcessInstancesByTagValue.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public PagedProcessInstanceSearchResult searchProcessInstancesByTagValue() {
		String tagValue = ParameterEncoder.decode((String)getRequestAttributes().get("tagValue"));
		String fromIndexStr = ParameterEncoder.decode((String)getRequestAttributes().get("fromIndex"));
		String pageSizeStr = ParameterEncoder.decode((String)getRequestAttributes().get("pageSize"));
		String sortBy = ParameterEncoder.decode((String)getRequestAttributes().get("sortBy"));
		String sortOrder = ParameterEncoder.decode((String)getRequestAttributes().get("sortOrder"));		
		String filter = ParameterEncoder.decode((String)getRequestAttributes().get("filter"));		
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userId"));

		int fromIndex = Integer.parseInt(fromIndexStr);
		int pageSize = Integer.parseInt(pageSizeStr);
		
		log.fine("REST SearchProcessInstancesByTagValue with parameter tagValue=[" + tagValue + "] fromIndex=[" + fromIndex + "] pageSize=[" + pageSize + "] sortBy=["  + sortBy + "] sortOrder=[" + sortOrder + "] filter=[" + filter + "] userId=[" + userId + "]");
		
		return engine.searchProcessInstancesListByTag(tagValue, fromIndex, pageSize, sortBy, sortOrder, filter, userId);
	}
}

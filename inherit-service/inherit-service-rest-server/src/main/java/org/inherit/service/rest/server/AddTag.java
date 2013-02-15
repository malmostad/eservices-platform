package org.inherit.service.rest.server;

import java.util.logging.Logger;

import org.inherit.service.common.domain.ActivityWorkflowInfo;
import org.inherit.service.common.domain.Tag;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.ServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public class AddTag extends ServerResource {
		
	public static final Logger log = Logger.getLogger(AddTag.class.getName());
			
	TaskFormService engine = new TaskFormService();
	
	@Get
	@Post
	public Tag addTag() {
		Tag result = null;
		
		String processActivityFormInstanceIdStr = ParameterEncoder.decode((String)getRequestAttributes().get("processActivityFormInstanceId"));
		String tagTypeIdStr = ParameterEncoder.decode((String)getRequestAttributes().get("tagTypeId"));
		String value = ParameterEncoder.decode((String)getRequestAttributes().get("value"));
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userid"));
		
		Long processActivityFormInstanceId = null;
		Long tagTypeId = null;
		try {
			processActivityFormInstanceId = Long.decode(processActivityFormInstanceIdStr);
			tagTypeId = Long.decode(tagTypeIdStr);
			result = engine.addTag(processActivityFormInstanceId, tagTypeId, value, userId);
		}
		catch (NumberFormatException nfe) {
			log.info("processActivityFormInstanceId=[" + processActivityFormInstanceIdStr + "] and/or tagTypeId=[" + tagTypeIdStr + "] is not a valid id");
		}		
		
		return result;
	}
}

package org.inheritsource.service.rest.server;

import java.util.List;
import java.util.logging.Logger;

import org.inheritsource.service.common.domain.Tag;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;



public class GetTagsByProcessInstance extends ServerResource {

	public static final Logger log = Logger.getLogger(GetTagsByProcessInstance.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public List<Tag> getTagsByProcessInstance() {
		
		String processInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("processInstanceUuid"));
		
		log.fine("REST getTagsByProcessInstance with parameter processInstanceUuid=[" + processInstanceUuid +  "]" );
		
		return engine.getTagsByProcessInstance(processInstanceUuid); 
	}
}

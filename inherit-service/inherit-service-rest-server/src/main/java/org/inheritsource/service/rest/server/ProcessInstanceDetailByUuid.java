package org.inheritsource.service.rest.server;

import java.util.logging.Logger;

import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;



public class ProcessInstanceDetailByUuid extends ServerResource {

	public static final Logger log = Logger.getLogger(ProcessInstanceDetailByUuid.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public ProcessInstanceDetails getProcessInstanceDetails() {
		String processInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("processInstanceUuid"));
		
		log.fine("REST getProcessInstanceDetails with parameter processInstanceUuid=[" + processInstanceUuid + "]" );
		
		return engine.getProcessInstanceDetails(processInstanceUuid);
	}
}

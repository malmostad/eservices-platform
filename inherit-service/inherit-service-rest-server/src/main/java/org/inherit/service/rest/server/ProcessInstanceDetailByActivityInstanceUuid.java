package org.inherit.service.rest.server;

import java.util.logging.Logger;

import org.inherit.bonita.client.BonitaEngineServiceImpl;
import org.inherit.service.common.domain.ProcessInstanceDetails;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class ProcessInstanceDetailByActivityInstanceUuid extends ServerResource {

	public static final Logger log = Logger.getLogger(ProcessInstanceDetailByActivityInstanceUuid.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public ProcessInstanceDetails getProcessInstanceDetailsByActivityInstance() {
		String activityInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("activityInstanceUuid"));
		
		log.fine("REST getProcessInstanceDetailsByActivityInstance with parameter activityInstanceUuid=[" + activityInstanceUuid + "]" );
		
		return engine.getProcessInstanceDetailsByActivityInstance(activityInstanceUuid);
	}
}

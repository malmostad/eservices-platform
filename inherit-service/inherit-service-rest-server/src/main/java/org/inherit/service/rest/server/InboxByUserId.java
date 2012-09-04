package org.inherit.service.rest.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.inherit.bonita.client.BonitaEngineServiceImpl;
import org.inherit.service.common.domain.InboxTaskItem;
import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class InboxByUserId extends ServerResource{

	public static final Logger log = Logger.getLogger(InboxByUserId.class.getName());
	
	TaskFormService engine = new TaskFormService();
	
	@Post
	public List<InboxTaskItem> getInboxTaskList() {
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userid"));
		
		log.fine("REST getUserInstancesList with parameter userid=[" + userId + "]" );
		
		return engine.getInboxTaskItems(userId);
	}
}

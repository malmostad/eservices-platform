package org.inherit.service.rest.server;

import java.util.Date;
import java.util.logging.Logger;

import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class SubmitForm extends ServerResource {

	public static final Logger log = Logger.getLogger(SubmitForm.class
			.getName());

	TaskFormService engine = new TaskFormService();

	@Post
	public String submitForm() {
		String docId = ParameterEncoder.decode((String) getRequestAttributes().get("docId"));
		String userId = ParameterEncoder.decode((String) getRequestAttributes().get("userId"));

		log.fine("REST getUserInstancesList with parameter userid=[" + userId
				+ "]");

		return engine.submitActivityForm(docId, userId);
	}

}

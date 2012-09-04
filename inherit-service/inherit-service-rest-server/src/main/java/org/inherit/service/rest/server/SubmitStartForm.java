package org.inherit.service.rest.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;

import org.inherit.service.common.util.ParameterEncoder;
import org.inherit.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class SubmitStartForm extends ServerResource {

	public static final Logger log = Logger.getLogger(SubmitStartForm.class
			.getName());

	TaskFormService engine = new TaskFormService();

	@Post
	public String submitStartForm() {

		String formPath;

		formPath = ParameterEncoder.decode((String) getRequestAttributes().get("formPath"));
		String docId = ParameterEncoder.decode((String) getRequestAttributes().get("docId"));
		String userId = ParameterEncoder.decode((String) getRequestAttributes().get("userId"));

		log.fine("REST getUserInstancesList with parameter userid=[" + userId
				+ "]");

		return engine.submitStartForm(formPath, docId, userId);
	}

	

}

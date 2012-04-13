package org.inherit.service.rest.server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.ext.xstream.XstreamConverter;

public class InheritServiceApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		
		router.attach("/statusByUserId/{userid}", StatusByUserId.class);
		router.attach("/inboxByUserId/{userid}", InboxByUserId.class);
		/* 
		router.attach("/bonita/{userid}/{password}", BonitaIdentityKey.class); 
		 
		router.attach("/processDefinition/{processDefinitionUUID}", BonitaProcessDefinition.class); 
		router.attach("/bonitaStartProcess/{formPath}/{userid}/{formId}/{docId}", StartBonitaProcess.class); 
		*/
		return router;
	}
	 
}

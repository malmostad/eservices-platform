package org.inherit.service.rest.server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class InheritServiceApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		
		router.attach("/statusByUserId/{userid}", StatusByUserId.class);
		router.attach("/inboxByUserId/{userid}", InboxByUserId.class);
		router.attach("/submitStartForm/{formPath}/{docId}/{userId}", SubmitStartForm.class);
		router.attach("/processInstanceDetailsByUuid/{processInstanceUuid}", ProcessInstanceDetailByUuid.class); 
		router.attach("/processInstanceDetailsByActivityInstanceUuid/{activityInstanceUuid}", ProcessInstanceDetailByActivityInstanceUuid.class); 
		
		//TODO kolla om det går att få bort
		router.attach("/bonitaIdentityKey/{userid}/{password}", BonitaIdentityKey.class); 
		
		return router;
	}
	 
}

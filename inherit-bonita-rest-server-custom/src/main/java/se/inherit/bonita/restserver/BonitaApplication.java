package se.inherit.bonita.restserver;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.ext.xstream.XstreamConverter;

public class BonitaApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		
		router.attach("/bonita/{userid}/{password}", BonitaIdentityKey.class); 
		router.attach("/bonitaStatusByUserId/{userid}", BonitaStatusByUserId.class); 
		router.attach("/processDefinition/{processDefinitionUUID}", BonitaProcessDefinition.class); 
		
		return router;
	}
	 
}

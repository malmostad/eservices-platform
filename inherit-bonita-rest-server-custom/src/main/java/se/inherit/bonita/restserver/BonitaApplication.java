package se.inherit.bonita.restserver;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class BonitaApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		
		router.attach("/bonita/{userid}/{password}", BonitaIdentityKey.class); //.extractQuery("userid", "userid", true);
		router.attach("/bonitaStatusByUserId/{userid}", BonitaStatusByUserId.class); //.extractQuery("userid", "userid", true);
		
		return router;
	}
	
}

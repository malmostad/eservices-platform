package org.inherit.service.rest.server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class InheritServiceApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		
		router.attach("/assignTask/{activityInstanceUuid}/{action}/{userid}", AssignTask.class); 
		router.attach("/getActivityInstanceItemById/{processActivityFormInstanceId}", ActivityInstanceItemById.class);
		router.attach("/getDashOpenActivitiesByUserId/{userid}/{remainingDays}", DashOpenActivitiesByUserId.class);
		router.attach("/getStartActivityInstanceItem/{formPath}/{userId}", StartActivityInstanceItemByFormPath.class);
		router.attach("/processInstanceDetailsByActivityInstanceUuid/{activityInstanceUuid}", ProcessInstanceDetailByActivityInstanceUuid.class); 
		router.attach("/processInstanceDetailsByUuid/{processInstanceUuid}", ProcessInstanceDetailByUuid.class); 
		
		// TODO take user from login instead of parameter userid
		router.attach("/addComment/{activityInstanceUuid}/{comment}/{userid}", AddComment.class); 
		router.attach("/getActivityInstanceItem/{activityInstanceUuid}/{userId}", ActivityInstanceItemByActivityInstanceUuid.class);
		router.attach("/inboxByUserId/{userid}", InboxByUserId.class);
		router.attach("/statusByUserId/{userid}", StatusByUserId.class);
		router.attach("/submitForm/{docId}/{userId}", SubmitForm.class);
		router.attach("/submitStartForm/{formPath}/{docId}/{userId}", SubmitStartForm.class);
		
		return router;
	}
	 
}

package org.inheritsource.service.rest.server.services;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;

@Component
@Path("/runtime")
public class RuntimeService {
	
	public static final Logger log = Logger.getLogger(RuntimeService.class.getName());
		
	@Autowired
	TaskFormService engine;	
	
	/**
	 * Get a start form ActivityInstanceItem. Initialize a new one if it 
	 * does not exist or continue with a previous partially filled in form. 
	 * 
	 * @param formPath
	 * @param userId
	 * @return
	 */
	@POST
    @Path("/activities/startActivityInstance")
    @Produces({"application/xml","application/json"})
	@Consumes("application/x-www-form-urlencoded")
	public ActivityInstanceItem getStartActivityInstanceItemByFormPath(@FormParam("formPath") String formPath, @FormParam("userId") String userId) {
		ActivityInstanceItem result = null;
		
		log.fine("REST call with parameter formPath=[" + formPath + "] by userId=[" + userId + "]");
		
		try {
			result = engine.getStartActivityInstanceItem(formPath, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.severe(e.toString());
		}
		return result;
	}
	
	/**
	 * Get user with a known inherit platform user uuid
	 * @param userUuid
	 * @return
	 */
	@POST
    @Path("/activities/byforminstance/{userUuid}")
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
	public ActivityInstanceItem getActivityInstanceItemByActivityInstanceUuid(Long processActivityFormInstanceId) {
		return null;
	}
	
	/**
	 * Get user with a known inherit platform user uuid
	 * @param userUuid
	 * @return
	 */
	@POST
    @Path("/activities/{activityInstanceId}")
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
	public ActivityInstanceItem getActivityInstanceItemByActivityInstanceId(String activityInstanceId) {
		return null;
	}
	
}

package se.inherit.portal.jaxrs;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.hippoecm.hst.jaxrs.services.AbstractResource;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service interface to execute AJAX requests, emeddded in site module 
 * in order to use the same authentication and session as the site. 
 * The JAAS authenticated userName will be used to check if the logged in
 * user is authorized to perform an operation.
 * @author bjmo
 *
 */
@Path("/site-ajax/")
public class SiteAjaxApplication extends AbstractResource {

	public static final Logger log = LoggerFactory.getLogger(SiteAjaxApplication.class);
			
	public static int GENERAL_ERROR = -1000;
	public static int PERMISSION_DENIED_ERROR = -1001;
	
	/*
	@RolesAllowed(value = { "everybody" })
	@GET
	@Path("/addComment/{comment}/")
	public String addComment(
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@Context UriInfo uriInfo,
            @PathParam("comment") String productType) {
		// do nothing for now...
		return "2";
	}
*/

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/addComment") // /{activityInstanceUuid}/{comment}/
	public int addComment (
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@FormParam("activityInstanceUuid") String activityInstanceUuid,
			@FormParam("comment") String comment) {
		
		int result = GENERAL_ERROR;
				
		String userId = getUserName(servletRequest);

		log.error("activityInstanceUuid: " + activityInstanceUuid);
		log.error("comment: " + comment);
		log.error("userId: " + userId);

		if (userId != null) {
			InheritServiceClient isc = new InheritServiceClient();
			result = isc.addComment(activityInstanceUuid, comment, userId);
		}
		else {
			result = PERMISSION_DENIED_ERROR;
		}
		
		return result; 
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/assignTask") //{activityInstanceUuid}/{action}/{targetUserId}")
	public int assignTask(
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@FormParam("activityInstanceUuid") String activityInstanceUuid,
			@FormParam("action") String action,
			@FormParam("targetUserId") String targetUserId) {
		
		int result = GENERAL_ERROR;
		
		String userId = getUserName(servletRequest);
		
		log.error("activityInstanceUuid: " + activityInstanceUuid);
		log.error("comment: " + action);
		log.error("targetUserId: " + targetUserId);
		log.error("userId: " + userId);
		
		if (userId != null) {
			// user is authenticated
			
			// TODO check if user is authorized to assign task???
			InheritServiceClient isc = new InheritServiceClient();
			result = isc.assignTask(activityInstanceUuid, action, targetUserId);
		}
		else {
			result = PERMISSION_DENIED_ERROR;
		}
		
		return result; 
	}
		


	private String getUserName(HttpServletRequest servletRequest) {
		String userName = null;
		Principal principal = servletRequest.getUserPrincipal();
		if (principal != null) {
			userName = principal.getName();
		}
		return userName;
	}
}

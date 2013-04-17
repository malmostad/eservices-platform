package se.inherit.portal.jaxrs;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.hippoecm.hst.jaxrs.services.AbstractResource;
import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.domain.ActivityWorkflowInfo;
import org.inherit.service.common.domain.CommentFeedItem;
import org.inherit.service.common.domain.Tag;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.portal.mycases.util.ServletUserNameUtil;

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
				
		String userId = getUserUuid(servletRequest);

		log.debug("activityInstanceUuid: " + activityInstanceUuid);
		log.debug("comment: " + comment);
		log.debug("userId: " + userId);

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
	@Produces("application/json")
	@Path("/assignTask") //{activityInstanceUuid}/{action}/{targetUserId}")
	public ActivityWorkflowInfo assignTask(
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@FormParam("activityInstanceUuid") String activityInstanceUuid,
			@FormParam("action") String action,
			@FormParam("targetUserId") String targetUserId) {
		
		ActivityWorkflowInfo result = null;
		
		String userId = getUserUuid(servletRequest);
		
		log.debug("activityInstanceUuid: " + activityInstanceUuid);
		log.debug("comment: " + action);
		log.debug("targetUserId: " + targetUserId);
		log.debug("userId: " + userId);
		
//		if (userId != null) {
			// user is authenticated
			
			// TODO check if user is authorized to assign task???
			InheritServiceClient isc = new InheritServiceClient();
			result = isc.assignTask(activityInstanceUuid, action, targetUserId);
/*		}
		else {
			// TODO respone http error
		}
*/		
		return result; 
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	@Path("/setActivityPriority") 
	public ActivityWorkflowInfo setActivityPriority(
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@FormParam("activityInstanceUuid") String activityInstanceUuid,
			@FormParam("priority") int priority) {
		
		ActivityWorkflowInfo result = null;
		
		String userId = getUserUuid(servletRequest);
		
		log.debug("activityInstanceUuid: " + activityInstanceUuid);
		log.debug("priority: " + priority);
		
//		if (userId != null) {
			// user is authenticated
			
			// TODO check if user is authorized to assign task???
			InheritServiceClient isc = new InheritServiceClient();
			result = isc.setActivityPriority(activityInstanceUuid, priority);
/*		}
		else {
			// TODO respone http error
		}
*/		
		return result; 
	}

	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	@Path("/getActivityWorkflowInfo") 
	public ActivityWorkflowInfo getActivityWorkflowInfo(
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@FormParam("activityInstanceUuid") String activityInstanceUuid) {
		
		ActivityWorkflowInfo result = null;
		
		String userId = getUserUuid(servletRequest);
		
		log.debug("activityInstanceUuid: " + activityInstanceUuid);
		log.debug("userId: " + userId);
		
//		if (userId != null) {
			// user is authenticated
			
			InheritServiceClient isc = new InheritServiceClient();
			result = isc.getActivityWorkflowInfo(activityInstanceUuid);
/*		}
		else {
			// TODO respone http error
		}
*/		
		return result; 
	}
		

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	@Path("/getCommentFeed") 
	public List<CommentFeedItem> getCommentFeed(
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@FormParam("activityInstanceUuid") String activityInstanceUuid) {
		
		ArrayList<CommentFeedItem> result = new ArrayList<CommentFeedItem>();
		
		String userId = getUserUuid(servletRequest);
		
		log.debug("activityInstanceUuid: " + activityInstanceUuid);
		log.debug("userId: " + userId);
		
//		if (userId != null) {
			// user is authenticated
			
			InheritServiceClient isc = new InheritServiceClient();
			result = isc.getCommentFeed(activityInstanceUuid, userId);
/*		}
		else {
			// TODO respone http error
		}
*/		
		return result; 
	}
		
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	@Path("/addTag") 
	public Tag addTag (
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@FormParam("processActivityFormInstanceId") long processActivityFormInstanceId,
			@FormParam("tagTypeId") long tagTypeId,
			@FormParam("value") String value) {
		
		Tag result = null;
				
		String userId = getUserUuid(servletRequest);

		log.debug("processActivityFormInstanceId: " + processActivityFormInstanceId);
		log.debug("tagTypeId: " + tagTypeId);
		log.debug("value: " + value);
		log.debug("userId: " + userId);

//		if (userId != null) {
			InheritServiceClient isc = new InheritServiceClient();
			result = isc.addTag(processActivityFormInstanceId, tagTypeId, value, userId);
	//	}
//		else {
	//				result = null;
	//	}
		
		return result; 
	}


	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/deleteTag") 
	public boolean deleteTag (
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@FormParam("processInstanceUuid") String processInstanceUuid,
			@FormParam("value") String value) {
		
		boolean result = false;
				
		String userId = getUserUuid(servletRequest);

		log.debug("processInstanceUuid: " + processInstanceUuid);
		log.debug("value: " + value);
		log.debug("userId: " + userId);

//		if (userId != null) {
			InheritServiceClient isc = new InheritServiceClient();
			result = isc.deleteTag(processInstanceUuid, value, userId);
	//	}
//		else {
	//				result = false;
	//	}
		
		return result; 
	}



	private String getUserUuid(HttpServletRequest servletRequest) {
		UserInfo userName = ServletUserNameUtil.getUserName(servletRequest);
		return (userName == null ? null : userName.getUuid());
	}
}

/* == Motrice Copyright Notice == 
 * 
 * Motrice Service Platform 
 * 
 * Copyright (C) 2011-2014 Motrice AB 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Affero General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Affero General Public License for more details. 
 * 
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>. 
 * 
 * e-mail: info _at_ motrice.se 
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 * phone: +46 8 641 64 14 
 
 */ 
 
 
package org.inheritsource.portal.jaxrs;

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
import org.inheritsource.portal.mycases.util.ServletUserNameUtil;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ActivityWorkflowInfo;
import org.inheritsource.service.common.domain.CommentFeedItem;
import org.inheritsource.service.common.domain.Tag;
import org.inheritsource.service.common.domain.UserDirectoryEntry;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.taskform.engine.TaskFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;


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
	
	private TaskFormService engine;
	
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

	public SiteAjaxApplication () {
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();  
		engine = (TaskFormService) ctx.getBean("engine");
	}
	
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
			result = engine.addComment(activityInstanceUuid, comment, userId);
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
			result = engine.assignTask(activityInstanceUuid, action, targetUserId);
			
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
			result = engine.setActivityPriority(activityInstanceUuid, priority);
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
			
			result = engine.getActivityWorkflowInfo(activityInstanceUuid);
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
		
		List<CommentFeedItem> result = new ArrayList<CommentFeedItem>();
		
		String userId = getUserUuid(servletRequest);
		
		log.debug("activityInstanceUuid: " + activityInstanceUuid);
		log.debug("userId: " + userId);
		
//		if (userId != null) {
			// user is authenticated
			
			result = engine.getCommentFeed(activityInstanceUuid, userId, null);
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
	@Path("/dirLookupUserEntries") 
	public List<UserDirectoryEntry> dirLookupUserEntries(
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@FormParam ("cnList") List<String> cnList ) {
		List<UserDirectoryEntry> result = new ArrayList<UserDirectoryEntry>();
		
		String userId = getUserUuid(servletRequest);
		
		//log.debug("userId: " + userId);
		//log.debug("cn: " + (cn==null ? "null" : cn.trim()));
		
//		if (userId != null) {
			// user is authenticated
		//log.setLevel(org.apache.log4j.Level.ALL);
		
		String [] cnArray = cnList.toArray(new String[cnList.size()]);
		result = engine.dirLookupUserEntries(cnArray);
/*		}
		else {
			// TODO response http error
		}
*/		
		return result; 
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	@Path("/dirSearchUserEntries") 
	public List<UserDirectoryEntry> dirSearchUserEntries(
			@Context HttpServletRequest servletRequest,
			@Context HttpServletResponse servletResponse,
			@FormParam("cn") String cn,
			@FormParam("sn") String sn,
			@FormParam("mail") String mail,
			@FormParam("givenName") String givenName,
			@FormParam("department") String department,
			@FormParam("company") String company) {
		
		List<UserDirectoryEntry> result = new ArrayList<UserDirectoryEntry>();
		
		String userId = getUserUuid(servletRequest);
		
		//log.debug("userId: " + userId);
		log.debug("cn: " + (cn==null ? "null" : cn.trim()));
		log.debug("sn: " + (sn==null ? "null" : sn.trim()));
		log.debug("mail: " + (mail==null ? "null" : mail.trim()));
		log.debug("givenName: " + (givenName==null ? "null" : givenName.trim()));
		log.debug("department: " + (department==null ? "null" : department.trim()));
		log.debug("company: " + (company==null ? "null" : company.trim()));
		
//		if (userId != null) {
			// user is authenticated
/*
		String[] fParams = {(cn==null ? "" : cn.trim()),
				            (sn==null ? "" : sn.trim()),
				            (mail==null ? "" : mail.trim()),
				            (givenName==null ? "" : givenName.trim()),
				            (department==null ? "" : department.trim()),
				            (company==null ? "" : company.trim())};

		String[] fParams = {(cn==null ? "" : cn.trim()),
	            (sn==null ? "" : sn.trim()),
	            (mail==null ? "" : mail.trim())};
*/	    
		String[] fParams = 	{(cn==null ? null : cn.trim()),
							 (sn==null ? null : sn.trim()),
							 (mail==null ? null : mail.trim()),
							 (givenName==null ? null : givenName.trim()),
							 (department==null ? null : department.trim()),
							 (company==null ? null : company.trim())
							};
		result = engine.dirSearchUserEntries(fParams);
/*		}
		else {
			// TODO response http error
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
			@FormParam("actinstId") String actinstId,
			@FormParam("tagTypeId") long tagTypeId,
			@FormParam("value") String value) {
		
		Tag result = null;
				
		String userId = getUserUuid(servletRequest);

		log.debug("actinstId: " + actinstId);
		log.debug("tagTypeId: " + tagTypeId);
		log.debug("value: " + value);
		log.debug("userId: " + userId);

//		if (userId != null) {
			result = engine.addTag(actinstId, tagTypeId, value, userId);
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
			result = engine.deleteTag(processInstanceUuid, value, userId);
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

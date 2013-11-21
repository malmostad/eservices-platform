/* 
 *  Process Aware Web Application Platform 
 * 
 *  Copyright (C) 2011-2013 Inherit S AB 
 * 
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Affero General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details. 
 * 
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 *  e-mail: info _at_ inherit.se 
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 *  phone: +46 8 641 64 14 
 */ 
 
package org.inheritsource.service.rest.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.inheritsource.service.common.domain.ActivityDefinitionInfo;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ActivityInstanceLogItem;
import org.inheritsource.service.common.domain.ActivityInstancePendingItem;
import org.inheritsource.service.common.domain.ActivityWorkflowInfo;
import org.inheritsource.service.common.domain.CommentFeedItem;
import org.inheritsource.service.common.domain.DashOpenActivities;
import org.inheritsource.service.common.domain.InboxTaskItem;
import org.inheritsource.service.common.domain.MyProfile;
import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.common.domain.ProcessDefinitionDetails;
import org.inheritsource.service.common.domain.ProcessDefinitionInfo;
import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.domain.ProcessInstanceListItem;
import org.inheritsource.service.common.domain.StartLogItem;
import org.inheritsource.service.common.domain.Tag;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.service.common.util.ConfigUtil;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.service.rest.client.domain.DocBoxFormData;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * A ready to use REST client implementation to inherit-service-rest-server.
 * @author bjmo
 *
 */
public class InheritServiceClient {

	public static final Logger log = Logger.getLogger(InheritServiceClient.class.getName());
	
	String serverBaseUrl;
	XStream xstream;
	XStream jsonxstream;
	
	public InheritServiceClient() {
		serverBaseUrl = ConfigUtil.getConfigProperties().getProperty("rest_uri", "http://localhost:8080/restrice/");
		serverBaseUrl = serverBaseUrl.trim();
		if (!serverBaseUrl.endsWith("/")) {
			serverBaseUrl += "/";
		}
		xstream = initXStream();
		jsonxstream = initJsonXStream();
	}

	public InheritServiceClient(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
		xstream = initXStream();
		jsonxstream = initJsonXStream();
	}
	
	private XStream initXStream() {
		XStream xstream = new XStream();
		
		xstream.alias("Date", Date.class);
		
		xstream.alias("set", HashSet.class);
		xstream.alias("list", ArrayList.class);
		
		xstream.alias("InboxTaskItem", InboxTaskItem.class);
		xstream.alias("ProcessDefinitionInfo", ProcessDefinitionInfo.class);
		xstream.alias("ProcessDefinitionDetails", ProcessDefinitionDetails.class);
		xstream.alias("ActivityDefinitionInfo", ActivityDefinitionInfo.class);
		xstream.alias("ProcessInstanceListItem", ProcessInstanceListItem.class);
		xstream.alias("CommentFeedItem", CommentFeedItem.class);
		
		xstream.alias("ProcessInstanceDetails", ProcessInstanceDetails.class);
		xstream.alias("ActivityInstanceItem", ActivityInstanceItem.class);
		xstream.alias("StartLogItem", StartLogItem.class);
		xstream.alias("ActivityInstanceLogItem", ActivityInstanceLogItem.class);
		xstream.alias("ActivityInstancePendingItem", ActivityInstancePendingItem.class);
		xstream.alias("DashOpenActivities", DashOpenActivities.class);
		xstream.alias("ActivityWorkflowInfo", ActivityWorkflowInfo.class);
		xstream.alias("PagedProcessInstanceSearchResult", PagedProcessInstanceSearchResult.class);
		xstream.alias("Tag", Tag.class);
		xstream.alias("UserInfo", UserInfo.class);
		xstream.alias("MyProfile", MyProfile.class);
		return xstream;
	}

	private XStream initJsonXStream() {
		XStream jsonxstream = new XStream(new JettisonMappedXmlDriver());
		jsonxstream.setMode(XStream.NO_REFERENCES);
		
		jsonxstream.alias("DocBoxFormData", DocBoxFormData.class);
		
		return jsonxstream;
	}

	@SuppressWarnings("unchecked")
	public PagedProcessInstanceSearchResult searchProcessInstancesStartedByUser(
			String searchForUserId, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, String userId) {
		PagedProcessInstanceSearchResult result = null;
		String uri = serverBaseUrl + "searchProcessInstancesStartedByUser/"
				+ ParameterEncoder.encode(searchForUserId) + "/" + fromIndex
				+ "/" + pageSize + "/" + ParameterEncoder.encode(sortBy) + "/"
				+ ParameterEncoder.encode(sortOrder) + "/"
				+ ParameterEncoder.encode(filter) + "/"
				+ ParameterEncoder.encode(userId) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (PagedProcessInstanceSearchResult) xstream
					.fromXML(response);
		}
		return result;
	}

	
	@SuppressWarnings("unchecked")
	public PagedProcessInstanceSearchResult searchProcessInstancesWithInvolvedUser(
			String searchForUserId, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, String userId) {
		PagedProcessInstanceSearchResult result = null;
		String uri = serverBaseUrl + "searchProcessInstancesWithInvolvedUser/"
				+ ParameterEncoder.encode(searchForUserId) + "/" + fromIndex
				+ "/" + pageSize + "/" + ParameterEncoder.encode(sortBy) + "/"
				+ ParameterEncoder.encode(sortOrder) + "/"
				+ ParameterEncoder.encode(filter) + "/"
				+ ParameterEncoder.encode(userId) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (PagedProcessInstanceSearchResult) xstream
					.fromXML(response);
		}
		return result;
	}

	public ProcessInstanceDetails getProcessInstanceDetailByUuid(String processInstanceUuid) {
		ProcessInstanceDetails result = null;
		String uri = serverBaseUrl + "processInstanceDetailsByUuid/" + ParameterEncoder.encode(processInstanceUuid) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (ProcessInstanceDetails)xstream.fromXML(response);
		}
		
		return result;
	}
	
	public ProcessInstanceDetails getProcessInstanceDetailByActivityInstanceUuid(String activityInstanceUuid) {
		ProcessInstanceDetails result = null;
		String uri = serverBaseUrl + "processInstanceDetailsByActivityInstanceUuid/" + ParameterEncoder.encode(activityInstanceUuid) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (ProcessInstanceDetails)xstream.fromXML(response);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<InboxTaskItem> getInboxByUserId(String bonitaUser) {
		 ArrayList<InboxTaskItem> result = null;
		String uri = serverBaseUrl + "inboxByUserId/" + ParameterEncoder.encode(bonitaUser) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (ArrayList<InboxTaskItem>)xstream.fromXML(response);
		}
		return result;
	}
	
	public InboxTaskItem getNextActivityInstanceItemByDocId(String docId, String userId) {
		InboxTaskItem result = null;
		String uri = serverBaseUrl + "getNextActivityInstanceItemByDocId/" + ParameterEncoder.encode(docId) + "/" + ParameterEncoder.encode(userId) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (InboxTaskItem)xstream.fromXML(response);
		}
		
		return result;
	}
	
	public ActivityInstanceItem getActivityInstanceItem(String activityInstanceUuid, String userId) {
		ActivityInstanceItem result = null;
		String uri = serverBaseUrl + "getActivityInstanceItem/" + ParameterEncoder.encode(activityInstanceUuid) + "/" + ParameterEncoder.encode(userId) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (ActivityInstanceItem)xstream.fromXML(response);
		}
		
		return result;
	}
	
	public ActivityInstanceItem getStartActivityInstanceItem(String formPath, String userId) {
		ActivityInstanceItem result = null;
		String uri = serverBaseUrl + "getStartActivityInstanceItem/" + ParameterEncoder.encode(formPath) + "/" + ParameterEncoder.encode(userId) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (ActivityInstanceItem)xstream.fromXML(response);
		}
		
		return result;
	}

	public ActivityInstanceItem getActivityInstanceItem(String processActivityFormInstanceId) {
		ActivityInstanceItem result = null;
		String uri = serverBaseUrl + "getActivityInstanceItemById/" + ParameterEncoder.encode(processActivityFormInstanceId) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (ActivityInstanceItem)xstream.fromXML(response);
		}
		return result;
	}

	public ActivityInstanceItem getStartActivityInstanceItem(Long processActivityFormInstanceId) {
		ActivityInstanceItem result = null;
		if (processActivityFormInstanceId != null) {
			result = getActivityInstanceItem(processActivityFormInstanceId.toString());
		}
		return result;
	}
	
	public String submitStartForm(String formPath, String docId, String userId) {
		String result;
		String uri;

		uri = serverBaseUrl + "submitStartForm/" + ParameterEncoder.encode(formPath) + 
				"/" + ParameterEncoder.encode(docId) + "/" + ParameterEncoder.encode(userId) + "?media=xml";
		
		result = (String) call(uri);
		/* 
		   ROL let the caller take care of the exception
		try {
			result = (String) call(uri);
		} catch (ResourceException e) {
			log.severe("submitStartForm uri: " + uri);
			System.out.println("Status description: " + e.getStatus().getDescription());
			System.out.println("Uri: " + e.getStatus().getUri());
			e.printStackTrace();
//			throw e;
			result = null;
		}
		*/
		return result;
	}

	public String submitForm(String docId, String userId) {
		String result = null;
		String uri;
		
		uri = serverBaseUrl + "submitForm/" 
				+ ParameterEncoder.encode(docId) + "/" 
				+ ParameterEncoder.encode(userId) + "?media=xml";
		log.severe("submitStartForm uri: " + uri);
		/* ROL let the caller take care of the exception */
		//result = (String) callAndCatchRE(uri);
		result = (String) call(uri);
		return result;
	}
	
	public String submitForm(String docId, String userId, String newDocId) {
		String result = null;
		String uri;
		
		uri = serverBaseUrl + "submitForm/" 
				+ ParameterEncoder.encode(docId) + "/" 
				+ ParameterEncoder.encode(userId) + "/" + ParameterEncoder.encode(newDocId) + "?media=xml";
		log.severe("submitStartForm uri: " + uri);
		/* ROL let the caller take care of the exception */
		//result = (String) callAndCatchRE(uri);
		result = (String) call(uri);
		return result;
	}
	
	public DashOpenActivities getDashOpenActivitiesByUserId(String userid, String remainingDays) {
		DashOpenActivities result = null;
		String uri;		
		uri = serverBaseUrl + "getDashOpenActivitiesByUserId/" 
				+ ParameterEncoder.encode(userid) + "/" 
				+ ParameterEncoder.encode(remainingDays) + "?media=xml";
		log.severe("getDashOpenActivitiesByUserId uri: " + uri);
		String response = callAndCatchRE(uri);
		
		if (response != null) {
			result = (DashOpenActivities)xstream.fromXML(response);
		}
		
		return result;
	}
	
	public DashOpenActivities getDashOpenActivitiesByUserId(String userid, int remainingDays) {
		DashOpenActivities result = null;
		result = getDashOpenActivitiesByUserId(userid, Integer.toString(remainingDays));
		return result;
	}
	
	public int addComment(String activityInstanceUuid, String comment, String userId) {
		int result = -2;
		String uri;
		
		uri = serverBaseUrl + "addComment/" + ParameterEncoder.encode(activityInstanceUuid) + 
					"/" + ParameterEncoder.encode(comment) + "/" + ParameterEncoder.encode(userId) + "?media=xml";
		log.severe("addComment uri: " + uri);
		
		String response = callAndCatchRE(uri);
		log.severe("response addComment: [" + response + "]");
		result = parseIntResponse(response);

		return result;
	}

	public ArrayList<CommentFeedItem> getCommentFeed(String activityInstanceUuid, String userId) {
		ArrayList<CommentFeedItem> result = null;
		String uri;
		
		uri = serverBaseUrl + "getCommentFeed/" + ParameterEncoder.encode(activityInstanceUuid) + 
					"/" + ParameterEncoder.encode(userId) + "?media=xml";
		log.severe("getCommentFeed uri: " + uri);
		
		String response = callAndCatchRE(uri);
		log.severe("response addComment: [" + response + "]");
		if (response != null) {
			result = (ArrayList<CommentFeedItem>)xstream.fromXML(response);
		}

		return result;
	}

	public ActivityWorkflowInfo assignTask(String activityInstanceUuid, String action, String userId) {
		ActivityWorkflowInfo result = null;
		String uri;
		
		uri = serverBaseUrl + "assignTask/" + ParameterEncoder.encode(activityInstanceUuid) + 
					"/" + ParameterEncoder.encode(action) + "/" + ParameterEncoder.encode(userId) + "?media=xml";
		log.severe("addComment uri: " + uri);
		
		String response = callAndCatchRE(uri);
		if (response != null) {
			result = (ActivityWorkflowInfo)xstream.fromXML(response);
		}

		return result;
	}
	
	public ActivityWorkflowInfo setActivityPriority(String activityInstanceUuid, int priority) {
		ActivityWorkflowInfo result = null;
		String uri;
		
		uri = serverBaseUrl + "setActivityPriority/" + ParameterEncoder.encode(activityInstanceUuid) + 
					"/" + priority + "?media=xml";
		log.severe("setActivityPriority uri: " + uri);
		
		String response = callAndCatchRE(uri);
		if (response != null) {
			result = (ActivityWorkflowInfo)xstream.fromXML(response);
		}

		return result;
	}

	public ActivityWorkflowInfo getActivityWorkflowInfo(String activityInstanceUuid) {
		ActivityWorkflowInfo result = null;
		String uri;
		
		uri = serverBaseUrl + "getActivityWorkflowInfo/" + ParameterEncoder.encode(activityInstanceUuid) + "?media=xml";
		log.severe("getActivityWorkflowInfo uri: " + uri);
		
		String response = callAndCatchRE(uri);
		if (response != null) {
			result = (ActivityWorkflowInfo)xstream.fromXML(response);
		}

		return result;
	}

	public Tag addTag(long processActivityFormInstanceId, long tagTypeId, String value, String userid) {
		Tag result = null;
		String uri;
		
		uri = serverBaseUrl + "addTag/" + processActivityFormInstanceId + "/" + tagTypeId + "/" + ParameterEncoder.encode(value) + "/" + ParameterEncoder.encode(userid) + "?media=xml";
		log.severe("addTag uri: " + uri);
		
		String response = callAndCatchRE(uri);
		if (response != null) {
			result = (Tag)xstream.fromXML(response);
		}

		return result;
	}

	public boolean deleteTag(String processInstanceUuid, String value, String userId) {
		boolean result = false;
		String uri;
		
		uri = serverBaseUrl + "deleteTag/" + ParameterEncoder.encode(processInstanceUuid) + 
					"/" + ParameterEncoder.encode(value) + "/" + ParameterEncoder.encode(userId) + "?media=xml";
		log.severe("deleteTag uri: " + uri);
		
		String response = callAndCatchRE(uri);
		log.severe("response deleteTag: [" + response + "]");
		result = parseBooleanResponse(response);

		return result;
	}
	
	public List<Tag> getTagsByProcessInstance(String processInstanceUuid) {
		ArrayList<Tag> result = null;
		String uri;
		
		uri = serverBaseUrl + "getTagsByProcessInstance/" + ParameterEncoder.encode(processInstanceUuid) +  "?media=xml";
		log.severe("getTagsByProcessInstance uri: " + uri);
		
		String response = callAndCatchRE(uri);
		log.severe("response getTagsByProcessInstance: [" + response + "]");
		if (response != null) {
			result = (ArrayList<Tag>)xstream.fromXML(response);
		}

		return result;
	}
	
	public void emailToInitiator(
			String processInstanceUuidStr,
			String activityInstanceUuidStr,
			String  mailSubjectLine,
			String  mailBodyText) {
		//int result;
		String uri;
		uri = serverBaseUrl + "emailToInitiator/" +
			ParameterEncoder.encode(processInstanceUuidStr) +
			"/" + ParameterEncoder.encode(activityInstanceUuidStr) +
			"/" + ParameterEncoder.encode(mailSubjectLine) +
			"/" + ParameterEncoder.encode(mailBodyText) + "?media=xml";
		log.severe("emailToInitiator uri: " + uri);
		String response = callAndCatchRE(uri);
		log.severe("response emailToInitiator: [" + response + "]");
		//result = parseIntResponse(response);
		//return result;
		return;
	}

	public void emailTo(
			String  mailTo,
			String  mailFrom,
			String  mailSubject,
			String  mailBody) {
		String uri = serverBaseUrl + "emailTo/" +
			ParameterEncoder.encode(mailTo) +
			"/" + ParameterEncoder.encode(mailFrom) +
			"/" + ParameterEncoder.encode(mailSubject) +
			"/" + ParameterEncoder.encode(mailBody) + "?media=xml";
		log.severe("emailTo uri: " + uri);
		String response = callAndCatchRE(uri);
		log.severe("response emailTo: [" + response + "]");
		return;
	}
	
	public Set<String> getUsersByRoleAndActivity(String roleName, String activityInstanceUuid) {
		Set<String> result = null;
		String uri;
		
		uri = serverBaseUrl + "getUsersByRoleAndActivity/" + ParameterEncoder.encode(roleName) + "/" + ParameterEncoder.encode(activityInstanceUuid) +  "?media=xml";
		log.severe("getUsersByRoleAndActivity uri: " + uri);
		
		String response = callAndCatchRE(uri);
		log.severe("response getUsersByRoleAndActivity: [" + response + "]");
		if (response != null) {
			result = (HashSet<String>)xstream.fromXML(response);
		}

		return result;
	}
	@SuppressWarnings("unchecked")
	public PagedProcessInstanceSearchResult searchProcessInstancesByTagValue(String tagValue, int fromIndex, int pageSize, String sortBy, String sortOrder, String filter, String userId) {
		PagedProcessInstanceSearchResult result = null;
		String uri = serverBaseUrl + "searchProcessInstancesByTagValue/" + ParameterEncoder.encode(tagValue)+ "/" + fromIndex + "/" + pageSize + "/" + ParameterEncoder.encode(sortBy) + "/" + ParameterEncoder.encode(sortOrder)  + "/" + ParameterEncoder.encode(filter) + "/" + ParameterEncoder.encode(userId) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (PagedProcessInstanceSearchResult)xstream.fromXML(response);
		}
		return result;
	}
	
	public UserInfo getUserByDn(String dn) {
		UserInfo result = null;
		String uri;
		
		uri = serverBaseUrl + "getUserByDn/" 
				+ ParameterEncoder.encode(dn) + "?media=xml";
		log.severe("getUserByDn uri: " + uri);
		String response = callAndCatchRE(uri);
		
		if (response != null) {
			result = (UserInfo)xstream.fromXML(response);
		}
		
		return result;
	}

	public UserInfo getUserBySerial(String serial, String certificateSubject) {
		UserInfo result = null;
		String uri;
		
		uri = serverBaseUrl + "getUserBySerial/" 
				+ ParameterEncoder.encode(serial) + "/" + ParameterEncoder.encode(certificateSubject) +  "?media=xml";
		log.severe("getUserBySerial uri: " + uri);
		String response = callAndCatchRE(uri);
		
		if (response != null) {
			result = (UserInfo)xstream.fromXML(response);
		}
		
		return result;
	}
	
	
	public String getPreviousActivitiesDataByDocId(String currentActivityFormDocId) {
		String result = null;
		String uri;
		
		uri = serverBaseUrl + "getPreviousActivitiesDataByDocId/" 
				+ ParameterEncoder.encode(currentActivityFormDocId) + "?media=xml";
		log.severe("getPreviousActivitiesDataByDocId uri: " + uri);
		result = (String) call(uri);
		return result;
	}
	
	public String getPreviousActivityDataByDocId(String currentActivityFormDocId, String previousActivityName, String uniqueXPathExpr) {
		String result = null;
		String uri;
		
		uri = serverBaseUrl + "getPreviousActivityDataByDocId/" 
				+ ParameterEncoder.encode(currentActivityFormDocId) + "/" 
				+ ParameterEncoder.encode(previousActivityName)  + "/" 
				+ ParameterEncoder.encode(uniqueXPathExpr) + "?media=xml";
		log.severe("getPreviousActivityDataByDocId uri: " + uri);
		result = (String) call(uri);
		return result;
	}

	public String getPreviousActivityDataByProcessInstanceUuid(String processInstanceUuid, String previousActivityName, String uniqueXPathExpr) {

		String result = null;
		String uri;
		
		uri = serverBaseUrl + "getPreviousActivityDataByProcessInstanceUuid/" 
				+ ParameterEncoder.encode(processInstanceUuid) + "/" 
				+ ParameterEncoder.encode(previousActivityName)  + "/" 
				+ ParameterEncoder.encode(uniqueXPathExpr) + "?media=xml";
		log.severe("getPreviousActivityDataByProcessInstanceUuid uri: " + uri);
		result = (String) call(uri);
		return result;
	}
	

	public String getPreviousActivityDataByInstanceUuid(String currentActivityInstanceUuid, String previousActivityName, String uniqueXPathExpr) {
		String result = null;
		String uri;
		
		uri = serverBaseUrl + "getPreviousActivityDataByInstanceUuid/" 
				+ ParameterEncoder.encode(currentActivityInstanceUuid) + "/" 
				+ ParameterEncoder.encode(previousActivityName)  + "/" 
				+ ParameterEncoder.encode(uniqueXPathExpr) + "?media=xml";
		log.severe("getPreviousActivityDataByInstanceUuid uri: " + uri);
		result = (String) call(uri);
		return result;
	}
	
	public DocBoxFormData getDocBoxFormData(String formDataUuid) {
		DocBoxFormData result = null;
		String uri;
		
		uri = "http://localhost:8080/docbox/doc/formdata/" 
				+ ParameterEncoder.encode(formDataUuid);
		
		log.severe("getDocBoxFormData uri: " + uri);
		
		String response = putAndCatchRE(uri, new DocBoxFormData());
		
		if (response != null) {
			log.severe("response: " + response);
			response = "{DocBoxFormData: " + response + "}";
			result = (DocBoxFormData)jsonxstream
					.fromXML(response);
		}
		return result;
	}

	public MyProfile getMyProfile(String userId) {
		MyProfile result = null;
		String uri = serverBaseUrl + "getMyProfile" + "/" + ParameterEncoder.encode(userId) + "?media=xml";
		String response = callAndCatchRE(uri);
		System.out.println(response);
		if (response != null) {
			result = (MyProfile)xstream.fromXML(response);
		}
		return result;
	}
	
	public DocBoxFormData addDocBoxSignature(String docBoxRef, String signature) {
		DocBoxFormData result = null;
		
		String uri;
		
		uri = "http://localhost:8080/docbox/doc/sig/" 
				+ ParameterEncoder.encode(docBoxRef);
		
		String response = null;
		try {
			response = call(uri, signature);
		}
		catch (ResourceException e) {
			
			if (Status.CLIENT_ERROR_CONFLICT.equals(e.getStatus())) {
				 // 409 (Conflict) on concurrent update conflict 
				log.info("optimistic lock conflict:  signature of old version cannot be added. uri=[" +
				 uri + "] and response = [" + response + "]");
				result = null;
				// TODO propagate and handle in gui
					
			} 
			else if (Status.CLIENT_ERROR_FORBIDDEN.equals(e.getStatus())) {
				// 403 (Forbidden) if the document number and/or the checksum 
				// in the signed text do not agree with the document being signed
				log.warning("the document number and/or the checksum in the signed text do not agree with the document being signed. uri=[" +
						 uri + "] and response = [" + response + "]");
				result = null;
					
			} 
			else if (Status.CLIENT_ERROR_NOT_FOUND.equals(e.getStatus())) {
				 // 404 (Not found) if the document was not found
				log.severe("the document was not found. uri=[" +
						 uri + "] and response = [" + response + "]");
				result = null;
					
			} 
			else {
				log.severe("Exception uri=[" +
						 uri + "] and response = [" + response + "] exception: " + e);
				result = null;
			}
			
		}
		
		if (response != null) {
			log.severe("response: " + response);
			response = "{DocBoxFormData: " + response + "}";
			result = (DocBoxFormData)jsonxstream
					.fromXML(response);
		}
		return result;
	}
	
	private boolean parseBooleanResponse(String val) {
		boolean result = false;
		try {
			if (val!=null) {
				result = ((Boolean)xstream.fromXML(val)).booleanValue();
				
			}
		}
		catch (Exception nfe) {
			log.warning("Cannot parse boolean value from val: " + val);
			result = false;
		}
		return result;
	}

	
	private int parseIntResponse(String val) {
		int result = -1000;
		try {
			if (val!=null) {
				result = ((Integer)xstream.fromXML(val)).intValue();
				
			}
		}
		catch (Exception nfe) {
			log.warning("Cannot parse integer value from val: " + val);
			result = -1001;
		}
		return result;
	}
	
	private String callAndCatchRE(String uri) {
		String result = null;
		try {
			Client client = new Client(new Context(), Protocol.HTTP);
			
			ClientResource cr = new ClientResource(uri);
			cr.setNext(client);
		
			cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "restuser", "restbpm");
				
			final String RESTLET_HTTP_HEADERS = "org.restlet.http.headers";
	        Map<String, Object> reqAttribs = cr.getRequestAttributes();
	        Form headers = (Form)reqAttribs.get(RESTLET_HTTP_HEADERS);
	        if (headers == null) {
	            headers = new Form();
	            reqAttribs.put(RESTLET_HTTP_HEADERS, headers);
	        } 
	        //headers.add("options", "user:" + bonitaUser); 

	        result = (String)cr.post(null, String.class);
	        			
		} catch (ResourceException e) {
			log.severe("call ResourceException: " + e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	private String putAndCatchRE(String uri, Object o) {
		String result = null;
		try {
			Client client = new Client(new Context(), Protocol.HTTP);
			
			ClientResource cr = new ClientResource(uri);
			cr.setNext(client);
		
			cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "restuser", "restbpm");
				
			final String RESTLET_HTTP_HEADERS = "org.restlet.http.headers";
	        Map<String, Object> reqAttribs = cr.getRequestAttributes();
	        Form headers = (Form)reqAttribs.get(RESTLET_HTTP_HEADERS);
	        if (headers == null) {
	            headers = new Form();
	            reqAttribs.put(RESTLET_HTTP_HEADERS, headers);
	        } 
	        //headers.add("options", "user:" + bonitaUser); 
	        
	        Representation r = cr.put(null);
	        log.severe("r=" + r);
	        if (r != null) {
	        	try {
					result = r.getText();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        			
		} catch (ResourceException e) {
			log.severe("call ResourceException: " + e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	private String call(String uri) throws ResourceException {
		return call(uri, null);
	}
	
	private String call(String uri, String body) throws ResourceException {
		String result;
		Client client = new Client(new Context(), Protocol.HTTP);

		ClientResource cr = new ClientResource(uri);
		cr.setNext(client);

		cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "restuser", "restbpm");

		final String RESTLET_HTTP_HEADERS = "org.restlet.http.headers";
		Map<String, Object> reqAttribs = cr.getRequestAttributes();
		Form headers = (Form)reqAttribs.get(RESTLET_HTTP_HEADERS);
		if (headers == null) {
			headers = new Form();
			reqAttribs.put(RESTLET_HTTP_HEADERS, headers);
		} 
		
		//headers.add("options", "user:" + bonitaUser); 

		result = (String)cr.post(body, String.class);
		return result;
	}
	
	public static void main(String args[]) {
		System.out.println("Testa InheritServ iceClient");
		InheritServiceClient c = new InheritServiceClient();
		
		PagedProcessInstanceSearchResult res = 
		//c.searchProcessInstancesStartedByUser("admin", 0, 10, "started", "desc", "STARTED", "admin");
		c.searchProcessInstancesStartedByUser("admin", 0, 10, "started", "desc", "FINISHED", "admin");
		System.out.println("res: " + res);
		
		//ActivityInstanceItem aIItem = c.getActivityInstanceItem("3908", "kermit");
		//System.out.println(aIItem);
		
		
		/*
		ArrayList<InboxTaskItem> inboxTaskItems = c.getInboxByUserId("admin");
		
		System.out.println("getInboxByUserId");
		if(inboxTaskItems != null) {
			for (InboxTaskItem item : inboxTaskItems) {
				System.out.println("inboxTaskItems item: " + item);
			}
		}
		*/
		/*
		System.out.println("DocBox form data: " + c.getDocBoxFormData("bmtestid1"));
		
		System.out.println("Next activity inbox item: " + c.getNextActivityInstanceItemByDocId("74e40834-43fd-4c63-8bee-00219562fe85", "john"));
		
		/*

		c.emailToInitiator(
				"procInstanceUuid",
				"Miljoforvaltningen_hemkompostering_matavfall--1.0--6--Delgivning--it1--mainActivityInstance--noLoop",
				"Re: mailSubjectLine",
				"<BodyText>");
		
		MyProfile profBean = c.getMyProfile("john");
		System.out.println(profBean.getUuid());
		System.out.println(profBean.getEmail());


		c.emailTo("info@inherit.se",
			  "Re: mailSubjectLine",
			  "<BodyText>");
		//System.out.println("InheritServiceClient, Resultat: " + res);

		PagedProcessInstanceSearchResult hits = c.searchProcessInstancesStartedByUser("john", 0, 5 , "started", "desc", "STARTED", "james");
		System.out.println("searchProcessInstancesStartedByUser");
		for (ProcessInstanceListItem item : hits.getHits()) {
			System.out.println("item: " + item);
		}

		ArrayList<InboxTaskItem> listInbx = c.getInboxByUserId("john");
		System.out.println("getInboxByUserId");
		for (InboxTaskItem item : listInbx) {
			System.out.println("listInbx item: " + item);
		}

		hits = c.searchProcessInstancesByTagValue("ture", 0, 5 , "started", "desc", "STARTED", "james");
		System.out.println("searchProcessInstancesByTagValue");
		for (ProcessInstanceListItem item : hits.getHits()) {
			System.out.println("item: " + item);
		}
		System.out.println("Hits: " + hits);
		System.out.println("Number of hits: " + hits.getNumberOfHits());
		 
		hits = c.searchProcessInstancesWithInvolvedUser("john", 2, 5 , "started", "desc", "STARTED", "james");
		System.out.println("searchProcessInstancesWithInvolvedUser");
		for (ProcessInstanceListItem item : hits.getHits()) {
			System.out.println("item: " + item);
		}
		System.out.println("Hits: " + hits);
		System.out.println("Number of hits: " + hits.getNumberOfHits());

		hits = c.searchProcessInstancesWithInvolvedUser("john", 2, 5 , "started", "desc", "FINISHED", "james");
		System.out.println("searchProcessInstancesWithInvolvedUser");
		for (ProcessInstanceListItem item : hits.getHits()) {
			System.out.println("item: " + item);
		}
		System.out.println("Hits: " + hits);
		System.out.println("Number of hits: " + hits.getNumberOfHits());

		System.out.println("getPreviousActivityDataByDocId: " + c.getPreviousActivityDataByDocId("fafe3e7d-0b45-49b6-be43-ee59ec8c1d55", "Beslut", "//section-1/control-1"));
		System.out.println("getPreviousActivityDataByInstanceUuid: " + c.getPreviousActivityDataByInstanceUuid("Arendeprocess--1.0--1--Handlaggning--it1--mainActivityInstance--noLoop", "Beslut", "//section-1/control-1"));
		System.out.println("getPreviousActivityDataByDocId: " + c.getPreviousActivityDataByDocId("fafe3e7d-0b45-49b6-be43-ee59ec8c1d55", "startform", "//section-1/control-1"));
		System.out.println("getPreviousActivityDataByInstanceUuid: " + c.getPreviousActivityDataByInstanceUuid("Arendeprocess--1.0--1--Handlaggning--it1--mainActivityInstance--noLoop", "startform", "//section-1/control-1"));
		System.out.println("getPreviousActivityDataByDocId: " + c.getPreviousActivityDataByDocId("fafe3e7d-0b45-49b6-be43-ee59ec8c1d55", "Handlaggning", "//section-1/control-1"));
		System.out.println("getPreviousActivityDataByInstanceUuid: " + c.getPreviousActivityDataByInstanceUuid("Arendeprocess--1.0--1--Handlaggning--it1--mainActivityInstance--noLoop", "Handlaggning", "//section-1/control-1"));
		
		System.out.println("getPreviousActivityDataByDocId: " + c.getPreviousActivitiesDataByDocId("fafe3e7d-0b45-49b6-be43-ee59ec8c1d55"));
*/
		System.out.println("InheritServiceClient avslutas");
	}
}

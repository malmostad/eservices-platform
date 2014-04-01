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
 
package org.inheritsource.taskform.engine;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ActivityInstanceLogItem;
import org.inheritsource.service.common.domain.ActivityInstancePendingItem;
import org.inheritsource.service.common.domain.ActivityWorkflowInfo;
import org.inheritsource.service.common.domain.CommentFeedItem;
import org.inheritsource.service.common.domain.DashOpenActivities;
import org.inheritsource.service.common.domain.FormInstance;
import org.inheritsource.service.common.domain.InboxTaskItem;
import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.domain.ProcessInstanceListItem;
import org.inheritsource.service.common.domain.StartForm;
import org.inheritsource.service.common.domain.StartLogItem;
import org.inheritsource.service.common.domain.Tag;
import org.inheritsource.service.common.domain.TimelineItem;
import org.inheritsource.service.common.domain.UserDirectoryEntry;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.service.common.domain.MyProfile;
import org.inheritsource.service.form.FormEngine;
import org.inheritsource.service.orbeon.OrbeonService;
import org.inheritsource.service.processengine.ActivitiEngineService;
import org.inheritsource.taskform.engine.persistence.TaskFormDb;
import org.inheritsource.taskform.engine.persistence.entity.ActivityFormDefinition;
import org.inheritsource.taskform.engine.persistence.entity.ProcessActivityFormInstance;
import org.inheritsource.taskform.engine.persistence.entity.StartFormDefinition;
import org.inheritsource.taskform.engine.persistence.entity.UserEntity;

public class TaskFormService {

	public static final Logger log = Logger.getLogger(TaskFormService.class
			.getName());

	private static final String MYPROFILEFORMPATH="malmo/profil";

	public static final String ACTION_ASSIGN = "assign";
	public static final String ACTION_ADD_CANDIDATE = "addcandidate";
	public static final String ACTION_REMOVE_CANDIDATE = "removecandidate";
	public static final String ACTION_UNASSIGN = "unassign";


	TaskFormDb taskFormDb;
	ActivitiEngineService activitiEngineService;
	ActorSelectorDirUtils aSelectorDirUtils;
    OrbeonService orbeonService;
    UserDirectoryService userDirectoryService;
    
	public TaskFormService() {
		taskFormDb = new TaskFormDb();
		orbeonService = new OrbeonService();
		userDirectoryService = new UserDirectoryService();
		//activitiEngineService = new ActivitiEngineService();
		
		// TODO hostname,port and base DN should be resolved from configuration
		aSelectorDirUtils = new ActorSelectorDirUtils("localhost", "1389",
				"ou=IDMGroups,OU=Organisation,OU=Malmo,DC=adm,DC=malmo,DC=se"); // Base
																				// DN
	}
	
	public ActivitiEngineService getActivitiEngineService() {
		return activitiEngineService;
	}

	public void setActivitiEngineService(ActivitiEngineService activitiEngineService) {
		this.activitiEngineService = activitiEngineService;
	}	
	
		
	public String getPreviousActivitiesData(String currentActivityFormDocId) {
		ProcessActivityFormInstance currentActivity = taskFormDb.getProcessActivityFormInstanceByFormDocId(currentActivityFormDocId);
		return currentActivity==null ? "" : getProcessInstanceChainActivitiesData(currentActivity.getProcessInstanceUuid());
	}
	
	public String getPreviousActivityDataByInstanceUuid(String currentActivityInstanceUuid, String previousActivityName, String uniqueXPathExpr) {
		ProcessActivityFormInstance currentActivity = taskFormDb.getProcessActivityFormInstanceByActivityInstanceUuid(currentActivityInstanceUuid);
		return currentActivity==null ? "" : getProcessInstanceActivityData(currentActivity.getProcessInstanceUuid(), previousActivityName, uniqueXPathExpr);
	}
	
	public String getPreviousActivityDataByDocId(String currentActivityFormDocId, String previousActivityName, String uniqueXPathExpr) {
		ProcessActivityFormInstance currentActivity = taskFormDb.getProcessActivityFormInstanceByFormDocId(currentActivityFormDocId);
		return currentActivity==null ? "" : getProcessInstanceActivityData(currentActivity.getProcessInstanceUuid(), previousActivityName, uniqueXPathExpr);
	}
	
	public String getProcessInstanceActivityData(String processInstanceUuid, String activityName, String uniqueXPathExpr) {
		String result = "";
		/*
		ProcessActivityFormInstance previousActivity = null;
		if ("startform".equalsIgnoreCase(activityName)) {
			previousActivity = taskFormDb.getStartProcessActivityFormInstanceByProcessInstanceUuid(processInstanceUuid);
		}
		else {
			String previousActivityUuid = activitiEngineService.getActivityInstanceUuid(processInstanceUuid, activityName);
			if (previousActivityUuid != null) {
				previousActivity = taskFormDb.getProcessActivityFormInstanceByActivityInstanceUuid(previousActivityUuid);
			}
		}
		if (previousActivity!=null) {
			result = orbeonService.getFormDataValue(previousActivity.getFormPath(), previousActivity.getFormDocId(), uniqueXPathExpr);
		}
		*/
		return result;
	}
	
	public String getProcessInstanceChainActivitiesData(String processInstanceUuid) {
		StringBuffer result = new StringBuffer();
		
		result.append("<pawap><process processInstanceUuid=\"");
		result.append(processInstanceUuid);
		result.append("\">");
		
		// anropa activitiEngineService och gör där en ny metod som returnerar alla processvariabler
		// bygg upp xml av dem
		
		@SuppressWarnings("unused")
		List<ProcessActivityFormInstance> pafis = taskFormDb.getProcessActivityFormInstances(processInstanceUuid);

		result.append("<pawap><formdata>");

		if (processInstanceUuid != null && !processInstanceUuid.isEmpty()) {
			getProcessInstanceActivitiesFormData(processInstanceUuid, result);
		}

		result.append("</formdata>");

		//result.append("<processdata>");
		  // anropa bonitaClient och gör där en ny metod som returnerar alla processvariabler
		  // bygg upp xml av dem
		//result.append("</processdata>");
		result.append("</pawap>");
		return result.toString();
	}
	
	public void getProcessInstanceActivitiesFormData(String processInstanceUuid, StringBuffer buf) {
		/*
		String parentProcessInstanceUuid = activitiEngineService.getParentProcessInstanceUuid(processInstanceUuid);
		if (parentProcessInstanceUuid != null && !parentProcessInstanceUuid.isEmpty()) {
			getProcessInstanceActivitiesFormData(parentProcessInstanceUuid,buf);
		}
		buf.append("<process processInstanceUuid=\"");
		buf.append(processInstanceUuid);
		buf.append("\">");
		List<ProcessActivityFormInstance> pafis = taskFormDb.getProcessActivityFormInstances(processInstanceUuid);
		if (pafis != null) {
			for (ProcessActivityFormInstance pafi : pafis) {
				if (pafi.getSubmitted() != null) {
					String formDataFragment = orbeonService.getFormData(
							pafi.getFormPath(), pafi.getFormDocId());
					if (formDataFragment != null) {
						if (pafi.isStartForm()) {
							buf.append("<startform>");
							buf.append(formDataFragment);
							buf.append("</startform>");
						} else {
							// activity form

							ActivityInstanceItem activityItem = activitiEngineService.getActivityInstanceItem(pafi.getActivityInstanceUuid());
							if (activityItem != null) {
								buf.append("<activity uuid=\"");
								buf.append(pafi.getActivityInstanceUuid());
								buf.append("\" activityName=\"");
								buf.append(activityItem.getActivityName());
								buf.append("\" activityDefinitionUuid=\"");
								buf.append(activityItem
										.getActivityDefinitionUuid());
								buf.append("\">");
								buf.append(formDataFragment);
								buf.append("</activity>");
							}
						}
					}
				}
			}
			buf.append("</process>");
		}
		return;
		*/
	}
 

	/**
	 * 
	 * @param userId
	 * @param remainingDays
	 *            atRisk activities is calculated as the number of activities
	 *            that will be overdue within remainingDays
	 * @return
	 */
	public DashOpenActivities getDashOpenActivitiesByUserId(String userId,
			int remainingDays) {
		DashOpenActivities result = activitiEngineService.getDashOpenActivitiesByUserId(
				userId, remainingDays);
		return result;
	}

	/**
	 * Get inbox task items a) partially filled start forms, not started
	 * processes b) started process with tasks associated to userId, check if
	 * partially filled form exist
	 * @param locale TODO
	 * @param userId
	 * 
	 * @return
	 */
	
	
	/*
	 * TODO: The field item.setProcessLabel("TODO"); in ActivitiEngineService 
	 * shall be handled here! FIXME
	 */
	public List<InboxTaskItem> getInboxTaskItems(Locale locale, String userId) {

		// Find partially filled not submitted start forms
		// TODO FIX THIS
		//List<ProcessActivityFormInstance> unsubmittedStartForms;
		//unsubmittedStartForms = taskFormDb.getPendingStartformFormInstances(userId);

		// find inbox tasks from activiti engine
		List<InboxTaskItem> inbox = activitiEngineService.getUserInbox(locale, userId);

		//inbox.addAll(unsubmittedStartForms);

		Collections.sort(inbox);
		
		return inbox;
	}
	
	private String calcExternalUrl(String formPath, Long processActivityFormInstanceId, String taskUuid) {
		String externalUrl = null;
		
		String activityUrlIdStr = processActivityFormInstanceId == null ? "taskUuid=" + taskUuid : "processActivityFormInstanceId=" + processActivityFormInstanceId; 
		
		if ("none".equals(formPath)) {
			// activity form is not defined
			externalUrl = "noform?" + activityUrlIdStr;
		}
		else if (formPath.startsWith("signstartform")) {
			externalUrl = "signform?" + activityUrlIdStr;
		}
		else if (formPath.startsWith("signactivity/")) {
			String activityName = formPath.substring(13);
			externalUrl = "signform?" + activityUrlIdStr + "&signActivityName=" + activityName;
		}
		
		return externalUrl;
	}

	public ProcessInstanceDetails getProcessInstanceDetails(
			String processInstanceUuid, Locale locale) {
		ProcessInstanceDetails details = activitiEngineService
				.getProcessInstanceDetails(processInstanceUuid, locale);
		return details;
	}

	public ProcessInstanceDetails getProcessInstanceDetailsByActivityInstance(
			String activityInstanceUuid, Locale locale) {
		ProcessInstanceDetails details = activitiEngineService
				.getProcessInstanceDetailsByActivityInstance(activityInstanceUuid, locale);
		return details;
	}

	public int addComment(String activityInstanceUuid, String comment,
			String userId) {
		int result = activitiEngineService.addComment(activityInstanceUuid, comment,
				userId);
		return result;
	}

	public List<CommentFeedItem> getCommentFeed(String activityInstanceUuid,
			String userId, Locale locale) {
		List<CommentFeedItem> result = activitiEngineService
				.getProcessInstanceCommentFeedByActivity(activityInstanceUuid, locale);
		return result;
	}

	public ActivityWorkflowInfo getActivityWorkflowInfo(
			String activityInstanceUuid) {
		ActivityWorkflowInfo result = activitiEngineService
				.getActivityWorkflowInfo(activityInstanceUuid);
		return result;
	}

	public ActivityWorkflowInfo assignTask(String activityInstanceUuid, String userId) {
		ActivityWorkflowInfo result = activitiEngineService.assignTask(
				activityInstanceUuid, userId);
		return result;
	}

	public ActivityWorkflowInfo assignTask(String activityInstanceUuid, String action, String userId) {
		ActivityWorkflowInfo result = null;
		
		if (ACTION_ASSIGN.equals(action)) {
			result = assignTask(activityInstanceUuid, userId);
		}
		else if (ACTION_ADD_CANDIDATE.equals(action)) {
			result = addCandidate(activityInstanceUuid, userId);
		}
		else if (ACTION_REMOVE_CANDIDATE.equals(action)) {
			result = removeCandidate(activityInstanceUuid, userId);
		}
		else if (ACTION_UNASSIGN.equals(action)) {
			result = unassignTask(activityInstanceUuid);
		}
		
		return result;
	}

	public ActivityWorkflowInfo unassignTask(String activityInstanceUuid) {
		ActivityWorkflowInfo result = activitiEngineService
				.unassignTask(activityInstanceUuid);
		return result;
	}

	public ActivityWorkflowInfo addCandidate(String activityInstanceUuid,
			String userId) {
		ActivityWorkflowInfo result = activitiEngineService.addCandidate(
				activityInstanceUuid, userId);
		return result;
	}

	public ActivityWorkflowInfo removeCandidate(String activityInstanceUuid,
			String userId) {
		ActivityWorkflowInfo result = activitiEngineService.removeCandidate(
				activityInstanceUuid, userId);
		return result;
	}

	public ActivityWorkflowInfo setActivityPriority(
			String activityInstanceUuid, int priority) {
		ActivityWorkflowInfo result = activitiEngineService.setPriority(
				activityInstanceUuid, priority);
		return result;
	}

	public StartLogItem getStartFormActivityInstanceLogItem(String processInstanceId) {
		return this.activitiEngineService.getStartLogItem(processInstanceId);
	}

    /*
	public String submitStartForm(String formPath, String docId, String userId)
			throws Exception {
		String result;
		log.severe("formPath=[" + formPath + "] docId=[" + docId + "] userId=["
				+ userId + "]");
		// lookup the process this start form is configured to start.
		try {
			StartFormDefinition startFormDef = taskFormDb
					.getStartFormDefinitionByFormPath(formPath);
			String processDefinitionUUIDStr = startFormDef
					.getProcessDefinitionUuid();

			// start the process
			String processInstanceUuid = activitiEngineService.startProcess(
					processDefinitionUUIDStr, userId);

			// store the start activity
			ProcessActivityFormInstance activity = new ProcessActivityFormInstance();
			activity.setFormDocId(docId);
			activity.setStartFormDefinition(startFormDef);
			activity.setFormTypeId(startFormDef.getFormTypeId());
			activity.setFormConnectionKey(startFormDef.getFormConnectionKey());
			activity.setProcessInstanceUuid(processInstanceUuid);
			activity.setSubmitted(new Date());
			activity.setUserId(userId);
			taskFormDb.saveProcessActivityFormInstance(activity);
			result = processInstanceUuid;
		} catch (Exception e) {
			// TODO catch block cleaning up possible inconsistencies. ROL
			e.printStackTrace();
			throw e;
		} finally {
		}
		return result;
	}
	*/

    /**
     * 
     * @param currentFormInstance submitted form
     * @param userId
     * @return guess next InboxTaskItem from current
     */
	public InboxTaskItem getNextActivityInstanceItemByDocId(FormInstance currentFormInstance, String userId) {
		InboxTaskItem result = null;
		
		try {
			List<InboxTaskItem> items = activitiEngineService.getUserInbox(null, userId);
			
			if (currentFormInstance instanceof ActivityInstanceLogItem) {
				ActivityInstanceLogItem logItem = (ActivityInstanceLogItem)currentFormInstance;
				for (InboxTaskItem item : items) {
					if (logItem.getProcessInstanceUuid()!=null && logItem.getProcessInstanceUuid().equals(item.getProcessInstanceUuid())) {
						result = item;
					}
				}
			}
			//result = activitiEngineService.getNextInboxTaskItem(currentProcessInstance, userId);
		}
		catch (Exception e) {
			log.severe("getNextActivityInstanceItemByDocId currentFormInstance=" + currentFormInstance + " Exception: " + e);
		}
		
		return result;
	}
	
	public ActivityInstanceItem getActivityInstanceItem(String actinstId, String formInstanceId, String userId) {
		
		ActivityInstanceItem result = activitiEngineService.getActivityInstanceItem(actinstId, formInstanceId, userId);
		
		return result;
	}
	
	public ActivityInstanceItem getStartActivityInstanceItem(String formPath,
			String userId) throws Exception {
		ActivityInstanceItem result = new ActivityInstancePendingItem();
		
		result = (ActivityInstanceItem)getActivitiEngineService().getFormEngine().getStartFormInstance(new Long(1), formPath , userId, result);

		// TODO replace LATER 
		initializeStartForm(result, userId);
		return result;
	}

	/**
	 * This conversion is only valid on StartForms
	 * 
	 * @param src
	 * @return
	 */
	private ActivityInstancePendingItem processActivityFormInstance2ActivityInstancePendingItem(
			ProcessActivityFormInstance src) {
		// TODO this method name is confusing, it is only working on start
		// forms...
		ActivityInstancePendingItem dst = new ActivityInstancePendingItem();
		try {
			dst.setProcessDefinitionUuid(src.getStartFormDefinition()
					.getProcessDefinitionUuid());
			dst.setActivityName("StartCaseTODO");
			dst.setActivityLabel("Starta ärende TODO");
			dst.setStartDate(null);
			dst.setCurrentState("TODO");
			dst.setLastStateUpdate(null);
			dst.setLastStateUpdateByUserId(src.getUserId());
			dst.setExpectedEndDate(null);
			dst.setPriority(0);
			dst.setStartedBy("");
			dst.setAssignedUser(taskFormDb.getUserByUuid(src.getUserId()));
			dst.setExpectedEndDate(null);
			dst.setEditUrl(src.calcEditUrl());
			
			dst.setProcessInstanceUuid(null);
			dst.setActivityInstanceUuid(null);
			dst.setActivityDefinitionUuid(null);
		} catch (RuntimeException re) {
			log.severe("Cannot convert ProcessActivityFormInstance " + src
					+ " to ActivityInstancePendingItem. RuntimeException: "
					+ re);
			throw re;
		}

		return dst;
	}

	/**
	 * This is only valid on orbeon forms
	 * @param formPath
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private ActivityInstanceItem initializeStartForm(FormInstance startFormInstance,
			String userId) throws Exception {

		// lookup the process this start form is configured to start.

		try {
			StartFormDefinition startFormDefinition = taskFormDb
					.getStartFormDefinitionByFormPath( startFormInstance.getDefinitionKey());

			// generate a type 4 UUID
			String docId = java.util.UUID.randomUUID().toString();

			// store the start form activity
			ProcessActivityFormInstance activityInstance = new ProcessActivityFormInstance();
			activityInstance.setFormDocId( startFormInstance.getInstanceId());
			activityInstance.setStartFormDefinition(startFormDefinition);
			activityInstance.setFormTypeId(startFormDefinition.getFormTypeId());
			activityInstance.setFormConnectionKey(startFormDefinition.getFormConnectionKey());
			activityInstance.setProcessInstanceUuid(null);
			activityInstance.setActivityInstanceUuid(null);
			activityInstance.setSubmitted(null);
			activityInstance.setUserId(userId);

			taskFormDb.saveProcessActivityFormInstance(activityInstance);
			return processActivityFormInstance2ActivityInstancePendingItem(activityInstance);
		} finally {

		}
	}

	/**
	 * submit form
	 * 
	 * @param docId
	 * @param userId 
	 * @param newDocId Replace docId with a new one on submit. 
	 * @return confirmation form instance. null if submission fails.
	 */
	public FormInstance submitActivityForm(String docId, String userId) throws Exception {
		return submitActivityForm(docId, userId, null);
	}
			
	
	/**
	 * submit form
	 * 
	 * @param docId
	 * @param userId 
	 * @param newDocId Replace docId with a new one on submit. 
	 * @return confirmation form instance. null if submission fails.
	 */
	public FormInstance submitActivityForm(String docId, String userId, String newDocId)
			throws Exception {
		FormInstance formInstance;
		
		// TODO, när userId är null 1) kolla om det finns i formulärdata i xpath angiven av startformdef 2) anonymous om startformdef tillåter anonym
		log.info("submitActivityForm docId=" + docId + " userId=" + userId + " newDocId=" + newDocId);
		try {
			formInstance = activitiEngineService.submitForm(docId, userId, newDocId);
			if (formInstance == null) {

				// maybe a start form
				ProcessActivityFormInstance activity = taskFormDb.getProcessActivityFormInstanceByFormDocId(docId);
				if (activity != null) {
					Date tstamp = new Date();
					activity.setSubmitted(tstamp);
					activity.setUserId(userId);
					
					if (newDocId != null) {
						activity.setFormDocId(newDocId);
					}
					
					if (activity.isStartForm()) {
						String startFormUser = null;
						switch (activity.getStartFormDefinition().getAuthTypeReq()) {
	    				    case USERSESSION: 
	    				    	// do nothing userId is going to be used
	    				    	break;
						    case USERSESSION_FORMDATA: 
						    	if (userId == null || userId.trim().length()==0) {
							      startFormUser = orbeonService.getFormDataValue(activity.getFormConnectionKey(), activity.getFormDocId(), activity.getStartFormDefinition().getUserDataXPath());
							      userId = startFormUser;
						    	}
								break;
							case FORMDATA_USERSESSION:
								startFormUser = orbeonService.getFormDataValue(activity.getFormConnectionKey(), activity.getFormDocId(), activity.getStartFormDefinition().getUserDataXPath());
								if (startFormUser != null && startFormUser.trim().length()>0) {
									userId = startFormUser;
								}
								break;
							case USERSESSION_FORMDATA_ANONYMOUS: 
						    	if (userId == null || userId.trim().length()==0) {
							      startFormUser = orbeonService.getFormDataValue(activity.getFormConnectionKey(), activity.getFormDocId(), activity.getStartFormDefinition().getUserDataXPath());
							      userId = startFormUser;
							      if (userId == null || userId.trim().length()==0) {
							    	  userId = UserInfo.ANONYMOUS_UUID;
							      }
							    }
								break;
							case FORMDATA_USERSESSION_ANONYMOUS: 
								startFormUser = orbeonService.getFormDataValue(activity.getFormConnectionKey(), activity.getFormDocId(), activity.getStartFormDefinition().getUserDataXPath());
								if (startFormUser != null && startFormUser.trim().length()>0) {
									userId = startFormUser;
								}
								if (userId == null || userId.trim().length()==0) {
							    	  userId = UserInfo.ANONYMOUS_UUID;
							    }
								break;
							case ANONYMOUS:
								userId = UserInfo.ANONYMOUS_UUID;
								break;
							default:
								log.severe("Unknown AuthTypeReq in StartFormDefinition, this should never happen...");
								break;
						}
						
						// start the process
						String pDefUuid = activity.getStartFormDefinition()
								.getProcessDefinitionUuid();
						
						// TODO move into formengine 
						Map<String,Object> variables = new HashMap<String,Object> ();
						variables.put(FormEngine.START_FORM_TYPEID, activity.getFormTypeId());
						variables.put(FormEngine.START_FORM_ASSIGNEE, userId); // set another user later (starta å invpånares vägnar)
						variables.put(FormEngine.START_FORM_DEFINITIONKEY, activity.getFormConnectionKey());
						variables.put(FormEngine.START_FORM_INSTANCEID, activity.getFormDocId());
						variables.put(FormEngine.START_FORM_DATA_URI, "TODO");
						
						String processInstanceUuid = activitiEngineService.startProcess(pDefUuid, variables, userId);
						activity.setProcessInstanceUuid(processInstanceUuid);
						
						formInstance = activitiEngineService.getFormEngine().getStartLogItem(activitiEngineService.getEngine().getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceUuid).includeProcessVariables().singleResult(), userId);

					}
				}
			}
		} catch (Exception e) {
			log.severe("Exception while submitting activity with docId=[" + docId + "] as userId=[" + userId + "] Exception: " + e);
			// ROL cleanup inconsistencies...
			throw e;
		}
		return formInstance;
	}

	public MyProfile getMyProfile(String Uuid) {
		MyProfile result = new MyProfile(Uuid);
		result.setEmail(orbeonService.getFormDataValue(
				MYPROFILEFORMPATH, Uuid, "//section-1/email"));
		return result;
	}
	
	private String searchForBonitaUser(String searchForUserId) {
		String searchForBonitaUser = searchForUserId;
		
		if (searchForBonitaUser!=null) {
			searchForBonitaUser = searchForBonitaUser.trim();
		}
		UserInfo userInfo = taskFormDb.getUserBySerial(searchForBonitaUser);
		if (userInfo != null) {
			searchForBonitaUser = userInfo.getUuid();
		}
		
		return searchForBonitaUser;
	}
	
    
    /**
     * Find process instances with involved user
     * @param searchForUserId involved user to search for
     * @param fromIndex paging start index. starts with 0
     * @param pageSize 
     * @param sortBy
     * @param sortOrder
     * @param filter filter on instance state, valid values are STARTED, FINISHED
     * @param userId The user that is actually performing the search, make it possibly to exclude hits because of privacy reasons for instance
     * @return
     */
    public PagedProcessInstanceSearchResult searchProcessInstancesStartedByUser(String searchForUserId, int fromIndex, int pageSize, String sortBy, String sortOrder, String filter, Locale locale, String userId) { 
            PagedProcessInstanceSearchResult result = activitiEngineService.getProcessInstancesStartedBy(searchForBonitaUser(searchForUserId), fromIndex, pageSize, sortBy, sortOrder, filter, locale, userId);
            return result;
    }

    
   /**
    * Find process instances with involved user
    * @param searchForUserId involved user to search for
    * @param fromIndex paging start index. starts with 0
    * @param pageSize 
    * @param sortBy
    * @param sortOrder
    * @param filter filter on instance state, valid values are STARTED, FINISHED
    * @param userId The user that is actually performing the search, make it possibly to exclude hits because of privacy reasons for instance
    * @return
    */
   public PagedProcessInstanceSearchResult searchProcessInstancesWithInvolvedUser(String searchForUserId, int fromIndex, int pageSize, String sortBy, String sortOrder, String filter, Locale locale, String userId) { 
           PagedProcessInstanceSearchResult result = activitiEngineService.getProcessInstancesWithInvolvedUser(searchForBonitaUser(searchForUserId), fromIndex, pageSize, sortBy, sortOrder, filter, locale, userId);
           return result;
   }
   
   
   /**
    * Find process instances that has a specified associated tag
    * @param tagValue  Tag value to search for
    * @param userId    The user that is actually performing the search, make it possibly to exclude hits because of privacy reasons for instance 

    * @return
    */
       public PagedProcessInstanceSearchResult searchProcessInstancesListByTag(String tagValue, int fromIndex, int pageSize, String sortBy, String sortOrder, String filter, Locale locale, String userId) { 
                List<String> uuids = taskFormDb.getProcessInstancesByTag(tagValue);
               PagedProcessInstanceSearchResult result = activitiEngineService.getProcessInstancesByUuids(uuids, fromIndex, pageSize, sortBy, sortOrder, filter, locale, userId);
               return result;
        }

	public Tag addTag(String actinstId, Long tagTypeId,
			String value, String userId) {
		
		return activitiEngineService.addTag(actinstId, tagTypeId, value, userId);
	}

	public boolean deleteTag(String processInstanceUuid, String value,
			String userId) {
		return taskFormDb.deleteTag(processInstanceUuid, value, userId);
	}

	public List<Tag> getTagsByProcessInstance(String processInstanceUuid) {
		return taskFormDb.getTagsByProcessInstance(processInstanceUuid);
	}

	public Set<String> getUsersByRoleAndActivity(String roleName,
			String activityInstanceUuid) {
		// TODO implement getDepartmentByactivityInstanceUuid, for now just
		// hardwire "Miljöförvaltningen"
		Set<String> result = null;
		try {
			result = aSelectorDirUtils.getUsersByDepartmentAndRole(
					"Miljöförvaltningen", roleName);
		} catch (Exception e) {
			log.severe("getUsersByRoleAndActivity roleName=" + roleName + 
					" activityInstanceUuid=" + activityInstanceUuid + 
					" Exception: " + e.toString());
		}

		return result;
	}

	public UserInfo getUserByUuid(String uuid) {
		UserInfo userInfo = taskFormDb.getUserByUuid(uuid);
		return userInfo;
	}
	
	public UserInfo getUserByDn(String dn) {
		UserInfo userInfo = taskFormDb.getUserByDn(dn);

		if (userInfo == null) {
			// new user in system

			String gn = null, sn = null, cn = null;
			String uuid = java.util.UUID.randomUUID().toString();
			String serial = null;

			// try to parse CN
			StringTokenizer st = new StringTokenizer(dn, ",");
			while (st.hasMoreTokens()) {
				String s = st.nextToken();

				StringTokenizer valuePairToken = new StringTokenizer(s, "=");
				String key = null;
				String value = null;
				if (valuePairToken.hasMoreTokens()) {
					key = valuePairToken.nextToken().trim();
				}
				if (valuePairToken.hasMoreTokens()) {
					value = valuePairToken.nextToken().trim();
				}
				if (!valuePairToken.hasMoreTokens()) {
					if ("CN".equalsIgnoreCase(key)) {
						cn = value;
					}
				}

			}

			if (cn != null && cn.trim().length() > 0) {
				// use cn as uuid...
				uuid = cn;
			}

			// store user
			UserEntity user = new UserEntity();
			user.setCategory(UserInfo.CATEGORY_INTERNAL);
			user.setSerial(serial);
			user.setCn(cn);
			user.setGn(gn);
			user.setSn(sn);
			user.setDn(dn);
			user.setUuid(uuid);
			userInfo = taskFormDb.createUser(user);

		}

		return userInfo;
	}

	public List<StartForm> getStartForms(String mode, Locale locale) { 
		return activitiEngineService.getFormEngine().getStartForms(locale);
	}
	
	public UserInfo getUserBySerial(String serial, String certificateSubject) {
		UserInfo userInfo = taskFormDb.getUserBySerial(serial);

		if (userInfo == null) {
			// new user in system

			String gn = null, sn = null, cn = null;
			String uuid = java.util.UUID.randomUUID().toString();
			String dn = null;

			// try to parse certificateSubject
			StringTokenizer st = new StringTokenizer(certificateSubject, ",");
			while (st.hasMoreTokens()) {
				String s = st.nextToken();

				StringTokenizer valuePairToken = new StringTokenizer(s, "=");
				String key = null;
				String value = null;
				if (valuePairToken.hasMoreTokens()) {
					key = valuePairToken.nextToken().trim();
				}
				if (valuePairToken.hasMoreTokens()) {
					value = valuePairToken.nextToken().trim();
				}
				if (!valuePairToken.hasMoreTokens()) {
					if ("CN".equalsIgnoreCase(key)) {
						cn = value;
					}
					if ("GIVENNAME".equalsIgnoreCase(key)) {
						gn = value;
					}
					if ("SURNAME".equalsIgnoreCase(key)) {
						sn = value;
					}
				}

			}
			// store user
			UserEntity user = new UserEntity();
			user.setCategory(UserInfo.CATEGORY_EXTERNAL);
			user.setSerial(serial);
			user.setCn(cn);
			user.setGn(gn);
			user.setSn(sn);
			user.setDn(dn);
			user.setUuid(uuid);
			userInfo = taskFormDb.createUser(user);

		}

		return userInfo;
	}
	
	public boolean isTok() {
		log.info("TOKEN WAS HERE");
		return true;
	}
	
	public String executeSomething(String taskId, String param) {
		log.info("TOKEN loggar " + taskId + " och " + param);
		return taskId + ":" +  param;
	}

	public List<UserDirectoryEntry> dirSearchUserEntries(String[] filterParams) {
		List<UserDirectoryEntry> result = null;
		try {
		    // log.info("TaskFormService.dirSearchUserEntries: " +
                    // (userDirectoryService==null ? "null" : "nonull"));
		    result = userDirectoryService.searchForUserEntries(filterParams);
		} catch (Exception e) {
			log.severe("TaskFormService.dirSearchUserEntries: " + 
                           (userDirectoryService==null ? "null" : "nonull") +
					" Exception:" + e.toString());
		}
		return result;
	}

	public List<UserDirectoryEntry> dirLookupUserEntries(String[] cnArray) {
		List<UserDirectoryEntry> result = null;
		try {
			//System.out.println("TaskFormService.dirLookupEntries: "
                        //  + (userDirectoryService==null ? "null" : "nonull"));
			result = userDirectoryService.lookupUserEntries(cnArray);
		} catch (Exception e) {
			log.severe("TaskFormService.dirLookupUserEntries: " +
				   (userDirectoryService==null ? "null" : "nonull") +
				   " Exception:" + e.toString());
		}
		return result;
	}
}

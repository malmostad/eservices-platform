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
 
 
package org.inheritsource.taskform.engine;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.inheritsource.service.common.util.ConfigUtil;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ActivityInstanceLogItem;
import org.inheritsource.service.common.domain.ActivityInstancePendingItem;
import org.inheritsource.service.common.domain.ActivityWorkflowInfo;
import org.inheritsource.service.common.domain.CommentFeedItem;
import org.inheritsource.service.common.domain.DashOpenActivities;
import org.inheritsource.service.common.domain.DocBoxFormData;
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
import org.inheritsource.service.delegates.DelegateUtil;
import org.inheritsource.service.docbox.DocBoxFacade;
import org.inheritsource.service.form.FormEngine;
import org.inheritsource.service.identity.ActorSelectorDirUtils;
import org.inheritsource.service.identity.UserDirectoryService;
import org.inheritsource.service.orbeon.OrbeonService;
import org.inheritsource.service.processengine.ActivitiEngineService;
import org.inheritsource.taskform.engine.persistence.TaskFormDb;
import org.inheritsource.taskform.engine.persistence.entity.ActivityFormDefinition;
import org.inheritsource.taskform.engine.persistence.entity.ProcessActivityFormInstance;
import org.inheritsource.taskform.engine.persistence.entity.StartFormDefinition;
import org.inheritsource.taskform.engine.persistence.entity.UserEntity;

public class TaskFormService {

	public static final Logger log = LoggerFactory.getLogger(TaskFormService.class
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
		
		// TODO base DN should be resolved from configuration
                String host = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.host");
                String port = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.port"); 
		aSelectorDirUtils = new ActorSelectorDirUtils(host , port,
				"ou=IDMGroups,OU=Organisation,OU=Malmo,DC=adm,DC=malmo,DC=se"); // Base
																				// DN
	}
	
	public ActivitiEngineService getActivitiEngineService() {
		return activitiEngineService;
	}

	public void setActivitiEngineService(ActivitiEngineService activitiEngineService) {
		this.activitiEngineService = activitiEngineService;
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

		// find inbox tasks from activiti engine
		List<InboxTaskItem> inbox = activitiEngineService.getUserInbox(locale, userId);

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
		log.error("formPath=[" + formPath + "] docId=[" + docId + "] userId=["
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
	public InboxTaskItem getNextActivityInstanceItemByDocId(FormInstance currentFormInstance, Locale locale, String userId) {
		InboxTaskItem result = null;
		
		try {
			List<InboxTaskItem> items = activitiEngineService.getUserInbox(locale, userId);
			
			if (currentFormInstance instanceof ActivityInstanceLogItem) {
				ActivityInstanceLogItem logItem = (ActivityInstanceLogItem)currentFormInstance;
				for (InboxTaskItem item : items) {
					if (logItem.getProcessInstanceUuid()!=null && logItem.getProcessInstanceUuid().equals(item.getProcessInstanceUuid())) {
						result = item;
					}
				}
			}
			
			if (result == null && items!= null && items.size()>0) {
				result = items.get(0);
			}
			//result = activitiEngineService.getNextInboxTaskItem(currentProcessInstance, userId);
		}
		catch (Exception e) {
			log.error("getNextActivityInstanceItemByDocId currentFormInstance=" + currentFormInstance + " Exception: " + e);
		}
		
		return result;
	}
	
	public ActivityInstanceItem getActivityInstanceItem(String actinstId, String formInstanceId, Locale locale, String userId) {
		
		ActivityInstanceItem result = activitiEngineService.getActivityInstanceItem(actinstId, formInstanceId, locale, userId);
		
		return result;
	}
	
	public ActivityInstanceItem getStartActivityInstanceItem(String formPath, Locale locale,
			String userId) throws Exception {
		ActivityInstanceItem result = new ActivityInstancePendingItem();
		
		result = (ActivityInstanceItem)getActivitiEngineService().getFormEngine().getStartFormInstance(new Long(1), formPath , userId, result, locale);

		return result;
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
			
	public FormInstance submitSignForm(String formInstanceId, String userId, String docboxRef, String signature) throws Exception {
		DocBoxFacade docBox = new DocBoxFacade();
        DocBoxFormData docBoxFormData = docBox.addDocBoxSignature(docboxRef, signature);
        FormInstance signedForm = null;
        
        if (docBoxFormData != null) {
        	// save DocboxRef as formDocId. 
			try {
				signedForm = submitActivityForm(formInstanceId, userId, docBoxFormData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return signedForm;
	}

	/*
	 *
	 * DocBoxFacade docBox = new DocBoxFacade();
        DocBoxFormData docBoxFormData = docBox.addDocBoxSignature(docboxRef, signature);
		        
        if (docBoxFormData != null) {
        	// save DocboxRef as formDocId. 
        	FormInstance signedForm = null;
			try {
				signedForm = engine.submitActivityForm(instanceId, userUuid, docBoxFormData.getDocboxRef());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    		String portStr = (request.getLocalPort() == 80 || request.getLocalPort() == 443) ? "" : ":" + request.getLocalPort();
    		String protocolStr = request.getLocalPort() == 443 ? "https" : ":" + "http";
    		String pdfUrl = protocolStr + "://" + request.getServerName() + portStr +  "/docbox/doc/ref/" + docBoxFormData.getDocboxRef();
    		
    		request.setAttribute("pdfUrl", pdfUrl);

    		InboxTaskItem nextTask = null;
    		if (signedForm!=null && !UserInfo.ANONYMOUS_UUID.equals(userUuid)) {
    	        nextTask = engine.getNextActivityInstanceItemByDocId(signedForm, user.getUuid());
    	        appendChannelLabels(request, nextTask);
    		}
    		request.setAttribute("nextTask", nextTask);
        }
	 */
	
	/**
	 * submit form
	 * 
	 * @param docId
	 * @param userId 
	 * @param newDocId Replace docId with a new one on submit. 
	 * @return confirmation form instance. null if submission fails.
	 */
	public FormInstance submitActivityForm(String docId, String userId, DocBoxFormData docBoxFormData)
			throws Exception {
		FormInstance formInstance;
		
		// TODO, när userId är null 1) kolla om det finns i formulärdata i xpath angiven av startformdef 2) anonymous om startformdef tillåter anonym
		log.info("submitActivityForm docId=" + docId + " userId=" + userId + " docBoxFormData=" + docBoxFormData);
		try {
			formInstance = activitiEngineService.submitForm(docId, userId, docBoxFormData);
			if (formInstance == null) {

				// maybe a start form
				ProcessActivityFormInstance activity = taskFormDb.getProcessActivityFormInstanceByFormDocId(docId);
				if (activity != null) {
					Date tstamp = new Date();
					activity.setSubmitted(tstamp);
					activity.setUserId(userId);
					
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
								log.error("Unknown AuthTypeReq in StartFormDefinition, this should never happen...");
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
						variables.put(FormEngine.START_FORM_DATA_URI, activity.getFormDataUri());
						
						String processInstanceUuid = activitiEngineService.startProcess(pDefUuid, variables, userId);
						activity.setProcessInstanceUuid(processInstanceUuid);
						
						taskFormDb.saveProcessActivityFormInstance(activity);
						
						formInstance = activitiEngineService.getFormEngine().getStartLogItem(activitiEngineService.getEngine().getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceUuid).includeProcessVariables().singleResult(), userId);

					}
				}
			}
		} catch (Exception e) {
			log.error("Exception while submitting activity with docId=[" + docId + "] as userId=[" + userId + "] Exception: " + e);
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
			log.error("getUsersByRoleAndActivity roleName=" + roleName + 
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
	public UserInfo getUserBySerial(String serial,String gn , String sn , String dn , String cn ) {
		UserInfo userInfo = taskFormDb.getUserBySerial(serial);

		if (userInfo == null) {
			// new user in system

		//	String gn = null, sn = null, cn = null;
			String uuid = java.util.UUID.randomUUID().toString();
	//		String dn = null;


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
			log.error("TaskFormService.dirSearchUserEntries: " + 
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
			log.error("TaskFormService.dirLookupUserEntries: " +
				   (userDirectoryService==null ? "null" : "nonull") +
				   " Exception:" + e.toString());
		}
		return result;
	}
	
	public DocBoxFormData getDocBoxFormDataToSign(ActivityInstanceItem activity, Locale locale) {
		return activitiEngineService.getDocBoxFormDataToSign(activity, locale);
	}

	public ActivityInstanceLogItem getActivityInstanceLogItemToNotify(ActivityInstanceItem activity, Locale locale) {
		return activitiEngineService.getActivityInstanceLogItemToNotify(activity, locale);
	}

}

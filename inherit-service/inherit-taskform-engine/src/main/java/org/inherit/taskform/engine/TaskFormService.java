package org.inherit.taskform.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.inherit.bonita.client.BonitaEngineServiceImpl;
import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.domain.ActivityInstanceLogItem;
import org.inherit.service.common.domain.ActivityInstancePendingItem;
import org.inherit.service.common.domain.ActivityWorkflowInfo;
import org.inherit.service.common.domain.CommentFeedItem;
import org.inherit.service.common.domain.DashOpenActivities;
import org.inherit.service.common.domain.InboxTaskItem;
import org.inherit.service.common.domain.ProcessInstanceDetails;
import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.inherit.service.common.domain.Tag;
import org.inherit.service.common.domain.TimelineItem;
import org.inherit.taskform.engine.persistence.TaskFormDb;
import org.inherit.taskform.engine.persistence.entity.ActivityFormDefinition;
import org.inherit.taskform.engine.persistence.entity.ProcessActivityFormInstance;
import org.inherit.taskform.engine.persistence.entity.StartFormDefinition;

public class TaskFormService {
	
	public static final Logger log = Logger.getLogger(TaskFormService.class.getName());
	
	TaskFormDb taskFormDb;
	BonitaEngineServiceImpl bonitaClient;
	
	public TaskFormService() {
		taskFormDb = new TaskFormDb();
		bonitaClient = new BonitaEngineServiceImpl();
	}
	
	/**
	 * 
	 * @param userId
	 * @param remainingDays atRisk activities is calculated as the number of activities that will be overdue within remainingDays
	 * @return
	 */
	public DashOpenActivities getDashOpenActivitiesByUserId(String userId, int remainingDays) {
		DashOpenActivities result = bonitaClient.getDashOpenActivitiesByUserId(userId, remainingDays);
		return result;
	}
	
	/**
	 * Get inbox task items a) partially filled start forms, not started processes 
	 * b) started process with tasks associated to userId, check if partially filled form exist
	 * @param userId
	 * @return
	 */
	public List<InboxTaskItem> getInboxTaskItems(String userId) {
		// activityDefinitionUUID vill bonita form engine ha det? lägg i så fall till uppslag på lämplig plats....
		List<InboxTaskItem> unsubmittedStartForms = new ArrayList<InboxTaskItem>();
		
		log.severe("=======> getInboxTaskItems " + userId); 
		// Find partially filled not submitted forms
		List<ProcessActivityFormInstance> pending;
		pending = taskFormDb.getPendingProcessActivityFormInstances(userId);
		
		HashMap<String, ProcessActivityFormInstance> forms = new HashMap<String, ProcessActivityFormInstance>();
		for (ProcessActivityFormInstance form : pending) {
			log.severe("=======> PENDING form: " + form);
			String activityInstanceUuid = form.getActivityInstanceUuid();
			if (activityInstanceUuid != null && activityInstanceUuid.trim().length()>0) {
				forms.put(activityInstanceUuid, form);
			}
			else {
				// this is a pending start form i.e. no process instance exist 
				InboxTaskItem startFormItem = new InboxTaskItem();
				startFormItem.setProcessActivityFormInstanceId(null);
				startFormItem.setActivityCreated(null); //TODO fill with timestamp ?
				startFormItem.setActivityLabel("Ansökan"); // TODO bonita login 
				startFormItem.setProcessActivityFormInstanceId(form.getProcessActivityFormInstanceId());
				if (form.getStartFormDefinition()!=null) {
					startFormItem.setProcessLabel(""); //TODO
					//startFormItem.setProcessLabel(bonitaClient.getProcessLabel(form.getStartFormDefinition().getProcessDefinitionUuid()));
				}
				else {
					startFormItem.setProcessLabel("");
				}
				unsubmittedStartForms.add(startFormItem);
			}
		}

		// find inbox tasks from BOS engine
		List<InboxTaskItem> inbox = bonitaClient.getUserInbox(userId);
		for (InboxTaskItem item : inbox) {
			String taskUuid = item.getTaskUuid();
			log.severe("=======> TASK bonita: " + item);
			ProcessActivityFormInstance form = forms.get(taskUuid);
			if (form != null) {
				// partial filled not submitted form exist for task	
				// assign id to inbox item that can be used later to edit/view existing form
				item.setProcessActivityFormInstanceId(form.getProcessActivityFormInstanceId());	
				forms.remove(form);
			}
			//else {
				// no one has opened this activity form yet
				// i.e. no ProcessActivityFormInstance has been stored in database so far
				// The ProcessActivityFormInstance is created on first time 
			//}
		}
		
		// partially filled forms 
		for (ProcessActivityFormInstance partForm : forms.values()) {
			if (partForm.getProcessInstanceUuid() == null) {
				// partially filled start form that will start a process if submitted

				log.warning("Unexpected partially filled form=" + partForm);
			}
			else {
				// there is a partially filled form associated to this user but the corresponding BPMN 
				// activity is no longer associated to this user
				log.info("This partial filled form is associated to a process/task instance but is not associated to an active task in user's inbox: " + partForm );
			}
		}
		
		inbox.addAll(unsubmittedStartForms);
		
		log.severe("=======> getInboxTaskItems " + userId + " size=" + (inbox==null ? 0 : inbox.size())); 
		return inbox;
	}
	
	public ProcessInstanceDetails getProcessInstanceDetails(String processInstanceUuid) {
		ProcessInstanceDetails details = bonitaClient.getProcessInstanceDetails(processInstanceUuid);
		appendTaskFormServiceData(details);
		return details;
	}
	
	public ProcessInstanceDetails getProcessInstanceDetailsByActivityInstance(String activityInstanceUuid) {
		ProcessInstanceDetails details = bonitaClient.getProcessInstanceDetailsByActivityInstance(activityInstanceUuid);
		appendTaskFormServiceData(details);
		return details;
	}
	
	private void appendTaskFormServiceData(ProcessInstanceDetails details) {
		if (details != null) {
			for (ActivityInstancePendingItem pendingItem : details.getPending()) {
				// pending activity => set edit url if ProcessActivityFormInstance is initialized
				ProcessActivityFormInstance activity = taskFormDb.getProcessActivityFormInstanceByActivityInstanceUuid(pendingItem.getActivityInstanceUuid());
				if (activity != null) {
					pendingItem.setFormUrl(activity.calcEditUrl());
				}
			}
			
			for (TimelineItem timelineItem : details.getTimeline().getItems()) {
				if (timelineItem instanceof ActivityInstanceLogItem) {
				  ActivityInstanceLogItem logItem = (ActivityInstanceLogItem)timelineItem;
					ProcessActivityFormInstance activity = taskFormDb.getProcessActivityFormInstanceByActivityInstanceUuid(logItem.getActivityInstanceUuid());
					// log item => set view url
					if (activity != null) {
						String viewUrl = activity.calcViewUrl();
						logItem.setFormUrl(viewUrl);
						logItem.setViewUrl(viewUrl);
					}
				}
			}
			ActivityInstanceLogItem startLogItem = getStartFormActivityInstanceLogItem(details.getProcessInstanceUuid());
			details.getTimeline().addAndSort(startLogItem);
		}
	}
	
	public int addComment(String activityInstanceUuid, String comment, String userId) {
		int result = bonitaClient.addComment(activityInstanceUuid, comment, userId); 
		return result;
	}
	
	public List<CommentFeedItem> getCommentFeed(String activityInstanceUuid, String userId) {
		List<CommentFeedItem> result = bonitaClient.getProcessInstanceCommentFeedByActivity(activityInstanceUuid); 
		return result;
	}
	
	public ActivityWorkflowInfo getActivityWorkflowInfo(String activityInstanceUuid) {
		ActivityWorkflowInfo result = bonitaClient.getActivityWorkflowInfo(activityInstanceUuid); 
		return result;
	}

	public ActivityWorkflowInfo assignTask(String activityInstanceUuid, String userId) {
		ActivityWorkflowInfo result = bonitaClient.assignTask(activityInstanceUuid, userId); 
		return result;
	}
	
	public ActivityWorkflowInfo unassignTask(String activityInstanceUuid) {
		ActivityWorkflowInfo result = bonitaClient.unassignTask(activityInstanceUuid); 
		return result;		
	}

	public ActivityWorkflowInfo addCandidate(String activityInstanceUuid, String userId) {
		ActivityWorkflowInfo result = bonitaClient.addCandidate(activityInstanceUuid, userId); 
		return result;
	}

	public ActivityWorkflowInfo removeCandidate(String activityInstanceUuid, String userId) {
		ActivityWorkflowInfo result = bonitaClient.removeCandidate(activityInstanceUuid, userId); 
		return result;
	}

	public ActivityWorkflowInfo setActivityPriority(String activityInstanceUuid, int priority) {
		ActivityWorkflowInfo result = bonitaClient.setPriority(activityInstanceUuid, priority); 
		return result;		
	}

	public ActivityInstanceLogItem getStartFormActivityInstanceLogItem(String processInstanceUuid) {
		ActivityInstanceLogItem result = null;
		
		if (processInstanceUuid != null) {
			
			ProcessActivityFormInstance startActivity = taskFormDb.getStartProcessActivityFormInstanceByProcessInstanceUuid(processInstanceUuid);
			if (startActivity != null) {
				result = new ActivityInstanceLogItem();
				result.setViewUrl(startActivity.calcViewUrl());
				result.setFormUrl(startActivity.calcViewUrl());
				result.setEndDate(startActivity.getSubmitted());
				result.setActivityLabel(startActivity.getFormPath());
				result.setPerformedByUserId(startActivity.getUserId());
			}
		}
		
		return result;
	}

	public String submitStartForm(String formPath, String docId, String userId) {
		log.severe("formPath=[" + formPath + "] docId=[" + docId + "] userId=[" + userId + "]" );
		// lookup the process this start form is configured to start.
		StartFormDefinition startFormDef = taskFormDb.getStartFormDefinitionByFormPath(formPath);
		String processDefinitionUUIDStr = startFormDef.getProcessDefinitionUuid();
		
		// start the process
		String processInstanceUuid = bonitaClient.startProcess(processDefinitionUUIDStr, userId);
		
		// store the start activity 
		ProcessActivityFormInstance activity = new ProcessActivityFormInstance();
		activity.setFormDocId(docId);
		activity.setStartFormDefinition(startFormDef);
		activity.setFormPath(formPath);
		activity.setProcessInstanceUuid(processInstanceUuid);
		activity.setSubmitted(new Date());
		activity.setUserId(userId);
		taskFormDb.saveProcessActivityFormInstance(activity);
		
		return processInstanceUuid;
	}
	
	
	private void apppendTaskFormServiceData (ActivityInstanceItem item, ProcessActivityFormInstance activityFormInstance) {
		if (item instanceof ActivityInstanceLogItem) {
			item.setFormUrl(activityFormInstance.calcViewUrl());
		}
		else {
			item.setFormUrl(activityFormInstance.calcEditUrl());
		}
		if (activityFormInstance.getProcessActivityFormInstanceId()!=null) {
			item.setProcessActivityFormInstanceId(activityFormInstance.getProcessActivityFormInstanceId().longValue());
		}
	}
	
	public ActivityInstanceItem getActivityInstanceItem(String activityInstanceUuid, String userId) {
		ActivityInstanceItem result = null;
		ProcessActivityFormInstance formInstance = taskFormDb.getProcessActivityFormInstanceByActivityInstanceUuid(activityInstanceUuid);
		
		if (formInstance == null) {
			result = this.initializeActivityForm(activityInstanceUuid, userId);
		}
		else {
			result = bonitaClient.getActivityInstanceItem(activityInstanceUuid);
			apppendTaskFormServiceData(result, formInstance);
		}
		return result;
	}
	 
	public ActivityInstanceItem getActivityInstanceItem(Long processActivityFormInstanceId) {
		ActivityInstanceItem result = null;
		ProcessActivityFormInstance formInstance = taskFormDb.getProcessActivityFormInstanceById(processActivityFormInstanceId);		
		if (formInstance != null) {
			if (formInstance.isStartForm()) {
				result = getStartFormActivityInstancePendingItem(formInstance.getFormDocId());
			}
			else {
				result = bonitaClient.getActivityInstanceItem(formInstance.getActivityInstanceUuid());
			}
			apppendTaskFormServiceData(result, formInstance);
		}
		return result;
	}
	
	public ActivityInstanceItem getStartActivityInstanceItem(String formPath, String userId) {
		ActivityInstanceItem result = null;
		ProcessActivityFormInstance formInstance = taskFormDb.getStartProcessActivityFormInstanceByFormPathAndUser(formPath, userId);
		
		if (formInstance == null) {
			result = this.initializeStartForm(formPath, userId);
		}
		else {
			result = getStartFormActivityInstancePendingItem(formInstance.getFormDocId());
			apppendTaskFormServiceData(result, formInstance);
		}
		return result;
	}

	private ActivityInstanceItem getStartFormActivityInstancePendingItem(String formDocId) {
		ProcessActivityFormInstance src =  taskFormDb.getProcessActivityFormInstanceByFormDocId(formDocId);
		return processActivityFormInstance2ActivityInstancePendingItem(src);
	}
		
	private ActivityInstancePendingItem processActivityFormInstance2ActivityInstancePendingItem(ProcessActivityFormInstance src) {
		ActivityInstancePendingItem dst = new ActivityInstancePendingItem();
		dst.setProcessDefinitionUuid(src.getStartFormDefinition().getProcessDefinitionUuid());
		dst.setActivityName("StartCaseTODO");
		dst.setActivityLabel("Starta ärende TODO");
		dst.setStartDate(null);
		dst.setCurrentState("TODO");
		dst.setLastStateUpdate(null);
		dst.setLastStateUpdateByUserId(src.getUserId());
		dst.setExpectedEndDate(null);
		dst.setPriority(0);
		dst.setStartedBy("");
		dst.setAssignedUserId(src.getUserId());
		dst.setExpectedEndDate(null);
		dst.setFormUrl(src.calcEditUrl());
		
		dst.setProcessInstanceUuid(null);
		dst.setActivityInstanceUuid(null);
		dst.setActivityDefinitionUuid(null);
		
		return dst;
	}

	
	private ActivityInstanceItem initializeActivityForm(String activityInstanceUuid, String userId) {
		ActivityInstanceItem activity = bonitaClient.getActivityInstanceItem(activityInstanceUuid);
		
		log.severe("===> activity=" + activity);
		StartFormDefinition startFormDefinition = taskFormDb.getStartFormDefinition(activity.getActivityDefinitionUuid(), activity.getProcessInstanceUuid());
		log.severe("===> startFormDefinition=" + startFormDefinition);
		ActivityFormDefinition activityFormDefinition = null;
		Long startFormId = null;
		if (startFormDefinition != null) {
			startFormId = startFormDefinition.getStartFormDefinitionId();
		}
		log.severe("===> activityInstanceUuid=" + activityInstanceUuid + ", startFormId=" + startFormId + ", userId=" + userId);
		activityFormDefinition = taskFormDb.getActivityFormDefinition(activity.getActivityDefinitionUuid(), startFormId);
		
		// generate a type 4 UUID
		String docId = java.util.UUID.randomUUID().toString();
		
		// store the start activity 
		ProcessActivityFormInstance activityInstance = new ProcessActivityFormInstance();
		activityInstance.setFormDocId(docId);
		activityInstance.setStartFormDefinition(startFormDefinition);
		activityInstance.setFormPath(activityFormDefinition.getFormPath());
		activityInstance.setProcessInstanceUuid(activity.getProcessInstanceUuid());
		activityInstance.setActivityInstanceUuid(activity.getActivityInstanceUuid());
		activityInstance.setUserId(userId);
		taskFormDb.saveProcessActivityFormInstance(activityInstance);
		
		apppendTaskFormServiceData(activity, activityInstance);
		return activity;
	}
	
	private ActivityInstanceItem initializeStartForm(String formPath, String userId) {
		log.severe("formPath=[" + formPath + "] userId=[" + userId + "]" );
		
		// lookup the process this start form is configured to start.
		StartFormDefinition startFormDefinition = taskFormDb.getStartFormDefinitionByFormPath(formPath);
		
		// generate a type 4 UUID
		String docId = java.util.UUID.randomUUID().toString();
		
		// store the start form activity 
		ProcessActivityFormInstance activityInstance = new ProcessActivityFormInstance();
		activityInstance.setFormDocId(docId);
		activityInstance.setStartFormDefinition(startFormDefinition);
		activityInstance.setFormPath(formPath);
		activityInstance.setProcessInstanceUuid(null);
		activityInstance.setActivityInstanceUuid(null);
		activityInstance.setSubmitted(null);
		activityInstance.setUserId(userId);
		
		taskFormDb.saveProcessActivityFormInstance(activityInstance);
		return processActivityFormInstance2ActivityInstancePendingItem(activityInstance);
	}
	
	/**
	 * submit form
	 * @param docId
	 * @param userId
	 * @return confirmation form viewUrl. null if submission fails.
	 */
	public String submitActivityForm(String docId, String userId) {
		String viewUrl = null;
		ProcessActivityFormInstance activity = taskFormDb.getProcessActivityFormInstanceByFormDocId(docId);
		
		if (activity == null) {
			log.severe("This should never happen :) cannot find activity with docId=[" + docId + "]" + " userId=[" + userId + "]");
		}
		else {			
			Date tstamp = new Date();
			activity.setSubmitted(tstamp);
			activity.setUserId(userId);
			
			boolean success = false;
			if (activity.isStartForm()) {
				// start the process
				String pDefUuid = activity.getStartFormDefinition().getProcessDefinitionUuid();
				String processInstanceUuid = bonitaClient.startProcess(pDefUuid, userId);
				activity.setProcessInstanceUuid(processInstanceUuid);
				success = (processInstanceUuid!=null && processInstanceUuid.trim().length()>0);
			}
			else if(bonitaClient.executeTask(activity.getActivityInstanceUuid(), userId)) {
				// execute activity task
				success = true;
			}
			
			if (success) {
				// if process/task in BPM engine is started/executed successful 
				// => update status of ProcessActivityFormInstance to submitted
				taskFormDb.saveProcessActivityFormInstance(activity);
				// calculate URL that can be used to render the confirmation
				viewUrl = activity.calcViewUrl();				
			}
		}
		return viewUrl;
	}
		
	public List<ProcessInstanceListItem> getUserInstancesList(String user) { 
		List<ProcessInstanceListItem> result = bonitaClient.getProcessInstancesStartedBy(user);
		return result;
	}
	
	public List<ProcessInstanceListItem> getProcessInstancesListByTag(String tagValue) { 
		List<ProcessInstanceListItem> result = taskFormDb.getProcessInstancesByTag(tagValue);
		return result;
	}

	
	public Tag addTag(Long processActivityFormInstanceId, Long tagTypeId, String value, String userId) {
		return taskFormDb.addTag(processActivityFormInstanceId, tagTypeId, value, userId);
	}

	public boolean deleteTag(String processInstanceUuid, String value, String userId) {
		return taskFormDb.deleteTag(processInstanceUuid, value, userId);
	}
	
	public List<Tag> getTagsByProcessInstance(String processInstanceUuid) {
		return taskFormDb.getTagsByProcessInstance(processInstanceUuid);
	}
}

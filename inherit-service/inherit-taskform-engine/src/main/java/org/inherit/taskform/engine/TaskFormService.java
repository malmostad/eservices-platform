package org.inherit.taskform.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.inherit.bonita.client.BonitaEngineServiceImpl;
import org.inherit.service.common.domain.ActivityFormInfo;
import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.domain.ActivityInstanceLogItem;
import org.inherit.service.common.domain.ActivityInstancePendingItem;
import org.inherit.service.common.domain.InboxTaskItem;
import org.inherit.service.common.domain.ProcessInstanceDetails;
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
	 * Get inbox task items a) partially filled start forms, not started processes 
	 * b) started process with tasks associated to userId, check if partially filled form exist
	 * @param userId
	 * @return
	 */
	public List<InboxTaskItem> getInboxTaskItems(String userId) {
		// activityDefinitionUUID vill bonita form engine ha det? lägg i så fall till uppslag på lämplig plats....
		
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
				item.setEditFormUrl(form.calcEditUrl());
				forms.remove(form);
			}
			else {
				// no one has opened this activity form yet
				// i.e. no ProcessActivityFormInstance has been stored in database so far
			}
		}
		
		// append partially filled forms
		for (ProcessActivityFormInstance partForm : forms.values()) {
			if (partForm.getProcessInstanceUuid() == null) {
				// partially filled start form that will start a process if submitted
				
				StartFormDefinition startForm = taskFormDb.getStartFormDefinitionByFormPath(partForm.getFormPath());
				String processLabel = bonitaClient.getProcessLabel(startForm.getProcessDefinitionUuid());
				
				
				InboxTaskItem partItem = new InboxTaskItem();
				partItem.setActivityCreated(null); //TODO fill with timestamp 
				partItem.setActivityLabel("START");
				partItem.setProcessActivityFormInstanceId(partForm.getProcessActivityFormInstanceId());
				partItem.setProcessLabel(processLabel);
				
				inbox.add(partItem);
			}
			else {
				// there is a partially filled form associated to this user but the corresponding BPMN 
				// activity is no longer associated to this user
				log.info("This partial filled form is associated to a process/task instance but is not associated to an active task in user's inbox: " + partForm );
			}
		}
		
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
			for (ActivityInstanceLogItem logItem : details.getActivityLog()) {
				ProcessActivityFormInstance activity = taskFormDb.getProcessActivityFormInstanceByActivityInstanceUuid(logItem.getActivityInstanceUuid());
				// log item => set view url
				if (activity != null) {
					logItem.setFormUrl(activity.calcViewUrl());
				}
			}
		}
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
	
	private ActivityInstanceItem initializeActivityForm(String activityInstanceUuid, String userId) {
		String formEditUrl = null;
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
			
			if(bonitaClient.executeTask(activity.getActivityInstanceUuid(), userId)) {
				// if task in BPM engine is executed succussfull 
				// => update status of ProcessActivityFormInstance to submitted 
				taskFormDb.saveProcessActivityFormInstance(activity);
				viewUrl = activity.calcViewUrl();
			}
		}
		return viewUrl;
	}
	
	
	/*
	public ActivityFormInfo getFormPathInfo(String processDefinitionUuid, String processActivityFormInstanceIdStr, String taskUuid) {
		Long processActivityFormInstanceId = null;
		
		ActivityFormInfo result = new ActivityFormInfo();
		if (processActivityFormInstanceIdStr != null && processActivityFormInstanceIdStr.trim().length()>0) {
			
			try {
				processActivityFormInstanceId = Long.parseLong(processActivityFormInstanceIdStr);
			}
			catch (NumberFormatException nfe) {
				log.warning("Could not parse Long from [" + processActivityFormInstanceIdStr + "]");
			}
		}
		
		// at first use processActivityFormInstanceId
		if (processActivityFormInstanceId != null) {
			ProcessActivityFormInstance actInst = taskFormDb.getProcessActivityFormInstanceById(processActivityFormInstanceId);
			String url = actInst.getFormPath();
			if (actInst.getFormDocId() != null && actInst.getFormDocId().trim().length()>0) {
				url +=  "/edit/" + actInst.getFormDocId().trim() + "?orbeon-embeddable=true";
			}
			else {
				url += "/new?orbeon-embeddable=true";
			}
			result.setFormUrl(url);
		}
		else if (taskUuid != null && taskUuid.trim().length()>0){
		// no hit on processActivityFormInstanceId => use taskUuid
			// no partially saved form => open new form
			String activityDefUuid = this.bonitaClient.getActivityDefintionUuid(taskUuid);
			ActivityFormDefinition ad = taskFormDb.getActivityDefinitionByUuid(activityDefUuid);
			
			String url = ad.getFormPath() + "/new?orbeon-embeddable=true";
		}	
		else {
		// no hit on taskUuid => start form with processDefinitionUuid
			//TODO taskFormDb.getProcessDefinitionsByUuid(processDefinitionUuid);
		}
		return result;
	}
	*/
	
}

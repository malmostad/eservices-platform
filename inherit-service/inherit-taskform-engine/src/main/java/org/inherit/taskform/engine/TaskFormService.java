package org.inherit.taskform.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.inherit.bonita.client.BonitaEngineServiceImpl;
import org.inherit.service.common.domain.ActivityFormInfo;
import org.inherit.service.common.domain.InboxTaskItem;
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
				forms.remove(form);
			}
			else {
				// ProcessActivityFormInstance written by another user?
				
				// TODO
			}
		}
		
		// append partially filled "start forms" that will start a process if submitted
		for (ProcessActivityFormInstance partForm : forms.values()) {
			if (partForm.getProcessActivityFormInstanceId() == null && partForm.getProcessInstanceUuid() == null) {
				InboxTaskItem partItem = new InboxTaskItem();
				partItem.setActivityCreated(null); //TODO fill with timestamp 
				partItem.setActivityLabel("TODO activity label");
				partItem.setProcessActivityFormInstanceId(partForm.getProcessActivityFormInstanceId());
				partItem.setProcessLabel("TODO process label");
				
				inbox.add(partItem);
			}
			else {
				log.warning("This partial filled form is associated to a process/task instance but is not associated to an active task in user's inbox: " + partForm );
			}
		}
		
		log.severe("=======> getInboxTaskItems " + userId + " size=" + (inbox==null ? 0 : inbox.size())); 
		return inbox;
	}
	
	public String submitStartForm(String formPath, String docId, String userId) {
		
		// lookup the process this start form is configured to start.
		StartFormDefinition startFormDef = taskFormDb.getStartFormDefinitionByFormPath(formPath);
		String processDefinitionUUIDStr = startFormDef.getProcessDefinitionUuid();
		
		// start the process
		String processInstanceUuid = bonitaClient.startProcess(processDefinitionUUIDStr, userId);
		
		// store the activity 
		ProcessActivityFormInstance activity = new ProcessActivityFormInstance();
		activity.setFormDocId(docId);
		activity.setFormPath(formPath);
		activity.setProcessInstanceUuid(processInstanceUuid);
		activity.setSubmitted(new Date());
		activity.setUserId(userId);
		taskFormDb.saveProcessActivityFormInstance(activity);
		
		return processInstanceUuid;
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

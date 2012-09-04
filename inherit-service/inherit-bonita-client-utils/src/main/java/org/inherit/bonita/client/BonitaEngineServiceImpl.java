package org.inherit.bonita.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.security.auth.login.LoginContext;

import org.inherit.bonita.client.util.BonitaUtil;
import org.inherit.service.common.domain.InboxTaskItem;
import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.ow2.bonita.facade.QueryDefinitionAPI;
import org.ow2.bonita.facade.def.majorElement.ProcessDefinition;
import org.ow2.bonita.facade.exception.ProcessNotFoundException;
import org.ow2.bonita.facade.runtime.ActivityState;
import org.ow2.bonita.facade.runtime.InstanceState;
import org.ow2.bonita.facade.runtime.ProcessInstance;
import org.ow2.bonita.facade.runtime.TaskInstance;
import org.ow2.bonita.facade.uuid.ActivityInstanceUUID;
import org.ow2.bonita.facade.uuid.ProcessDefinitionUUID;
import org.ow2.bonita.facade.uuid.ProcessInstanceUUID;
import org.ow2.bonita.util.AccessorUtil;

public class BonitaEngineServiceImpl {

	public static final Logger log = Logger.getLogger(BonitaEngineServiceImpl.class.getName());
	
	HashMap<String, String> defUuid2LabelCache = new HashMap<String,String>();
	
	public BonitaEngineServiceImpl() {
		
	}
	
	public List<ProcessInstanceListItem> getUserInstancesList(String user) { // ArrayList<ProcessInstanceListItem>
		List<ProcessInstanceListItem> result = new ArrayList<ProcessInstanceListItem>();

		try {
			
			
			Set<ProcessInstance> piList = new HashSet<ProcessInstance>();
			LoginContext loginContext = BonitaUtil.loginWithUser(user); 
			piList = AccessorUtil.getQueryRuntimeAPI().getUserInstances();
			
			for (ProcessInstance pi : piList) {
	
				ProcessInstanceListItem item = new ProcessInstanceListItem();
	
				item.setProcessInstanceUuid(pi.getUUID().getValue());
				
				// find out process label
				item.setProcessLabel(getProcessLabel(pi.getProcessDefinitionUUID()));
	
				// find out process instance status
				if (InstanceState.FINISHED.equals(pi.getInstanceState())) {
					item.setStatus("Avslutad");
				}
				else if (InstanceState.CANCELLED.equals(pi.getInstanceState()) || InstanceState.ABORTED.equals(pi.getInstanceState())) {
					item.setStatus("Avbruten");
				} 
				else {
					StringBuffer sb = new StringBuffer();
					Set<TaskInstance> tasks = pi.getTasks();
					for (TaskInstance task : tasks) {
						if (ActivityState.READY.equals(task.getState()) || ActivityState.EXECUTING.equals(task.getState())) {
							sb.append(task.getActivityLabel());
						}
					}
					item.setStatus(sb.toString());
				}
				
				item.setEndDate(pi.getEndedDate());
				item.setStartDate(pi.getStartedDate());
				
				result.add(item);
			}
	
			BonitaUtil.logoutWithUser(loginContext);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}

		return result;
	}
	
	public String getActivityDefintionUuid(String taskUuid) { // ArrayList<ProcessInstanceListItem>
		String result = null;

		try {
			LoginContext loginContext = BonitaUtil.login(); 
			ActivityInstanceUUID aiUuid = new ActivityInstanceUUID(taskUuid);
			TaskInstance task = AccessorUtil.getQueryRuntimeAPI().getTask(aiUuid);

			if (task != null) {
				result = task.getActivityDefinitionUUID().getValue();
			}
			
			BonitaUtil.logout(loginContext);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}
		return result;
	}

	
	public List<InboxTaskItem> getUserInbox(String userId) { // ArrayList<ProcessInstanceListItem>
		List<InboxTaskItem> result = new ArrayList<InboxTaskItem>();

		try {
			LoginContext loginContext = BonitaUtil.login(); 
			Collection<TaskInstance> taskList = AccessorUtil.getQueryRuntimeAPI().getTaskList(userId, ActivityState.READY);
			
			for (TaskInstance taskInstance : taskList) {
				InboxTaskItem taskItem = new InboxTaskItem();
				
				taskItem.setActivityCreated(taskInstance.getStartedDate());
				taskItem.setActivityLabel(taskInstance.getActivityLabel());
				taskItem.setProcessLabel(getProcessLabel(taskInstance.getProcessDefinitionUUID()));;
				taskItem.setProcessActivityFormInstanceId(new Long(0)); // TODO
				taskItem.setTaskUuid(taskInstance.getUUID().getValue());
				taskItem.setActivityDefinitionUUID(taskInstance.getActivityDefinitionUUID().getValue());
				result.add(taskItem);
			}
			BonitaUtil.logout(loginContext);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}
		return result;
	}
	
	/**
	 * @param processDefinitionUUIDStr Specifies which process to start in the BPM engine. The engine will try to find latest version of process if version is omitted. (process name instead of uuid)
	 * @param userid The user that will be logged as process initiator
	 * @return BPM engine's processInstanceUuid that identifies the created process instance in the BPM engine
	 */
	public String startProcess(String processDefinitionUUIDStr, String userid) {
			String result = null;
			
	    	try {
	    	
	    		LoginContext loginContext = BonitaUtil.loginWithUser(userid);

	    		ProcessDefinition processDefinition = null;
	    		if (processDefinitionUUIDStr!=null && processDefinitionUUIDStr.indexOf("--")>=0) {
                    log.info("Start process with explicit version: " + processDefinitionUUIDStr);
    	    		ProcessDefinitionUUID processDefinitionUUID = new ProcessDefinitionUUID(processDefinitionUUIDStr);
    	    	    processDefinition = AccessorUtil.getQueryDefinitionAPI().getProcess(processDefinitionUUID);            }
	    		else {
	    			// no version in uuid, try to find last deployed process with processName=processDefinitionUUIDStr
                    log.info("Start process with latest version: " + processDefinitionUUIDStr);
                    processDefinition = AccessorUtil.getQueryDefinitionAPI().getLastProcess(processDefinitionUUIDStr);      
	    		}
	    		
	    		if  (processDefinition != null) {
	    			log.info("Start process: " + processDefinition.getUUID() + ", user: " + userid );
	    			
	    			Map<String,Object> variables = new HashMap<String, Object>();
	    			//variables.put("docId", docId);
	    			//variables.put("formId", formId);

	    			ProcessInstanceUUID instanceUuid = AccessorUtil.getRuntimeAPI().instantiateProcess(processDefinition.getUUID(), variables);
	    			if (instanceUuid != null) {
	    				result = instanceUuid.getValue();
	    			}
	    		}
	            
		        BonitaUtil.logoutWithUser(loginContext);

	    	} catch (Exception e) {
	        	log.severe("Failed to start process: " + e); // instance=TestaCheckboxlist--1.0--8
	        }
	    	
	        return result;
		
	}
		
	private String getProcessLabel(ProcessDefinitionUUID processDefinitionUUID) {
		String result = defUuid2LabelCache.get(processDefinitionUUID.getValue());

		if (result == null) {
			QueryDefinitionAPI definitionAPI = AccessorUtil.getQueryDefinitionAPI();
			try {
				ProcessDefinition processDefinition = definitionAPI.getProcess(processDefinitionUUID);
				result = processDefinition.getLabel();
			} catch (ProcessNotFoundException e) {
				result = "n/a";
			}
			defUuid2LabelCache.put(processDefinitionUUID.getValue(), result);
		}

		return result;
	}
	
}

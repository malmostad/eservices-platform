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
import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.domain.ActivityInstanceLogItem;
import org.inherit.service.common.domain.ActivityInstancePendingItem;
import org.inherit.service.common.domain.CommentFeedItem;
import org.inherit.service.common.domain.DashOpenActivities;
import org.inherit.service.common.domain.InboxTaskItem;
import org.inherit.service.common.domain.ProcessInstanceDetails;
import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.ow2.bonita.facade.BAMAPI;
import org.ow2.bonita.facade.QueryDefinitionAPI;
import org.ow2.bonita.facade.def.majorElement.ActivityDefinition;
import org.ow2.bonita.facade.def.majorElement.ProcessDefinition;
import org.ow2.bonita.facade.exception.ProcessNotFoundException;
import org.ow2.bonita.facade.runtime.ActivityInstance;
import org.ow2.bonita.facade.runtime.ActivityState;
import org.ow2.bonita.facade.runtime.Comment;
import org.ow2.bonita.facade.runtime.InstanceState;
import org.ow2.bonita.facade.runtime.ProcessInstance;
import org.ow2.bonita.facade.runtime.TaskInstance;
import org.ow2.bonita.facade.uuid.ActivityInstanceUUID;
import org.ow2.bonita.facade.uuid.ProcessDefinitionUUID;
import org.ow2.bonita.facade.uuid.ProcessInstanceUUID;
import org.ow2.bonita.light.LightActivityInstance;
import org.ow2.bonita.light.LightProcessInstance;
import org.ow2.bonita.util.AccessorUtil;

public class BonitaEngineServiceImpl {

	public static final Logger log = Logger.getLogger(BonitaEngineServiceImpl.class.getName());
	
	HashMap<String, String> defUuid2LabelCache = new HashMap<String,String>();
	
	public BonitaEngineServiceImpl() {
		
	}
	
	public boolean executeTask(String activityInstanceUuid, String userId) {
		boolean successful = false;
		ActivityInstanceUUID activityInstanceUUID = new ActivityInstanceUUID(activityInstanceUuid);

		try {
			LoginContext loginContext = BonitaUtil.loginWithUser(userId); 
			AccessorUtil.getRuntimeAPI().executeTask(activityInstanceUUID, false);
			successful = true;
			BonitaUtil.logoutWithUser(loginContext);
		} catch (Exception e) {
			log.severe("Could not execute task: " + e);
		}

		return successful;
	}
		
	public ActivityInstanceItem getActivityInstanceItem(String activityInstanceUuid) {
		ActivityInstanceItem result = null;
		try {
			log.severe("activityInstanceUuid=" + activityInstanceUuid);
			LoginContext loginContext = BonitaUtil.login(); 
			ActivityInstanceUUID activityUUID = new ActivityInstanceUUID(activityInstanceUuid);
			
			ActivityInstance ai = AccessorUtil.getQueryRuntimeAPI().getActivityInstance(activityUUID);
			result = this.createActivityInstanceItem(ai);
			BonitaUtil.logout(loginContext);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}		
		return result;
	}

	
	public List<ProcessInstanceListItem> getProcessInstancesStartedBy(String user) {
		List<ProcessInstanceListItem> result = new ArrayList<ProcessInstanceListItem>();

		try {
			
			
			Set<ProcessInstance> piList = new HashSet<ProcessInstance>();
			LoginContext loginContext = BonitaUtil.loginWithUser(user); 
			piList = AccessorUtil.getQueryRuntimeAPI().getUserInstances();
			
			for (ProcessInstance pi : piList) {
				ProcessInstanceListItem item = new ProcessInstanceListItem();
				loadProcessInstanceBriefProperties(pi, item);
				result.add(item);
			}
	
			BonitaUtil.logoutWithUser(loginContext);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}

		return result;
	}
	
	public List<ProcessInstanceListItem> getProcessInstancesInvolvedUser(String user, int fromIndex, int pageSize) {
		// TODO add sort order???
		List<ProcessInstanceListItem> result = new ArrayList<ProcessInstanceListItem>();

		try {
			
			
			List<LightProcessInstance> piList = null;
			LoginContext loginContext = BonitaUtil.loginWithUser(user); 
			piList = AccessorUtil.getQueryRuntimeAPI().getLightParentProcessInstancesWithInvolvedUser(user, fromIndex, pageSize);
			
			for (LightProcessInstance pi : piList) {
				ProcessInstanceListItem item = new ProcessInstanceListItem();
				loadProcessInstanceBriefProperties(pi, item);
				result.add(item);
			}
	
			BonitaUtil.logoutWithUser(loginContext);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}

		return result;
	}
	

	/**
	 * Get detailed process instance information. 
	 * - performed manual activities
	 * - current activities
	 * - comment feed 
	 * - 
	 * @param processInstanceUuid
	 * @return
	 */
	public ProcessInstanceDetails getProcessInstanceDetails(String processInstanceUuid) {
		ProcessInstanceDetails result = null;
		
		try {
			LoginContext loginContext = BonitaUtil.login(); 
			ProcessInstanceUUID piUuid = new ProcessInstanceUUID(processInstanceUuid);
			result = getProcessInstanceDetailsByUuid(piUuid);
			BonitaUtil.logout(loginContext);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}
		
		
		return result;
	}

	public ProcessInstanceDetails getProcessInstanceDetailsByActivityInstance(String activityInstanceUuid) {
		ProcessInstanceDetails result = null;
		
		try {
			LoginContext loginContext = BonitaUtil.login(); 
			ActivityInstanceUUID aUuid = new ActivityInstanceUUID(activityInstanceUuid);
			LightActivityInstance lai = AccessorUtil.getQueryRuntimeAPI().getLightActivityInstance(aUuid);
			ProcessInstanceUUID piUuid = lai.getProcessInstanceUUID(); 
			result = getProcessInstanceDetailsByUuid(piUuid);
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

	public DashOpenActivities getDashOpenActivitiesByUserId(String userId, int remainingDays) {
		DashOpenActivities dash = null;
		
		try {
    		LoginContext loginContext = BonitaUtil.loginWithUser(userId);
    		
    		BAMAPI bamApi = AccessorUtil.getBAMAPI();
    		int openSteps = bamApi.getNumberOfUserOpenSteps();
    		int overdueSteps = bamApi.getNumberOfUserOverdueSteps();
    		int atRiskSteps = bamApi.getNumberOfUserStepsAtRisk(remainingDays);
    		
    		dash = new DashOpenActivities();
    		dash.setOnTrack(openSteps-overdueSteps-atRiskSteps);
    		dash.setAtRisk(atRiskSteps);
    		dash.setOverdue(overdueSteps);
    		 
    		BonitaUtil.logoutWithUser(loginContext);
    	} catch (Exception e) {
        	log.severe("Failed to caclulate DashOpenActivities : " + e); // instance=TestaCheckboxlist--1.0--8
        }
		
		return dash;
	}
	
	private ProcessInstanceDetails getProcessInstanceDetailsByUuid(ProcessInstanceUUID piUuid) throws Exception {
		ProcessInstanceDetails result = null;
		
		ProcessInstance pi = AccessorUtil.getQueryRuntimeAPI().getProcessInstance(piUuid);
		if (pi != null) {
			result = new ProcessInstanceDetails();
			loadProcessInstanceBriefProperties(pi, result);
			Set<ActivityInstance> ais = pi.getActivities();
			for (ActivityInstance ai : ais) {
				ActivityInstanceItem item = createActivityInstanceItem(ai);
				if (item != null) {
					result.addActivityInstanceItem(item);
				}
			}
			loadProcessInstanceCommentFeed(pi, result);
		}
		
		return result;
	}
	
	private CommentFeedItem loadCommentFeedItem(Comment comment) {
		CommentFeedItem result = new CommentFeedItem();
		if (comment.getActivityUUID() != null) {
			result.setActivityInstanceUuid(comment.getActivityUUID().getValue());
		}
		result.setMessage(comment.getMessage());
		if (comment.getInstanceUUID() != null) {
			result.setProcessInstanceUuid(comment.getInstanceUUID().getValue());
		}
		result.setTimestamp(comment.getDate());
		result.setUserId(comment.getUserId());
		return result;
	}
	
	private void loadProcessInstanceCommentFeed(ProcessInstance src, ProcessInstanceDetails dst) {
		List<CommentFeedItem> commentFeed = new ArrayList<CommentFeedItem>();
		List<Comment> comments = src.getCommentFeed();
		if (comments != null) {
			for (Comment comment : comments) {
				CommentFeedItem item = loadCommentFeedItem(comment);
				dst.getTimeline().add(item);
			}
		}
	}
	
	private void loadProcessInstanceBriefProperties(LightProcessInstance src, ProcessInstanceListItem dst) {
		dst.setProcessInstanceUuid(src.getUUID().getValue());
		
		// find out process label
		dst.setProcessLabel(getProcessLabel(src.getProcessDefinitionUUID()));

		// find out process instance status
		if (InstanceState.FINISHED.equals(src.getInstanceState())) {
			dst.setStatus("Avslutad");
		}
		else if (InstanceState.CANCELLED.equals(src.getInstanceState()) || InstanceState.ABORTED.equals(src.getInstanceState())) {
			dst.setStatus("Avbruten");
		} 
		else {
			if (src instanceof ProcessInstance) {
				StringBuffer sb = new StringBuffer();
				Set<TaskInstance> tasks = ((ProcessInstance)src).getTasks();
				for (TaskInstance task : tasks) {
					if (ActivityState.READY.equals(task.getState()) || ActivityState.EXECUTING.equals(task.getState())) {
						sb.append(task.getActivityLabel());
					}
				}
				dst.setStatus(sb.toString());
			}
			else {
				dst.setStatus("TODO!");
			}
		}
		
		dst.setEndDate(src.getEndedDate());
		dst.setStartDate(src.getStartedDate());
		dst.setStartedBy(src.getStartedBy());
	}
	
	private void loadActivityInstanceItem(ActivityInstance src, ActivityInstanceItem dst) {
		if (dst != null) {
			dst.setProcessDefinitionUuid(src.getProcessDefinitionUUID().getValue());
			dst.setProcessInstanceUuid(src.getProcessInstanceUUID().getValue());
			dst.setActivityDefinitionUuid(src.getActivityDefinitionUUID().getValue());
			dst.setActivityInstanceUuid(src.getUUID().getValue());
			dst.setActivityName(src.getActivityName());
			dst.setActivityLabel(src.getActivityLabel());
			dst.setStartDate(src.getStartedDate());
			dst.setCurrentState(src.getState().name());
			dst.setLastStateUpdate(src.getLastStateUpdate().getUpdatedDate());
			dst.setLastStateUpdateByUserId(src.getLastStateUpdate().getUpdatedBy());
			dst.setExpectedEndDate(src.getExpectedEndDate());
			dst.setPriority(src.getPriority());
			
			TaskInstance task = src.getTask();
			if (task != null) {
				dst.setStartedBy(task.getStartedBy());
			}
			else {
				dst.setStartedBy("SYSTEM");
			}
			
		}
		// TODO set type... 
	}
	
	private void loadActivityInstanceLogItem(ActivityInstance src, ActivityInstanceLogItem dst) {
		dst.setEndDate(src.getEndedDate());
		dst.setPerformedByUserId(src.getLastStateUpdate().getUpdatedBy()); // TODO check if this is correct userid
	}
	
	private void loadActivityInstancePendingItem(ActivityInstance src, ActivityInstancePendingItem dst) {
		dst.setCandidates(src.getLastAssignUpdate().getCandidates());
		dst.setAssignedUserId(src.getLastAssignUpdate().getAssignedUserId());
		dst.setExpectedEndDate(src.getExpectedEndDate());
	}
	
	private ActivityInstanceItem createActivityInstanceItem(ActivityInstance ai) {
		ActivityInstanceItem result = null;
		
		if (ai != null) {
			log.severe("BPMN activity uuid: " + ai.getActivityInstanceId() + " label=" + ai.getActivityLabel());
			if (ai.getState().equals(ActivityState.FINISHED)) {
				// Finished activity 
				if (ai.getType() == ActivityDefinition.Type.Human) {
					// only manual activities
					result = new ActivityInstanceLogItem();
					loadActivityInstanceLogItem(ai, (ActivityInstanceLogItem)result);
				}
			}
			else {
				// pending activity
				result = new ActivityInstancePendingItem();
				loadActivityInstancePendingItem(ai, (ActivityInstancePendingItem)result);
			}
			loadActivityInstanceItem(ai, result);
		}
		
		log.severe("return: " + result);
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

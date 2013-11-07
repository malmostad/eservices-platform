package org.inheritsource.service.processengine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.security.auth.login.LoginContext;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ActivityInstanceLogItem;
import org.inheritsource.service.common.domain.ActivityInstancePendingItem;
import org.inheritsource.service.common.domain.ActivityWorkflowInfo;
import org.inheritsource.service.common.domain.CommentFeedItem;
import org.inheritsource.service.common.domain.DashOpenActivities;
import org.inheritsource.service.common.domain.InboxTaskItem;
import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.common.domain.ProcessDefinitionDetails;
import org.inheritsource.service.common.domain.ProcessDefinitionInfo;
import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.domain.UserInfo;


public class ActivitiEngineService {

	private ProcessEngine engine = null; 
	public static final Logger log = Logger.getLogger(ActivitiEngineService.class.getName());
	
	public ActivitiEngineService() {
		initEngine();		
	}

	public void logTableSizes() {
		Map<String, Long> counts = engine.getManagementService().getTableCount();
		
		for (String key : counts.keySet()) {
			log.info("Key: " + key + " count: " + counts.get(key));
		}
	}
	
	private void initEngine() {

		ProcessEngineConfiguration engineConfig = null;
		
		// try to find config 
		engineConfig = loadConfigFromFile("/usr/local/etc/motrice/activiti.cfg.xml");
		
		if (engineConfig == null) {
			// fall back to default - activiti.cfg.xml resource in classpath (src/main/resources) 
			engineConfig = loadConfigFromResource("activiti.cfg.xml");
		}
		
		if (engineConfig == null) {
			log.severe("Could not find default config - the process engine will not be working");
		}
		else {
			engine =  engineConfig.buildProcessEngine();
		}
		
	}
	
	
	private ProcessEngineConfiguration loadConfigFromFile(String fileName) {
		ProcessEngineConfiguration engineConfig = null;
		try {
			engineConfig = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(new FileInputStream(fileName))
					  .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
					  .setJobExecutorActivate(true);
		}
		catch (Exception e) {
			log.severe("Could not find config file: " + fileName);
		}
		return engineConfig;
	}
	
	private ProcessEngineConfiguration loadConfigFromResource(String resource) {
		ProcessEngineConfiguration engineConfig = null;
		try {
			engineConfig = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(resource)
					  .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
					  .setJobExecutorActivate(true);
		}
		catch (Exception e) {
			log.severe("Could not find config resource: " + resource);
		}
		return engineConfig;
	}
	
	public Deployment deployBpmn(String bpmnFile) {
		RepositoryService repositoryService = engine.getRepositoryService();
		Deployment deployment = null;
		
		try {
			String resourceName = Paths.get(bpmnFile).getFileName().toString();
			deployment = repositoryService.createDeployment().
				addInputStream(resourceName, new FileInputStream(bpmnFile)).deploy();
			
		} catch (Exception e) {
			log.severe("File '" + bpmnFile + "' not found: " + e.getMessage());
		}
		return deployment;
	}
	
	public ProcessInstance startProcessInstanceByKey(String key, Map<String, Object> variables) {
		RuntimeService runtimeService = engine.getRuntimeService();
		ProcessInstance processInstance = null;
		
		try {
			processInstance = runtimeService.startProcessInstanceByKey(key, variables);
		} catch (Exception e) {
			log.severe("Unable to start process instance with key: " + key);
		}
		return processInstance;
	}
		
	public void listDeployedProcesses() {
		List<ProcessDefinition> processes = engine.getRepositoryService().createProcessDefinitionQuery().list();
		for (ProcessDefinition process : processes) {
			log.severe("Process: " + process.getId() + ": " + process + "START");
			log.severe("getDeploymentId: " + process.getDeploymentId());
			log.severe("getDescription: " + process.getDescription());
			log.severe("getDiagramResourceName: " + process.getDiagramResourceName());
			log.severe("getId: " + process.getId());
			log.severe("getKey: " + process.getKey());
			log.severe("getName: " + process.getName());
			log.severe("getResourceName: " + process.getResourceName());
			log.severe("version: " + process.getVersion());
			log.severe("END");
		}
	}
	
	public List<String> getDeployedDeploymentIds() {
		List<Deployment> deployments = engine.getRepositoryService().createDeploymentQuery().list();
		ArrayList<String> deploymentIds = new ArrayList<String>();
		for (Deployment deployment : deployments) {
			deploymentIds.add(deployment.getId());
		}
		return deploymentIds;
	}
	
	public void deleteDeploymentByDeploymentId(String deploymentId, boolean cascade) {
		engine.getRepositoryService().deleteDeployment(deploymentId, cascade);
	}
	
	
	
	public List<InboxTaskItem> getUserInbox(String userId) {
		List<InboxTaskItem> result = new ArrayList<InboxTaskItem>();
		
		List<Task> tasks = engine.getTaskService().createTaskQuery().taskInvolvedUser(userId).orderByTaskCreateTime().asc().list();
		//List<Task> tasks = engine.getTaskService().createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().asc().list();
		
		result = taskList2InboxTaskItemList(tasks);
		
		return result;
	}
	
	private List<InboxTaskItem> taskList2InboxTaskItemList(List<Task> tasks) {
		List<InboxTaskItem> result = null;
		if (tasks != null) {
			result = new ArrayList<InboxTaskItem>();
			for (Task task : tasks) {
				result.add(task2InboxTaskItem(task));
			}
	    }
		return result;
	}
	
	private InboxTaskItem task2InboxTaskItem(Task task) {
		InboxTaskItem item = null;
		if (task != null) {
			item = new InboxTaskItem();
			item.setActivityCreated(task.getCreateTime());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityLabel(task.getName());
			item.setExpectedEndDate(task.getDueDate());
			item.setExternalUrl(""); // Will be set in TaskFormService
			item.setProcessActivityFormInstanceId(new Long(0)); // Will be set in TaskFormService
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setProcessLabel("Process Label");  // FIXME: Maybe get some name from process definition?
			item.setStartedByFormPath(""); // Will be set in TaskFormService
			item.setTaskUuid(task.getId()); // Note task.getId() gives a simple int ant is not a real uuid
		}
		return item;
	}
	
	
	public Set<ProcessDefinitionInfo> getProcessDefinitions() {
		// TODO Auto-generated method stub
		return null;	
	}

	public ProcessDefinitionDetails getProcessDefinitionDetailsByUuid(
			String processDefinitionUUIDStr) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getActivityInstanceUuid(String processInstanceUuid,
			String activityName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ActivityInstanceItem getActivityInstanceItem(
			String activityInstanceUuid) {
		ActivityInstanceItem result = null;
		
		//activityInstanceUuid is taskId
		Task task = engine.getTaskService().createTaskQuery().taskId(activityInstanceUuid).singleResult();
		
		if(task != null) {
			result = task2ActivityInstancePendingItem(task);
		} else {
			HistoricTaskInstance historicTask = engine.getHistoryService().createHistoricTaskInstanceQuery().taskId(activityInstanceUuid).singleResult();
			if(historicTask != null) {
				result = task2ActivityInstanceLogItem(historicTask);
			}
		}
		
		return result;
	}
	
	private ActivityInstanceItem task2ActivityInstancePendingItem(Task task) {
		ActivityInstancePendingItem item = null;
		if (task != null) {
			item = new ActivityInstancePendingItem();
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());
			item.setActivityName(task.getName());
			item.setActivityLabel(""); // FIXME What to map?
			item.setStartDate(task.getCreateTime());
			item.setCurrentState("FIXME"); // task.getDelegationState()?
			item.setLastStateUpdate(null);  // FIXME  Mapping??
			item.setLastStateUpdateByUserId("FIXME"); // FIXME: Mapping Owner / assigned ??
			item.setStartedBy(task.getOwner()); // FIXME: Mapping correct??
			item.setProcessActivityFormInstanceId(new Long(0)); // FIXME
			item.setFormUrl("");
			item.setFormDocId("");
			item.setActivityType(99); // FIXME
			item.setPriority(task.getPriority());
			item.setExpectedEndDate(task.getDueDate());
			
			// ActivityInstancePendingItem
			item.setCandidates(new HashSet<UserInfo>()); //FIXME
			
			UserInfo assignedUser = new UserInfo();
			assignedUser.setUuid(task.getAssignee()); // FIXME
			assignedUser.setLabelShort(task.getAssignee()); // FIXME
			item.setAssignedUser(assignedUser);

		}
		return item;
	}
	
	private ActivityInstanceItem task2ActivityInstanceLogItem(HistoricTaskInstance task) {
		ActivityInstanceLogItem item = null;
		if (task != null) {
			item = new ActivityInstanceLogItem();
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());
			item.setActivityName(task.getName());
			item.setActivityLabel(""); // FIXME What to map?	
			item.setStartDate(task.getStartTime());
			item.setCurrentState("FIXME"); // task.getDelegationState()?
			item.setLastStateUpdate(null);  // FIXME  Mapping??
			item.setLastStateUpdateByUserId("FIXME"); // FIXME: Mapping Owner / assigned ??
			item.setStartedBy(task.getOwner()); // FIXME: Mapping correct??
			item.setProcessActivityFormInstanceId(new Long(0)); // FIXME
			item.setFormUrl("");
			item.setFormDocId("");
			item.setActivityType(99); // FIXME
			item.setPriority(task.getPriority());
			item.setExpectedEndDate(task.getDueDate());
			
			// ActivityInstancelogItem
			item.setEndDate(task.getDueDate());
			
			UserInfo performedByUser = new UserInfo();
			performedByUser.setUuid(task.getAssignee()); // FIXME
			item.setPerformedByUser(performedByUser);
			item.setViewUrl(""); // FIXME
		}
		return item;
	}
	
	public DashOpenActivities getDashOpenActivitiesByUserId(String userId,
			int remainingDays) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProcessInstanceDetails getProcessInstanceDetails(
			String processInstanceUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProcessInstanceDetails getProcessInstanceDetailsByActivityInstance(
			String activityInstanceUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	public int addComment(String activityInstanceUuid, String comment,
			String userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<CommentFeedItem> getProcessInstanceCommentFeedByActivity(
			String activityInstanceUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	public ActivityWorkflowInfo getActivityWorkflowInfo(
			String activityInstanceUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	public ActivityWorkflowInfo assignTask(String activityInstanceUuid,
			String userId) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			Task task = engine.getTaskService().createTaskQuery().taskId(activityInstanceUuid).singleResult();
			task.setAssignee(userId);
			engine.getTaskService().saveTask(task);
			
			activityWorkflowInfo = new ActivityWorkflowInfo();
			activityWorkflowInfo.setPriority(task.getPriority());
			UserInfo assignedUser = new UserInfo();
			assignedUser.setUuid(userId);
			activityWorkflowInfo.setAssignedUser(assignedUser);
			
			// FIXME: This might not be correct!!!
			List<IdentityLink> identityLinks = 
				engine.getTaskService().getIdentityLinksForTask(activityInstanceUuid);
			HashSet<UserInfo> candidates = new HashSet<UserInfo>();
			
			if(identityLinks != null) {
				for(IdentityLink iL : identityLinks) {
					if(iL.getType().equals(IdentityLinkType.CANDIDATE)) {
						UserInfo candidate = new UserInfo();
						candidate.setUuid(iL.getUserId());
						candidates.add(candidate);
					}
				}
			}
			
			activityWorkflowInfo.setCandidates(candidates);
			
		} catch (Exception e) {
			log.severe("Unable to assignTask with taskIs: " + activityInstanceUuid);
		}
		
		return activityWorkflowInfo;
	}

	public ActivityWorkflowInfo addCandidate(String activityInstanceUuid,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public ActivityWorkflowInfo removeCandidate(String activityInstanceUuid,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public ActivityWorkflowInfo setPriority(String activityInstanceUuid,
			int priority) {
		// TODO Auto-generated method stub
		return null;
	}

	public String startProcess(String processDefinitionId, String userId) {
		String processInstanceId = null;
		
		// FIXME:
		// Add some kind of user check... ???
		
		try {
			ProcessInstance processInstance = engine.getRuntimeService().startProcessInstanceById(processDefinitionId);
			processInstanceId = processInstance.getProcessInstanceId();
			// set owner and assignee for the created task
			
			Task task = engine.getTaskService().createTaskQuery().
				processInstanceId(processInstanceId).singleResult();
			task.setAssignee(userId);
			task.setOwner(userId);
			engine.getTaskService().saveTask(task);
		} catch (Exception e) {
			log.severe("Unable to start process instance with processDefinitionId: " + processDefinitionId);
		}
		
		return processInstanceId;
	}

	public boolean executeTask(String activityInstanceUuid, String userId) {
		
		// FIXME: Does clame the job?
		// Bonita uses login and logout but this is impl is not using a webservice interface so mighy be 
		// nothing to deal with?
		//  
		
		boolean successful = false;
			
		try {
			// Note: ActivitiTaskAlreadyClaimedException - when the task is already claimed by another user.
			engine.getTaskService().claim(activityInstanceUuid, userId);
			
			// Note: ActivitiException is thrown when this task is DelegationState.PENDING delegation.
			engine.getTaskService().complete(activityInstanceUuid);
			successful = true;
		} catch (Exception e) {
			log.severe("Could not executeTask with taskId: " + activityInstanceUuid + 
				" exception: " + e);
		}

		return successful;
	}

	public PagedProcessInstanceSearchResult getProcessInstancesStartedBy(
			String searchForBonitaUser, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String filter, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public PagedProcessInstanceSearchResult getProcessInstancesWithInvolvedUser(
			String searchForBonitaUser, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String filter, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public PagedProcessInstanceSearchResult getProcessInstancesByUuids(
			List<String> uuids, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean createUser(String uuid) {
		boolean successful = false;
		
		try {
			User user = engine.getIdentityService().newUser(uuid);
			engine.getIdentityService().saveUser(user);
			successful = true;
		} catch (Exception e) {
			log.severe("Could not createUser with uuid: " + uuid +  " exception: " + e);
		}
		
		return successful;
	}

	public ActivityWorkflowInfo unassignTask(String activityInstanceUuid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		ActivitiEngineService activitiEngineService = new ActivitiEngineService();
		
		//activitiEngineService.engine.getTaskService().addCandidateUser("4204", "KALLE STROPP");
		
		//ActivityWorkflowInfo aWI = activitiEngineService.assignTask("4204", "GRODAN BOLL");
		
		//log.severe(aWI.toString());
		//log.severe(" assigned User ID: " + aWI.getAssignedUser().getUuid());
		//log.severe(" candidates: " + ((UserInfo)aWI.getCandidates().toArray()[0]).getUuid()     );
		
		//activitiEngineService.createUser("nya usern lasse");
		//activitiEngineService.executeTask("4502", "dont care");
		
		/*
		log.severe("Number of process instances Before: " + 
				activitiEngineService.engine.getRuntimeService().createProcessInstanceQuery().count());     
		
		String userId = "kermit";
		String processDefinitionUUIDStr = "Arendeprocess:1:3904";
		
		activitiEngineService.startProcess(processDefinitionUUIDStr, userId);
		
		// Verify that we started a new process instance
		log.severe("Number of process instances After: " + 
				activitiEngineService.engine.getRuntimeService().createProcessInstanceQuery().count());   
*/
		
		
		/*
		// getDeployedDeploymentIds
		List<String> deploymentIds = activitiEngineService.getDeployedDeploymentIds();
		// deleteDeploymentByDeploymentId
		for(String deploymentId : deploymentIds) {
			activitiEngineService.deleteDeploymentByDeploymentId(deploymentId, true);
		}
			
		Deployment deployment = activitiEngineService.deployBpmn
				("/home/pama/workspace/motrice/pawap/bpm-processes/Arendeprocess.bpmn20.xml");
		
		log.severe("Deployment with id: " + deployment.getId());
		
		// getDeployedDeploymentIds
		//deploymentIds = activitiEngineService.getDeployedDeploymentIds();
		//log.severe("after deploy: " + deploymentIds.size());
		//for(String deploymentId : deploymentIds) {
		//	log.severe("deloymentId: " + deploymentId);
		//}
				
		
		log.severe("Number of process definitions: " + 
				activitiEngineService.engine.getRepositoryService().createProcessDefinitionQuery().count());
			
		Map<String, Object> variables = new HashMap<String, Object>();
		ProcessInstance processInstance = activitiEngineService.startProcessInstanceByKey("Arendeprocess", variables);
		
		// Verify that we started a new process instance
		log.severe("Number of process instances: " + 
			activitiEngineService.engine.getRuntimeService().createProcessInstanceQuery().count());           
		
		// set assignee for the created task with the processinstanceid above.
		
		Task task = activitiEngineService.engine.getTaskService().createTaskQuery().
			processInstanceId(processInstance.getProcessInstanceId()).singleResult();
		task.setAssignee("kermit");
		task.setOwner("kermit");
		activitiEngineService.engine.getTaskService().saveTask(task);
		*/
	
		/*
		ActivityInstanceItem activityInstanceItem = activitiEngineService.getActivityInstanceItem("3908");
		log.severe("activityInstanceItem: " + activityInstanceItem);
		
		//activitiEngineService.listDeployedProcesses();
		  
		  
		List<InboxTaskItem> tasks = activitiEngineService.getUserInbox("kermit");
		log.severe("Inbox item count: " + tasks.size());
		for (InboxTaskItem t : tasks) {
			log.severe("Inbox item: " + t);
		}
		*/
		
		System.exit(0);
	}
	
}

package org.inheritsource.service.processengine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
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
	
	public void deployBpmnProcess(String bpmnFile) {
		RepositoryService repositoryService = engine.getRepositoryService();
		
		log.severe("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());            
		log.severe("Deploy file: " + bpmnFile);            

		try {
			Deployment deployment = repositoryService.createDeployment().addInputStream("Arendeprocess", new FileInputStream(bpmnFile)).deploy();
			log.severe("Deployed id=" + deployment.getId() + " name=" + deployment.getName() + " category=" + deployment.getCategory() + " time=" + deployment.getDeploymentTime());
			
			deployment = repositoryService.createDeployment()
			  .addClasspathResource("VacationRequest.bpmn20.xml")
			  .deploy();
			log.severe("Deploye VacationRequest id=" + deployment.getId() + " name=" + deployment.getName() + " category=" + deployment.getCategory() + " time=" + deployment.getDeploymentTime());
			

			
		} catch (FileNotFoundException e) {
			log.severe("File '" + bpmnFile + "' not found: " + e.getMessage());
		}
		
		log.severe("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());            
	}
	
	public void listDeployedProcesses() {
		//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		//List<ProcessDefinition> processes = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
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
	
	
	

	public static void main(String[] args) {
		ActivitiEngineService engine = new ActivitiEngineService();
		
		//engine.logTableSizes();
		//engine.deployBpmnProcess("/home/pama/workspace/motrice/pawap/bpm-processes/Arendeprocess.bpmn20.xml");
		//engine.listDeployedProcesses();
		
		List<InboxTaskItem> tasks = engine.getUserInbox("kermit");
		log.severe("Inbox item count: " + tasks.size());
		for (InboxTaskItem task : tasks) {
			log.severe("Inbox item: " + task);
		}
		
		System.exit(0);
	}
	
	public List<InboxTaskItem> getUserInbox(String userId) {
		List<InboxTaskItem> result = new ArrayList<InboxTaskItem>();
		
		List<Task> tasks = engine.getTaskService().createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().asc().list();
		
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
			item.setProcessLabel("Process Label");  // FIXME
			item.setStartedByFormPath(""); // Will be set in TaskFormService
			item.setTaskUuid(task.getId());
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
			item = new ActivityInstancePendingItem(); // FIXME Could be LogItem?
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());// FIXME getExecutionId()??
			item.setActivityName(task.getName());
			item.setActivityLabel(task.getName()); // FIXME Wrong mapping?
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
			item.setAssignedUser(assignedUser);

		}
		return item;
	}
	
	private ActivityInstanceItem task2ActivityInstanceLogItem(HistoricTaskInstance task) {
		ActivityInstanceLogItem item = null;
		if (task != null) {
			item = new ActivityInstanceLogItem(); // FIXME Could be LogItem?
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());// FIXME getExecutionId()??
			item.setActivityName(task.getName());
			item.setActivityLabel(task.getName()); // FIXME Wrong mapping?
			item.setStartDate(task.getClaimTime()); // FIXME??
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
		// TODO Auto-generated method stub
		return null;
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

	public String startProcess(String processDefinitionUUIDStr, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean executeTask(String activityInstanceUuid, String userId) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

	public ActivityWorkflowInfo unassignTask(String activityInstanceUuid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

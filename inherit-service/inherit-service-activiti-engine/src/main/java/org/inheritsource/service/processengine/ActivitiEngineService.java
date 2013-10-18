package org.inheritsource.service.processengine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.inheritsource.service.common.domain.InboxTaskItem;

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
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<ProcessDefinition> processes = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
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
	
	public List<InboxTaskItem> getUserInbox(String userId) {
		List<InboxTaskItem> result = new ArrayList<InboxTaskItem>();
		
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().asc().list();
		
		result = taskList2InboxTaskItemList(tasks);
		
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
			item.setExternalUrl("TODO from taskformdb");
			item.setProcessActivityFormInstanceId(new Long(0)); // "TODO from taskformdb"
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setProcessLabel("TODO");
			item.setStartedByFormPath("TODO from taskformdb");
			item.setTaskUuid(task.getId());
		}
		return item;
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
	
	public static void main(String[] args) {
		ActivitiEngineService engine = new ActivitiEngineService();
		engine.logTableSizes();
		//engine.deployBpmnProcess("/home/bjmo/workspaces/motrice/pawap/bpm-processes/Arendeprocess.bpmn20.xml");
		engine.listDeployedProcesses();
		
		List<InboxTaskItem> tasks = engine.getUserInbox("kermit");
		log.severe("Inbox item count: " + tasks.size());
		for (InboxTaskItem task : tasks) {
			log.severe("Inbox item: " + task);
		}
		
		System.exit(0);
	}
}

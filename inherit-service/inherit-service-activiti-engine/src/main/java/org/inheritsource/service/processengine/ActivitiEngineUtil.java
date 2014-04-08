package org.inheritsource.service.processengine;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;



public class ActivitiEngineUtil {

	public static final Logger log = Logger.getLogger(ActivitiEngineUtil.class.getName());
	
	public static ProcessEngineConfiguration loadConfigFromFile(String fileName) {
		ProcessEngineConfiguration engineConfig = null;
		try {
			engineConfig = ProcessEngineConfiguration.
				createProcessEngineConfigurationFromInputStream(new FileInputStream(fileName));
		}
		catch (Exception e) {
			log.severe("Could not find config file: " + fileName + 
					" exeception: " + e);
		}
		return engineConfig;
	}
	
	public static ProcessEngineConfiguration loadConfigFromResource(String resource) {
		ProcessEngineConfiguration engineConfig = null;
		try {
			engineConfig = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(resource)
					  .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
					  .setJobExecutorActivate(true);
		}
		catch (Exception e) {
			log.severe("Could not find config resource: " + resource + 
					" exeception: " + e);
		}
		return engineConfig;
	}
	
	public static void listDeployedProcesses(ProcessEngine engine) {
		List<ProcessDefinition> processes = engine.getRepositoryService().createProcessDefinitionQuery().list();
		for (ProcessDefinition process : processes) {
			log.info("Process: " + process.getId() + ": " + process + "START");
			log.info("getDeploymentId: " + process.getDeploymentId());
			log.info("getDescription: " + process.getDescription());
			log.info("getDiagramResourceName: " + process.getDiagramResourceName());
			log.info("getId: " + process.getId());
			log.info("getKey: " + process.getKey());
			log.info("getName: " + process.getName());
			log.info("getResourceName: " + process.getResourceName());
			log.info("version: " + process.getVersion());
			log.info("END");
		}
	}
	
	public static void logTableSizes(ProcessEngine engine) {
		Map<String, Long> counts = engine.getManagementService().getTableCount();
		
		for (String key : counts.keySet()) {
			log.info("Key: " + key + " count: " + counts.get(key));
		}
	}
	
	/**
	 * Use preloaded local task variables from task object, 
	 * try to load variables from engine if empty
	 * @param engine
	 * @param task
	 * @return
	 */
	public static Map<String, Object> getTaskLocalVarables(ProcessEngine engine, Task task) {
		Map <String, Object> localVars = task.getTaskLocalVariables();
		
		// variables empty, try to load them
		if (localVars == null || localVars.size()==0) {
			localVars = engine.getTaskService().getVariablesLocal(task.getId());
		}
		
		return localVars;
	}
}

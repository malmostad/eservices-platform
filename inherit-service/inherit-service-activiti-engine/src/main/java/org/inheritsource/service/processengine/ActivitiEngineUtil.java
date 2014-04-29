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

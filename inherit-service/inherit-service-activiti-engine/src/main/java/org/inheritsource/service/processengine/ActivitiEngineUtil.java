package org.inheritsource.service.processengine;

import java.io.FileInputStream;
import java.util.logging.Logger;

import org.activiti.engine.ProcessEngineConfiguration;



public class ActivitiEngineUtil {

	public static final Logger log = Logger.getLogger(ActivitiEngineService.class.getName());
	
	public static ProcessEngineConfiguration loadConfigFromFile(String fileName) {
		ProcessEngineConfiguration engineConfig = null;
		try {
			engineConfig = ProcessEngineConfiguration.
				createProcessEngineConfigurationFromInputStream(new FileInputStream(fileName));
		}
		catch (Exception e) {
			log.severe("Could not find config file: " + fileName + e);
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
			log.severe("Could not find config resource: " + resource);
		}
		return engineConfig;
	}
}

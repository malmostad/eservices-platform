package org.inheritsource.service.coordinatrice;

import java.util.Locale;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.inheritsource.service.form.FormEngine;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
public class CoordinatriceFacade {
	
	public static final Logger log = Logger.getLogger(CoordinatriceFacade.class.getName());
	
	private ProcessEngine engine = null; 

	public CoordinatriceFacade() {
		
	}
	
	public ProcessEngine getEngine() {
		return engine;
	}

	public void setEngine(ProcessEngine engine) {
		this.engine = engine;
	}
	
	
	
	public ActivityLabel getLabel(String procdefkey, String locale, String activityname, int procdefversion) {
		ActivityLabel label = null;
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource service = client.resource("http://localhost:8080/coordinatrice/rest/");
			label = 
					service
					.path("activitylabel")
					.path(procdefkey)
					.path(locale)
					.path(activityname)
					.queryParam("version", String.valueOf(procdefversion))
					.accept(MediaType.APPLICATION_JSON)
					.get(ActivityLabel.class);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}
		return label;
	}
	
	
	public String getLabel(String processDefinitionId, String activityname, Locale locale) {
		log.severe("XXXXX " + processDefinitionId + " : " + activityname + " : " + (locale != null ? locale.getLanguage() : "null"));
		String result = activityname;
		
		ProcessDefinition procDef = engine.getRepositoryService().getProcessDefinition(processDefinitionId);
		
		if (procDef != null && locale != null) {
			ActivityLabel label = getLabel(procDef.getKey(), locale.getLanguage(), activityname, procDef.getVersion());
			if (label != null) {
				result = label.getLabel();
			}
		}
		
		return result;
	}

	public StartFormLabel getStartFormLabel(String appName, String formName, String locale, int formdefversion) {
		StartFormLabel label = null;
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			// $PREFIX/startformlabel/$app-name/$form-name/$locale[?version=$formdefversion]
			WebResource service = client.resource("http://localhost:8080/coordinatrice/rest/");
			label = 
					service
					.path("startformlabel")
					.path(appName)
					.path(formName)
					.path(locale)
					.queryParam("version", String.valueOf(formdefversion))
					.accept(MediaType.APPLICATION_JSON)
					.get(StartFormLabel.class);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}
		return label;
	}
	
	public String getStartFormLabelByStartFormDefinitionKey(String defKey, Locale locale, String defaultValue) {
		String label = null;
		System.out.println("Find name for: " + defKey);
		if (defKey != null) {
			String[] parts = defKey.split("/");
			
			if (parts.length == 2) {
				String appName = parts[0];
				System.out.println("appname: " + appName);
				if (parts[1].length()>6) {
					String formName = parts[1].substring(0, parts[1].length()-6);
					int formdefversion = Integer.parseInt(parts[1].substring(parts[1].length()-3, parts[1].length()));
					System.out.println("formName: " + formName);
					System.out.println("formdefversion: " + formdefversion);
					StartFormLabel startFormLabel = getStartFormLabel(appName, formName, locale.getLanguage(), formdefversion);
					if (startFormLabel != null) {
						label = startFormLabel.label;
					}
				}
				
			}
			
		}

		// no coordinatrice label, try to find a process name
		if (label == null) {
			label = defaultValue;
		}
		
		// no process name, fall back to hard coded generic value
		if (label == null || label.trim().length()==0) {
			label = "case";
		}
		return label;
	}
	
	public String getStartFormLabel(String processInstanceId, Locale locale) {
		String label = null;
		
		ProcessInstance processInstance = getEngine().getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables().singleResult();		
		
		if (processInstance != null && processInstance.getProcessVariables() != null && locale != null) {

			// First try to find form label from coordinatrice
			String defKey = (String) processInstance.getProcessVariables().get(FormEngine.START_FORM_DEFINITIONKEY);
			
			String processLabel = null;
			ProcessDefinition procdef = getEngine().getRepositoryService().getProcessDefinition(processInstance.getProcessDefinitionId());
			if (procdef != null) {
				processLabel = procdef.getName();
			}
			
			label = getStartFormLabelByStartFormDefinitionKey(defKey, locale, processLabel);
			
		}
		return label;
	}

	public ProcessDefinitionState getProcessDefinitionState(String procdefId) {
		ProcessDefinitionState state = null;
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			
			WebResource service = client.resource("http://localhost:8080/coordinatrice/rest/");
			state = 
					service
					.path("procdef")
					.path("state")
					.path(procdefId)
					.accept(MediaType.APPLICATION_JSON)
					.get(ProcessDefinitionState.class);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}
		return state;
	}

	public static void main(String[] args) {
		CoordinatriceFacade c = new CoordinatriceFacade();
		System.out.println(c.getStartFormLabel("BMTest", "BMTestForm", "sv", 1));
		System.out.println(c.getProcessDefinitionState("TestFunctionProcess1:1:9"));
		System.out.println(c.getProcessDefinitionState("TestFunctionProcess1:2:305"));
	}

}

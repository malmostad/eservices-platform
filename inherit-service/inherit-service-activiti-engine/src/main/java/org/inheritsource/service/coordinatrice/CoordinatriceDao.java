package org.inheritsource.service.coordinatrice;

import java.util.Locale;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
public class CoordinatriceDao {
	
	public static final Logger log = Logger.getLogger(CoordinatriceDao.class.getName());
	
	private ProcessEngine engine = null; 

	public CoordinatriceDao() {
		
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
	
}

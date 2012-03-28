package se.inherit.bonita.restserver;

import java.util.Map;
import java.util.HashMap;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.bonitasoft.console.security.server.api.ICredentialsEncryptionAPI;
import org.bonitasoft.console.security.server.api.SecurityAPIFactory;
import org.ow2.bonita.facade.def.majorElement.ProcessDefinition;
import org.ow2.bonita.facade.def.majorElement.impl.ProcessDefinitionImpl;
import org.ow2.bonita.facade.uuid.ProcessDefinitionUUID;
import org.ow2.bonita.facade.uuid.ProcessInstanceUUID;
import org.ow2.bonita.util.AccessorUtil;
import org.ow2.bonita.util.SimpleCallbackHandler;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.bonita.domain.ProcessDefinitionInfo;

public class StartBonitaProcess extends ServerResource {

	public static final Logger logger = LoggerFactory.getLogger(StartBonitaProcess.class);
	
	@Post("xml")
	public String startBonitaProcess() {
		String result = null;
		
		String formPath = (String)getRequestAttributes().get("formPath");
		String user = (String)getRequestAttributes().get("userid");

		String formId = ((String)getRequestAttributes().get("formId")).replaceAll("____", "/");
		
		/*
		if (formPath.startsWith("Spridning_bekampningsmedel--")) {
			formId = "malmo/yrkesmassig-anvandning-av-bekampningsmedel";
		}
		if (formPath.startsWith("Franvaroanmalan--")) {
			formId = "malmo/anmalan-av-franvaro";
		}
		if (formPath.startsWith("Ansokan--")) {
			formId = "malmo/anmalan-av-franvaro";
		}
		*/
		
		String docId = (String)getRequestAttributes().get("docId");

		System.out.println("startBonitaProcess formPath=[" + formPath + "] : " + "formId=[" + formId +"] docId=[" + docId + "] user=[" + user + "]");
		
		
		String processDefinitionUUIDStr = formPath;
		
		if ("BMTest/BMTestForm".equals(formPath)) {
			processDefinitionUUIDStr = "Felanmalan--2.0";
		}
		
    	try {
    	
    		LoginContext loginContext = BonitaUtil.loginWithUser(user);

    		ProcessDefinitionUUID processDefinitionUUID = new ProcessDefinitionUUID(processDefinitionUUIDStr);
    		ProcessDefinition processDefinition = AccessorUtil.getQueryDefinitionAPI().getProcess(processDefinitionUUID);
    		
    		if  (processDefinition != null) {
    			
    			System.out.println("Starta process " + processDefinition.getUUID());
			Map<String,Object> variables = new HashMap<String, Object>();
			variables.put("docId", docId);
			variables.put("formId", formId);

    			ProcessInstanceUUID instanceUuid = AccessorUtil.getRuntimeAPI().instantiateProcess(processDefinition.getUUID(), variables);
    			if (instanceUuid != null) {
    				result = instanceUuid.getValue();
    			}
    		}
            
	        BonitaUtil.logoutWithUser(loginContext);

    	} catch (Exception e) {
        	logger.error("Could not create a proper bonita form identity key: " + e); // instance=TestaCheckboxlist--1.0--8
        }
    	
        return result;
	}

}

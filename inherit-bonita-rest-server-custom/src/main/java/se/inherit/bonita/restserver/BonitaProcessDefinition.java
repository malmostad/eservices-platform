package se.inherit.bonita.restserver;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.bonitasoft.console.security.server.api.ICredentialsEncryptionAPI;
import org.bonitasoft.console.security.server.api.SecurityAPIFactory;
import org.ow2.bonita.facade.def.majorElement.ProcessDefinition;
import org.ow2.bonita.facade.def.majorElement.impl.ProcessDefinitionImpl;
import org.ow2.bonita.facade.uuid.ProcessDefinitionUUID;
import org.ow2.bonita.util.AccessorUtil;
import org.ow2.bonita.util.SimpleCallbackHandler;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.bonita.domain.ProcessDefinitionInfo;

public class BonitaProcessDefinition extends ServerResource {

	public static final Logger logger = LoggerFactory.getLogger(BonitaProcessDefinition.class);
	
	@Post("xml")
	public ProcessDefinitionInfo getBonitaProcessDefinitionInfo() {
		ProcessDefinitionInfo result = null;
		System.out.println("TJOHOOOOOOOOOOOOOO");
		
		String processDefinitionUUIDStr = (String)getRequestAttributes().get("processDefinitionUUID");

    	try {
    	
    		LoginContext loginContext = BonitaUtil.login();

    		ProcessDefinitionUUID processDefinitionUUID = new ProcessDefinitionUUID(processDefinitionUUIDStr);
    		ProcessDefinition processDefinition = AccessorUtil.getQueryDefinitionAPI().getProcess(processDefinitionUUID);
    		
    		if  (processDefinition != null) {
    			result = new ProcessDefinitionInfo(processDefinition.getUUID().getValue(), processDefinition.getName(), processDefinition.getLabel());
    			
    			System.out.println("getBonitaProcessDefinitionInfo " + result);
    		}
            
	        BonitaUtil.logout(loginContext);

    	} catch (Exception e) {
        	logger.error("Could not create a proper bonita form identity key: " + e); // instance=TestaCheckboxlist--1.0--8
        }
    	
        return result;
	}

}

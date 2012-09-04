package org.inherit.service.rest.server;

import javax.security.auth.login.LoginContext;

// TODO vill minnas att beroende mot bonita-security går att få bort, kolla..., troligen använda SSO och user samt skippa identityKey
import org.bonitasoft.console.security.server.api.ICredentialsEncryptionAPI;
import org.bonitasoft.console.security.server.api.SecurityAPIFactory;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import java.util.logging.Logger;

import org.inherit.bonita.client.util.BonitaUtil;
import org.inherit.service.common.util.ParameterEncoder;

public class BonitaIdentityKey extends ServerResource {

	public static final Logger log = Logger.getLogger(BonitaIdentityKey.class.getName());
	
	@Post
	public String getBonitaIdentityKey() {
		log.severe("=====================> getBonitaIdentityKey");

		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userid"));
		String pwd = ParameterEncoder.decode((String)getRequestAttributes().get("password"));

		String identityKey = null;
    	
    	try {
    	
    		LoginContext loginContext = BonitaUtil.login();

    		
    		ICredentialsEncryptionAPI credEncApi = SecurityAPIFactory.getCredentialsEncryptionAPI();
            String encCred = credEncApi.encryptCredential(userId + ICredentialsEncryptionAPI.USER_CREDENTIALS_SEPARATOR + pwd);
            identityKey = credEncApi.generateTemporaryToken(encCred);
            
	        BonitaUtil.logout(loginContext);
	        
	        log.severe("=====================> ID key: " + identityKey);

    	} catch (Exception e) {
        	log.severe("Could not create a proper bonita form identity key: " + e); // instance=TestaCheckboxlist--1.0--8
        }
    	
    	log.severe("Return identityKey: " + identityKey);
    	
        return identityKey;
	}

}

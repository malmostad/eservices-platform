package se.inherit.bonita.restserver;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.bonitasoft.console.security.server.api.ICredentialsEncryptionAPI;
import org.bonitasoft.console.security.server.api.SecurityAPIFactory;
import org.ow2.bonita.util.SimpleCallbackHandler;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BonitaIdentityKey extends ServerResource {

	public static final Logger logger = LoggerFactory.getLogger(BonitaIdentityKey.class);
	
	@Post
	public String getBonitaIdentityKey() {
		
		String userId = (String)getRequestAttributes().get("userid");
		String pwd = (String)getRequestAttributes().get("password");

		String identityKey = null;
    	
    	try {
    	
    		LoginContext loginContext = BonitaUtil.login();

    		
    		ICredentialsEncryptionAPI credEncApi = SecurityAPIFactory.getCredentialsEncryptionAPI();
            String encCred = credEncApi.encryptCredential(userId + ICredentialsEncryptionAPI.USER_CREDENTIALS_SEPARATOR + pwd);
            identityKey = credEncApi.generateTemporaryToken(encCred);

	        BonitaUtil.logout(loginContext);

    	} catch (Exception e) {
        	logger.error("Could not create a proper bonita form identity key: " + e); // instance=TestaCheckboxlist--1.0--8
        }
    	
    	logger.error("Return identityKey: " + identityKey);
    	
        return identityKey;
	}

}

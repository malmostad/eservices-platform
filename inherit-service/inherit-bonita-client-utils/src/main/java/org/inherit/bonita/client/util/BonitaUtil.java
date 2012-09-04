package org.inherit.bonita.client.util;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.ow2.bonita.identity.auth.UserOwner;
import org.ow2.bonita.util.SimpleCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BonitaUtil {
	
	public static final Logger logger = LoggerFactory.getLogger(BonitaUtil.class);
	
	public static LoginContext login() {
		LoginContext loginContext = null;
		logger.info("Login BonitaStore");
		String user = "restuser";
		String pwd = "restbpm";
		try {
			loginContext = new LoginContext("BonitaStore",
					new SimpleCallbackHandler(user, pwd));
			loginContext.login();

		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loginContext;
	}

	public static void logout(LoginContext loginContext) {
		try {
			loginContext.logout();
		} catch (LoginException e) {
			logger.error("Could not logout bonita REST api JAAS context: " + e);
		}
	}
	
	public static LoginContext loginWithUser(String user) { //, String pwd) {
		logger.info("Login BonitaAuth");

		LoginContext loginContext = null;
		try {
			loginContext = new LoginContext("BonitaAuth",
					new SimpleCallbackHandler(user, "bpm"));

			logger.info("Login user: " + user);
			
			loginContext.login();
			loginContext.logout();

			logger.info("Login ok");
			
			logger.info("Login BonitaStore");
			String restuser = "restuser";
			String restpwd = "restbpm";
			loginContext = new LoginContext("BonitaStore",
					new SimpleCallbackHandler(restuser, restpwd));
			loginContext.login();
			
			UserOwner.setUser(user);
		} catch (LoginException e) {
			logger.error("Could not login to bonita REST api JAAS context: " + e);
		}

		return loginContext;
	}


	public static void logoutWithUser(LoginContext loginContext) {
		try {
			UserOwner.setUser(null);
			loginContext.logout();
		} catch (LoginException e) {
			logger.error("Could not logout bonita REST api JAAS context: " + e);
		}
	}
}

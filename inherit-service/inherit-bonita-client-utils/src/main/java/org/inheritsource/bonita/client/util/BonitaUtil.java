/* 
 *  Process Aware Web Application Platform 
 * 
 *  Copyright (C) 2011-2013 Inherit S AB 
 * 
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Affero General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details. 
 * 
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 *  e-mail: info _at_ inherit.se 
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 *  phone: +46 8 641 64 14 
 */ 
 
package org.inheritsource.bonita.client.util;

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

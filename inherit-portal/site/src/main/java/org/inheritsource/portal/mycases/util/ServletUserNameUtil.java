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
 
package org.inheritsource.portal.mycases.util;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.taskform.engine.TaskFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class ServletUserNameUtil {

	public static final Logger log = LoggerFactory.getLogger(ServletUserNameUtil.class);
	
	private static TaskFormService engine;
	
	private static void checkEngine() {
		if (engine == null) {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();  
			engine = (TaskFormService) ctx.getBean("engine");
		}
	}
	
	public static UserInfo getUserName(final HttpServletRequest request) {
		UserInfo result = null;

		checkEngine();
		
	    String dn = request.getHeader("x-ipl-dn");
	    String ser = request.getHeader("x-ipl-ser");
	    String certificateSubject = request.getHeader("x-ipl-cer-sub");
	    
	    if (dn == null) {
	    	if (ser != null) {
	    		engine.getUserBySerial(ser, certificateSubject);
	    	}
	    }
	    else {
	    	if (ser == null) {
	    		result = engine.getUserByDn(dn);
	    	}
	    	else {
	    		log.debug("Only one of header x-ipl-dn and x-ipl-ser should be used");
	    		log.debug("x-ipl-dn=[" + dn + "]");
	    		log.debug("x-ipl-ser=[" + ser + "]");
	    		
	    		/** 
	    		 * TODO workaround to detect by path komin/extern
	    		 */
	    		String pathInfo = request.getPathInfo();
	    		if (pathInfo != null && pathInfo.indexOf("komin")>0) {
	    			result = engine.getUserByDn(dn);
	    		}
	    		else {
	    			result = engine.getUserBySerial(ser, certificateSubject);
	    		}
	    		
	    	}
	    }

	    if (result == null) {
			log.info("userName header not found, get user principal instead"); 
			Principal principal = request.getUserPrincipal();
			if (principal != null) {
				String hippoDn = "CN=" + principal.getName() + ",OU=Personal,OU=Organisation,OU=Hippo Internal User,DC=adm,DC=inherit,DC=se";
				result = engine.getUserByDn(hippoDn);
				//"CN=tesetj,OU=Personal,OU=Organisation,OU=Malmo,DC=adm,DC=malmo,DC=se"
			}
	    }
	    
	    log.info("Render page with userInfo=[" + result + "]");
	   
	    return result;
	}
	
}

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
import org.inheritsource.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletUserNameUtil {

	public static final Logger log = LoggerFactory.getLogger(ServletUserNameUtil.class);
			
	public static UserInfo getUserName(final HttpServletRequest request) {
		UserInfo result = null;
		InheritServiceClient isc = new InheritServiceClient();
		
	    String dn = request.getHeader("x-ipl-dn");
	    String ser = request.getHeader("x-ipl-ser");
	    String certificateSubject = request.getHeader("x-ipl-cer-sub");
	    
	    if (dn == null) {
	    	if (ser != null) {
	    		result = isc.getUserBySerial(ser, certificateSubject);
	    	}
	    }
	    else {
	    	if (ser == null) {
	    		result = isc.getUserByDn(dn);
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
	    			result = isc.getUserByDn(dn);
	    		}
	    		else {
	    			result = isc.getUserBySerial(ser, certificateSubject);
	    		}
	    		
	    	}
	    }

	    if (result == null) {
			log.info("userName header not found, get user principal instead"); 
			Principal principal = request.getUserPrincipal();
			if (principal != null) {
				String hippoDn = "CN=" + principal.getName() + ",OU=Personal,OU=Organisation,OU=Hippo Internal User,DC=adm,DC=inherit,DC=se";
				result = isc.getUserByDn(hippoDn);
				//"CN=tesetj,OU=Personal,OU=Organisation,OU=Malmo,DC=adm,DC=malmo,DC=se"
			}
	    }
	    
	    log.info("Render page with userInfo=[" + result + "]");
	   
	    return result;
	}
	
}

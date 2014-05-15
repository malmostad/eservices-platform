/* == Motrice Copyright Notice == 
 * 
 * Motrice Service Platform 
 * 
 * Copyright (C) 2011-2014 Motrice AB 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Affero General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Affero General Public License for more details. 
 * 
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>. 
 * 
 * e-mail: info _at_ motrice.se 
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 * phone: +46 8 641 64 14 
 
 */ 
 
package org.inheritsource.service.rest.server.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.taskform.engine.TaskFormService;

@Component
@Path("/identity")
public class IdentityService {

	public static final Logger log = Logger.getLogger(IdentityService.class.getName());
	
	@Autowired
	TaskFormService engine;
	
	public IdentityService() {
		log.severe("Creating IdentityService");
	}
	
	/**
	 * Get user with a known inherit platform user uuid
	 * @param userUuid
	 * @return
	 */
	@POST
    @Path("/users/{userUuid}")
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
	public UserInfo getUser(@PathParam("userUuid") String userUuid) {
		log.finest("userUuid=" + userUuid);
		return engine.getUserByUuid(userUuid);
	}

	/**
	 * Get user info corresponding to citizen SAML. 
	 * First time user will be initialized in platform with this request. 
	 * @param userDn from SAML ticket
	 * @return
	 */
	@POST
    @Path("/users/dn/{userDn}")
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
	public UserInfo getUserByDn(@PathParam("userDn") String userDn) {
		log.finest("userDn=" + userDn);
		return engine.getUserByDn(userDn);
	}

	
	/**
	 * Get user info corresponding to citizen SAML. 
	 * First time user will be initialized in platform with this request. 
	 * @param serial from portwise SAML 
	 * @param certificateSubject from portwise SAML
	 * @return 
	 */
	@POST
    @Path("/users/serial/{serial}/{certificateSubject}")
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
	public UserInfo getUserBySerial(@PathParam("serial") String serial, @PathParam("certificateSubject") String certificateSubject) {
		log.finest("serial=" + serial + ", certificateSubject=" + certificateSubject);
		return engine.getUserBySerial(serial, certificateSubject);
	}
		
}

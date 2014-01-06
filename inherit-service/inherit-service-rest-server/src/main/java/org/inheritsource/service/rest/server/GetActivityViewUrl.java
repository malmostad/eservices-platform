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
 
package org.inheritsource.service.rest.server;

import java.util.logging.Logger;

import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class GetActivityViewUrl extends ServerResource {

	public static final Logger log = Logger.getLogger(SubmitForm.class
			.getName());

	TaskFormService engine = new TaskFormService();

	@Post
	public String getActivityViewUrl() {
		String result;
		String processInstanceUuid = ParameterEncoder.decode((String) getRequestAttributes().get("processInstanceUuid"));
		String activityName = ParameterEncoder.decode((String) getRequestAttributes().get("activityName"));
		String userId = ParameterEncoder.decode((String) getRequestAttributes().get("userId"));
		
		log.fine("REST getUserInstancesList with parameter userid=[" + userId
				+ "]");
		try {
			result = engine.getActivityViewUrl(processInstanceUuid, activityName, userId);
		} catch (Exception e) {
			// TODO The status message is not transferred to the client side (ROL)
			setStatus(new Status(Status.SERVER_ERROR_INTERNAL,"Error in service layer, Start form not submitted"));
			// throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
			//		"Error in service layer, Start form not submitted", e);
			result=null;
		}
		return result;
	}

}

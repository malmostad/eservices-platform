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

import org.inheritsource.service.common.domain.ActivityWorkflowInfo;
import org.inheritsource.service.common.domain.Tag;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.ServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;


public class AddTag extends ServerResource {
		
	public static final Logger log = Logger.getLogger(AddTag.class.getName());
			
	TaskFormService engine = new TaskFormService();
	
	@Get
	@Post
	public Tag addTag() {
		Tag result = null;
		
		String processActivityFormInstanceIdStr = ParameterEncoder.decode((String)getRequestAttributes().get("processActivityFormInstanceId"));
		String tagTypeIdStr = ParameterEncoder.decode((String)getRequestAttributes().get("tagTypeId"));
		String value = ParameterEncoder.decode((String)getRequestAttributes().get("value"));
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userid"));
		
		Long processActivityFormInstanceId = null;
		Long tagTypeId = null;
		try {
			processActivityFormInstanceId = Long.decode(processActivityFormInstanceIdStr);
			tagTypeId = Long.decode(tagTypeIdStr);
			result = engine.addTag(processActivityFormInstanceId, tagTypeId, value, userId);
		}
		catch (NumberFormatException nfe) {
			log.info("processActivityFormInstanceId=[" + processActivityFormInstanceIdStr + "] and/or tagTypeId=[" + tagTypeIdStr + "] is not a valid id");
		}		
		
		return result;
	}
}

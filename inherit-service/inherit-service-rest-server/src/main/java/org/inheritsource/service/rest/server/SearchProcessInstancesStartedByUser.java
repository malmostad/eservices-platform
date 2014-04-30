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
 
 
package org.inheritsource.service.rest.server;

import java.util.logging.Logger;

import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import org.springframework.beans.factory.annotation.Autowired;


public class SearchProcessInstancesStartedByUser extends ServerResource {

	public static final Logger log = Logger.getLogger(SearchProcessInstancesStartedByUser.class.getName());
	
	@Autowired
	TaskFormService engine;	
	
	@Post
	public PagedProcessInstanceSearchResult searchProcessInstancesStartedByUser() {
		String searchForUserId = ParameterEncoder.decode((String)getRequestAttributes().get("searchForUserId"));
		String fromIndexStr = ParameterEncoder.decode((String)getRequestAttributes().get("fromIndex"));
		String pageSizeStr = ParameterEncoder.decode((String)getRequestAttributes().get("pageSize"));
		String sortBy = ParameterEncoder.decode((String)getRequestAttributes().get("sortBy"));
		String sortOrder = ParameterEncoder.decode((String)getRequestAttributes().get("sortOrder"));		
		String filter = ParameterEncoder.decode((String)getRequestAttributes().get("filter"));		
		String userId = ParameterEncoder.decode((String)getRequestAttributes().get("userId"));
		
		int fromIndex = Integer.parseInt(fromIndexStr);
		int pageSize = Integer.parseInt(pageSizeStr);
		
		log.fine("REST searchProcessInstancesStartedByUser with parameter searchForUserId=[" + searchForUserId + "] fromIndex=[" + fromIndex + "] pageSize=[" + pageSize + "] sortBy=["  + sortBy + "] sortOrder=[" + sortOrder + "] filter=[" + filter + "] userId=[" + userId + "]");
		
		return engine.searchProcessInstancesStartedByUser(searchForUserId, fromIndex, pageSize, sortBy, sortOrder, filter, null, userId);

	}
}

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

import org.inheritsource.service.common.util.ParameterEncoder;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * Inherit platform services back end REST API. 
 * TODO Add authentication. Keep it protected from external use. 
 * @author bjmo
 *
 */
public class InheritServiceApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		
		router.attach("/getActivityInstanceItemById/{processActivityFormInstanceId}", ActivityInstanceItemById.class);
		router.attach("/getDashOpenActivitiesByUserId/{userid}/{remainingDays}", DashOpenActivitiesByUserId.class);
		router.attach("/getStartActivityInstanceItem/{formPath}/{userId}", StartActivityInstanceItemByFormPath.class);
		router.attach("/processInstanceDetailsByActivityInstanceUuid/{activityInstanceUuid}", ProcessInstanceDetailByActivityInstanceUuid.class); 
		router.attach("/processInstanceDetailsByUuid/{processInstanceUuid}", ProcessInstanceDetailByUuid.class); 
		router.attach("/getPreviousActivityDataByDocId/{currentActivityFormDocId}/{previousActivityName}/{uniqueXPathExpr}", GetPreviousActivityDataByDocId.class); 
		router.attach("/getPreviousActivityDataByInstanceUuid/{currentActivityInstanceUuid}/{previousActivityName}/{uniqueXPathExpr}", GetPreviousActivityDataByInstanceUuid.class); 
		router.attach("/getPreviousActivitiesDataByDocId/{currentActivityFormDocId}", GetPreviousActivitiesDataByDocId.class);
		router.attach("/getPreviousActivityDataByProcessInstanceUuid/{processInstanceUuid}/{previousActivityName}/{uniqueXPathExpr}", GetPreviousActivityDataByProcessInstanceUuid.class);
		
		// TODO take user from login instead of parameter userid
		router.attach("/addComment/{activityInstanceUuid}/{comment}/{userid}", AddComment.class); 
		router.attach("/getCommentFeed/{activityInstanceUuid}/{userid}", GetCommentFeed.class); 
		router.attach("/assignTask/{activityInstanceUuid}/{action}/{userid}", AssignTask.class); 
		router.attach("/getActivityInstanceItem/{activityInstanceUuid}/{userId}", ActivityInstanceItemByActivityInstanceUuid.class);
		router.attach("/getNextActivityInstanceItemByDocId/{docId}/{userId}", NextActivityInstanceItemByDocId.class);		
		router.attach("/getActivityWorkflowInfo/{activityInstanceUuid}", GetActivityWorkflowInfo.class); 
		router.attach("/inboxByUserId/{userid}", InboxByUserId.class);
		router.attach("/submitForm/{docId}/{userId}/{newDocId}", SubmitForm.class);
		router.attach("/submitForm/{docId}/{userId}", SubmitForm.class);
		router.attach("/submitStartForm/{formPath}/{docId}/{userId}", SubmitStartForm.class);
		router.attach("/setActivityPriority/{activityInstanceUuid}/{priority}", SetActivityPriority.class); 
	
		router.attach("/addTag/{processActivityFormInstanceId}/{tagTypeId}/{value}/{userid}", AddTag.class); 
		router.attach("/deleteTag/{processInstanceUuid}/{value}/{userid}", DeleteTagByValue.class); 
		router.attach("/getTagsByProcessInstance/{processInstanceUuid}", GetTagsByProcessInstance.class); 
				
		router.attach("/searchProcessInstancesByTagValue/{tagValue}/{fromIndex}/{pageSize}/{sortBy}/{sortOrder}/{filter}/{userId}", SearchProcessInstancesByTagValue.class); 
		router.attach("/searchProcessInstancesStartedByUser/{searchForUserId}/{fromIndex}/{pageSize}/{sortBy}/{sortOrder}/{filter}/{userId}", SearchProcessInstancesStartedByUser.class);
		router.attach("/searchProcessInstancesWithInvolvedUser/{searchForUserId}/{fromIndex}/{pageSize}/{sortBy}/{sortOrder}/{filter}/{userId}", SearchProcessInstancesWithInvolvedUser.class);
		
		
		
		router.attach("/getUsersByRoleAndActivity/{roleName}/{activityInstanceUuid}", GetUsersByRoleAndActivity.class);
		
		
		router.attach("/getUserByDn/{dn}", GetUserByDn.class);
		router.attach("/getUserBySerial/{serial}/{certificateSubject}", GetUserBySerial.class);
		router.attach("/emailToInitiator/{processInstanceUuid}/{activityInstanceUuid}/{mailSubject}/{mailBody}", EmailToInitiator.class);
		router.attach("/emailTo/{mailTo}/{mailFrom}/{mailSubject}/{mailBody}", EmailTo.class);
		router.attach("/getMyProfile/{userid}", GetMyProfileByUserId.class);
		router.attach("/getProcessDefinitionDetails/{processDefinitionUUID}", GetProcessDefinitionDetails.class);
		router.attach("/getProcessDefinitions", GetProcessDefinitions.class);
		router.attach("/setActivityForm/{activityDefinitionUuid}/{formPath}", SetActivityForm.class);
		
		return router;
	}
}

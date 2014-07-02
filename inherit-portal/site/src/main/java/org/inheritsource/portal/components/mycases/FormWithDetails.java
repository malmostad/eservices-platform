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
 
 
package org.inheritsource.portal.components.mycases;

import java.util.List;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.domain.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormWithDetails extends Form  {
 
	public static final Logger log = LoggerFactory.getLogger(FormWithDetails.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		super.doBeforeRender(request, response);
				
		// TODO jag undrar varför jag plockar upp taskUuid här och inte alltid plockar från
		//      request.getAttribute("activity") som hanterar detta i basklassen.
		String activityInstanceUuid = getPublicRequestParameter(request,
				"taskUuid"); // TODO change to activityInstanceUuid???

		log.debug("activityInstanceUuid= {}" , activityInstanceUuid);
		
		ProcessInstanceDetails piDetails = null;
		if (activityInstanceUuid != null && activityInstanceUuid.trim().length() > 0) {
			piDetails = engine.getProcessInstanceDetailsByActivityInstance(activityInstanceUuid, request.getLocale());
		} else {
			ActivityInstanceItem activity = (ActivityInstanceItem)request.getAttribute("activity");
			if (activity != null && activity.getActivityInstanceUuid()!=null) {
				piDetails = engine.getProcessInstanceDetailsByActivityInstance(activity.getActivityInstanceUuid(), request.getLocale());
			}
		}
		
		appendChannelLabels(request, piDetails);
		
		request.setAttribute("processInstanceDetails", piDetails);
		log.error("XXX FormWithDetails details: {} ",  piDetails);
		
		if (piDetails != null && piDetails.getProcessInstanceUuid() != null) {
			List<Tag> tags = engine.getTagsByProcessInstance(piDetails.getProcessInstanceUuid());
			request.setAttribute("tags", tags);
			
			log.error("XXX FormWithDetails tags: {} " , tags);
		}
		
		if (piDetails != null && piDetails.getTimeline() != null) {
			request.setAttribute("timelineByDay", piDetails.getTimeline().getTimelineByDay());
			log.debug("timeline= {}" , piDetails.getTimeline().getTimelineByDay());
			
			log.error("XXX FormWithDetails timeline:{} ",  piDetails.getTimeline().getTimelineByDay());
		}
    }
	
}

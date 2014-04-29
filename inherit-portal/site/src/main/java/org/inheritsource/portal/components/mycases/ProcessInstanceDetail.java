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

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.domain.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessInstanceDetail extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory.getLogger(ProcessInstanceDetail.class);

	@Override
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {
		
		UserInfo userInfo = getUserName(request);

		String processInstanceUuid = getPublicRequestParameter(request,
				"processInstanceUuid");
		String activityInstanceUuid = getPublicRequestParameter(request,
				"activityInstanceUuid");

		
		ProcessInstanceDetails piDetails = null;
		if (processInstanceUuid != null && processInstanceUuid.trim().length() > 0) {
			piDetails = engine.getProcessInstanceDetails(processInstanceUuid, request.getLocale());
		} else if (activityInstanceUuid != null && activityInstanceUuid.trim().length() > 0) {
			piDetails = engine.getProcessInstanceDetailsByActivityInstance(activityInstanceUuid, request.getLocale());
		}
		
		appendChannelLabels(request, piDetails);
		
		request.setAttribute("processInstanceDetails", piDetails);
		
		if (piDetails != null && piDetails.getTimeline() != null) {
			request.setAttribute("timelineByDay", piDetails.getTimeline().getTimelineByDay());
		}
		
		request.setAttribute("userInfo", userInfo);
	}
}

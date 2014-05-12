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
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ActivityInstanceLogItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyForm extends Form  {
 
	public static final Logger log = LoggerFactory.getLogger(NotifyForm.class);

	public static final String START = "START";
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		super.doBeforeRender(request, response);
		
		ActivityInstanceItem activity = (ActivityInstanceItem)request.getAttribute("activity");

		ActivityInstanceLogItem notifyItem = engine.getActivityInstanceLogItemToNotify(activity, request.getLocale());

		request.setAttribute("notifyItem", notifyItem);
	}
	
}

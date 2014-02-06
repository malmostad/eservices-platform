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
 
package org.inheritsource.portal.components.mycases;


import javax.servlet.http.HttpSession;

import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.portal.beans.EServiceDocument;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ActivityInstanceLogItem;
import org.inheritsource.service.common.domain.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ViewForm extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory.getLogger(ViewForm.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		
        HippoBean doc = getContentBean(request);
        
        UserInfo user = getUserName(request);
        
        
        log.debug("user: " + request.getUserPrincipal());
        
        String processActivityFormInstanceId = getPublicRequestParameter(request, "processActivityFormInstanceId");
        
		ActivityInstanceLogItem logItem = null;
		if (processActivityFormInstanceId != null && processActivityFormInstanceId.trim().length()>0) {
		    // specific taskFormDb ProcessActivityFormInstance is requested
			Long id = null;
			try {
				id = Long.decode(processActivityFormInstanceId);
				ActivityInstanceItem activity = engine.getActivityInstanceItem(id);
			    if (activity instanceof ActivityInstanceLogItem) {
			    	logItem = (ActivityInstanceLogItem) activity;
			    }
			}
			catch (NumberFormatException nfe) {
				log.info("processActivityFormInstanceId=[" + processActivityFormInstanceId + "] is not a valid id");
			}		
		    
		}
        
        appendChannelLabels(request, logItem);
        
    	request.setAttribute("logItem", logItem);
    	
    	request.setAttribute("user", user);
    	
    	log.info("ViewForm logItem:" + logItem);
	}
	
}

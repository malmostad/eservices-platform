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
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Form extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory.getLogger(Form.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		
        HippoBean doc = getContentBean(request);
        
        UserInfo user = getUserName(request);
        
        
        log.debug("user: " + request.getUserPrincipal());
        InheritServiceClient isc = new InheritServiceClient();
        
        String processActivityFormInstanceId = getPublicRequestParameter(request, "processActivityFormInstanceId");
        String taskUuid = getPublicRequestParameter(request, "taskUuid");
        
        if (doc == null) {
            log.warn("Did not find a content bean for relative content path '{}' for pathInfo '{}'", 
                         request.getRequestContext().getResolvedSiteMapItem().getRelativeContentPath(),
                         request.getRequestContext().getResolvedSiteMapItem().getPathInfo());
            response.setStatus(404);
            return;
        }
        request.setAttribute("document",doc);
 
        ActivityInstanceItem activity = null;
        if (taskUuid != null && taskUuid.trim().length()>0) {
        	// specific BPMN engine activity instance is requested
        	activity = isc.getActivityInstanceItem(taskUuid, user.getUuid());
        } 
        else {
        	if (processActivityFormInstanceId != null && processActivityFormInstanceId.trim().length()>0) {
        		// specific taskFormDb ProcessActivityFormInstance is requested
        		activity = isc.getActivityInstanceItem(processActivityFormInstanceId);
        	}
        	else if (doc instanceof EServiceDocument) {
	        	// no activity is specified. Use content path to find a start form.
	        	EServiceDocument eServiceDocument = (EServiceDocument)doc;
	        	// look up existing form (docId) if partially saved form exist
	        	// otherwise create new form
	        	
	        	String userId = (user == null ? getTransientUserId(request) : user.getUuid());
	        	
	        	activity = isc.getStartActivityInstanceItem(eServiceDocument.getFormPath(), userId);
	        }
        }
        
        appendChannelLabels(request, activity);
        
    	request.setAttribute("activity", activity);
    	
    	HippoBean guide = null;
    	if (activity != null && doc != null) {
    		String piUuid = activity.getProcessDefinitionUuid();
    		String aiUuid = activity.getActivityDefinitionUuid();
    		
    		if (piUuid != null && aiUuid != null) {    			
	    		String guidePath = getMount(request).getCanonicalContentPath() + "/mycases/processes/" + piUuid.toLowerCase() + "/" + aiUuid.toLowerCase();
	    		log.debug("guide path: " + guidePath );
	    		
	    		try {
					guide = (HippoBean) getObjectBeanManager(request).getObject(guidePath);
				} catch (ObjectBeanManagerException e) {
					// TODO Auto-generated catch block
					log.error("Error while searching for activity guide with path=[" + guidePath + "] Exception: " + e);
				}
    		}
    	}
    	request.setAttribute("guide", guide);
    	request.setAttribute("user", user);
    	
    	log.info("Guide:" + guide);
    	log.info("Form activity:" + activity);
	}
	
	/**
	 * Get a transient userId to use in database when the user is not authenticated.
	 * @param request
	 * @return
	 */
	private String getTransientUserId(final HstRequest request) {
		String result = null;
		HttpSession session = request.getSession();
		if (session != null) {
			result = "sessionId#" + session.getId();
		}
		return result;
	}
}

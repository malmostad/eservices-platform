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

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.service.common.domain.DocBoxFormData;
import org.inheritsource.service.common.domain.FormInstance;
import org.inheritsource.service.common.domain.InboxTaskItem;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.service.docbox.DocBoxFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO Obsolete class => remove
public class SignFormConfirm extends MyCasesBaseComponent {

	public static final String ORBEON_PERSISTENCE_API_BASE_URL = "/orbeon/fr/service/exist/crud/";
	
	public static final String CASE_STARTER_SERIAL = "case-starter-serial";
	
	public static final Logger log = LoggerFactory.getLogger(SignFormConfirm.class);

	@Override
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {

		String docboxRef = getPublicRequestParameter(request, "docboxRef"); 
		String docNo = getPublicRequestParameter(request, "docNo"); 
		String status = getPublicRequestParameter(request, "status"); 
		String signature = getPublicRequestParameter(request, "signature"); 
		String instanceId = getPublicRequestParameter(request, "instance_id");

		log.info("SignFormConfirm:" + docboxRef + " docNo=" + docNo + " status=" + status + " signature=" + signature);

		UserInfo user = getUserName(request);
		
		// strategy 1. use anonymous user as fall back which is unusable during sign...
		String userUuid = UserInfo.ANONYMOUS_UUID; 
		
		if (user != null) {
			// strategy 2. use authenticated user's uuid
			userUuid = user.getUuid();
		}
		
		HippoBean doc = getContentBean(request);
				
		if (doc == null) {
			log.warn(
					"Did not find a content bean for relative content path '{}' for pathInfo '{}'",
					request.getRequestContext().getResolvedSiteMapItem()
							.getRelativeContentPath(), request
							.getRequestContext().getResolvedSiteMapItem()
							.getPathInfo());
		}
		request.setAttribute("document",doc);

		FormInstance signedForm = null;
		try {
			//signedForm = engine.submitSignForm(instanceId, userUuid, docboxRef, signature);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        if (signedForm != null) {
            		
    		request.setAttribute("pdfUrl", signedForm.getActUri());
    		
    		InboxTaskItem nextTask = null;
            if (signedForm!=null && !UserInfo.ANONYMOUS_UUID.equals(userUuid)) {
            	nextTask = engine.getNextActivityInstanceItemByDocId(signedForm, request.getLocale(), user.getUuid());
            }
            request.setAttribute("nextTask", nextTask);
        }
		// utför aktivitet i processmotor och lagra signatur ... om signaturen är ok....
		
        
		
		request.setAttribute("docboxRef", docboxRef);
		request.setAttribute("docNo", docNo);		
		
	}
}
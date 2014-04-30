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

import java.io.IOException;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.portal.beans.EServiceDocument;
import org.inheritsource.service.common.domain.FormInstance;
import org.inheritsource.service.common.domain.InboxTaskItem;
import org.inheritsource.service.common.domain.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Confirm extends MyCasesBaseComponent {

	public static final String ORBEON_PERSISTENCE_API_BASE_URL = "/orbeon/fr/service/exist/crud/";
	
	public static final String CASE_STARTER_SERIAL = "case-starter-serial";
	
	public static final Logger log = LoggerFactory.getLogger(Confirm.class);

	@Override
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {

		String docId = getPublicRequestParameter(request, "document"); // parameter
		// from
		// orbeon
		// workflow-send

		
		UserInfo user = getUserName(request);
		
		// strategy 1. use anonymous user as fall back
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
			// response.setStatus(404);
			// return;
		}

		request.setAttribute("document", doc);
		

		FormInstance  formInstance = null;
//		viewUrl = isc.submitForm(docId, user.getUuid());
		try {
			formInstance = engine.submitActivityForm(docId, userUuid);
		} catch (Exception re) {
			try {
				response.sendRedirect("/site/internal-error");
			} catch (IOException ioe) {
				log.error("==============> ioexception in catch block");
			}
		}

		
		log.error("XXX Confirm " + formInstance);
		
		if (formInstance!=null) {
			request.setAttribute("formUrl", formInstance.getViewUrl());
		}
		
		InboxTaskItem nextTask = null;
		if (!UserInfo.ANONYMOUS_UUID.equals(userUuid)) {
	        nextTask = engine.getNextActivityInstanceItemByDocId(formInstance, user.getUuid());
	        appendChannelLabels(request, nextTask);
		}
		request.setAttribute("nextTask", nextTask);
		
		log.error("XXX Confirm " + nextTask);
	}
}
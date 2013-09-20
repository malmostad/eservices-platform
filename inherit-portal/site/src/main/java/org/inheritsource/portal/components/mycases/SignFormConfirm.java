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

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignFormConfirm extends MyCasesBaseComponent {

	public static final String ORBEON_PERSISTENCE_API_BASE_URL = "/orbeon/fr/service/exist/crud/";
	
	public static final String CASE_STARTER_SERIAL = "case-starter-serial";
	
	public static final Logger log = LoggerFactory.getLogger(SignFormConfirm.class);

	@Override
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {

		
		String docBoxId = getPublicRequestParameter(request, "docBoxId"); 
		String status = getPublicRequestParameter(request, "status"); 
		String signature = getPublicRequestParameter(request, "signature"); 

		log.error("SignFormConfirm:" + docBoxId);

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

		InheritServiceClient isc = new InheritServiceClient();

		
		// utför aktivitet i processmotor och lagra signatur ... om signaturen är ok....
		
		
		request.setAttribute("docBoxId", docBoxId);
		request.setAttribute("status", status);
		request.setAttribute("signature", signature);

		
	}
}
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

import java.io.IOException;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.portal.beans.EServiceDocument;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.service.rest.client.InheritServiceClient;
import org.restlet.resource.ResourceException;
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
		

		
		
		InheritServiceClient isc = new InheritServiceClient();

		String viewUrl = null;
//		viewUrl = isc.submitForm(docId, user.getUuid());
		try {
			viewUrl = isc.submitForm(docId, userUuid);
		} catch (ResourceException re) {
			try {
				response.sendRedirect("/site/internal-error");
			} catch (IOException ioe) {
				log.error("==============> ioexception in catch block");
			}
		}

		request.setAttribute("formUrl", viewUrl);

		if (viewUrl == null) {
			// render a fail url???

			// TODO 20130506 teori: nedanstående är död kod...
			
			// this can be removed later on, only when start form not is
			// initialized before....
			if (doc instanceof EServiceDocument) {
				EServiceDocument eServiceDocument = (EServiceDocument) doc;

				// confirm page url
				String formUrl = eServiceDocument.getFormPath() + "/view/"
						+ docId + "?orbeon-embeddable=true";
				request.setAttribute("formUrl", formUrl);

				// TODO remove later
				log.error("==============> orbeon confirm form url:" + formUrl);

				try {
					isc.submitStartForm(eServiceDocument.getFormPath(),
							docId,
							userUuid);
				} catch (ResourceException re) {
					try {
						response.sendRedirect("/site/internal-error");
					} catch (IOException ioe) {
						log.error("==============> ioexception in catch block");
					}
				}
			}
		}
	}
}
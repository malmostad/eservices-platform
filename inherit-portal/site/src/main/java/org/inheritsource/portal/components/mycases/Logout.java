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

import javax.servlet.http.Cookie;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logout extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(Logout.class);

	final String cookieName = "iPlanetDirectoryPro";
	final String cookiePath = "/";
	final String cookieDomain = ".malmo.se";
	final String redirectLogoutLocation = "https://idp.test.malmo.se/wa/logout";

	void cancelCookie(HstRequest request, HstResponse response,
			String name, String path, String domain) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		/*
		 * cookie.setPath((request.getContextPath().length() > 0) ? request
		 * .getContextPath() : "/");
		 */
		cookie.setPath(path);
		cookie.setDomain(domain);
		response.addCookie(cookie);
	}

	@Override
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {

		HippoBean doc = getContentBean(request);

		if (doc == null) {
			log.warn(
					"Did not find a content bean for relative content path '{}' for pathInfo '{}'",
					request.getRequestContext().getResolvedSiteMapItem()
							.getRelativeContentPath(), request
							.getRequestContext().getResolvedSiteMapItem()
							.getPathInfo());
			response.setStatus(404);
		} else {
			request.setAttribute("document", doc);
			cancelCookie(request, response, cookieName, cookiePath, cookieDomain);

			try {
				response.sendRedirect(redirectLogoutLocation);
			} catch (IOException ioe) {
				log.warn("Redirect failed '{}' for pathInfo '{}'", request
						.getRequestContext().getResolvedSiteMapItem()
						.getRelativeContentPath(), request.getRequestContext()
						.getResolvedSiteMapItem().getPathInfo());
				response.setStatus(404);
			}

		}
	}
}

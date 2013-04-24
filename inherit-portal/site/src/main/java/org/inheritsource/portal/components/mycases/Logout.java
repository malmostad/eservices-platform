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

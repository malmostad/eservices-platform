package se.inherit.portal.components.mycases;

import java.security.Principal;
import java.util.Locale;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inherit.portal.beans.EServiceDocument;

public class Confirm extends BaseHstComponent {

	public static final Logger log = LoggerFactory
			.getLogger(Confirm.class);

	@Override
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {

		HippoBean doc = getContentBean(request);

        // TODO SSO SAML JAAS
        //Principal principal = request.getUserPrincipal();
        //String userName = principal.getName();
		String userName = "eva_extern";

		if (doc == null) {
			log.warn(
					"Did not find a content bean for relative content path '{}' for pathInfo '{}'",
					request.getRequestContext().getResolvedSiteMapItem()
							.getRelativeContentPath(), request
							.getRequestContext().getResolvedSiteMapItem()
							.getPathInfo());
			response.setStatus(404);
			return;
		}

		request.setAttribute("document", doc);

		String docId = getPublicRequestParameter(request, "docId");

		if (doc instanceof EServiceDocument) {
			EServiceDocument eServiceDocument = (EServiceDocument) doc;

			// confirm page url
			String formUrl = eServiceDocument.getFormPath() + "/view/" + docId	+ "?orbeon-embeddable=true";
			request.setAttribute("formUrl", formUrl);
			
			// TODO remove later
			log.error("==============> orbeon confirm form url:" + formUrl);

			InheritServiceClient isc = new InheritServiceClient();
			isc.submitStartForm(eServiceDocument.getFormPath(), docId, userName);

		}	
	}

}

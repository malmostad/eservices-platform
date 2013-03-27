package se.inherit.portal.components.mycases;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inherit.portal.beans.EServiceDocument;

public class Confirm extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory
			.getLogger(Confirm.class);

	@Override
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {

		HippoBean doc = getContentBean(request);

		UserInfo user = getUserName(request);
		
		if (doc == null) {
			log.warn(
					"Did not find a content bean for relative content path '{}' for pathInfo '{}'",
					request.getRequestContext().getResolvedSiteMapItem()
							.getRelativeContentPath(), request
							.getRequestContext().getResolvedSiteMapItem()
							.getPathInfo());
			//response.setStatus(404);
			//return;
		}

		request.setAttribute("document", doc);
		String docId = getPublicRequestParameter(request, "docId");
		
		InheritServiceClient isc = new InheritServiceClient();
		
		String viewUrl = isc.submitForm(docId, user.getUuid());
		request.setAttribute("formUrl", viewUrl);
		
		if (viewUrl==null) {
			// render a fail url???
			
			// this can be removed later on, only when start form not is initialized before....
			if (doc instanceof EServiceDocument) {
				EServiceDocument eServiceDocument = (EServiceDocument) doc;

				// confirm page url
				String formUrl = eServiceDocument.getFormPath() + "/view/" + docId	+ "?orbeon-embeddable=true";
				request.setAttribute("formUrl", formUrl);
				
				// TODO remove later
				log.error("==============> orbeon confirm form url:" + formUrl);

				isc.submitStartForm(eServiceDocument.getFormPath(), docId, user.getUuid());

			}	
		}
				
	}

}

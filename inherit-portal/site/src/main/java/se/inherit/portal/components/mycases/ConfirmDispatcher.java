package se.inherit.portal.components.mycases;

import java.util.HashMap;
import java.util.Map;

import org.hippoecm.hst.util.HstResponseUtils;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConfirmDispatcher is a work around. 
 * - Orbeon Form submit to ConfirmDispatcher.
 * - ConfirmDispatcher detects channel from url 
 * - ConfirmDispatcher redirects to confirm in correct channel with docId param 
 * @author bjmo
 *
 */
public class ConfirmDispatcher extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory.getLogger(ConfirmDispatcher.class);
	@Override
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {
		
		String[] docIdParams =  getPublicRequestParameters(request, "document");
		// default confirm path
		String path = "/mycases/form/confirm";
	
		String referrer = request.getHeader("referer");
		
		log.error("referrer: " + referrer);
		
		// some sample confirm urls with referrer urls
		
		//  confirm  /site/public/mycases/startforms/start/demo-ansokan/confirm?docId=772dd5ca-4b7c-4c34-bb96-af88e8bfd3b6
		//  referrer http://eservices.malmo.se/site/public/mycases/form?processActivityFormInstanceId=13
		
		// confirm   /site/public/mycases/startforms/start/demo-ansokan/confirm?docId=729d9b5e-5cbd-4050-bd29-cedd447701f0
		// referrer  "http://eservices.malmo.se/site/mycases/startforms/start/demo-ansokan"
		
		// try to find out channel from referrer url
		StringBuffer confirmUrl = new StringBuffer();
		boolean partOfChannelUrl = false;
		if (referrer != null) {
			String[] splits = referrer.split("/");
			for (String split : splits) {
				if (partOfChannelUrl) {
					confirmUrl.append("/");
					confirmUrl.append(split);
				}
				if ("site".equals(split)) {
					partOfChannelUrl = true;
				}
				if ("mycases".equals(split)) {
					partOfChannelUrl = false;
				}
			}
			path = confirmUrl.toString() + "/form/confirm";
		}
		
		log.error("path: " + path);
		
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("document", docIdParams);
		HstResponseUtils.sendRedirect(request, response, path, params);
	}
}

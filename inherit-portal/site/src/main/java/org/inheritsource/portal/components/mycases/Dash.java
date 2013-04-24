package org.inheritsource.portal.components.mycases;


import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.service.common.domain.DashOpenActivities;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Dash extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory.getLogger(Dash.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        
		HippoBean doc = getContentBean(request);
		UserInfo user = getUserName(request);
		
        if (doc == null) {
            log.warn("Did not find a content bean for relative content path '{}' for pathInfo '{}'", 
                         request.getRequestContext().getResolvedSiteMapItem().getRelativeContentPath(),
                         request.getRequestContext().getResolvedSiteMapItem().getPathInfo());
            //response.setStatus(404);
            //return;
        }
        request.setAttribute("document",doc);

        InheritServiceClient isc = new InheritServiceClient();
        
        DashOpenActivities dash = isc.getDashOpenActivitiesByUserId(user.getUuid(), 7);
                
        request.setAttribute("dash", dash);
        
    }
}

package se.inherit.portal.components.mycases.search;

import java.util.ArrayList;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.portal.components.mycases.MyCasesBaseComponent;

public class SearchCasesStartedByUserId extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory.getLogger(SearchCasesStartedByUserId.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		
        HippoBean doc = getContentBean(request);
        UserInfo user = getUserName(request);
        
        if (doc == null) {
            log.warn("Did not find a content bean for relative content path '{}' for pathInfo '{}'", 
                         request.getRequestContext().getResolvedSiteMapItem().getRelativeContentPath(),
                         request.getRequestContext().getResolvedSiteMapItem().getPathInfo());
            response.setStatus(404);
            return;
        }
        request.setAttribute("document",doc);

        InheritServiceClient isc = new InheritServiceClient();
        ArrayList<ProcessInstanceListItem> processInstances = isc.searchProcessInstancesStartedByUser(user.getUuid());
        request.setAttribute("processInstances", processInstances);
    }

}

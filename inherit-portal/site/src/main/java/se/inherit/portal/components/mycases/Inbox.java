package se.inherit.portal.components.mycases;

import java.util.ArrayList;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.InboxTaskItem;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Inbox extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory.getLogger(Inbox.class);
	
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
        ArrayList<InboxTaskItem> tasks = isc.getInboxByUserId(user.getUuid());
        
        // Lookup process and activity labels from JCR channel if they exist
        if (tasks != null) {
        	for (InboxTaskItem task : tasks) { 
        		appendChannelLabels(request, task);
        	}
        }
        
        request.setAttribute("tasks", tasks);
        
    }
}

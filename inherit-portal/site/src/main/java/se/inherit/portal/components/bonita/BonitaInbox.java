package se.inherit.portal.components.bonita;

import java.security.Principal;
import java.util.Collection;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.bonita.domain.InboxTaskItem;
import se.inherit.bonita.restclient.BonitaClient;

public class BonitaInbox  extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(BonitaInbox.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		
        HippoBean doc = getContentBean(request);
        
        Principal principal = request.getUserPrincipal();
        String userName = principal.getName();
        
        if (doc == null) {
            log.warn("Did not find a content bean for relative content path '{}' for pathInfo '{}'", 
                         request.getRequestContext().getResolvedSiteMapItem().getRelativeContentPath(),
                         request.getRequestContext().getResolvedSiteMapItem().getPathInfo());
            response.setStatus(404);
            return;
        }
        request.setAttribute("document",doc);

        BonitaClient bc = new BonitaClient("http://localhost:58080/bonita-server-rest/", "http://localhost:58080/inherit-bonita-rest-server-custom-1.0-SNAPSHOT/", "restuser", "restbpm");
       // Collection<LightTaskInstance> tasks = bc.getLightTaskListByUserId("admin", "READY");
        Collection<InboxTaskItem> tasks = bc.getUserTaskList(userName);
        
        request.setAttribute("tasks", tasks);
        
    }
}

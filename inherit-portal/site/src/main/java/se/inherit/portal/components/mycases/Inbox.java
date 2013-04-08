package se.inherit.portal.components.mycases;

import java.util.ArrayList;

import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.InboxTaskItem;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.portal.beans.EServiceDocument;
import se.inherit.portal.beans.TextDocument;


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
        
        // TODO remove this debug print
        if (tasks != null) {
        	String canonicalContentPath = getMount(request).getCanonicalContentPath();
        	for (InboxTaskItem task : tasks) {
        		
        		String startFormContentPath =  canonicalContentPath + "/mycases/startforms/" + task.getStartedByFormPath();
        		
	    		log.error("xxxxxxxxxxxxxxxxx startFormContentPath: " + startFormContentPath );
	    		
	    		try {
	    			EServiceDocument startForm = (EServiceDocument) getObjectBeanManager(request).getObject(startFormContentPath);
	    			if (startForm != null) {
	    				task.setProcessLabel(startForm.getTitle());
	    			}
				} catch (ObjectBeanManagerException e) {
					// TODO Auto-generated catch block
					log.error("Error while searching for activity guide with path=[" + startFormContentPath + "] Exception: " + e);
				}
	    		
	    		String piUuid = task.getProcessDefinitionUuid();
	    		String aiUuid = task.getActivityDefinitionUuid();
	    		
	    		if (piUuid != null && aiUuid != null) {    			
		    		String guidePath = canonicalContentPath + "/mycases/processes/" + piUuid.toLowerCase() + "/" + aiUuid.toLowerCase();
		    		log.error("xxxxxxxxxxxxxxxxx guide path: " + guidePath );
		    		
		    		try {
		    			TextDocument guide = (TextDocument) getObjectBeanManager(request).getObject(guidePath);
		    			if (guide != null) {
		    				task.setActivityLabel(guide.getTitle());
		    			}
					} catch (ObjectBeanManagerException e) {
						// TODO Auto-generated catch block
						log.error("Error while searching for activity guide with path=[" + guidePath + "] Exception: " + e);
					}
	    		}	    		
        	}
        	
        	log.error("tasks count: " + tasks.size());
        }
        else {
        	log.error("tasks count: no tasks");
        }
        
        request.setAttribute("tasks", tasks);
        
    }
}

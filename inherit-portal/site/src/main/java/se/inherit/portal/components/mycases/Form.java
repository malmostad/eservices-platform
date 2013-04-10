package se.inherit.portal.components.mycases;


import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.portal.beans.EServiceDocument;


public class Form extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory.getLogger(Form.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		
        HippoBean doc = getContentBean(request);
        
        UserInfo user = getUserName(request);
        
        
        log.error("=================> user: " + request.getUserPrincipal());
        InheritServiceClient isc = new InheritServiceClient();
        
        String processActivityFormInstanceId = getPublicRequestParameter(request, "processActivityFormInstanceId");
        String taskUuid = getPublicRequestParameter(request, "taskUuid");
        
        if (doc == null) {
            log.warn("Did not find a content bean for relative content path '{}' for pathInfo '{}'", 
                         request.getRequestContext().getResolvedSiteMapItem().getRelativeContentPath(),
                         request.getRequestContext().getResolvedSiteMapItem().getPathInfo());
            response.setStatus(404);
            return;
        }
        request.setAttribute("document",doc);
 
        ActivityInstanceItem activity = null;
        if (taskUuid != null && taskUuid.trim().length()>0) {
        	// specific BPMN engine activity instance is requested
        	activity = isc.getActivityInstanceItem(taskUuid, user.getUuid());
        } 
        else {
        	if (processActivityFormInstanceId != null && processActivityFormInstanceId.trim().length()>0) {
        		// specific taskFormDb ProcessActivityFormInstance is requested
        		activity = isc.getActivityInstanceItem(processActivityFormInstanceId);
        	}
        	else if (doc instanceof EServiceDocument) {
	        	// no activity is specified. Use content path to find a start form.
	        	EServiceDocument eServiceDocument = (EServiceDocument)doc;
	        	// look up existing form (docId) if partially saved form exist
	        	// otherwise create new form
	        	activity = isc.getStartActivityInstanceItem(eServiceDocument.getFormPath(), user.getUuid());
	        }
        }
    	request.setAttribute("activity", activity);
    	
    	HippoBean guide = null;
    	if (activity != null && doc != null) {
    		String piUuid = activity.getProcessDefinitionUuid();
    		String aiUuid = activity.getActivityDefinitionUuid();
    		
    		if (piUuid != null && aiUuid != null) {    			
	    		String guidePath = getMount(request).getCanonicalContentPath() + "/mycases/processes/" + piUuid.toLowerCase() + "/" + aiUuid.toLowerCase();
	    		log.error("xxxxxxxxxxxxxxxxx guide path: " + guidePath );
	    		
	    		try {
					guide = (HippoBean) getObjectBeanManager(request).getObject(guidePath);
				} catch (ObjectBeanManagerException e) {
					// TODO Auto-generated catch block
					log.error("Error while searching for activity guide with path=[" + guidePath + "] Exception: " + e);
				}
    		}
    	}
    	request.setAttribute("guide", guide);
    	request.setAttribute("user", user);
    	
    	log.info("Guide:" + guide);
    	log.info("Form activity:" + activity);
	}
}

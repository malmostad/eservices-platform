package se.inherit.portal.components.mycases;

import java.security.Principal;
import java.util.Locale;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.domain.ProcessInstanceDetails;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.portal.beans.EServiceDocument;


public class Form  extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(Form.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		
        HippoBean doc = getContentBean(request);
        
        // TODO SSO SAML JAAS
        //Principal principal = request.getUserPrincipal();
        //String userName = principal.getName();
        String userName = "eva_extern";
        
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
        	activity = isc.getActivityInstanceItem(taskUuid, userName);
        } 
        else {
        	if (processActivityFormInstanceId != null && processActivityFormInstanceId.trim().length()>0) {
        		// specific taskFormDb ProcessActivityFormInstance is requested
        		activity = isc.getStartActivityInstanceItem(processActivityFormInstanceId);
        	}
        	else if (doc instanceof EServiceDocument) {
	        	// no activity is specified. Use content path to find a start form.
	        	EServiceDocument eServiceDocument = (EServiceDocument)doc;
	        	// look up existing form (docId) if partially saved form exist
	        	// otherwise create new form
	        	activity = isc.getStartActivityInstanceItem(eServiceDocument.getFormPath(), userName);
	        }
        }
    	request.setAttribute("activity", activity);
    	
    	log.error("XXXXXXXXXXXXXXXXXXXX form activity:" + activity);

	}
}

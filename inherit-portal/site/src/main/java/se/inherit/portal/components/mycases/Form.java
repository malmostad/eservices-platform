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
 
        String formUrl = null;
        if (taskUuid != null && taskUuid.trim().length()>0) {
        	// specific BPMN engine activity instance is requested
        	ActivityInstanceItem activity = isc.getActivityInstanceItem(taskUuid, userName);
            formUrl = activity.getFormUrl();
        } 
        else {
        	// no activity is specified. Use content path to find a start form.
        	// todo create startforminstance to make partial save possibly 
        	// kanske bara ha en "osubmittad" instans per formulär???
	        if (doc instanceof EServiceDocument) {
	        	EServiceDocument eServiceDocument = (EServiceDocument)doc;
	        	
	        	// TODO
	        	//if (docId!=null && docId.trim().length()>0) {
	        	//	formUrl = eServiceDocument.getFormPath() + "/edit/" + docId + "?orbeon-embeddable=true";
	        	//}
	        	//else {
	        		formUrl = eServiceDocument.getFormPath() + "/new?orbeon-embeddable=true";
	        	//}
	        }
        }
    	request.setAttribute("formUrl", formUrl);
    	log.error("XXXXXXXXXXXXXXXXXXXX form url:" + formUrl);

	}
}

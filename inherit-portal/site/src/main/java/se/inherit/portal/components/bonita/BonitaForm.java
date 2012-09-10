package se.inherit.portal.components.bonita;

import java.security.Principal;
import java.util.Locale;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BonitaForm  extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(BonitaForm.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		
        HippoBean doc = getContentBean(request);
        
        Principal principal = request.getUserPrincipal();
        //String userName = principal.getName();
        String userName = "eva_extern";
        
        if (doc == null) {
            log.warn("Did not find a content bean for relative content path '{}' for pathInfo '{}'", 
                         request.getRequestContext().getResolvedSiteMapItem().getRelativeContentPath(),
                         request.getRequestContext().getResolvedSiteMapItem().getPathInfo());
            response.setStatus(404);
            return;
        }
        request.setAttribute("document",doc);

        String taskUuid = getPublicRequestParameter(request, "taskUuid");
        String activityDefinitionUUID = getPublicRequestParameter(request, "activityDefinitionUUID");
        
        String bonitaFormUrl = "";
        if (taskUuid != null && activityDefinitionUUID!=null) {
	        String localeStr = "en";
	        Locale locale = request.getLocale();
	        if (locale != null) {
	        	localeStr = locale.getLanguage();
	        }
	        
	        InheritServiceClient isc = new InheritServiceClient(); 
	        String identityKey = isc.getBonitaIdentityKey(userName, "bpm");
	        
	        //BonitaClient bc = new BonitaClient("http://localhost:58080/bonita-server-rest/", "http://localhost:58080/inherit-bonita-rest-server-custom-1.0-SNAPSHOT/", "restuser", "restbpm");
	        //String identityKey = bc.getFormIdentityKey(userName, "bpm");
	        //TODO password 
	        
	        //String taskUuid = "&task=DemoSlask--1.0--1--Registrera_namn--it1--mainActivityInstance--noLoop";
	       
	        //bonitaFormUrl = "http://localhost:58080/bonita/console/homepage?locale=" + localeStr + "&task=" + taskUuid + "&mode=form&identityKey=" + identityKey;
	        //http://host:port/webapp-name/application/BonitaApplication.html?locale=<locale>#form=<activityDefinitionUUID>$entry&task=<activityInstanceUUID>
	        
	        bonitaFormUrl = "http://" + request.getServerName() + "/bonita-app/application/BonitaApplication.html?locale=" + localeStr + "#form=" + activityDefinitionUUID + "$entry&task=" + taskUuid + "&mode=form&identityKey=" + identityKey;
        }
        
        request.setAttribute("bonitaFormUrl", bonitaFormUrl);
       
        request.setAttribute("testUser", request.getRemoteUser() + principal.getName());
        
        log.error("XXXXXXXXXXXXXXXXXXXX bonitaFormUrl:" + bonitaFormUrl);
    }
}

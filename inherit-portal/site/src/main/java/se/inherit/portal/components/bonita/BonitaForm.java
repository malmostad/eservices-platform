package se.inherit.portal.components.bonita;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import javax.security.auth.login.LoginContext;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.ow2.bonita.facade.def.majorElement.ProcessDefinition;
import org.ow2.bonita.light.LightTaskInstance;
import org.ow2.bonita.util.AccessorUtil;
import org.ow2.bonita.util.BonitaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.bonita.restclient.BonitaClient;

public class BonitaForm  extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(BonitaForm.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		
        HippoBean doc = getContentBean(request);
        
        if (doc == null) {
            log.warn("Did not find a content bean for relative content path '{}' for pathInfo '{}'", 
                         request.getRequestContext().getResolvedSiteMapItem().getRelativeContentPath(),
                         request.getRequestContext().getResolvedSiteMapItem().getPathInfo());
            response.setStatus(404);
            return;
        }
        request.setAttribute("document",doc);

        String taskUuid = getPublicRequestParameter(request, "taskUuid");
        
        String bonitaFormUrl = "";
        if (taskUuid != null) {
	        String localeStr = "en";
	        Locale locale = request.getLocale();
	        if (locale != null) {
	        	localeStr = locale.getLanguage();
	        }
	        
	        BonitaClient bc = new BonitaClient("http://localhost:58081/bonita-server-rest/", "http://localhost:58081/inherit-bonita-rest-server-custom-1.0-SNAPSHOT/", "restuser", "restbpm");
	        String identityKey = bc.getFormIdentityKey("admin", "bpm");
	        //String taskUuid = "&task=DemoSlask--1.0--1--Registrera_namn--it1--mainActivityInstance--noLoop";
	       
	        bonitaFormUrl = "http://localhost:58080/bonita/console/homepage?locale=" + localeStr + "&task=" + taskUuid + "&mode=form&identityKey=" + identityKey;
        }
        
        request.setAttribute("bonitaFormUrl", bonitaFormUrl);
       
        
        log.error("XXXXXXXXXXXXXXXXXXXX bonitaFormUrl:" + bonitaFormUrl);
    }
}

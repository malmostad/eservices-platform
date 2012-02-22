package se.inherit.portal.components.bonita;

import java.security.Principal;
import java.util.Locale;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.bonita.restclient.BonitaClient;

public class BonitaStartProcessInstanceForm  extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(BonitaStartProcessInstanceForm.class);
	
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
        
        String bonitaFormUrl = "";
        String localeStr = "en";
        Locale locale = request.getLocale();
        if (locale != null) {
        	localeStr = locale.getLanguage();
        }
	        
        BonitaClient bc = new BonitaClient("http://localhost:58080/bonita-server-rest/", "http://localhost:58080/inherit-bonita-rest-server-custom-1.0-SNAPSHOT/", "restuser", "restbpm");
        String identityKey = bc.getFormIdentityKey(userName, "bpm");
        // TODO password
        
	    //String taskUuid = "&task=DemoSlask--1.0--1--Registrera_namn--it1--mainActivityInstance--noLoop";
	    //bonitaFormUrl = "http://localhost:58080/bonita/console/homepage?locale=" + localeStr + "&task=" + taskUuid + "&mode=form&identityKey=" + identityKey;
	        
        //bonitaFormUrl = "http://localhost:58080/bonita/console/homepage?locale=" + localeStr + "#process=MyDemoProcess--2.0&form=MyDemoProcess--2.0$entry&mode=form&identityKey=" + identityKey;
//        bonitaFormUrl = "http://demo.inherit.se/bonita-app/application/BonitaApplication.html?locale=" + localeStr + "#process=MyDemoProcess--3.0&form=MyDemoProcess--3.0$entry&mode=form&identityKey=" + identityKey;
        bonitaFormUrl = "http://demo.inherit.se/bonita-app/application/BonitaApplication.html?locale=" + localeStr + "#process=MyDemoFelanmalanProcess--1.0&form=MyDemoFelanmalanProcess--1.0$entry&mode=form&identityKey=" + identityKey;
        
        
        request.setAttribute("bonitaFormUrl", bonitaFormUrl);
       
        
        log.error("XXXXXXXXXXXXXXXXXXXX bonitaFormUrl:" + bonitaFormUrl);
    }
}

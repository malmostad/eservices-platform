package se.inherit.portal.components.mycases;

import java.security.Principal;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

		
public class MyCasesBaseComponent extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(MyCasesBaseComponent.class);

	public UserInfo getUserName(final HstRequest request) {
		UserInfo result = null;
		InheritServiceClient isc = new InheritServiceClient();
		
	    String dn = request.getHeader("x-ipl-dn");
	    String ser = request.getHeader("x-ipl-ser");
	    String certificateSubject = request.getHeader("x-ipl-cer-sub");
	    
	    if (dn == null) {
	    	if (ser != null) {
	    		result = isc.getUserBySerial(ser, certificateSubject);
	    	}
	    }
	    else {
	    	if (ser == null) {
	    		result = isc.getUserByDn(dn);
	    	}
	    	else {
	    		log.error("Only one of header x-ipl-dn and x-ipl-ser should be used");
	    		log.error("x-ipl-dn=[" + dn + "]");
	    		log.error("x-ipl-ser=[" + ser + "]");
	    		
	    		/** 
	    		 * TODO workaround to detect by path komin/extern
	    		 */
	    		String pathInfo = request.getPathInfo();
	    		if (pathInfo != null && pathInfo.indexOf("komin")>0) {
	    			result = isc.getUserByDn(dn);
	    		}
	    		else {
	    			result = isc.getUserBySerial(ser, certificateSubject);
	    		}
	    		
	    	}
	    }

	    if (result == null) {
			log.info("userName header not found, get user principal instead"); 
			Principal principal = request.getUserPrincipal();
			if (principal != null) {
				String hippoDn = "CN=" + principal.getName() + ",OU=Personal,OU=Organisation,OU=Hippo Internal User,DC=adm,DC=inherit,DC=se";
				result = isc.getUserByDn(hippoDn);
				//"CN=tesetj,OU=Personal,OU=Organisation,OU=Malmo,DC=adm,DC=malmo,DC=se"
			}
	    }
	    
	    log.info("Render page with userInfo=[" + result + "]");
	   
	    return result;
	}
}

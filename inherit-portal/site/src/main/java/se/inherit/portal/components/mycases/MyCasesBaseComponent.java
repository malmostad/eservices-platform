package se.inherit.portal.components.mycases;

import java.security.Principal;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

		
public class MyCasesBaseComponent extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(MyCasesBaseComponent.class);

	public String getUserName(final HstRequest request) {
		// TODO SSO SAML JAAS
		String userName = "eva_extern";
		Principal principal = request.getUserPrincipal();
		if (principal != null) {
			userName = principal.getName();
		}

		log.info("Render page with userName=[" + userName + "]");
		
		return userName;
	}
}

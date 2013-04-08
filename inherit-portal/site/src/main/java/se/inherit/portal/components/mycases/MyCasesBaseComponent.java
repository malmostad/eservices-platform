package se.inherit.portal.components.mycases;

import java.security.Principal;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.portal.mycases.util.ServletUserNameUtil;

		
public class MyCasesBaseComponent extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(MyCasesBaseComponent.class);

	public UserInfo getUserName(final HstRequest request) {
		return ServletUserNameUtil.getUserName(request);
	}
}

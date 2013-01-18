package se.inherit.portal.components.mycases;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserSessionHeader extends MyCasesBaseComponent {

    public static final Logger log = LoggerFactory.getLogger(UserSessionHeader.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        String userName = getUserName(request);
        if (userName != null) {
        	request.setAttribute("userName", userName);
        }
    }

}

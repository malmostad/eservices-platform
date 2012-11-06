package se.inherit.portal.components.mycases;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.ProcessInstanceDetails;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessInstanceDetail extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(ProcessInstanceDetail.class);

	@Override
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {
		// TODO SSO SAML JAAS
		// Principal principal = request.getUserPrincipal();
		// String userName = principal.getName();

		String processInstanceUuid = getPublicRequestParameter(request,
				"processInstanceUuid");
		String activityInstanceUuid = getPublicRequestParameter(request,
				"activityInstanceUuid");

		InheritServiceClient isc = new InheritServiceClient();
		ProcessInstanceDetails piDetails = null;
		if (processInstanceUuid != null && processInstanceUuid.trim().length() > 0) {
			piDetails = isc.getProcessInstanceDetailByUuid(processInstanceUuid);
		} else if (activityInstanceUuid != null && activityInstanceUuid.trim().length() > 0) {
			piDetails = isc.getProcessInstanceDetailByActivityInstanceUuid(activityInstanceUuid);
		}
		request.setAttribute("processInstanceDetails", piDetails);

	}
}

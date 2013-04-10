package se.inherit.portal.components.mycases;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.ProcessInstanceDetails;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessInstanceDetail extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory.getLogger(ProcessInstanceDetail.class);

	@Override
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {
		
		UserInfo userInfo = getUserName(request);

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
		
		if (piDetails != null && piDetails.getTimeline() != null) {
			request.setAttribute("timelineByDay", piDetails.getTimeline().getTimelineByDay());
		}
		
		request.setAttribute("userInfo", userInfo);
	}
}

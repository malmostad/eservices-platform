package se.inherit.portal.components.mycases;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.ProcessInstanceDetails;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FormWithDetails  extends Form {

	public static final Logger log = LoggerFactory.getLogger(FormWithDetails.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		super.doBeforeRender(request, response);
		
		String activityInstanceUuid = getPublicRequestParameter(request,
				"taskUuid"); // TODO change to activityInstanceUuid???

		InheritServiceClient isc = new InheritServiceClient();
		ProcessInstanceDetails piDetails = null;
		if (activityInstanceUuid != null && activityInstanceUuid.trim().length() > 0) {
			piDetails = isc.getProcessInstanceDetailByActivityInstanceUuid(activityInstanceUuid);
		}
		request.setAttribute("processInstanceDetails", piDetails);
    }
}

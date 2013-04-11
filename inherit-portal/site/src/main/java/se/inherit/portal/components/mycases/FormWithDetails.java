package se.inherit.portal.components.mycases;

import java.util.List;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.domain.ProcessInstanceDetails;
import org.inherit.service.common.domain.Tag;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FormWithDetails extends Form  {
 
	public static final Logger log = LoggerFactory.getLogger(FormWithDetails.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		super.doBeforeRender(request, response);
		
//		String userName = getUserName(request);
		
		String activityInstanceUuid = getPublicRequestParameter(request,
				"taskUuid"); // TODO change to activityInstanceUuid???

		log.error("activityInstanceUuid=" + activityInstanceUuid);
		InheritServiceClient isc = new InheritServiceClient();
		
		ProcessInstanceDetails piDetails = null;
		if (activityInstanceUuid != null && activityInstanceUuid.trim().length() > 0) {
			piDetails = isc.getProcessInstanceDetailByActivityInstanceUuid(activityInstanceUuid);
		} else {
			ActivityInstanceItem activity = (ActivityInstanceItem)request.getAttribute("activity");
			if (activity != null && activity.getActivityInstanceUuid()!=null) {
				piDetails = isc.getProcessInstanceDetailByActivityInstanceUuid(activity.getActivityInstanceUuid());
			}
		}
		String canonicalContentPath = getMount(request).getCanonicalContentPath();
		
		request.setAttribute("processInstanceDetails", piDetails);
		
		if (piDetails != null && piDetails.getProcessInstanceUuid() != null) {
			List<Tag> tags = isc.getTagsByProcessInstance(piDetails.getProcessInstanceUuid());
			request.setAttribute("tags", tags);
		}
		
		if (piDetails != null && piDetails.getTimeline() != null) {
			request.setAttribute("timelineByDay", piDetails.getTimeline().getTimelineByDay());
			log.error("timeline=" + piDetails.getTimeline().getTimelineByDay());
		}
    }
	
	private void lookupJcrNamesOfActivitiesAndProcesses(ProcessInstanceDetails piDetails, String canonicalContentPath) {
		
	}
}

package se.inherit.portal.components.mycases;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.core.component.HstRequest;
import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.domain.ActivityInstanceLogItem;
import org.inherit.service.common.domain.ActivityInstancePendingItem;
import org.inherit.service.common.domain.InboxTaskItem;
import org.inherit.service.common.domain.PagedProcessInstanceSearchResult;
import org.inherit.service.common.domain.ProcessInstanceDetails;
import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.inherit.service.common.domain.StartLogItem;
import org.inherit.service.common.domain.TimelineItem;
import org.inherit.service.common.domain.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.portal.beans.EServiceDocument;
import se.inherit.portal.beans.TextDocument;
import se.inherit.portal.mycases.util.ServletUserNameUtil;

		
public class MyCasesBaseComponent extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(MyCasesBaseComponent.class);

	public UserInfo getUserName(final HstRequest request) {
		return ServletUserNameUtil.getUserName(request);
	}
	
	/**
	 * Look up activity label from hippo content repository. 
	 * The channel's mount point i.e. canonicalContentPath is used as base path
	 * @param request
	 * @param processDefinitionUuid
	 * @param activityDefinitionUuid
	 * @param defaultLabel
	 * @return the activity label, defaultLabel if no guide can be found in the JCR.
	 */
	public String getJcrActivityLabel(final HstRequest request, final String processDefinitionUuid, final String activityDefinitionUuid, String defaultLabel) {
		String jcrActivityLabel = defaultLabel;
		if (processDefinitionUuid != null && activityDefinitionUuid != null) {    			
			String canonicalContentPath = getMount(request).getCanonicalContentPath();
    		String guidePath = canonicalContentPath + "/mycases/processes/" + processDefinitionUuid.toLowerCase() + "/" + activityDefinitionUuid.toLowerCase();
    		log.debug("getJcrTaskLabel look upp label from guide path: " + guidePath );
    		
    		try {
    			TextDocument guide = (TextDocument) getObjectBeanManager(request).getObject(guidePath);
    			if (guide != null) {
    				jcrActivityLabel = guide.getTitle();
    			}
			} catch (ObjectBeanManagerException e) {
				// It is ok that no label can be found in jcr, log exception and continue
				log.warn("Error while searching for activity guide with path=[" + guidePath + "] Exception: " + e);
			}
		}	    		
		return jcrActivityLabel;
	}
	
	/**
	 * Look up process label from hippo content repository. 
	 * The channel's mount point i.e. canonicalContentPath is used as base path
	 * @param request
	 * @param startedByFormPath
	 * @param defaultLabel
	 * @return the process label, defaultLabel if no startForm can be found in the JCR.
	 */
	public String getJcrProcessLabel(final HstRequest request, final String startedByFormPath, String defaultLabel) {
		String processLabel = defaultLabel;
		String canonicalContentPath = getMount(request).getCanonicalContentPath();
		String startFormContentPath =  canonicalContentPath + "/mycases/startforms/" + startedByFormPath;
		
		log.debug("getJcrProcessInstanceLabel look upp label from startFormContentPath: " + startFormContentPath );
		
		try {
			EServiceDocument startForm = (EServiceDocument) getObjectBeanManager(request).getObject(startFormContentPath);
			if (startForm != null) {
				processLabel = startForm.getTitle();
			}
		} catch (ObjectBeanManagerException e) {
			// It is ok that no label can be found in jcr, log exception and continue
			log.warn("Error while searching for start form with path=[" + startFormContentPath + "] Exception: " + e);
		}
		
		return processLabel;
	}
	
	/**
	 * Try to find labels from current cannel's content repository, the fall back is
	 * to use the existing labels in the task.
	 * @param request
	 * @param task
	 */
	public void appendChannelLabels(final HstRequest request, InboxTaskItem task) {
		if (task!=null) {
			task.setProcessLabel(getJcrProcessLabel(request, task.getStartedByFormPath(), task.getProcessLabel()));        	
			task.setActivityLabel(getJcrActivityLabel(request, task.getProcessDefinitionUuid(), task.getActivityDefinitionUuid(), task.getActivityLabel()));
		}
	}
	
	
	/**
	 * Try to find labels from current cannel's content repository, the fall back is
	 * to use the existing labels in the pendingItem.
	 * @param request
	 * @param pendingItem
	 */
	public void appendChannelLabels(final HstRequest request, ActivityInstanceItem pendingItem) {
		if (pendingItem!=null) {
			pendingItem.setActivityLabel(getJcrActivityLabel(request, pendingItem.getProcessDefinitionUuid(), pendingItem.getActivityDefinitionUuid(), pendingItem.getActivityLabel()));
		}
	}

	/**
	 * Try to find labels from current cannel's content repository, the fall back is
	 * to use the existing labels in the processInstanceListItem.
	 * @param request
	 * @param processInstanceListItem
	 */
	public void appendChannelLabels(final HstRequest request, ProcessInstanceListItem processInstanceListItem) {
		if (processInstanceListItem!=null) {
			processInstanceListItem.setProcessLabel(getJcrProcessLabel(request, processInstanceListItem.getStartedByFormPath(), processInstanceListItem.getProcessLabel()));        	
			if (processInstanceListItem.getActivities() != null) {
				for (InboxTaskItem activity : processInstanceListItem.getActivities()) {
					appendChannelLabels(request, activity);
				}
			}
		}
	}
	
	/**
	 * Try to find labels from current cannel's content repository, the fall back is
	 * to use the existing labels in the timelineItem.
	 * @param request
	 * @param timelineItem
	 */
	public void appendChannelLabels(final HstRequest request, TimelineItem timelineItem, String processLabel) {
		if (timelineItem!=null) {
			if (timelineItem instanceof StartLogItem) {
				StartLogItem startLogItem = (StartLogItem)timelineItem;
				startLogItem.setActivityLabel(processLabel);
			}
			else if (timelineItem instanceof ActivityInstanceLogItem) {
				ActivityInstanceLogItem activity = (ActivityInstanceLogItem)timelineItem;
				appendChannelLabels(request, activity);
			}
		}
	}

	
	
	/**
	 * Try to find labels from current cannel's content repository, the fall back is
	 * to use the existing labels in the searchResult.
	 * @param request
	 * @param searchResult
	 */
	public void appendChannelLabels(final HstRequest request,  PagedProcessInstanceSearchResult searchResult) {
        if (searchResult != null && searchResult.getHits() != null) {
        	for (ProcessInstanceListItem piItem : searchResult.getHits()) {
        		appendChannelLabels(request, piItem);
			}	
        }
	}
	
	/**
	 * Try to find labels from current cannel's content repository, the fall back is
	 * to use the existing labels in the processInstanceDetails.
	 * @param request
	 * @param processInstanceDetails
	 */
	public void appendChannelLabels(final HstRequest request, ProcessInstanceDetails processInstanceDetails) {
		if (processInstanceDetails != null) {
			processInstanceDetails.setProcessLabel(getJcrProcessLabel(request, processInstanceDetails.getStartedByFormPath(), processInstanceDetails.getProcessLabel()));
		
			if (processInstanceDetails.getPending() != null) {
    			for (ActivityInstancePendingItem pendingItem : processInstanceDetails.getPending()) {
    				appendChannelLabels(request, pendingItem);
    			}
    		}
			if (processInstanceDetails.getTimeline() != null) {
				if (processInstanceDetails.getTimeline().getItems() != null) {
		        	for (TimelineItem timelineItem : processInstanceDetails.getTimeline().getItems()) {
		        		appendChannelLabels(request, timelineItem, processInstanceDetails.getProcessLabel());
		        	}
				}
			}
		}
	}
}

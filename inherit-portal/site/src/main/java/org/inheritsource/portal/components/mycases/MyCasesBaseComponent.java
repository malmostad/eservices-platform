/* 
 *  Process Aware Web Application Platform 
 * 
 *  Copyright (C) 2011-2013 Inherit S AB 
 * 
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Affero General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details. 
 * 
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 *  e-mail: info _at_ inherit.se 
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 *  phone: +46 8 641 64 14 
 */ 
 
package org.inheritsource.portal.components.mycases;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.core.component.HstRequest;
import org.inheritsource.portal.beans.EServiceDocument;
import org.inheritsource.portal.beans.TextDocument;
import org.inheritsource.portal.mycases.util.ServletUserNameUtil;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ActivityInstanceLogItem;
import org.inheritsource.service.common.domain.ActivityInstancePendingItem;
import org.inheritsource.service.common.domain.InboxTaskItem;
import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.domain.ProcessInstanceListItem;
import org.inheritsource.service.common.domain.StartLogItem;
import org.inheritsource.service.common.domain.TimelineItem;
import org.inheritsource.service.common.domain.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


		
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

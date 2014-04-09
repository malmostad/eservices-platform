package org.inheritsource.service.common.domain;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

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

/**
 * Common properties to view a activity instance 
 * @author bjmo
 *
 */
@XStreamAlias("ActivityInstanceItem")
public abstract class ActivityInstanceItem extends FormInstance implements Serializable {

	private static final long serialVersionUID = 8069401830437496246L;
	
	public static final int ACTIVITY_TYPE_SERVICE_TASK = 1;
	public static final int ACTIVITY_TYPE_USER_TASK = 2;

	String processDefinitionUuid;
	String processInstanceUuid;
	String activityDefinitionUuid;
	String activityInstanceUuid;
	String activityName;
	String activityLabel;	
	Date startDate;
	String currentState;
	Date lastStateUpdate;
	String lastStateUpdateByUserId;
	String startedBy;
	String guideUri;
	Date expectedEndDate;
	int activityType;
	int priority;
	long processActivityFormInstanceId;
	
	public String getProcessDefinitionUuid() {
		return processDefinitionUuid;
	}
	public void setProcessDefinitionUuid(String processDefinitionUuid) {
		this.processDefinitionUuid = processDefinitionUuid;
	}
	public String getProcessInstanceUuid() {
		return processInstanceUuid;
	}
	public void setProcessInstanceUuid(String processInstanceUuid) {
		this.processInstanceUuid = processInstanceUuid;
	}
	public String getActivityDefinitionUuid() {
		return activityDefinitionUuid;
	}
	public void setActivityDefinitionUuid(String activityDefinitionUuid) {
		this.activityDefinitionUuid = activityDefinitionUuid;
	}
	public String getActivityInstanceUuid() {
		return activityInstanceUuid;
	}
	public void setActivityInstanceUuid(String activityInstanceUuid) {
		this.activityInstanceUuid = activityInstanceUuid;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityLabel() {
		return activityLabel;
	}
	public void setActivityLabel(String activityLabel) {
		this.activityLabel = activityLabel;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getCurrentState() {
		return currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public Date getLastStateUpdate() {
		return lastStateUpdate;
	}
	public void setLastStateUpdate(Date lastStateUpdate) {
		this.lastStateUpdate = lastStateUpdate;
	}
	public String getLastStateUpdateByUserId() {
		return lastStateUpdateByUserId;
	}
	public void setLastStateUpdateByUserId(String lastStateUpdateByUserId) {
		this.lastStateUpdateByUserId = lastStateUpdateByUserId;
	}
	public String getStartedBy() {
		return startedBy;
	}
	public void setStartedBy(String startedBy) {
		this.startedBy = startedBy;
	}
	public String getGuideUri() {
		return guideUri;
	}
	public void setGuideUri(String guideUri) {
		this.guideUri = guideUri;
	}
	public Date getExpectedEndDate() {
		return expectedEndDate;
	}
	public void setExpectedEndDate(Date expectedEndDate) {
		this.expectedEndDate = expectedEndDate;
	}
	public int getActivityType() {
		return activityType;
	}
	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public long getProcessActivityFormInstanceId() {
		return processActivityFormInstanceId;
	}
	public void setProcessActivityFormInstanceId(long processActivityFormInstanceId) {
		this.processActivityFormInstanceId = processActivityFormInstanceId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((activityDefinitionUuid == null) ? 0
						: activityDefinitionUuid.hashCode());
		result = prime
				* result
				+ ((activityInstanceUuid == null) ? 0 : activityInstanceUuid
						.hashCode());
		result = prime * result
				+ ((activityLabel == null) ? 0 : activityLabel.hashCode());
		result = prime * result
				+ ((activityName == null) ? 0 : activityName.hashCode());
		result = prime * result + activityType;
		result = prime * result
				+ ((currentState == null) ? 0 : currentState.hashCode());
		result = prime * result
				+ ((expectedEndDate == null) ? 0 : expectedEndDate.hashCode());
		result = prime * result
				+ ((guideUri == null) ? 0 : guideUri.hashCode());
		result = prime * result
				+ ((lastStateUpdate == null) ? 0 : lastStateUpdate.hashCode());
		result = prime
				* result
				+ ((lastStateUpdateByUserId == null) ? 0
						: lastStateUpdateByUserId.hashCode());
		result = prime * result + priority;
		result = prime
				* result
				+ (int) (processActivityFormInstanceId ^ (processActivityFormInstanceId >>> 32));
		result = prime
				* result
				+ ((processDefinitionUuid == null) ? 0 : processDefinitionUuid
						.hashCode());
		result = prime
				* result
				+ ((processInstanceUuid == null) ? 0 : processInstanceUuid
						.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result
				+ ((startedBy == null) ? 0 : startedBy.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityInstanceItem other = (ActivityInstanceItem) obj;
		if (activityDefinitionUuid == null) {
			if (other.activityDefinitionUuid != null)
				return false;
		} else if (!activityDefinitionUuid.equals(other.activityDefinitionUuid))
			return false;
		if (activityInstanceUuid == null) {
			if (other.activityInstanceUuid != null)
				return false;
		} else if (!activityInstanceUuid.equals(other.activityInstanceUuid))
			return false;
		if (activityLabel == null) {
			if (other.activityLabel != null)
				return false;
		} else if (!activityLabel.equals(other.activityLabel))
			return false;
		if (activityName == null) {
			if (other.activityName != null)
				return false;
		} else if (!activityName.equals(other.activityName))
			return false;
		if (activityType != other.activityType)
			return false;
		if (currentState == null) {
			if (other.currentState != null)
				return false;
		} else if (!currentState.equals(other.currentState))
			return false;
		if (expectedEndDate == null) {
			if (other.expectedEndDate != null)
				return false;
		} else if (!expectedEndDate.equals(other.expectedEndDate))
			return false;
		if (guideUri == null) {
			if (other.guideUri != null)
				return false;
		} else if (!guideUri.equals(other.guideUri))
			return false;
		if (lastStateUpdate == null) {
			if (other.lastStateUpdate != null)
				return false;
		} else if (!lastStateUpdate.equals(other.lastStateUpdate))
			return false;
		if (lastStateUpdateByUserId == null) {
			if (other.lastStateUpdateByUserId != null)
				return false;
		} else if (!lastStateUpdateByUserId
				.equals(other.lastStateUpdateByUserId))
			return false;
		if (priority != other.priority)
			return false;
		if (processActivityFormInstanceId != other.processActivityFormInstanceId)
			return false;
		if (processDefinitionUuid == null) {
			if (other.processDefinitionUuid != null)
				return false;
		} else if (!processDefinitionUuid.equals(other.processDefinitionUuid))
			return false;
		if (processInstanceUuid == null) {
			if (other.processInstanceUuid != null)
				return false;
		} else if (!processInstanceUuid.equals(other.processInstanceUuid))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (startedBy == null) {
			if (other.startedBy != null)
				return false;
		} else if (!startedBy.equals(other.startedBy))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ActivityInstanceItem [processDefinitionUuid="
				+ processDefinitionUuid + ", processInstanceUuid="
				+ processInstanceUuid + ", activityDefinitionUuid="
				+ activityDefinitionUuid + ", activityInstanceUuid="
				+ activityInstanceUuid + ", activityName=" + activityName
				+ ", activityLabel=" + activityLabel + ", startDate="
				+ startDate + ", currentState=" + currentState
				+ ", lastStateUpdate=" + lastStateUpdate
				+ ", lastStateUpdateByUserId=" + lastStateUpdateByUserId
				+ ", startedBy=" + startedBy + ", guideUri=" + guideUri
				+ ", expectedEndDate=" + expectedEndDate + ", activityType="
				+ activityType + ", priority=" + priority
				+ ", processActivityFormInstanceId="
				+ processActivityFormInstanceId + "]";
	}
	
}

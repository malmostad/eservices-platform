package org.inherit.service.common.domain;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
Copyright (C) 2012 Inherit S AB

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

@XStreamAlias("InboxTaskItem")
public class InboxTaskItem implements Serializable {
	
	private static final long serialVersionUID = -5668377849020426352L;
	
	String processLabel;
	String activityLabel;
	Date activityCreated;
	
	// internal taskform-engine id, do not store, use in session communication 
	Long processActivityFormInstanceId;

	String taskUuid;
	String activityDefinitionUUID;
	
	public InboxTaskItem() {
		
	}

	public String getProcessLabel() {
		return processLabel;
	}

	public void setProcessLabel(String processLabel) {
		this.processLabel = processLabel;
	}

	public String getActivityLabel() {
		return activityLabel;
	}

	public void setActivityLabel(String activityLabel) {
		this.activityLabel = activityLabel;
	}

	public Date getActivityCreated() {
		return activityCreated;
	}

	public void setActivityCreated(Date activityCreated) {
		this.activityCreated = activityCreated;
	}

	public Long getProcessActivityFormInstanceId() {
		return processActivityFormInstanceId;
	}

	public void setProcessActivityFormInstanceId(Long processActivityFormInstanceId) {
		this.processActivityFormInstanceId = processActivityFormInstanceId;
	}

	
	
	public String getTaskUuid() {
		return taskUuid;
	}

	public void setTaskUuid(String taskUuid) {
		this.taskUuid = taskUuid;
	}
	
	public String getActivityDefinitionUUID() {
		return activityDefinitionUUID;
	}

	public void setActivityDefinitionUUID(String activityDefinitionUUID) {
		this.activityDefinitionUUID = activityDefinitionUUID;
	}
	
	public String getEditFormUrl() {
		String result = null;
		if (getProcessActivityFormInstanceId() != null && getProcessActivityFormInstanceId().longValue()>0) {
			result = "form?processActivityFormInstanceId=" +  getProcessActivityFormInstanceId();
		}
		else if (getTaskUuid() != null) {
			result = "form?taskUuid=" + getTaskUuid();				
		}
		return result;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activityCreated == null) ? 0 : activityCreated.hashCode());
		result = prime
				* result
				+ ((activityDefinitionUUID == null) ? 0
						: activityDefinitionUUID.hashCode());
		result = prime * result
				+ ((activityLabel == null) ? 0 : activityLabel.hashCode());
		result = prime
				* result
				+ ((processActivityFormInstanceId == null) ? 0
						: processActivityFormInstanceId.hashCode());
		result = prime * result
				+ ((processLabel == null) ? 0 : processLabel.hashCode());
		result = prime * result
				+ ((taskUuid == null) ? 0 : taskUuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InboxTaskItem other = (InboxTaskItem) obj;
		if (activityCreated == null) {
			if (other.activityCreated != null)
				return false;
		} else if (!activityCreated.equals(other.activityCreated))
			return false;
		if (activityDefinitionUUID == null) {
			if (other.activityDefinitionUUID != null)
				return false;
		} else if (!activityDefinitionUUID.equals(other.activityDefinitionUUID))
			return false;
		if (activityLabel == null) {
			if (other.activityLabel != null)
				return false;
		} else if (!activityLabel.equals(other.activityLabel))
			return false;
		if (processActivityFormInstanceId == null) {
			if (other.processActivityFormInstanceId != null)
				return false;
		} else if (!processActivityFormInstanceId
				.equals(other.processActivityFormInstanceId))
			return false;
		if (processLabel == null) {
			if (other.processLabel != null)
				return false;
		} else if (!processLabel.equals(other.processLabel))
			return false;
		if (taskUuid == null) {
			if (other.taskUuid != null)
				return false;
		} else if (!taskUuid.equals(other.taskUuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InboxTaskItem [processLabel=" + processLabel
				+ ", activityLabel=" + activityLabel + ", activityCreated="
				+ activityCreated + ", processActivityFormInstanceId="
				+ processActivityFormInstanceId + ", taskUuid=" + taskUuid
				+ ", activityDefinitionUUID=" + activityDefinitionUUID + "]";
	}

}

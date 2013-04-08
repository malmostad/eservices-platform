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
	Date expectedEndDate;
	
	// internal taskform-engine id, do not store, use in session communication 
	Long processActivityFormInstanceId;

	String processInstanceUuid;
	String taskUuid;
	String processDefinitionUuid;
	String activityDefinitionUuid;
	
	/** 
	 * The FORM_PATH that identifies the StartFormDefinition that started the case 
	 */
	String startedByFormPath;
	
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

	public Date getExpectedEndDate() {
		return expectedEndDate;
	}

	public void setExpectedEndDate(Date expectedEndDate) {
		this.expectedEndDate = expectedEndDate;
	}

	public Long getProcessActivityFormInstanceId() {
		return processActivityFormInstanceId;
	}

	public void setProcessActivityFormInstanceId(Long processActivityFormInstanceId) {
		this.processActivityFormInstanceId = processActivityFormInstanceId;
	}
	
	public String getProcessInstanceUuid() {
		return processInstanceUuid;
	}

	public void setProcessInstanceUuid(String processInstanceUuid) {
		this.processInstanceUuid = processInstanceUuid;
	}

	public String getTaskUuid() {
		return taskUuid;
	}

	public void setTaskUuid(String taskUuid) {
		this.taskUuid = taskUuid;
	}
	
	public String getActivityDefinitionUuid() {
		return activityDefinitionUuid;
	}

	public void setActivityDefinitionUuid(String activityDefinitionUuid) {
		this.activityDefinitionUuid = activityDefinitionUuid;
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
	
	public String getProcessDefinitionUuid() {
		return processDefinitionUuid;
	}

	public void setProcessDefinitionUuid(String processDefinitionUuid) {
		this.processDefinitionUuid = processDefinitionUuid;
	}

	public String getStartedByFormPath() {
		return startedByFormPath;
	}

	public void setStartedByFormPath(String startedByFormPath) {
		this.startedByFormPath = startedByFormPath;
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
				+ ((activityDefinitionUuid == null) ? 0
						: activityDefinitionUuid.hashCode());
		result = prime * result
				+ ((activityLabel == null) ? 0 : activityLabel.hashCode());
		result = prime * result
				+ ((expectedEndDate == null) ? 0 : expectedEndDate.hashCode());
		result = prime
				* result
				+ ((processActivityFormInstanceId == null) ? 0
						: processActivityFormInstanceId.hashCode());
		result = prime
				* result
				+ ((processDefinitionUuid == null) ? 0 : processDefinitionUuid
						.hashCode());
		result = prime
				* result
				+ ((processInstanceUuid == null) ? 0 : processInstanceUuid
						.hashCode());
		result = prime * result
				+ ((processLabel == null) ? 0 : processLabel.hashCode());
		result = prime
				* result
				+ ((startedByFormPath == null) ? 0 : startedByFormPath
						.hashCode());
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
		if (activityDefinitionUuid == null) {
			if (other.activityDefinitionUuid != null)
				return false;
		} else if (!activityDefinitionUuid.equals(other.activityDefinitionUuid))
			return false;
		if (activityLabel == null) {
			if (other.activityLabel != null)
				return false;
		} else if (!activityLabel.equals(other.activityLabel))
			return false;
		if (expectedEndDate == null) {
			if (other.expectedEndDate != null)
				return false;
		} else if (!expectedEndDate.equals(other.expectedEndDate))
			return false;
		if (processActivityFormInstanceId == null) {
			if (other.processActivityFormInstanceId != null)
				return false;
		} else if (!processActivityFormInstanceId
				.equals(other.processActivityFormInstanceId))
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
		if (processLabel == null) {
			if (other.processLabel != null)
				return false;
		} else if (!processLabel.equals(other.processLabel))
			return false;
		if (startedByFormPath == null) {
			if (other.startedByFormPath != null)
				return false;
		} else if (!startedByFormPath.equals(other.startedByFormPath))
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
				+ activityCreated + ", expectedEndDate=" + expectedEndDate
				+ ", processActivityFormInstanceId="
				+ processActivityFormInstanceId + ", processInstanceUuid="
				+ processInstanceUuid + ", taskUuid=" + taskUuid
				+ ", processDefinitionUuid=" + processDefinitionUuid
				+ ", activityDefinitionUuid=" + activityDefinitionUuid
				+ ", startedByFormPath=" + startedByFormPath + "]";
	}



}

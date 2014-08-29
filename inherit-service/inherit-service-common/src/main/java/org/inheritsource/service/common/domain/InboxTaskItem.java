/* == Motrice Copyright Notice == 
 * 
 * Motrice Service Platform 
 * 
 * Copyright (C) 2011-2014 Motrice AB 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Affero General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Affero General Public License for more details. 
 * 
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>. 
 * 
 * e-mail: info _at_ motrice.se 
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 * phone: +46 8 641 64 14 
 
 */ 
 
package org.inheritsource.service.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;


public class InboxTaskItem extends FormInstance implements Serializable, Comparable<InboxTaskItem> {
	
	private static final long serialVersionUID = -5668377849020426352L;
		
	String processLabel;
	String activityLabel;
	Date activityCreated;
	Date expectedEndDate;
	
	String rootProcessInstanceUuid;
	String processInstanceUuid;
	String rootProcessDefinitionUuid;
	String processDefinitionUuid;
	String activityDefinitionUuid;
	
	List<Tag> tags;
	
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
	
	public String getRootProcessInstanceUuid() {
		return rootProcessInstanceUuid;
	}

	public void setRootProcessInstanceUuid(String rootProcessInstanceUuid) {
		this.rootProcessInstanceUuid = rootProcessInstanceUuid;
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

	
	/*
	public String getEditFormUrl() {
		String result = null;
		if (externalUrl != null) {
			result = externalUrl;
		}
		else if (getProcessActivityFormInstanceId() != null && getProcessActivityFormInstanceId().longValue()>0) {
			result = "form?processActivityFormInstanceId=" +  getProcessActivityFormInstanceId();
		}
		else if (getTaskUuid() != null) {
			result = "form?taskUuid=" + getTaskUuid();				
		}
		return result;
	}
	*/
	
	public String getRootProcessDefinitionUuid() {
		return rootProcessDefinitionUuid;
	}

	public void setRootProcessDefinitionUuid(String rootProcessDefinitionUuid) {
		this.rootProcessDefinitionUuid = rootProcessDefinitionUuid;
	}

	public String getProcessDefinitionUuid() {
		return processDefinitionUuid;
	}

	public void setProcessDefinitionUuid(String processDefinitionUuid) {
		this.processDefinitionUuid = processDefinitionUuid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public int compareTo(InboxTaskItem other) {
		int result = -1;
		if (other == null) {
			result = 1;
		} else {
			if (getActivityCreated() == null) {
				if (other.getActivityCreated() == null) {
					result = 0;
				} 
			}
			else {
				if (other.getActivityCreated() == null) {
					result = 1;
				}
				else {
					result = getActivityCreated().compareTo(other.getActivityCreated());
				}
			}
		} 
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
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
				+ ((rootProcessDefinitionUuid == null) ? 0
						: rootProcessDefinitionUuid.hashCode());
		result = prime
				* result
				+ ((rootProcessInstanceUuid == null) ? 0
						: rootProcessInstanceUuid.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
		if (rootProcessDefinitionUuid == null) {
			if (other.rootProcessDefinitionUuid != null)
				return false;
		} else if (!rootProcessDefinitionUuid
				.equals(other.rootProcessDefinitionUuid))
			return false;
		if (rootProcessInstanceUuid == null) {
			if (other.rootProcessInstanceUuid != null)
				return false;
		} else if (!rootProcessInstanceUuid
				.equals(other.rootProcessInstanceUuid))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InboxTaskItem [processLabel=" + processLabel
				+ ", activityLabel=" + activityLabel + ", activityCreated="
				+ activityCreated + ", expectedEndDate=" + expectedEndDate
				+ ", rootProcessInstanceUuid=" + rootProcessInstanceUuid
				+ ", processInstanceUuid=" + processInstanceUuid
				+ ", rootProcessDefinitionUuid=" + rootProcessDefinitionUuid
				+ ", processDefinitionUuid=" + processDefinitionUuid
				+ ", activityDefinitionUuid=" + activityDefinitionUuid
				+ ", tags=" + tags + "]";
	}

}

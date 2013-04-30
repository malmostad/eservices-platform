package org.inheritsource.service.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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

@XStreamAlias("ProcessInstanceListItem")
public class ProcessInstanceListItem implements Serializable {
    
	// TODO byt namn på klassen
	
	public static final int STATUS_PENDING = 1;
	public static final int STATUS_FINISHED = 2;
	public static final int STATUS_CANCELLED = 3;
	public static final int STATUS_ABORTED = 4;

	
	private static final long serialVersionUID = -1985629397822454110L;
	
	private String processLabel;
	private String processInstanceLabel;
	private int status = STATUS_PENDING;
	private Set<InboxTaskItem> activities;
    private Date startDate;
    private String startedBy;
    private Date endDate;
    private String processInstanceUuid;
    
	/** 
	 * The FORM_PATH that identifies the StartFormDefinition that started the case 
	 */
	String startedByFormPath;

   // private List<CommentFeedItem> commentFeed;
    
	public ProcessInstanceListItem() {
		
	}

	public String getProcessLabel() {
		return processLabel;
	}

	public void setProcessLabel(String processLabel) {
		this.processLabel = processLabel;
	}

	public String getProcessInstanceLabel() {
		return processInstanceLabel;
	}

	public void setProcessInstanceLabel(String processInstanceLabel) {
		this.processInstanceLabel = processInstanceLabel;
	}

	public Set<InboxTaskItem> getActivities() {
		return activities;
	}

	public void setActivities(Set<InboxTaskItem> activities) {
		this.activities = activities;
	}

	public String getActivitiesStr() {
		StringBuffer status = new StringBuffer();
		if (activities != null) {
			for (InboxTaskItem activity : activities) {
				status.append(activity.getActivityLabel() + " ");
			}
		}
		return status.toString();
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public String getStartedBy() {
		return startedBy;
	}

	public void setStartedBy(String startedBy) {
		this.startedBy = startedBy;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getProcessInstanceUuid() {
		return processInstanceUuid;
	}

	public void setProcessInstanceUuid(String processInstanceUuid) {
		this.processInstanceUuid = processInstanceUuid;
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
				+ ((activities == null) ? 0 : activities.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime
				* result
				+ ((processInstanceLabel == null) ? 0 : processInstanceLabel
						.hashCode());
		result = prime
				* result
				+ ((processInstanceUuid == null) ? 0 : processInstanceUuid
						.hashCode());
		result = prime * result
				+ ((processLabel == null) ? 0 : processLabel.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result
				+ ((startedBy == null) ? 0 : startedBy.hashCode());
		result = prime
				* result
				+ ((startedByFormPath == null) ? 0 : startedByFormPath
						.hashCode());
		result = prime * result + status;
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
		ProcessInstanceListItem other = (ProcessInstanceListItem) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (processInstanceLabel == null) {
			if (other.processInstanceLabel != null)
				return false;
		} else if (!processInstanceLabel.equals(other.processInstanceLabel))
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
		if (startedByFormPath == null) {
			if (other.startedByFormPath != null)
				return false;
		} else if (!startedByFormPath.equals(other.startedByFormPath))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcessInstanceListItem [processLabel=" + processLabel
				+ ", processInstanceLabel=" + processInstanceLabel
				+ ", status=" + status + ", activities=" + activities
				+ ", startDate=" + startDate + ", startedBy=" + startedBy
				+ ", endDate=" + endDate + ", processInstanceUuid="
				+ processInstanceUuid + ", startedByFormPath="
				+ startedByFormPath + "]";
	}


}

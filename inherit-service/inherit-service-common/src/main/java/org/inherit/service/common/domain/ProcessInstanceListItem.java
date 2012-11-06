package org.inherit.service.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

@XStreamAlias("ProcessInstanceListItem")
public class ProcessInstanceListItem implements Serializable {
    
	// TODO byt namn på klassen
	
	private static final long serialVersionUID = -1985629397822454110L;
	
	private String processLabel;
    private String status;
    private Date startDate;
    private String startedBy;
    private Date endDate;
    private String processInstanceUuid;
    
    private List<CommentFeedItem> commentFeed;
    
	public ProcessInstanceListItem() {
		
	}

	public String getProcessLabel() {
		return processLabel;
	}

	public void setProcessLabel(String processLabel) {
		this.processLabel = processLabel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<CommentFeedItem> getCommentFeed() {
		return commentFeed;
	}

	public void setCommentFeed(List<CommentFeedItem> commentFeed) {
		this.commentFeed = commentFeed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((commentFeed == null) ? 0 : commentFeed.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
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
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		if (commentFeed == null) {
			if (other.commentFeed != null)
				return false;
		} else if (!commentFeed.equals(other.commentFeed))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
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
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcessInstanceListItem [processLabel=" + processLabel
				+ ", status=" + status + ", startDate=" + startDate
				+ ", startedBy=" + startedBy + ", endDate=" + endDate
				+ ", processInstanceUuid=" + processInstanceUuid
				+ ", commentFeed=" + commentFeed + "]";
	}



}

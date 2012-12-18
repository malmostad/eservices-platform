package org.inherit.service.common.domain;

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

/**
 * Log item of an executed activity instance
 * @author bjmo
 *
 */
@XStreamAlias("ActivityInstanceLogItem")
public class ActivityInstanceLogItem extends ActivityInstanceItem implements TimelineItem {
	
	private static final long serialVersionUID = 4136625617508159625L;
	
	Date endDate;
	String performedByUserId;
	String viewUrl;
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getPerformedByUserId() {
		return performedByUserId;
	}
	public void setPerformedByUserId(String performedByUserId) {
		this.performedByUserId = performedByUserId;
	}
	
	public String getViewUrl() {
		return viewUrl;
	}
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime
				* result
				+ ((performedByUserId == null) ? 0 : performedByUserId
						.hashCode());
		result = prime * result + ((viewUrl == null) ? 0 : viewUrl.hashCode());
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
		ActivityInstanceLogItem other = (ActivityInstanceLogItem) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (performedByUserId == null) {
			if (other.performedByUserId != null)
				return false;
		} else if (!performedByUserId.equals(other.performedByUserId))
			return false;
		if (viewUrl == null) {
			if (other.viewUrl != null)
				return false;
		} else if (!viewUrl.equals(other.viewUrl))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ActivityInstanceLogItem [endDate=" + endDate
				+ ", performedByUserId=" + performedByUserId 
				+ ", viewUrl=" + viewUrl
				+ " " + super.toString() +   "]";
	}
	
	/* implement TimeLineItem */
	
	@Override
	public Date getTimestamp() {
		return endDate;
	}
	@Override
	public int getType() {
		return TimelineItem.TYPE_FINISHED;
	}
	@Override
	public String getBriefDescription() {
		return activityLabel;
	}
	@Override
	public String getDescription() {
		return null;
	}
	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return this.getPerformedByUserId();
	}
	
	

}

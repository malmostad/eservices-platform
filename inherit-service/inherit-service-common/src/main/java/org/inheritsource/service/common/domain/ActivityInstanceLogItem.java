package org.inheritsource.service.common.domain;

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
 * Log item of an executed activity instance
 * @author bjmo
 *
 */
@XStreamAlias("ActivityInstanceLogItem")
public class ActivityInstanceLogItem extends ActivityInstanceItem implements TimelineItem {
	
	private static final long serialVersionUID = 4136625617508159625L;
	
	Date endDate;
	UserInfo performedByUser;
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public UserInfo getPerformedByUser() {
		return performedByUser;
	}
	public void setPerformedByUser(UserInfo performedByUser) {
		this.performedByUser = performedByUser;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime
				* result
				+ ((performedByUser == null) ? 0 : performedByUser
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
		if (performedByUser == null) {
			if (other.performedByUser != null)
				return false;
		} else if (!performedByUser.equals(other.performedByUser))
			return false;
		if (viewUrl == null) {
			if (other.viewUrl != null)
				return false;
		} else if (!viewUrl.equals(other.viewUrl))
			return false;
		return true;
	}
	
	/* implement TimeLineItem */
	
	@Override
	public Date getTimestamp() {
		return endDate;
	}
	
	
	
	@Override
	public String toString() {
		return "ActivityInstanceLogItem [endDate=" + endDate
				+ ", performedByUser=" + performedByUser + ", viewUrl="
				+ viewUrl + ", processDefinitionUuid=" + processDefinitionUuid
				+ ", processInstanceUuid=" + processInstanceUuid
				+ ", activityDefinitionUuid=" + activityDefinitionUuid
				+ ", activityInstanceUuid=" + activityInstanceUuid
				+ ", activityName=" + activityName + ", activityLabel="
				+ activityLabel + ", startDate=" + startDate
				+ ", currentState=" + currentState + ", lastStateUpdate="
				+ lastStateUpdate + ", lastStateUpdateByUserId="
				+ lastStateUpdateByUserId + ", startedBy=" + startedBy
				+ ", expectedEndDate=" + expectedEndDate + ", formUrl="
				+ formUrl + ", formDocId=" + formDocId + ", activityType="
				+ activityType + ", priority=" + priority
				+ ", processActivityFormInstanceId="
				+ processActivityFormInstanceId + ", page=" + page
				+ ", viewUrlExternal=" + viewUrlExternal + ", editUrl="
				+ editUrl + ", editUrlExternal=" + editUrlExternal
				+ ", dataUri=" + dataUri + ", definitionKey=" + definitionKey
				+ ", typeId=" + typeId + ", instanceId=" + instanceId
				+ ", actUri=" + actUri + ", actinstId=" + actinstId
				+ ", submitted=" + submitted + ", submittedBy=" + submittedBy
				+ "]";
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
	public UserInfo getUser() {
		// TODO Auto-generated method stub
		return getPerformedByUser();
	}
	
	

}

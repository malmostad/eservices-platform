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

import java.util.Date;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("ActivityInstancePendingItem")
public class ActivityInstancePendingItem extends ActivityInstanceItem {

	private static final long serialVersionUID = 6551923994738613262L;

	Set<CandidateInfo> candidates;
	UserInfo assignedUser;
	
	public ActivityInstancePendingItem() {
		
	}

	public Set<CandidateInfo> getCandidates() {
		return candidates;
	}

	public void setCandidates(Set<CandidateInfo> candidates) {
		this.candidates = candidates;
	}

	public UserInfo getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(UserInfo assignedUser) {
		this.assignedUser = assignedUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((assignedUser == null) ? 0 : assignedUser.hashCode());
		result = prime * result
				+ ((candidates == null) ? 0 : candidates.hashCode());
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
		ActivityInstancePendingItem other = (ActivityInstancePendingItem) obj;
		if (assignedUser == null) {
			if (other.assignedUser != null)
				return false;
		} else if (!assignedUser.equals(other.assignedUser))
			return false;
		if (candidates == null) {
			if (other.candidates != null)
				return false;
		} else if (!candidates.equals(other.candidates))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityInstancePendingItem [candidates=" + candidates
				+ ", assignedUser=" + assignedUser + ", processDefinitionUuid="
				+ processDefinitionUuid + ", processInstanceUuid="
				+ processInstanceUuid + ", activityDefinitionUuid="
				+ activityDefinitionUuid + ", activityInstanceUuid="
				+ activityInstanceUuid + ", activityName=" + activityName
				+ ", activityLabel=" + activityLabel + ", startDate="
				+ startDate + ", currentState=" + currentState
				+ ", lastStateUpdate=" + lastStateUpdate
				+ ", lastStateUpdateByUserId=" + lastStateUpdateByUserId
				+ ", startedBy=" + startedBy + ", expectedEndDate="
				+ expectedEndDate + ", activityType=" + activityType
				+ ", priority=" + priority + ", processActivityFormInstanceId="
				+ processActivityFormInstanceId + ", page=" + page
				+ ", viewUrl=" + viewUrl + ", viewUrlExternal="
				+ viewUrlExternal + ", editUrl=" + editUrl
				+ ", editUrlExternal=" + editUrlExternal + ", dataUri="
				+ dataUri + ", definitionKey=" + definitionKey + ", typeId="
				+ typeId + ", instanceId=" + instanceId + ", actUri=" + actUri
				+ ", actinstId=" + actinstId + ", submitted=" + submitted
				+ ", submittedBy=" + submittedBy + "]";
	}

}

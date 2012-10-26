package org.inherit.service.common.domain;

import java.util.Date;
import java.util.Set;

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

@XStreamAlias("ActivityInstancePendingItem")
public class ActivityInstancePendingItem extends ActivityInstanceItem {

	private static final long serialVersionUID = 6551923994738613262L;

	Date expectedEndDate;
	Set<String> candidates;
	String assignedUserId;
	
	public ActivityInstancePendingItem() {
		
	}

	public Date getExpectedEndDate() {
		return expectedEndDate;
	}

	public void setExpectedEndDate(Date expectedEndDate) {
		this.expectedEndDate = expectedEndDate;
	}

	public Set<String> getCandidates() {
		return candidates;
	}

	public void setCandidates(Set<String> candidates) {
		this.candidates = candidates;
	}

	public String getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(String assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((assignedUserId == null) ? 0 : assignedUserId.hashCode());
		result = prime * result
				+ ((candidates == null) ? 0 : candidates.hashCode());
		result = prime * result
				+ ((expectedEndDate == null) ? 0 : expectedEndDate.hashCode());
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
		if (assignedUserId == null) {
			if (other.assignedUserId != null)
				return false;
		} else if (!assignedUserId.equals(other.assignedUserId))
			return false;
		if (candidates == null) {
			if (other.candidates != null)
				return false;
		} else if (!candidates.equals(other.candidates))
			return false;
		if (expectedEndDate == null) {
			if (other.expectedEndDate != null)
				return false;
		} else if (!expectedEndDate.equals(other.expectedEndDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityInstancePendingItem [expectedEndDate="
				+ expectedEndDate + ", candidates=" + candidates
				+ ", assignedUserId=" + assignedUserId 
				+ " " + super.toString() + "]";
	}
	
	
}

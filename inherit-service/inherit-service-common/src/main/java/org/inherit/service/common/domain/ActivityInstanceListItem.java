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

@XStreamAlias("ActivityInstanceListItem")
public class ActivityInstanceListItem implements Serializable {

	private static final long serialVersionUID = -4720396738012036540L;
	
	String activityLabel;
	String currentState;
	Date lastStateUpdate;
	String lastStateUpdateByUserId;
	
	// internal taskform-engine id, do not store, use in session communication 
	Long processActivityFormInstanceId;

	public ActivityInstanceListItem() {
		
	}

	public String getActivityLabel() {
		return activityLabel;
	}

	public void setActivityLabel(String activityLabel) {
		this.activityLabel = activityLabel;
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

	public Long getProcessActivityFormInstanceId() {
		return processActivityFormInstanceId;
	}

	public void setProcessActivityFormInstanceId(Long processActivityFormInstanceId) {
		this.processActivityFormInstanceId = processActivityFormInstanceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activityLabel == null) ? 0 : activityLabel.hashCode());
		result = prime * result
				+ ((currentState == null) ? 0 : currentState.hashCode());
		result = prime * result
				+ ((lastStateUpdate == null) ? 0 : lastStateUpdate.hashCode());
		result = prime
				* result
				+ ((lastStateUpdateByUserId == null) ? 0
						: lastStateUpdateByUserId.hashCode());
		result = prime
				* result
				+ ((processActivityFormInstanceId == null) ? 0
						: processActivityFormInstanceId.hashCode());
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
		ActivityInstanceListItem other = (ActivityInstanceListItem) obj;
		if (activityLabel == null) {
			if (other.activityLabel != null)
				return false;
		} else if (!activityLabel.equals(other.activityLabel))
			return false;
		if (currentState == null) {
			if (other.currentState != null)
				return false;
		} else if (!currentState.equals(other.currentState))
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
		if (processActivityFormInstanceId == null) {
			if (other.processActivityFormInstanceId != null)
				return false;
		} else if (!processActivityFormInstanceId
				.equals(other.processActivityFormInstanceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityInstanceListItem [activityLabel=" + activityLabel
				+ ", currentState=" + currentState + ", lastStateUpdate="
				+ lastStateUpdate + ", lastStateUpdateByUserId="
				+ lastStateUpdateByUserId + ", processActivityFormInstanceId="
				+ processActivityFormInstanceId + "]";
	}
	
}

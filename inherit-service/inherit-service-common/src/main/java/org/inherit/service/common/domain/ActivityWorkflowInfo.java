package org.inherit.service.common.domain;

import java.io.Serializable;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ActivityWorkflowInfo")
public class ActivityWorkflowInfo implements Serializable {
	
	private static final long serialVersionUID = 8813710157742764480L;
	
	UserInfo assignedUser;
	Set<UserInfo> candidates;
	int priority;
	
	public ActivityWorkflowInfo() {
		
	}

	public UserInfo getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(UserInfo assignedUser) {
		this.assignedUser = assignedUser;
	}

	public Set<UserInfo> getCandidates() {
		return candidates;
	}

	public void setCandidates(Set<UserInfo> candidates) {
		this.candidates = candidates;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assignedUser == null) ? 0 : assignedUser.hashCode());
		result = prime * result
				+ ((candidates == null) ? 0 : candidates.hashCode());
		result = prime * result + priority;
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
		ActivityWorkflowInfo other = (ActivityWorkflowInfo) obj;
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
		if (priority != other.priority)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityWorkflowInfo [assignedUser=" + assignedUser
				+ ", candidates=" + candidates + ", priority=" + priority + "]";
	}
	
}

package org.inherit.service.common.domain;

import java.io.Serializable;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ActivityWorkflowInfo")
public class ActivityWorkflowInfo implements Serializable {
	
	private static final long serialVersionUID = 8813710157742764480L;
	
	String assignedUserId;
	Set<String> candidates;
	int priority;
	
	public ActivityWorkflowInfo() {
		
	}

	public String getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(String assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public Set<String> getCandidates() {
		return candidates;
	}

	public void setCandidates(Set<String> candidates) {
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
				+ ((assignedUserId == null) ? 0 : assignedUserId.hashCode());
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
		if (priority != other.priority)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityWorkflowInfo [assignedUserId=" + assignedUserId
				+ ", candidates=" + candidates + ", priority=" + priority + "]";
	}
	
}

package org.inheritsource.service.common.domain;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CommentFeedItem")
public class CommentFeedItem implements Serializable, TimelineItem {
	
	private static final long serialVersionUID = 7249719404625798791L;
	
	String processDefinitionUuid;
	String processInstanceUuid;
	String processLabel;
	String activityDefinitionUuid;
	String activityInstanceUuid;
	String activityLabel;
	Date timestamp;
	String message;
	UserInfo user;
	
	public CommentFeedItem() {

	}

	public String getProcessInstanceUuid() {
		return processInstanceUuid;
	}

	public void setProcessInstanceUuid(String processInstanceUuid) {
		this.processInstanceUuid = processInstanceUuid;
	}

	public String getProcessLabel() {
		return processLabel;
	}

	public void setProcessLabel(String processLabel) {
		this.processLabel = processLabel;
	}

	public String getActivityInstanceUuid() {
		return activityInstanceUuid;
	}

	public void setActivityInstanceUuid(String activityInstanceUuid) {
		this.activityInstanceUuid = activityInstanceUuid;
	}

	public String getActivityLabel() {
		return activityLabel;
	}

	public void setActivityLabel(String activityLabel) {
		this.activityLabel = activityLabel;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}
	
	public String getProcessDefinitionUuid() {
		return processDefinitionUuid;
	}

	public void setProcessDefinitionUuid(String processDefinitionUuid) {
		this.processDefinitionUuid = processDefinitionUuid;
	}

	public String getActivityDefinitionUuid() {
		return activityDefinitionUuid;
	}

	public void setActivityDefinitionUuid(String activityDefinitionUuid) {
		this.activityDefinitionUuid = activityDefinitionUuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activityDefinitionUuid == null) ? 0
						: activityDefinitionUuid.hashCode());
		result = prime
				* result
				+ ((activityInstanceUuid == null) ? 0 : activityInstanceUuid
						.hashCode());
		result = prime * result
				+ ((activityLabel == null) ? 0 : activityLabel.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		CommentFeedItem other = (CommentFeedItem) obj;
		if (activityDefinitionUuid == null) {
			if (other.activityDefinitionUuid != null)
				return false;
		} else if (!activityDefinitionUuid.equals(other.activityDefinitionUuid))
			return false;
		if (activityInstanceUuid == null) {
			if (other.activityInstanceUuid != null)
				return false;
		} else if (!activityInstanceUuid.equals(other.activityInstanceUuid))
			return false;
		if (activityLabel == null) {
			if (other.activityLabel != null)
				return false;
		} else if (!activityLabel.equals(other.activityLabel))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
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
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CommentFeedItem [processDefinitionUuid="
				+ processDefinitionUuid + ", processInstanceUuid="
				+ processInstanceUuid + ", processLabel=" + processLabel
				+ ", activityDefinitionUuid=" + activityDefinitionUuid
				+ ", activityInstanceUuid=" + activityInstanceUuid
				+ ", activityLabel=" + activityLabel + ", timestamp="
				+ timestamp + ", message=" + message + ", user=" + user + "]";
	}

	@Override
	public Date getTimestamp() {
		return timestamp;
	}

	@Override
	public int getType() {
		return TimelineItem.TYPE_COMMENT;
	}

	@Override
	public String getBriefDescription() {
		return message;
	}

	@Override
	public String getDescription() {
		return message;
	}

	@Override
	public String getViewUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}

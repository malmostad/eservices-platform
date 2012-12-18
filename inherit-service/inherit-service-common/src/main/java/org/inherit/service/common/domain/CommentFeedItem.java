package org.inherit.service.common.domain;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CommentFeedItem")
public class CommentFeedItem implements Serializable, TimelineItem {
	
	private static final long serialVersionUID = 7249719404625798791L;
	
	String processInstanceUuid;
	String activityInstanceUuid;
	Date timestamp;
	String message;
	String userId;
	
	public CommentFeedItem() {

	}

	public String getProcessInstanceUuid() {
		return processInstanceUuid;
	}

	public void setProcessInstanceUuid(String processInstanceUuid) {
		this.processInstanceUuid = processInstanceUuid;
	}

	public String getActivityInstanceUuid() {
		return activityInstanceUuid;
	}

	public void setActivityInstanceUuid(String activityInstanceUuid) {
		this.activityInstanceUuid = activityInstanceUuid;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activityInstanceUuid == null) ? 0 : activityInstanceUuid
						.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime
				* result
				+ ((processInstanceUuid == null) ? 0 : processInstanceUuid
						.hashCode());
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		if (activityInstanceUuid == null) {
			if (other.activityInstanceUuid != null)
				return false;
		} else if (!activityInstanceUuid.equals(other.activityInstanceUuid))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (processInstanceUuid == null) {
			if (other.processInstanceUuid != null)
				return false;
		} else if (!processInstanceUuid.equals(other.processInstanceUuid))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CommentFeedItem [processInstanceUuid=" + processInstanceUuid
				+ ", activityInstanceUuid=" + activityInstanceUuid
				+ ", timeStamp=" + timestamp + ", message=" + message
				+ ", userId=" + userId + "]";
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

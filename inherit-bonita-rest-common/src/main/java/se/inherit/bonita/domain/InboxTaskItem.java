package se.inherit.bonita.domain;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
Copyright (C) 2011 Inherit S AB

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

@XStreamAlias("message")
public class InboxTaskItem implements Serializable {
	
	private static final long serialVersionUID = -6929971693624511343L;

	String processName;
	String taskUUID;
	String processLabel;
	String activityLabel;
	Date createdDate;
	String activityDefinitionUUID;
	
	public InboxTaskItem() {
		
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getTaskUUID() {
		return taskUUID;
	}

	public void setTaskUUID(String taskUUID) {
		this.taskUUID = taskUUID;
	}

	public String getProcessLabel() {
		return processLabel;
	}

	public void setProcessLabel(String processLabel) {
		this.processLabel = processLabel;
	}

	public String getActivityLabel() {
		return activityLabel;
	}

	public void setActivityLabel(String activityLabel) {
		this.activityLabel = activityLabel;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getActivityDefinitionUUID() {
		return activityDefinitionUUID;
	}

	public void setActivityDefinitionUUID(String activityDefinitionUUID) {
		this.activityDefinitionUUID = activityDefinitionUUID;
	}

	
	
	
	
}

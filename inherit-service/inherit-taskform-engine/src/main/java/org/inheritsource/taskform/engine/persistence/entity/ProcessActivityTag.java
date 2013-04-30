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
 
package org.inheritsource.taskform.engine.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ProcessActivityTag {

	@Id
	@GeneratedValue
	Long processActivityTagId;

	@ManyToOne
    @JoinColumn(name="tagTypeId", nullable=false)
	TagType type;

	@ManyToOne
    @JoinColumn(name="processActivityFormInstanceId", nullable=false)
	ProcessActivityFormInstance processActivityFormInstance;

	@Column(nullable=false)
	String value;
	
	/**
	 * Timestamp of last write
	 */
	@Column(nullable=false)
	Date timestamp = null;
	
	/**
	 * The last writer
	 */
	@Column(nullable=false)
	String userId;
	
	public ProcessActivityTag() {
		
	}

	public Long getProcessActivityTagId() {
		return processActivityTagId;
	}

	public void setProcessActivityTagId(Long processActivityTagId) {
		this.processActivityTagId = processActivityTagId;
	}

	public TagType getType() {
		return type;
	}

	public void setType(TagType type) {
		this.type = type;
	}

	public ProcessActivityFormInstance getProcessActivityFormInstance() {
		return processActivityFormInstance;
	}

	public void setProcessActivityFormInstance(
			ProcessActivityFormInstance processActivityFormInstance) {
		this.processActivityFormInstance = processActivityFormInstance;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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
				+ ((processActivityFormInstance == null) ? 0
						: processActivityFormInstance.hashCode());
		result = prime
				* result
				+ ((processActivityTagId == null) ? 0 : processActivityTagId
						.hashCode());
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		ProcessActivityTag other = (ProcessActivityTag) obj;
		if (processActivityFormInstance == null) {
			if (other.processActivityFormInstance != null)
				return false;
		} else if (!processActivityFormInstance
				.equals(other.processActivityFormInstance))
			return false;
		if (processActivityTagId == null) {
			if (other.processActivityTagId != null)
				return false;
		} else if (!processActivityTagId.equals(other.processActivityTagId))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcessActivityTag [processActivityTagId="
				+ processActivityTagId + ", type=" + type
				+ ", processActivityFormInstance="
				+ processActivityFormInstance + ", value=" + value
				+ ", timestamp=" + timestamp + ", userId=" + userId + "]";
	}

	
}

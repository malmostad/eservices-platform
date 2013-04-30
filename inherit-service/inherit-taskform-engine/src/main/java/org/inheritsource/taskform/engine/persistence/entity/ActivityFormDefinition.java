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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Define which form to use with a specific activity definition. 
 * 
 * In general each activityDefinitionUuid should have a default 
 * form defined. The default form can be overridden with a specific 
 * form depending on the startFormDefinition. 
 * 
 * @author bjmo
 *
 */
@Entity
public class ActivityFormDefinition {

	public static final String ENTRY_UUID = "ENTRY";
	
	@Id
	@GeneratedValue
	Long activityFormDefinitionId;
	
	/**
	 * If null - default form for activity
	 * If assigned id, special form depending on process start form 
	 * 
	 * In general each activityDefinitionUuid should have a default 
	 * form defined.
	 */
	@ManyToOne
    @JoinColumn(name="startFormDefinitionId", nullable=true)
	StartFormDefinition startFormDefinition;
	
	
	/**
	 * Activity defintion uuid 
	 */
	String activityDefinitionUuid;
	
	/**
	 * Path to activity form
	 */
	String formPath;

	
	public ActivityFormDefinition() {
		
	}


	public Long getActivityFormDefinitionId() {
		return activityFormDefinitionId;
	}


	public void setActivityFormDefinitionId(Long activityFormDefinitionId) {
		this.activityFormDefinitionId = activityFormDefinitionId;
	}


	public StartFormDefinition getStartFormDefinition() {
		return startFormDefinition;
	}


	public void setStartFormDefinition(StartFormDefinition startFormDefinition) {
		this.startFormDefinition = startFormDefinition;
	}


	public String getActivityDefinitionUuid() {
		return activityDefinitionUuid;
	}


	public void setActivityDefinitionUuid(String activityDefinitionUuid) {
		this.activityDefinitionUuid = activityDefinitionUuid;
	}


	public String getFormPath() {
		return formPath;
	}


	public void setFormPath(String formPath) {
		this.formPath = formPath;
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
				+ ((activityFormDefinitionId == null) ? 0
						: activityFormDefinitionId.hashCode());
		result = prime * result
				+ ((formPath == null) ? 0 : formPath.hashCode());
		result = prime
				* result
				+ ((startFormDefinition == null) ? 0 : startFormDefinition
						.hashCode());
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
		ActivityFormDefinition other = (ActivityFormDefinition) obj;
		if (activityDefinitionUuid == null) {
			if (other.activityDefinitionUuid != null)
				return false;
		} else if (!activityDefinitionUuid.equals(other.activityDefinitionUuid))
			return false;
		if (activityFormDefinitionId == null) {
			if (other.activityFormDefinitionId != null)
				return false;
		} else if (!activityFormDefinitionId
				.equals(other.activityFormDefinitionId))
			return false;
		if (formPath == null) {
			if (other.formPath != null)
				return false;
		} else if (!formPath.equals(other.formPath))
			return false;
		if (startFormDefinition == null) {
			if (other.startFormDefinition != null)
				return false;
		} else if (!startFormDefinition.equals(other.startFormDefinition))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "ActivityFormDefinition [activityFormDefinitionId="
				+ activityFormDefinitionId + ", startFormDefinition="
				+ startFormDefinition + ", activityDefinitionUuid="
				+ activityDefinitionUuid + ", formPath=" + formPath + "]";
	}
	
	

	
}

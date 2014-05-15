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
 
 
package org.inheritsource.taskform.engine.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name="mtf_activity_form_definition")
public class ActivityFormDefinition {

	public static final String ENTRY_UUID = "ENTRY";
	
	@Id
	@GeneratedValue
	@Column(name="activity_form_definition_id")
	Long activityFormDefinitionId;
		
	/**
	 * Process definition id (Activiti TaskDefinitionKey) 
	 */
	@Column(name="procdef_id")
	String procdefId;
	
	/**
	 * Activity definition id (Activiti TaskDefinitionKey) 
	 */
	@Column(name="actdef_id")
	String actdefId;
	
	/**
	 * form type identifies a Motrice form handler i.e. Orbeon, sign, noform etc
	 */
	@Column(name="form_type_id")
	Long formTypeId;

	/**
	 * identifies a specific form in a Motrice form handler engine. The form handler engine 
	 * is responsibly to know how to interpret the formDefinitionKey
	 */
	@Column(name="form_connection_key")
	String formConnectionKey;
	
	public ActivityFormDefinition() {
		
	}

	public Long getActivityFormDefinitionId() {
		return activityFormDefinitionId;
	}

	public void setActivityFormDefinitionId(Long activityFormDefinitionId) {
		this.activityFormDefinitionId = activityFormDefinitionId;
	}

	public String getProcdefId() {
		return procdefId;
	}

	public void setProcdefId(String procdefId) {
		this.procdefId = procdefId;
	}

	public String getActdefId() {
		return actdefId;
	}

	public void setActdefId(String actdefId) {
		this.actdefId = actdefId;
	}

	public Long getFormTypeId() {
		return formTypeId;
	}

	public void setFormTypeId(Long formTypeId) {
		this.formTypeId = formTypeId;
	}

	public String getFormConnectionKey() {
		return formConnectionKey;
	}

	public void setFormConnectionKey(String formConnectionKey) {
		this.formConnectionKey = formConnectionKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actdefId == null) ? 0 : actdefId.hashCode());
		result = prime
				* result
				+ ((activityFormDefinitionId == null) ? 0
						: activityFormDefinitionId.hashCode());
		result = prime
				* result
				+ ((formConnectionKey == null) ? 0 : formConnectionKey
						.hashCode());
		result = prime * result
				+ ((formTypeId == null) ? 0 : formTypeId.hashCode());
		result = prime * result
				+ ((procdefId == null) ? 0 : procdefId.hashCode());
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
		if (actdefId == null) {
			if (other.actdefId != null)
				return false;
		} else if (!actdefId.equals(other.actdefId))
			return false;
		if (activityFormDefinitionId == null) {
			if (other.activityFormDefinitionId != null)
				return false;
		} else if (!activityFormDefinitionId
				.equals(other.activityFormDefinitionId))
			return false;
		if (formConnectionKey == null) {
			if (other.formConnectionKey != null)
				return false;
		} else if (!formConnectionKey.equals(other.formConnectionKey))
			return false;
		if (formTypeId == null) {
			if (other.formTypeId != null)
				return false;
		} else if (!formTypeId.equals(other.formTypeId))
			return false;
		if (procdefId == null) {
			if (other.procdefId != null)
				return false;
		} else if (!procdefId.equals(other.procdefId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityFormDefinition [activityFormDefinitionId="
				+ activityFormDefinitionId + ", procdefId=" + procdefId
				+ ", actdefId=" + actdefId + ", formTypeId=" + formTypeId
				+ ", formConnectionKey=" + formConnectionKey + "]";
	}
	
}

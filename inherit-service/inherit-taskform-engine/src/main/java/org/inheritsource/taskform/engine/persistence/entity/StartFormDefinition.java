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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A given form path can have an associated processDefinitionUuid that will 
 * be started on submit. 
 * Note that different form paths can start the same process.
 * @author bjmo
 *
 */
@Entity
public class StartFormDefinition {

	@Id
	@GeneratedValue
	Long startFormDefinitionId;
	
	/**
	 * Process definition uuid to start when this start form is submitted
	 */
	String processDefinitionUuid;
		
	/**
	 *  Path to start form
	 */
	@Column(nullable = false, unique = true)
	String formPath;
	

	public StartFormDefinition() {
		
	}

	public Long getStartFormDefinitionId() {
		return startFormDefinitionId;
	}

	public void setStartFormDefinitionId(Long startFormDefinitionId) {
		this.startFormDefinitionId = startFormDefinitionId;
	}

	public String getProcessDefinitionUuid() {
		return processDefinitionUuid;
	}

	public void setProcessDefinitionUuid(String processDefinitionUuid) {
		this.processDefinitionUuid = processDefinitionUuid;
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
		result = prime * result
				+ ((formPath == null) ? 0 : formPath.hashCode());
		result = prime
				* result
				+ ((processDefinitionUuid == null) ? 0 : processDefinitionUuid
						.hashCode());
		result = prime
				* result
				+ ((startFormDefinitionId == null) ? 0 : startFormDefinitionId
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
		StartFormDefinition other = (StartFormDefinition) obj;
		if (formPath == null) {
			if (other.formPath != null)
				return false;
		} else if (!formPath.equals(other.formPath))
			return false;
		if (processDefinitionUuid == null) {
			if (other.processDefinitionUuid != null)
				return false;
		} else if (!processDefinitionUuid.equals(other.processDefinitionUuid))
			return false;
		if (startFormDefinitionId == null) {
			if (other.startFormDefinitionId != null)
				return false;
		} else if (!startFormDefinitionId.equals(other.startFormDefinitionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StartFormDefinition [startFormDefinitionId="
				+ startFormDefinitionId + ", processDefinitionUuid="
				+ processDefinitionUuid + ", formPath=" + formPath + "]";
	}
	
}

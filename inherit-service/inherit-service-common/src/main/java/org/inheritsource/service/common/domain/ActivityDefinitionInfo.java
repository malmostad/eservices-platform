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
 
package org.inheritsource.service.common.domain;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("ActivityDefinitionInfo")
public class ActivityDefinitionInfo implements Serializable {
	
	private static final long serialVersionUID = 4331963577881197712L;

	/**
	 * activity definition uuid in bpmn engine 
	 */
	private String uuid;
	
	/**
	 * activity name in bpmn engine 
	 */
	private String name;
	
	/**
	 * activity label in bpmn engine 
	 */
	private String label;
		
	/**
	 * Can be
	 * - orbeon formPath at the form APP/FORM--vxxx
	 * - "none" , html form with a single submit button is rendered instead of orbeon form
	 * - "signstartform"
	 * - "signactivity/ACTIVITYNAME" where ACTIVITYNAME is the name of a prevoius performed activity in bpmn engine
	 * - "paystartform" 
	 * - "paystartform/ACTIVITYNAME" where ACTIVITYNAME is the name of a prevoius performed activity in bpmn engine
	 */
	String formPath;
	
	
	public ActivityDefinitionInfo() {
		
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
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
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		ActivityDefinitionInfo other = (ActivityDefinitionInfo) obj;
		if (formPath == null) {
			if (other.formPath != null)
				return false;
		} else if (!formPath.equals(other.formPath))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "ActivityDefinitionInfo [uuid=" + uuid + ", name=" + name
				+ ", label=" + label + ", formPath=" + formPath + "]";
	}
	

		
}

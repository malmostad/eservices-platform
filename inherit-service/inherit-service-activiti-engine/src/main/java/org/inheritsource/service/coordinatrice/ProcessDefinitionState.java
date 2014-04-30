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
 
package org.inheritsource.service.coordinatrice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="processdefinitionstate")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessDefinitionState implements Serializable {
	
	private static final long serialVersionUID = 7266283364658397170L;
	
	String procdefId; 
	String procdefVer;
	int stateCode;
	String stateName;
	String editable;
    int startableCode;     
	String startableName;
	
	public ProcessDefinitionState() {
		
	}

	public String getProcdefId() {
		return procdefId;
	}

	public void setProcdefId(String procdefId) {
		this.procdefId = procdefId;
	}

	public String getProcdefVer() {
		return procdefVer;
	}

	public void setProcdefVer(String procdefVer) {
		this.procdefVer = procdefVer;
	}

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public int getStartableCode() {
		return startableCode;
	}

	public void setStartableCode(int startableCode) {
		this.startableCode = startableCode;
	}

	public String getStartableName() {
		return startableName;
	}

	public void setStartableName(String startableName) {
		this.startableName = startableName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((editable == null) ? 0 : editable.hashCode());
		result = prime * result
				+ ((procdefId == null) ? 0 : procdefId.hashCode());
		result = prime * result
				+ ((procdefVer == null) ? 0 : procdefVer.hashCode());
		result = prime * result + startableCode;
		result = prime * result
				+ ((startableName == null) ? 0 : startableName.hashCode());
		result = prime * result + stateCode;
		result = prime * result
				+ ((stateName == null) ? 0 : stateName.hashCode());
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
		ProcessDefinitionState other = (ProcessDefinitionState) obj;
		if (editable == null) {
			if (other.editable != null)
				return false;
		} else if (!editable.equals(other.editable))
			return false;
		if (procdefId == null) {
			if (other.procdefId != null)
				return false;
		} else if (!procdefId.equals(other.procdefId))
			return false;
		if (procdefVer == null) {
			if (other.procdefVer != null)
				return false;
		} else if (!procdefVer.equals(other.procdefVer))
			return false;
		if (startableCode != other.startableCode)
			return false;
		if (startableName == null) {
			if (other.startableName != null)
				return false;
		} else if (!startableName.equals(other.startableName))
			return false;
		if (stateCode != other.stateCode)
			return false;
		if (stateName == null) {
			if (other.stateName != null)
				return false;
		} else if (!stateName.equals(other.stateName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcessDefinitionState [procdefId=" + procdefId
				+ ", procdefVer=" + procdefVer + ", stateCode=" + stateCode
				+ ", stateName=" + stateName + ", editable=" + editable
				+ ", startableCode=" + startableCode + ", startableName="
				+ startableName + "]";
	}
    
	
}

package org.inheritsource.service.coordinatrice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="activitylabel")
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

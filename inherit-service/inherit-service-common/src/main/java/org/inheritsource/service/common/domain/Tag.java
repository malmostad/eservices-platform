package org.inheritsource.service.common.domain;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Tag")
public class Tag implements Serializable {
	
	private static final long serialVersionUID = -3351037984381420720L;
	
	Long processActivityTagId;
	Long processActivityFormInstanceId;
	Long typeId;
	String typeLabel;
	String value;
	
	public Tag() {
		
	}

	public Long getProcessActivityTagId() {
		return processActivityTagId;
	}

	public void setProcessActivityTagId(Long processActivityTagId) {
		this.processActivityTagId = processActivityTagId;
	}

	public Long getProcessActivityFormInstanceId() {
		return processActivityFormInstanceId;
	}

	public void setProcessActivityFormInstanceId(Long processActivityFormInstanceId) {
		this.processActivityFormInstanceId = processActivityFormInstanceId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((processActivityFormInstanceId == null) ? 0
						: processActivityFormInstanceId.hashCode());
		result = prime
				* result
				+ ((processActivityTagId == null) ? 0 : processActivityTagId
						.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result
				+ ((typeLabel == null) ? 0 : typeLabel.hashCode());
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
		Tag other = (Tag) obj;
		if (processActivityFormInstanceId == null) {
			if (other.processActivityFormInstanceId != null)
				return false;
		} else if (!processActivityFormInstanceId
				.equals(other.processActivityFormInstanceId))
			return false;
		if (processActivityTagId == null) {
			if (other.processActivityTagId != null)
				return false;
		} else if (!processActivityTagId.equals(other.processActivityTagId))
			return false;
		if (typeId == null) {
			if (other.typeId != null)
				return false;
		} else if (!typeId.equals(other.typeId))
			return false;
		if (typeLabel == null) {
			if (other.typeLabel != null)
				return false;
		} else if (!typeLabel.equals(other.typeLabel))
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
		return "Tag [processActivityTagId=" + processActivityTagId
				+ ", processActivityFormInstanceId="
				+ processActivityFormInstanceId + ", typeId=" + typeId
				+ ", typeLabel=" + typeLabel + ", value=" + value + "]";
	}
	
	
}

package org.inheritsource.service.common.domain;

import java.io.Serializable;

public class UserInfo implements Serializable, Comparable {

	private static final long serialVersionUID = -4478283095497351127L;

	public static final int CATEGORY_INTERNAL = 1;
	public static final int CATEGORY_EXTERNAL = 2;

	String uuid;
	
	String labelShort;
	
	String label;
	
	int category;

	public UserInfo() {
		
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getLabelShort() {
		return labelShort;
	}

	public void setLabelShort(String labelShort) {
		this.labelShort = labelShort;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + category;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result
				+ ((labelShort == null) ? 0 : labelShort.hashCode());
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
		UserInfo other = (UserInfo) obj;
		if (category != other.category)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (labelShort == null) {
			if (other.labelShort != null)
				return false;
		} else if (!labelShort.equals(other.labelShort))
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
		return labelShort;
	}

	@Override
	public int compareTo(Object o) {
		String ostr = (o==null ? "null" : o.toString());
		return this.toString().compareTo(ostr);
	}
	
	
}

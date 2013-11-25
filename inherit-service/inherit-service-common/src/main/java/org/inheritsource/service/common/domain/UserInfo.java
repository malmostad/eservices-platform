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
 
package org.inheritsource.service.common.domain;

import java.io.Serializable;

public class UserInfo implements Serializable, Comparable {

	private static final long serialVersionUID = -4478283095497351127L;

	public static final int CATEGORY_INTERNAL = 1;
	public static final int CATEGORY_EXTERNAL = 2;

	public static final String ANONYMOUS_UUID = "anonymous";
	
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
		return "" + labelShort;
	}

	@Override
	public int compareTo(Object o) {
		String ostr = (o==null ? "null" : o.toString());
		return this.toString().compareTo(ostr);
	}
	
	
}

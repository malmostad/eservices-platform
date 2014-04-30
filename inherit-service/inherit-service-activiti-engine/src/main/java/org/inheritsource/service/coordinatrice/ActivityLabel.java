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

@XmlRootElement(name="activitylabel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivityLabel implements Serializable {
	
	private static final long serialVersionUID = 6692433040348188467L;
	
	String procdefKey;
    String procdefVer;
    String actdefName;
    String locale;
    String label;
    
    public ActivityLabel() {
    	
    }

	public String getProcdefKey() {
		return procdefKey;
	}

	public void setProcdefKey(String procdefKey) {
		this.procdefKey = procdefKey;
	}

	public String getProcdefVer() {
		return procdefVer;
	}

	public void setProcdefVer(String procdefVer) {
		this.procdefVer = procdefVer;
	}

	public String getActdefName() {
		return actdefName;
	}

	public void setActdefName(String actdefName) {
		this.actdefName = actdefName;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actdefName == null) ? 0 : actdefName.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result
				+ ((procdefKey == null) ? 0 : procdefKey.hashCode());
		result = prime * result
				+ ((procdefVer == null) ? 0 : procdefVer.hashCode());
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
		ActivityLabel other = (ActivityLabel) obj;
		if (actdefName == null) {
			if (other.actdefName != null)
				return false;
		} else if (!actdefName.equals(other.actdefName))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (procdefKey == null) {
			if (other.procdefKey != null)
				return false;
		} else if (!procdefKey.equals(other.procdefKey))
			return false;
		if (procdefVer == null) {
			if (other.procdefVer != null)
				return false;
		} else if (!procdefVer.equals(other.procdefVer))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityLabel [procdefKey=" + procdefKey + ", procdefVer="
				+ procdefVer + ", actdefName=" + actdefName + ", locale="
				+ locale + ", label=" + label + "]";
	}
    
}

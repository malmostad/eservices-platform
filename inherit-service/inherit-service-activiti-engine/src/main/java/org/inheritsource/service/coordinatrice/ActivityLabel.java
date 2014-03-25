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

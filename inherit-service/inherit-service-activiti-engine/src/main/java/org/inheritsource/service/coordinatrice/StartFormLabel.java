package org.inheritsource.service.coordinatrice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="startformlabel")
@XmlAccessorType(XmlAccessType.FIELD)
public class StartFormLabel implements Serializable {
	

	private static final long serialVersionUID = -7749250935363869651L;

	String appName;
	String formName;
	String formdefId;
	int formdefVer;
	String locale;
	String label;
	
	public StartFormLabel() {
		
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormdefId() {
		return formdefId;
	}

	public void setFormdefId(String formdefId) {
		this.formdefId = formdefId;
	}

	public int getFormdefVer() {
		return formdefVer;
	}

	public void setFormdefVer(int formdefVer) {
		this.formdefVer = formdefVer;
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
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result
				+ ((formName == null) ? 0 : formName.hashCode());
		result = prime * result
				+ ((formdefId == null) ? 0 : formdefId.hashCode());
		result = prime * result + formdefVer;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
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
		StartFormLabel other = (StartFormLabel) obj;
		if (appName == null) {
			if (other.appName != null)
				return false;
		} else if (!appName.equals(other.appName))
			return false;
		if (formName == null) {
			if (other.formName != null)
				return false;
		} else if (!formName.equals(other.formName))
			return false;
		if (formdefId == null) {
			if (other.formdefId != null)
				return false;
		} else if (!formdefId.equals(other.formdefId))
			return false;
		if (formdefVer != other.formdefVer)
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
		return true;
	}

	@Override
	public String toString() {
		return "StartFormLabel [appName=" + appName + ", formName=" + formName
				+ ", formdefId=" + formdefId + ", formdefVer=" + formdefVer
				+ ", locale=" + locale + ", label=" + label + "]";
	}
	
	
}

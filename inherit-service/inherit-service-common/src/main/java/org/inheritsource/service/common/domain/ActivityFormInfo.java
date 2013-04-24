package org.inheritsource.service.common.domain;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ActivityFormInfo")
public class ActivityFormInfo implements Serializable {
	private static final long serialVersionUID = 218585670301498715L;

	String formUrl;
	
	public ActivityFormInfo() {
		
	}

	public String getFormUrl() {
		return formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formUrl == null) ? 0 : formUrl.hashCode());
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
		ActivityFormInfo other = (ActivityFormInfo) obj;
		if (formUrl == null) {
			if (other.formUrl != null)
				return false;
		} else if (!formUrl.equals(other.formUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FormPathInfo [formUrl=" + formUrl + "]";
	}
	
	
}

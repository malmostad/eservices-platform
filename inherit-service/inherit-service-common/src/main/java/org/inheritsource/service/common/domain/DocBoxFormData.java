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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="docboxformdata")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocBoxFormData implements Serializable {
	
	private static final long serialVersionUID = -7487553605148993796L;

	String formDataUuid;

	String docboxRef;
	String docNo;
	String checkSum;
	int signCount;
	
	String docUri;
	String signText;
	
	public DocBoxFormData() {
		
	}

	public String getFormDataUuid() {
		return formDataUuid;
	}

	public void setFormDataUuid(String formDataUuid) {
		this.formDataUuid = formDataUuid;
	}

	public String getDocboxRef() {
		return docboxRef;
	}

	public void setDocboxRef(String docboxRef) {
		this.docboxRef = docboxRef;
	}

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}

	public int getSignCount() {
		return signCount;
	}

	public void setSignCount(int signCount) {
		this.signCount = signCount;
	}
	
	public String getDocUri() {
		return docUri;
	}

	public void setDocUri(String docUri) {
		this.docUri = docUri;
	}

	public String getSignText() {
		return signText;
	}

	public void setSignText(String signText) {
		this.signText = signText;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((checkSum == null) ? 0 : checkSum.hashCode());
		result = prime * result + ((docNo == null) ? 0 : docNo.hashCode());
		result = prime * result + ((docUri == null) ? 0 : docUri.hashCode());
		result = prime * result
				+ ((docboxRef == null) ? 0 : docboxRef.hashCode());
		result = prime * result
				+ ((formDataUuid == null) ? 0 : formDataUuid.hashCode());
		result = prime * result + signCount;
		result = prime * result
				+ ((signText == null) ? 0 : signText.hashCode());
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
		DocBoxFormData other = (DocBoxFormData) obj;
		if (checkSum == null) {
			if (other.checkSum != null)
				return false;
		} else if (!checkSum.equals(other.checkSum))
			return false;
		if (docNo == null) {
			if (other.docNo != null)
				return false;
		} else if (!docNo.equals(other.docNo))
			return false;
		if (docUri == null) {
			if (other.docUri != null)
				return false;
		} else if (!docUri.equals(other.docUri))
			return false;
		if (docboxRef == null) {
			if (other.docboxRef != null)
				return false;
		} else if (!docboxRef.equals(other.docboxRef))
			return false;
		if (formDataUuid == null) {
			if (other.formDataUuid != null)
				return false;
		} else if (!formDataUuid.equals(other.formDataUuid))
			return false;
		if (signCount != other.signCount)
			return false;
		if (signText == null) {
			if (other.signText != null)
				return false;
		} else if (!signText.equals(other.signText))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DocBoxFormData [formDataUuid=" + formDataUuid + ", docboxRef="
				+ docboxRef + ", docNo=" + docNo + ", checkSum=" + checkSum
				+ ", signCount=" + signCount + ", docUri=" + docUri
				+ ", signText=" + signText + "]";
	}
	
}

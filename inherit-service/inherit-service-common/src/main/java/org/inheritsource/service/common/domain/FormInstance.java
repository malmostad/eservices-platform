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
import java.util.Date;
import java.util.HashMap;

public class FormInstance implements Serializable {
	
	private static final long serialVersionUID = 8906051980892687842L;

	String page;
	
	String viewUrl;
	
	String viewUrlExternal;
	
	String editUrl;
	
	String editUrlExternal;
	
	String dataUri;
	
	String definitionKey;
	
	Long typeId;
	
	String instanceId;
	
	String actUri;
	
	String actinstId;
	
	Date submitted = null;
	
	UserInfo submittedBy = null;
	
	HashMap<String, Object> attributes = new HashMap<String, Object>();

	public FormInstance() {
		super();
	}

	/**
	 * 
	 * @return Page name within motrice portal that know how to render this form in edit mode. Ex) form, signform, noform
	 */
	public String getPage() {
		return page;
	}
	
	/*
	 * This url represent a 
	 */
	public String getRelativePageLink() {
		StringBuffer result = new StringBuffer();
		result.append(page);
		result.append("?");
		if (actinstId == null || actinstId.trim().length()==0) {
			// it is implicit a start form if no actinstId
			if (instanceId != null && instanceId.trim().length()>0) {
				result.append("startforminstId=");
				result.append(instanceId);
				result.append("&");
			}
			result.append("startformkey=");
			result.append(definitionKey);
			result.append("&");
		}
		else {
			// it is an activity in process engine
			result.append("actinstId=");
			result.append(actinstId);
			result.append("&");

			if (instanceId != null && instanceId.trim().length()>0) {
				result.append("instanceId=");
				result.append(instanceId);
				result.append("&");
			}
		}
		return result.toString();
	}

	/**
	 * 
	 * @param page Page name within motrice portal that know how to render this form in edit mode. Ex) form, signform, noform
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * 
	 * @return form engine view url, this url will render a html view on a submitted form
	 */
	public String getViewUrl() {
		return viewUrl;
	}

	/**
	 * 
	 * @param viewUrl form engine view url, this url will render a html view on a submitted form
	 */
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
	
	/**
	 * 
	 * @return true if this form has to be rendered in an IFRAME or via a navigate
	 */
	public String getViewUrlExternal() {
		return viewUrlExternal;
	}

	/**
	 * 
	 * @param viewExternal set to true if this form has to be rendered in an IFRAME or via a navigate
	 */
	public void setViewUrlExternal(String viewUrlExternal) {
		this.viewUrlExternal = viewUrlExternal;
	}

	/**
	 * 
	 * @return form engine edit url, this url will render a html form open for edit. null if form is submitted.
	 */
	public String getEditUrl() {
		return editUrl;
	}

	/**
	 * 
	 * @param editUrl form engine edit url, this url will render a html form open for edit. null if form is submitted.
	 */
	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}

	/**
	 * 
	 * @return true if this edit form has to be rendered in an IFRAME or via a navigate
	 */
	public String getEditUrlExternal() {
		return editUrlExternal;
	}

	/**
	 * 
	 * @param editExternal set to true if this edit form has to be rendered in an IFRAME or via a navigate
	 */
	public void setEditUrlExternal(String editUrlExternal) {
		this.editUrlExternal = editUrlExternal;
	}

	/**
	 * 	
	 * @return URI representing the data behind a submitted form. The format of this data should be known by the process designer with the knowledge of the combined key typeId and definitionKey.
	 */
	public String getDataUri() {
		return dataUri;
	}

	/**
	 * 
	 * @param dataUri URI representing the data behind a submitted form. The format of this data should be known by the process designer with the knowledge of the combined key typeId and definitionKey.
	 */
	public void setDataUri(String dataUri) {
		this.dataUri = dataUri;
	}

	/**
	 * 
	 * @return form definition key. The form handler knows how to use this key
	 */
	public String getDefinitionKey() {
		return definitionKey;
	}

	/**
	 * 
	 * @param definitionKey form definition key. The form handler knows how to use this key
	 */
	public void setDefinitionKey(String definitionKey) {
		this.definitionKey = definitionKey;
	}

	/**
	 * 
	 * @return identify the form handler
	 */
	public Long getTypeId() {
		return typeId;
	}

	/**
	 * 
	 * @param typeId identify the form handler
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * 
	 * @return URI to a document prepared for digital preservation. In Motrice this is normally a DocBox reference.
	 */
	public String getActUri() {
		return actUri;
	}

	/**
	 * 
	 * @param actUri URI to a document prepared for digital preservation. In Motrice this is normally a DocBox reference.
	 */
	public void setActUri(String actUri) {
		this.actUri = actUri;
	}
	
	/**
	 * 
	 * @return The process engine activity instance id
	 */
	public String getActinstId() {
		return actinstId;
	}

	/**
	 * 
	 * @param actinstId  The process engine activity instance id
	 */
	public void setActinstId(String actinstId) {
		this.actinstId = actinstId;
	}

	public boolean isSubmitted() {
		return (submitted != null);
	}
	
	public Date getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}

	/**
	 * 
	 * @return the user that submitted the task (form)
	 */
	public UserInfo getSubmittedBy() {
		return submittedBy;
	}

	/**
	 * 
	 * @param submittedBy the user that submitted the task (form)
	 */
	public void setSubmittedBy(UserInfo submittedBy) {
		this.submittedBy = submittedBy;
	}

	public HashMap<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actUri == null) ? 0 : actUri.hashCode());
		result = prime * result
				+ ((actinstId == null) ? 0 : actinstId.hashCode());
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((dataUri == null) ? 0 : dataUri.hashCode());
		result = prime * result
				+ ((definitionKey == null) ? 0 : definitionKey.hashCode());
		result = prime * result + ((editUrl == null) ? 0 : editUrl.hashCode());
		result = prime * result
				+ ((editUrlExternal == null) ? 0 : editUrlExternal.hashCode());
		result = prime * result
				+ ((instanceId == null) ? 0 : instanceId.hashCode());
		result = prime * result + ((page == null) ? 0 : page.hashCode());
		result = prime * result
				+ ((submitted == null) ? 0 : submitted.hashCode());
		result = prime * result
				+ ((submittedBy == null) ? 0 : submittedBy.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result + ((viewUrl == null) ? 0 : viewUrl.hashCode());
		result = prime * result
				+ ((viewUrlExternal == null) ? 0 : viewUrlExternal.hashCode());
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
		FormInstance other = (FormInstance) obj;
		if (actUri == null) {
			if (other.actUri != null)
				return false;
		} else if (!actUri.equals(other.actUri))
			return false;
		if (actinstId == null) {
			if (other.actinstId != null)
				return false;
		} else if (!actinstId.equals(other.actinstId))
			return false;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (dataUri == null) {
			if (other.dataUri != null)
				return false;
		} else if (!dataUri.equals(other.dataUri))
			return false;
		if (definitionKey == null) {
			if (other.definitionKey != null)
				return false;
		} else if (!definitionKey.equals(other.definitionKey))
			return false;
		if (editUrl == null) {
			if (other.editUrl != null)
				return false;
		} else if (!editUrl.equals(other.editUrl))
			return false;
		if (editUrlExternal == null) {
			if (other.editUrlExternal != null)
				return false;
		} else if (!editUrlExternal.equals(other.editUrlExternal))
			return false;
		if (instanceId == null) {
			if (other.instanceId != null)
				return false;
		} else if (!instanceId.equals(other.instanceId))
			return false;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		if (submitted == null) {
			if (other.submitted != null)
				return false;
		} else if (!submitted.equals(other.submitted))
			return false;
		if (submittedBy == null) {
			if (other.submittedBy != null)
				return false;
		} else if (!submittedBy.equals(other.submittedBy))
			return false;
		if (typeId == null) {
			if (other.typeId != null)
				return false;
		} else if (!typeId.equals(other.typeId))
			return false;
		if (viewUrl == null) {
			if (other.viewUrl != null)
				return false;
		} else if (!viewUrl.equals(other.viewUrl))
			return false;
		if (viewUrlExternal == null) {
			if (other.viewUrlExternal != null)
				return false;
		} else if (!viewUrlExternal.equals(other.viewUrlExternal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FormInstance [page=" + page + ", viewUrl=" + viewUrl
				+ ", viewUrlExternal=" + viewUrlExternal + ", editUrl="
				+ editUrl + ", editUrlExternal=" + editUrlExternal
				+ ", dataUri=" + dataUri + ", definitionKey=" + definitionKey
				+ ", typeId=" + typeId + ", instanceId=" + instanceId
				+ ", actUri=" + actUri + ", actinstId=" + actinstId
				+ ", submitted=" + submitted + ", submittedBy=" + submittedBy
				+ ", attributes=" + attributes + "]";
	}

}

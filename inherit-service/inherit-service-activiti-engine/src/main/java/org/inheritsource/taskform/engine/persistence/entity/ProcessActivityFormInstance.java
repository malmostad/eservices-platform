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
 
 
package org.inheritsource.taskform.engine.persistence.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="mtf_process_activity_form_instance")
public class ProcessActivityFormInstance {
	
	@Id
	@GeneratedValue
	Long processActivityFormInstanceId;
	
	/**
	 * If null, this is a not submitted start form
	 */
	String processInstanceUuid;
	
	/**
	 * If not null, this is a start form instance
	 */
	@ManyToOne
    @JoinColumn(name="start_form_definition_id", nullable=true)
	StartFormDefinition startFormDefinition;
	
	/**
	 * if null, this is a start form
	 */
	String activityInstanceUuid;
	
	/**
	 * Data id that holds filled in form data
	 */
	@Column(unique=true, nullable=false)
	String formDocId;
	
	/**
	 * form type identifies a Motrice form handler i.e. Orbeon, sign, noform etc
	 */
	@Column(name="form_type_id")
	Long formTypeId;

	/**
	 * identifies a specific form in a Motrice form handler engine. The form handler engine 
	 * is responsibly to know how to interpret the formDefinitionKey
	 */
	@Column(name="form_connection_key")
	String formConnectionKey;
	
	/**
	 * 
	 */
	@Column(name="form_data_uri")
	String formDataUri;
	
	/**
	 * If null, this form is still not submitted, otherwise submission time stamp.
	 */
	Date submitted = null;
	
	/**
	 * The last writer to this instance
	 */
	@Column(nullable=false)
	String userId;
	// TODO rename userId to userUuid
	
	@OneToMany
	@JoinColumn(name="processActivityFormInstanceId")
	Set<ProcessActivityTag> processActivityTags; 
	
	public ProcessActivityFormInstance() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getProcessActivityFormInstanceId() {
		return processActivityFormInstanceId;
	}

	public void setProcessActivityFormInstanceId(Long processActivityFormInstanceId) {
		this.processActivityFormInstanceId = processActivityFormInstanceId;
	}

	public String getProcessInstanceUuid() {
		return processInstanceUuid;
	}

	public void setProcessInstanceUuid(String processInstanceUuid) {
		this.processInstanceUuid = processInstanceUuid;
	}

	public String getActivityInstanceUuid() {
		return activityInstanceUuid;
	}

	public void setActivityInstanceUuid(String activityInstanceUuid) {
		this.activityInstanceUuid = activityInstanceUuid;
	}

	public String getFormDocId() {
		return formDocId;
	}

	public void setFormDocId(String formDocId) {
		this.formDocId = formDocId;
	}

	public String getFormDataUri() {
		return formDataUri;
	}

	public void setFormDataUri(String formDataUri) {
		this.formDataUri = formDataUri;
	}

	public Long getFormTypeId() {
		return formTypeId;
	}

	public void setFormTypeId(Long formTypeId) {
		this.formTypeId = formTypeId;
	}

	public String getFormConnectionKey() {
		return formConnectionKey;
	}

	public void setFormConnectionKey(String formConnectionKey) {
		this.formConnectionKey = formConnectionKey;
	}

	public Date getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public StartFormDefinition getStartFormDefinition() {
		return startFormDefinition;
	}

	public void setStartFormDefinition(StartFormDefinition startFormDefinition) {
		this.startFormDefinition = startFormDefinition;
	}

	public String calcEditUrl() {
		return getFormConnectionKey() + "/edit/" + getFormDocId() + "?orbeon-embeddable=true&pawap-mode=load-deps";
	}

	public String calcViewUrl() {
		// TODO Since orbeon 4.3 Iframe work-around exclude: orbeon-embeddable=true";
		//      note: remeber to include orbeon-embeddable=true when rendering
		//            in div tag
		//            It is imortant to exclude orbeon-embeddable=true when rendering in 
		//            IFRAME...
		String viewUrl = getFormConnectionKey() + "/view/" + getFormDocId() + "?";
		
		if (getFormConnectionKey() != null && (getFormConnectionKey().startsWith("signactivity/") || getFormConnectionKey().equals("signstartform"))) {
			viewUrl = "/docbox/doc/ref/" + formDocId;
	    }
		return  viewUrl;
	}

	public String calcPdfUrl() {
		return getFormConnectionKey() + "/pdf/" + getFormDocId();
	}
	
	public boolean isStartForm() {
		return (activityInstanceUuid == null);
	}
	
	public Set<ProcessActivityTag> getProcessActivityTags() {
		return processActivityTags;
	}

	public void setProcessActivityTags(Set<ProcessActivityTag> processActivityTags) {
		this.processActivityTags = processActivityTags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activityInstanceUuid == null) ? 0 : activityInstanceUuid
						.hashCode());
		result = prime
				* result
				+ ((formConnectionKey == null) ? 0 : formConnectionKey
						.hashCode());
		result = prime * result
				+ ((formDataUri == null) ? 0 : formDataUri.hashCode());
		result = prime * result
				+ ((formDocId == null) ? 0 : formDocId.hashCode());
		result = prime * result
				+ ((formTypeId == null) ? 0 : formTypeId.hashCode());
		result = prime
				* result
				+ ((processActivityFormInstanceId == null) ? 0
						: processActivityFormInstanceId.hashCode());
		result = prime
				* result
				+ ((processActivityTags == null) ? 0 : processActivityTags
						.hashCode());
		result = prime
				* result
				+ ((processInstanceUuid == null) ? 0 : processInstanceUuid
						.hashCode());
		result = prime
				* result
				+ ((startFormDefinition == null) ? 0 : startFormDefinition
						.hashCode());
		result = prime * result
				+ ((submitted == null) ? 0 : submitted.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		ProcessActivityFormInstance other = (ProcessActivityFormInstance) obj;
		if (activityInstanceUuid == null) {
			if (other.activityInstanceUuid != null)
				return false;
		} else if (!activityInstanceUuid.equals(other.activityInstanceUuid))
			return false;
		if (formConnectionKey == null) {
			if (other.formConnectionKey != null)
				return false;
		} else if (!formConnectionKey.equals(other.formConnectionKey))
			return false;
		if (formDataUri == null) {
			if (other.formDataUri != null)
				return false;
		} else if (!formDataUri.equals(other.formDataUri))
			return false;
		if (formDocId == null) {
			if (other.formDocId != null)
				return false;
		} else if (!formDocId.equals(other.formDocId))
			return false;
		if (formTypeId == null) {
			if (other.formTypeId != null)
				return false;
		} else if (!formTypeId.equals(other.formTypeId))
			return false;
		if (processActivityFormInstanceId == null) {
			if (other.processActivityFormInstanceId != null)
				return false;
		} else if (!processActivityFormInstanceId
				.equals(other.processActivityFormInstanceId))
			return false;
		if (processActivityTags == null) {
			if (other.processActivityTags != null)
				return false;
		} else if (!processActivityTags.equals(other.processActivityTags))
			return false;
		if (processInstanceUuid == null) {
			if (other.processInstanceUuid != null)
				return false;
		} else if (!processInstanceUuid.equals(other.processInstanceUuid))
			return false;
		if (startFormDefinition == null) {
			if (other.startFormDefinition != null)
				return false;
		} else if (!startFormDefinition.equals(other.startFormDefinition))
			return false;
		if (submitted == null) {
			if (other.submitted != null)
				return false;
		} else if (!submitted.equals(other.submitted))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcessActivityFormInstance [processActivityFormInstanceId="
				+ processActivityFormInstanceId + ", processInstanceUuid="
				+ processInstanceUuid + ", startFormDefinition="
				+ startFormDefinition + ", activityInstanceUuid="
				+ activityInstanceUuid + ", formDocId=" + formDocId
				+ ", formTypeId=" + formTypeId + ", formConnectionKey="
				+ formConnectionKey + ", formDataUri=" + formDataUri
				+ ", submitted=" + submitted + ", userId=" + userId
				+ ", processActivityTags=" + processActivityTags + "]";
	}
	
}

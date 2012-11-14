package org.inherit.taskform.engine.persistence.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
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
    @JoinColumn(name="startFormDefinitionId", nullable=true)
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
	 * Form path to form definition
	 */
	@Column(nullable=false)
	String formPath;
	
	/**
	 * If null, this form is still not submitted, otherwise submission time stamp.
	 */
	Date submitted = null;
	
	/**
	 * The last writer to this instance
	 */
	@Column(nullable=false)
	String userId;

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

	public String getFormPath() {
		return formPath;
	}

	public void setFormPath(String formPath) {
		this.formPath = formPath;
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
		return getFormPath() + "/edit/" + getFormDocId() + "?orbeon-embeddable=true";
	}

	public String calcViewUrl() {
		return getFormPath() + "/view/" + getFormDocId() + "?orbeon-embeddable=true";
	}

	public String calcPdfUrl() {
		return getFormPath() + "/pdf/" + getFormDocId() + "?orbeon-embeddable=true";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activityInstanceUuid == null) ? 0 : activityInstanceUuid
						.hashCode());
		result = prime * result
				+ ((formDocId == null) ? 0 : formDocId.hashCode());
		result = prime * result
				+ ((formPath == null) ? 0 : formPath.hashCode());
		result = prime
				* result
				+ ((processActivityFormInstanceId == null) ? 0
						: processActivityFormInstanceId.hashCode());
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
		if (formDocId == null) {
			if (other.formDocId != null)
				return false;
		} else if (!formDocId.equals(other.formDocId))
			return false;
		if (formPath == null) {
			if (other.formPath != null)
				return false;
		} else if (!formPath.equals(other.formPath))
			return false;
		if (processActivityFormInstanceId == null) {
			if (other.processActivityFormInstanceId != null)
				return false;
		} else if (!processActivityFormInstanceId
				.equals(other.processActivityFormInstanceId))
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
				+ ", formPath=" + formPath + ", submitted=" + submitted
				+ ", userId=" + userId + "]";
	}

	
}

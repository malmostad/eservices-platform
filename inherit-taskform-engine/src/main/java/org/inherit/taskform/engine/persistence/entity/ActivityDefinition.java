package org.inherit.taskform.engine.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ActivityDefinition {

	@Id
	@GeneratedValue
	Long activityDefinitionId;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="processDefinitionId")
	ProcessDefinition processDefinition;
	
	String uuid;
	
	String label;
	
	String formPath;

	
	public ActivityDefinition() {
		
	}

	public Long getActivityDefinitionId() {
		return activityDefinitionId;
	}

	public void setActivityDefinitionId(Long activityDefinitionId) {
		this.activityDefinitionId = activityDefinitionId;
	}
	
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFormPath() {
		return formPath;
	}

	public void setFormPath(String formPath) {
		this.formPath = formPath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activityDefinitionId == null) ? 0 : activityDefinitionId
						.hashCode());
		result = prime * result
				+ ((formPath == null) ? 0 : formPath.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime
				* result
				+ ((processDefinition == null || processDefinition.getProcessDefinitionId() == null) ? 0 : processDefinition.getProcessDefinitionId().hashCode());
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
		ActivityDefinition other = (ActivityDefinition) obj;
		if (activityDefinitionId == null) {
			if (other.activityDefinitionId != null)
				return false;
		} else if (!activityDefinitionId.equals(other.activityDefinitionId))
			return false;
		if (formPath == null) {
			if (other.formPath != null)
				return false;
		} else if (!formPath.equals(other.formPath))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (processDefinition == null) {
			if (other.processDefinition != null)
				return false;
		} else {
			if (other.processDefinition == null)
				return false;
			if (processDefinition.getProcessDefinitionId() == null) {
				if (other.processDefinition.getProcessDefinitionId() != null)
					return false;
			} else if (!processDefinition.getProcessDefinitionId().equals(other.processDefinition.getProcessDefinitionId()))
				return false;
			
		}
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityDefinition [activityDefinitionId="
				+ activityDefinitionId + ", processDefinitionId="
				+ (processDefinition==null ? "null" : processDefinition.getProcessDefinitionId() )+ ", uuid=" + uuid + ", label=" + label
				+ ", formPath=" + formPath + "]";
	}



	
}

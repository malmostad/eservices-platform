package org.inherit.taskform.engine.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ProcessDefinition {
	
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_DISABLED = 2;
	public static final int STATUS_REMOVED = 3;
	
	@Id
	@GeneratedValue
	Long processDefinitionId;
	
	String uuid;
	
	String label;
	
	int status;
	
	String formPath;

		
	
	@OneToMany(mappedBy="processDefinition")
	Set<ActivityDefinition> activityDefinitions = new HashSet<ActivityDefinition>();
	
	public ProcessDefinition() {
		
	}
	
	public Long getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(Long processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Set<ActivityDefinition> getActivityDefinitions() {
		return activityDefinitions;
	}

	public void setActivityDefinitions(Set<ActivityDefinition> activityDefinitions) {
		this.activityDefinitions = activityDefinitions;
	}
	
	public void addActivityDefinition(ActivityDefinition activityDefinition) {
		activityDefinitions.add(activityDefinition);
		activityDefinition.setProcessDefinition(this);
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
				+ ((activityDefinitions == null) ? 0 : activityDefinitions
						.hashCode());
		result = prime * result
				+ ((formPath == null) ? 0 : formPath.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime
				* result
				+ ((processDefinitionId == null) ? 0 : processDefinitionId
						.hashCode());
		result = prime * result + status;
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
		ProcessDefinition other = (ProcessDefinition) obj;
		if (activityDefinitions == null) {
			if (other.activityDefinitions != null)
				return false;
		} else if (!activityDefinitions.equals(other.activityDefinitions))
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
		if (processDefinitionId == null) {
			if (other.processDefinitionId != null)
				return false;
		} else if (!processDefinitionId.equals(other.processDefinitionId))
			return false;
		if (status != other.status)
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
		return "ProcessDefinition [processDefinitionId=" + processDefinitionId
				+ ", uuid=" + uuid + ", label=" + label + ", status=" + status
				+ ", formPath=" + formPath + ", activityDefinitions="
				+ activityDefinitions + "]";
	}


	
	
}

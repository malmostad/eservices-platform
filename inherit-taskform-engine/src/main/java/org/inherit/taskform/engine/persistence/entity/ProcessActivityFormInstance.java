package org.inherit.taskform.engine.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ProcessActivityFormInstance {
	
	@Id
	@GeneratedValue
	Long processActivityFormInstanceId;
	
	String processInstanceUuid;
	
	String activityInstanceUuid;
	
	String formDocId;
	
}

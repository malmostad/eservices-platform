package org.inherit.taskform.engine.persistence.entity;

import java.util.Date;

import javax.persistence.CascadeType;
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
	
	String processInstanceUuid;
	
	String activityInstanceUuid;
	
	String formDocId;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="activityDefinitionId")
	ActivityDefinition activityDefinition;
	
	Date submitted = null;
	
	
}

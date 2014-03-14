package org.inheritsource.service.form;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.inheritsource.service.common.domain.FormInstance;
import org.inheritsource.taskform.engine.persistence.entity.ActivityFormDefinition;

public abstract class TaskFormHandler {

	public abstract FormInstance getPendingFormInstance(FormInstance form, Task task, String userId);

	public abstract FormInstance initializeFormInstance(ActivityFormDefinition activityFormDefinition, Task task, String userId, FormInstance initialInstance);
	
	public abstract FormInstance initializeStartFormInstance(Long formTypeId, String formConnectionKey, String userId, FormInstance initialInstance);
	
	public abstract FormInstance getHistoricFormInstance(FormInstance form, HistoricTaskInstance historicTask, String userId);
	
	public abstract FormInstance getHistoricStartFormInstance(FormInstance startForm, HistoricProcessInstance historicProcessInstance, String userId);

	public abstract FormInstance validateSubmit(FormInstance form, Task task, String userId);
	
	public abstract FormInstance afterSubmit(FormInstance form, Task task, String userId);

}

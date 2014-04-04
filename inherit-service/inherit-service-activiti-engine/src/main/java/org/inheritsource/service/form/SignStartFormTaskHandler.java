package org.inheritsource.service.form;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.inheritsource.service.common.domain.FormInstance;
import org.inheritsource.taskform.engine.persistence.entity.ActivityFormDefinition;

public class SignStartFormTaskHandler extends TaskFormHandler {

	public static final String PAGE = "signform";
	
	@Override
	public FormInstance getPendingFormInstance(FormInstance form, Task task,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormInstance initializeFormInstance(
			ActivityFormDefinition activityFormDefinition, Task task,
			String userId, FormInstance initialInstance) {
		FormInstance form = initialInstance;
		/*
		// generate a type 4 UUID
		String docId = java.util.UUID.randomUUID().toString();

		form.setPage(PAGE);

		String docBoxRef = 
		
		// initialize local task variables, will be stored by FormEngine
		form.setInstanceId(docId);
		form.setTypeId(activityFormDefinition.getFormTypeId());
		form.setDefinitionKey(activityFormDefinition.getFormConnectionKey());
		form.setActUri(null);
		form.setDataUri(orbeonDataBaseUri + form.getDefinitionKey() + "/data/" + form.getInstanceId() + ".xml");
		
		// submitted is default to not submitted
		
		calcUris(form);
				*/
		return form;
	}

	@Override
	public FormInstance initializeStartFormInstance(Long formTypeId,
			String formConnectionKey, String userId,
			FormInstance initialInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormInstance getHistoricFormInstance(FormInstance form,
			HistoricTaskInstance historicTask, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormInstance getHistoricStartFormInstance(FormInstance startForm,
			HistoricProcessInstance historicProcessInstance, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormInstance validateSubmit(FormInstance form, Task task,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormInstance afterSubmit(FormInstance form, Task task, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}

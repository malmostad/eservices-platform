package org.inheritsource.service.form;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.inheritsource.service.common.domain.FormInstance;
import org.inheritsource.service.common.util.ConfigUtil;
import org.inheritsource.taskform.engine.persistence.entity.ActivityFormDefinition;

public class NoTaskFormHandler extends TaskFormHandler {
	public static final String PAGE = "noform";
		
	NoTaskFormHandler() {
		
	}
	
	@Override
	public FormInstance initializeFormInstance(ActivityFormDefinition activityFormDefinition, Task task, String userId, FormInstance initialInstance) {
		FormInstance form = initialInstance;
		
		// generate a type 4 UUID
		String docId = java.util.UUID.randomUUID().toString();

		form.setPage(PAGE);

		// initialize local task variables, will be stored by FormEngine
		form.setInstanceId(docId);
		if (activityFormDefinition == null) {
			// no form
			form.setTypeId(new Long(6)); // no form type id 
			form.setDefinitionKey(null);
		}
		else {
			form.setTypeId(activityFormDefinition.getFormTypeId());
			form.setDefinitionKey(activityFormDefinition.getFormConnectionKey());
		}
		form.setActUri(null);
		form.setDataUri(null);
		
		// submitted is default to not submitted
		
		calcUris(form);
				
		return form;
	}
	
	@Override
	public FormInstance initializeStartFormInstance(Long formTypeId, String formConnectionKey, String userId, FormInstance initialInstance) {
		FormInstance form = initialInstance;
		
		// generate a type 4 UUID
		String docId = java.util.UUID.randomUUID().toString();

		form.setPage(PAGE);

		// initialize local task variables, will be stored by FormEngine
		form.setInstanceId(docId);
		form.setTypeId(formTypeId);
		form.setDefinitionKey(formConnectionKey);
		form.setActUri(null);
		form.setDataUri(null);
		
		// submitted is default to not submitted
		
		calcUris(form);
				
		return form;
	}

	
	@Override
	public FormInstance getPendingFormInstance(FormInstance form, Task task, String userId) {
		form.setPage(PAGE);

		// common local task variables should be loaded already by FormEngine 
		
		calcUris(form);
		
		return form;
	}
	
	@Override
	public FormInstance getHistoricFormInstance(FormInstance form, HistoricTaskInstance historicTask, String userId) {
		form.setPage(PAGE);

		// common local task variables should be loaded already by FormEngine 
		
		calcUris(form);
		
		return form;
	}
	
	private void calcUris(FormInstance form) {
		form.setEditUrl(null);
		form.setEditUrlExternal(null);
		form.setViewUrl(null);
		form.setViewUrlExternal(null);
	}

	@Override
	public FormInstance afterSubmit(FormInstance form, Task task, String userId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public FormInstance getHistoricStartFormInstance(FormInstance startForm, HistoricProcessInstance historicProcessInstance, String userId) {
		startForm.setPage(PAGE);

		// common local task variables should be loaded already by FormEngine

		startForm.setSubmitted(historicProcessInstance.getStartTime());
		
		calcUris(startForm);

		return startForm;
	}

	@Override
	public FormInstance validateSubmit(FormInstance form, Task task,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}

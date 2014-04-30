package org.inheritsource.service.delegates;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.inheritsource.service.common.domain.DocBoxFormData;
import org.inheritsource.service.docbox.DocBoxFacade;
import org.inheritsource.service.form.FormEngine;

public class CreateDocBoxRefExecutionListener implements ExecutionListener {

	private static final long serialVersionUID = -1522506737389383556L;
	
	protected DocBoxFacade docBoxFacade = new DocBoxFacade();

	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("CreateDocBoxActExecutionListener called from " + execution.getCurrentActivityName() + " in process " + execution.getProcessInstanceId() + " at " + new Date()); 
		
		
		TaskService taskService = execution.getEngineServices().getTaskService();
		Task task = taskService.createTaskQuery().executionId(execution.getId()).includeTaskLocalVariables().singleResult();
		
		if (task != null) {
			if (ExecutionListener.EVENTNAME_END.equals(execution.getEventName())) {
				
				Map<String, Object> taskVars = execution.getEngineServices().getTaskService().getVariablesLocal(task.getId());
				
				String formInstanceId = (String)taskVars.get(FormEngine.FORM_INSTANCEID);
				Long formTypeId = (Long)taskVars.get(FormEngine.FORM_TYPEID);
				
				if (formTypeId != null && formTypeId.longValue() == 1) {
					// DocBox has only orbeon support
					if (formInstanceId != null && formInstanceId.trim().length()>0) {
						// there is an instance
						DocBoxFormData docBoxFormData = docBoxFacade.getDocBoxFormData(formInstanceId);
						
						System.out.println("XXXXXXXXXXX task docBoxFormData" + docBoxFormData);

						if (docBoxFormData != null) {
							String docBoxRef = docBoxFormData.getDocboxRef();
							if (docBoxRef != null && docBoxRef.trim().length()>0) {
								// there is a docBoxRef 
								String taskDocBoxRefVarName = DelegateUtil.calcTaskVariableName(FormEngine.FORM_DOCBOXREF, task.getId());
								execution.getEngineServices().getRuntimeService().setVariable(execution.getId(), taskDocBoxRefVarName, docBoxRef);
							}
						}
					}
				}
			}
		}
		else {
			System.out.println("XXXXXXXXXXX start! CreateDocBoxRefExecutionListener");
			// no task
			if (ExecutionListener.EVENTNAME_START.equals(execution.getEventName())) {
				
				Map<String, Object> taskVars = execution.getEngineServices().getRuntimeService().getVariables(execution.getId());
				
				String formInstanceId = (String)taskVars.get(FormEngine.START_FORM_INSTANCEID);
				Long formTypeId = (Long)taskVars.get(FormEngine.START_FORM_TYPEID);
				
				if (formTypeId != null && formTypeId.longValue() == 1) {
					// DocBox has only orbeon support
					if (formInstanceId != null && formInstanceId.trim().length()>0) {
						// there is an instance
						DocBoxFormData docBoxFormData = docBoxFacade.getDocBoxFormData(formInstanceId);
						if (docBoxFormData != null) {
							String docBoxRef = docBoxFormData.getDocboxRef();
							if (docBoxRef != null && docBoxRef.trim().length()>0) {
								// there is a docBoxRef 
								String startDocBoxRefVarName = FormEngine.START_FORM_DOCBOXREF;
								execution.getEngineServices().getRuntimeService().setVariable(execution.getId(), startDocBoxRefVarName, docBoxRef);
							}
						}
					}
				}
			}
		}
	}
}

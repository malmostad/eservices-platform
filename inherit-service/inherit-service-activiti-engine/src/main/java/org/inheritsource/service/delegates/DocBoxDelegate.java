package org.inheritsource.service.delegates;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.inheritsource.service.docbox.DocBoxFacade;
import org.inheritsource.service.docbox.DocBoxFormData;
import org.inheritsource.service.form.FormEngine;

public class DocBoxDelegate implements JavaDelegate {

	public static final Logger log = Logger.getLogger(DocBoxDelegate.class.getName());
	
	private DocBoxFacade docBoxFacade = new DocBoxFacade(); 
	
	public DocBoxDelegate() {
		
	}
	
	public DocBoxFacade getDocBoxFacade() {
		return docBoxFacade;
	}

	public void setDocBoxFacade(DocBoxFacade docBoxFacade) {
		this.docBoxFacade = docBoxFacade;
	}

	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("DocBoxDelegate called from " + execution.getCurrentActivityName() + " in process " + execution.getProcessInstanceId() + " at " + new Date()); 
		
		TaskService taskService = execution.getEngineServices().getTaskService();
		Task task = taskService.createTaskQuery().executionId(execution.getId()).includeTaskLocalVariables().singleResult();
		
		
		if (task != null) {
			
			Map<String, Object> taskVars = task.getTaskLocalVariables();
			
			String formInstanceId = (String)taskVars.get(FormEngine.FORM_INSTANCEID);
			Long formTypeId = (Long)taskVars.get(FormEngine.FORM_TYPEID);
			
			if (formTypeId != null && formTypeId.longValue() == 1) {
				// DocBox has only orbeon support
				if (formInstanceId != null && formInstanceId.trim().length()>0) {
					// there is an instance
					DocBoxFormData docBoxFormData = docBoxFacade.getDocBoxFormData(formInstanceId);
					if (docBoxFormData != null) {
						String docBoxRef = docBoxFormData.getDocboxRef();
						if (docBoxRef != null && docBoxRef.trim().length()>0) {
							// there is a docBoxRef 
							execution.getEngineServices().getTaskService().setVariableLocal(task.getId(), FormEngine.FORM_DOCBOXREF, docBoxRef);
						}
					}
				}
			}
		}
	}
	
}

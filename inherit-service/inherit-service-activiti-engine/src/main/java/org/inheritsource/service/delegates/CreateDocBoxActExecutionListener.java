package org.inheritsource.service.delegates;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.inheritsource.service.common.util.ConfigUtil;
import org.inheritsource.service.form.FormEngine;

public class CreateDocBoxActExecutionListener extends CreateDocBoxRefExecutionListener {

	private static final long serialVersionUID = 3567118234556647844L;	
	public static final Logger log = Logger.getLogger(CreateDocBoxActExecutionListener.class.getName());
	
	private String actBaseUri = null;
	
	public CreateDocBoxActExecutionListener() {
		actBaseUri = ConfigUtil.getConfigProperties().getProperty("docbox.doc.base.url", null);
		
		if (actBaseUri != null) {
			actBaseUri = actBaseUri.trim();
		}
		
		log.info("Using actBaseUri=[" + actBaseUri + "]");
	}
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		super.notify(execution);
		System.out.println("CreateDocBoxActExecutionListener called from " + execution.getCurrentActivityName() + " in process " + execution.getProcessInstanceId() + " at " + new Date());
		
		TaskService taskService = execution.getEngineServices().getTaskService();
		Task task = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
		
		if (task != null) {
			// task end event
			if (ExecutionListener.EVENTNAME_END.equals(execution.getEventName())) {
				String taskDocBoxRefVarName = DelegateUtil.calcTaskVariableName(FormEngine.FORM_DOCBOXREF, task.getId());
				String docBoxRef = (String) execution.getEngineServices().getRuntimeService().getVariable(execution.getId(), taskDocBoxRefVarName);
				if (docBoxRef != null && docBoxRef.trim().length()>0 && actBaseUri!=null) {
					String actUri = actBaseUri + docBoxRef;
					String taskDocActVarName = DelegateUtil.calcTaskVariableName(FormEngine.FORM_ACT_URI, task.getId());
					execution.getEngineServices().getRuntimeService().setVariable(execution.getId(), taskDocActVarName, actUri);
				}
				else {
					log.severe("Could not create act for task with taskId=[" + task.getId() + "] actBaseUri=[" + actBaseUri + "] and docBoxRef=[" + docBoxRef + "]" );
				}
			}
		}
		else {
			// not a task, assume process start event
			if (ExecutionListener.EVENTNAME_START.equals(execution.getEventName())) {
				
				String docBoxRef = (String) execution.getEngineServices().getRuntimeService().getVariable(execution.getId(), FormEngine.START_FORM_DOCBOXREF);
				if (docBoxRef != null && docBoxRef.trim().length()>0 && actBaseUri!=null) {
					String actUri = actBaseUri + docBoxRef;
					execution.getEngineServices().getRuntimeService().setVariable(execution.getId(), FormEngine.START_FORM_ACT_URI, actUri);
				}
				else {
					log.severe("Could not create act for start form with executionId=[" + execution.getId() + "] actBaseUri=[" + actBaseUri + "] and docBoxRef=[" + docBoxRef + "]" );
				}
			}
		}
		
	}


}

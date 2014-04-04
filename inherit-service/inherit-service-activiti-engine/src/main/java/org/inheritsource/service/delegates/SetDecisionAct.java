package org.inheritsource.service.delegates;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.inheritsource.service.docbox.DocBoxFacade;
import org.inheritsource.service.docbox.DocBoxFormData;
import org.inheritsource.service.form.FormEngine;

public class SetDecisionAct implements ExecutionListener {

	public static final Logger log = Logger.getLogger(SetDecisionAct.class.getName());
	
	private static final long serialVersionUID = -1522506737389383556L;
	
	public static final String DECISION_ACT_URI = "decision.act.uri";
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("CreateDocBoxActExecutionListener called from " + execution.getCurrentActivityName() + " in process " + execution.getProcessInstanceId() + " at " + new Date()); 
		
		Map<String, Object> procVars = execution.getEngineServices().getRuntimeService().getVariables(execution.getId());
		
		String prevVal = (String)procVars.get(DECISION_ACT_URI);
		if (prevVal != null && prevVal.trim().length()>0) {
			log.warning("The process variable " + DECISION_ACT_URI + " is already set to '" + prevVal + "' ond will probably be overwritten");
		}
		
		TaskService taskService = execution.getEngineServices().getTaskService();
		Task task = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
		
		
		if (task != null) {
			if (ExecutionListener.EVENTNAME_END.equals(execution.getEventName())) {
				
				Map<String, Object> taskVars = execution.getEngineServices().getTaskService().getVariablesLocal(task.getId());
				
				String formActUri = (String)taskVars.get(FormEngine.FORM_ACT_URI);

				if (formActUri != null && formActUri.trim().length()>0) {
					execution.getEngineServices().getRuntimeService().setVariable(execution.getId(), DECISION_ACT_URI, formActUri);
				}
				else {
					log.severe("Invalid use of SetDecisionAct ExecutionListener, the task local variable " + FormEngine.FORM_ACT_URI + "is expected to have a value");
				}	
			}
			else {
				log.severe("Invalid use of SetDecisionAct ExecutionListener, the task local variable " + FormEngine.FORM_ACT_URI + "is expected to be triggered by an end event");
			}
		}
		else {
			if (ExecutionListener.EVENTNAME_START.equals(execution.getEventName())) {
				
				String formActUri = (String)procVars.get(FormEngine.START_FORM_ACT_URI);

				if (formActUri != null && formActUri.trim().length()>0) {
					execution.getEngineServices().getRuntimeService().setVariable(execution.getId(), DECISION_ACT_URI, formActUri);
				}
				else {
					log.severe("Invalid use of SetDecisionAct ExecutionListener, the process variable " + FormEngine.START_FORM_ACT_URI + "is expected to have a value");
				}	
			}
			else {
				log.severe("Invalid use of SetDecisionAct ExecutionListener, the task local variable " + FormEngine.FORM_ACT_URI + "is expected to be triggered by an end event");
			}
		}
	}
}

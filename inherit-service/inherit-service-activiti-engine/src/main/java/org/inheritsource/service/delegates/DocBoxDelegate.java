package org.inheritsource.service.delegates;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;

public class DocBoxDelegate implements JavaDelegate {

	public static final Logger log = Logger.getLogger(DocBoxDelegate.class.getName());
	
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("DocBoxDelegate called from " + execution.getCurrentActivityName() + " in process " + execution.getProcessInstanceId() + " at " + new Date()); 
		
		TaskService taskService = execution.getEngineServices().getTaskService();
		Task task = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
		
		if (task != null) {
			Map<String, Object> taskVars = task.getTaskLocalVariables();
			
			// TODO 
		}
	}
	
}

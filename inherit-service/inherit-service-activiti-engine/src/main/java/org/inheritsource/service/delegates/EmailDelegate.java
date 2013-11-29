package org.inheritsource.service.delegates;

import java.util.Date;
import java.util.logging.Logger;

import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.DelegateExecution;

;

public class EmailDelegate implements JavaDelegate {
	public static final Logger log = Logger.getLogger(EmailDelegate.class.getName());

	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("Email Delegate called from " + execution.getCurrentActivityName() + " in process " + execution.getProcessInstanceId() + " at " + new Date());  
    }
	
}

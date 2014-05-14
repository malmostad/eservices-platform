/* == Motrice Copyright Notice == 
 * 
 * Motrice Service Platform 
 * 
 * Copyright (C) 2011-2014 Motrice AB 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Affero General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Affero General Public License for more details. 
 * 
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>. 
 * 
 * e-mail: info _at_ motrice.se 
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 * phone: +46 8 641 64 14 
 
 */ 
 
package org.inheritsource.service.delegates;

import java.util.Date;
import java.util.logging.Logger;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.task.Task;
import org.inheritsource.service.form.FormEngine;
import org.inheritsource.taskform.engine.TaskFormService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SimplifiedServiceMessageDelegate implements JavaDelegate, ApplicationContextAware {
	
	public static final Logger log = Logger.getLogger(SimplifiedServiceMessageDelegate.class.getName());

	public static String PROC_VAR_RECIPIENT_USER_ID = "recipientUserId";
	public static String PROC_VAR_SERVICE_DOC_URI = "serviceDocUri";
	
	public static String ACT_VAR_MESSAGE_TEXT = "emailMessageText";
	public static String ACT_VAR_SUBJECT = "emailSubject";

	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("SimplifiedServiceMessageDelegate called from " + execution.getCurrentActivityName() + " in process " + execution.getProcessInstanceId() + " at " + new Date());

		TaskFormService service = (TaskFormService) context.getBean("engine");
		
		/*
		TaskService taskService = execution.getEngineServices().getTaskService();
		Task task = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
		
		String taskDocActVarName = DelegateUtil.calcTaskVariableName(FormEngine.FORM_ACT_URI, task.getId());
		String serviceDocUri = (String)execution.getEngineServices().getRuntimeService().getVariable(execution.getId(), PROC_VAR_SERVICE_DOC_URI);

		if (serviceDocUri != null && serviceDocUri.trim().length()>0) {
			execution.getEngineServices().getRuntimeService().setVariable(execution.getId(), taskDocActVarName, serviceDocUri);
		}
		else {
			log.severe("Invalid use of SimplifiedServiceMessageDelegate, the task local variable " + PROC_VAR_SERVICE_DOC_URI + "is expected to have a value");
		}	
		*/
		
		String recipientUserId = (String)execution.getEngineServices().getRuntimeService().getVariable(execution.getId(), PROC_VAR_RECIPIENT_USER_ID);
		if (recipientUserId == null || recipientUserId.trim().length()==0) {
			log.severe("Invalid use of SimplifiedServiceMessageDelegate, the task local variable " + PROC_VAR_RECIPIENT_USER_ID + "is expected to have a value");
		}	
		
		String messageText = (String) execution.getEngineServices().getRuntimeService().getVariableLocal(execution.getId(), ACT_VAR_MESSAGE_TEXT);
		if (messageText == null || messageText.trim().length()==0) { 
			messageText = "Du har ett beslut i din inkorg https://eservice.malmo.se/site/public/mycases/inbox";
		}
		String messageSubject = (String) execution.getEngineServices().getRuntimeService().getVariableLocal(execution.getId(), ACT_VAR_SUBJECT);
		if (messageSubject == null || messageSubject.trim().length()==0) { 
			messageSubject = "Delgivning";
		}
		
		
		System.out.println("Email to: " +  recipientUserId);
		System.out.println("Email subject: " +  messageSubject);
		System.out.println("Email text: " +  messageText);

    }

	// work around app context
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		context = arg0;
		
	} 
	
	private static ApplicationContext context;

}

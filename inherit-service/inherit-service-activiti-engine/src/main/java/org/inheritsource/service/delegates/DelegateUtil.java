package org.inheritsource.service.delegates;

import org.inheritsource.service.form.FormEngine;

public class DelegateUtil {
	
	/*
	 * calc a process variable name with the taskId included, use this in order
	 * to store a task variable as a process variable. Activiti does not work
	 * properly if task variables is set at an ExecutionListener's end event 
	 */
	public static String calcTaskVariableName(String variableName, String taskId) {
		return variableName + "[" + taskId + "]";
	}

}

package org.inheritsource.taskform.form;

public interface TaskFormHandler {
	
	/**
	 * 
	 * @param String taskId
	 * @param String userId
	 * @return URL that will render a read only view of this form, return null if form is not initialized
	 */
	String getViewUrl(String taskId, String userId);
	

	/**
	 * 
	 * @param taskId
	 * @param userId
	 * @return URL that will render an editable version of this form, call initTaskForm if form is not initialized
	 */
	String getEditUrl(String taskId, String userId);
	
	/**
	 * 
	 * @return URI that holds the form instance data, return null if task form is not submitted (i.e. task is not executed)
	 */
	String getDataUri(String taskId);
	
	
	/**
	 * The form definition key identifies the form definition in the form engine.  
	 * @param taskId
	 * @return 
	 */
	String getFormDefinitionKey(String taskId);
	
	/**
	 * 
	 * @param taskId
	 * @return form handler's formInstanceId
	 * @throws Exception
	 */
	String initForm(String taskId) throws Exception;
	
	/**
	 * Depending on the form handler, taskId and/or instanceId is included on submission and passed to this method 
	 * @param taskId
	 * @param instanceId
	 * @return successful
	 */
	boolean afterSubmit(String taskId, String instanceId);

}

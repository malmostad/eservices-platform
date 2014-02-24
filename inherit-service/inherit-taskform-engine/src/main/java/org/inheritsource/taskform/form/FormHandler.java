package org.inheritsource.taskform.form;

public interface FormHandler {
	
	/**
	 * 
	 * @return
	 */
	String getViewUrl();
	
	/**
	 * 
	 * @return
	 */
	String getEditUrl();
	
	/**
	 * 
	 * @return
	 */
	String getDataUri();
	
	/**
	 * 
	 * @param formDefinitionId
	 * @param formInstanceId
	 * @throws Exception
	 */
	void afterSubmit(String formDefinitionId, String formInstanceId) throws Exception;
	
	/**
	 * 
	 * @return true if this form can be post loaded in div tag.  
	 */
	boolean isEmbedable();
	

	/**
	 * 
	 * @return
	 */
	boolean isExternal();
}

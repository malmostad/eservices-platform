package org.inheritsource.service.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.inheritsource.service.common.domain.FormInstance;
import org.inheritsource.service.common.domain.StartForm;
import org.inheritsource.service.common.domain.StartLogItem;
import org.inheritsource.service.coordinatrice.ProcessDefinitionState;
import org.inheritsource.service.identity.IdentityService;
import org.inheritsource.service.processengine.ActivitiEngineService;
import org.inheritsource.taskform.engine.persistence.TaskFormDb;
import org.inheritsource.taskform.engine.persistence.entity.ActivityFormDefinition;
import org.inheritsource.taskform.engine.persistence.entity.StartFormDefinition;

public class FormEngine {
	
	public static final Logger log = Logger.getLogger(FormEngine.class.getName());
	
	// motrice start process instance variables by convention
	public static final String START_FORM_TYPEID        = "motrice.start.form.typeId";
	public static final String START_FORM_DEFINITIONKEY = "motrice.start.form.definitionKey";
	public static final String START_FORM_INSTANCEID    = "motrice.start.form.instanceId";
	public static final String START_FORM_ASSIGNEE      = "motrice.start.form.assignee";
	public static final String START_FORM_DATA_URI      = "motrice.start.form.dataUri";
	public static final String START_FORM_ACT_URI       = "motrice.start.form.preservation.actUri";
	public static final String START_FORM_DOCBOXREF     = "motrice.start.form.preservation.docBoxRef";
	
	// motrice task local instance variables by convention
	public static final String FORM_TYPEID              = "motrice.form.typeId";
	public static final String FORM_DEFINITIONKEY       = "motrice.form.definitionKey";
	public static final String FORM_INSTANCEID          = "motrice.form.instanceId";
	public static final String FORM_DATA_URI            = "motrice.form.dataUri";
	public static final String FORM_ACT_URI             = "motrice.form.preservation.actUri";
	public static final String FORM_DOCBOXREF           = "motrice.form.preservation.docBoxRef";
	
	ActivitiEngineService activitiEngineService;
	TaskFormDb taskFormDb; 
	IdentityService identityService; 
	
	private Map<String, TaskFormHandler> formTypeId2Handler = new HashMap<String, TaskFormHandler>();
	
	public FormEngine() {
		
	}
	
	
	
	public TaskFormDb getTaskFormDb() {
		return taskFormDb;
	}



	public void setTaskFormDb(TaskFormDb taskFormDb) {
		this.taskFormDb = taskFormDb;
	}



	public IdentityService getIdentityService() {
		return identityService;
	}



	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}



	private TaskFormHandler getTaskFormHandler(Long typeId) {
		TaskFormHandler result = formTypeId2Handler.get(typeId.toString());
		
		if (result == null) {
			log.severe("Cannot find TaskFormHandler for typeId=" + typeId);
		}
		
		return result;
	}
	
	public ActivitiEngineService getActivitiEngineService() {
		return activitiEngineService;
	}

	public void setActivitiEngineService(ActivitiEngineService activitiEngineService) {
		this.activitiEngineService = activitiEngineService;
	}

	public Map<String, TaskFormHandler> getFormTypeId2Handler() {
		return formTypeId2Handler;
	}

	public void setFormTypeId2Handler(
			Map<String, TaskFormHandler> formTypeId2Handler) {
		this.formTypeId2Handler = formTypeId2Handler;
	}

	
	public FormInstance getHistoricFormInstance(HistoricTaskInstance historicTask, String userId, FormInstance initialInstance) {
		FormInstance formInstance = null;
		
		if (historicTask!=null) {

			Map <String, Object> localVars = historicTask.getTaskLocalVariables();
			
			Long typeId = (Long)localVars.get(FORM_TYPEID);
			if (typeId != null) {
				formInstance = getFormInstanceByCommonLocalVars(localVars, initialInstance);
				
				formInstance.setSubmitted(historicTask.getEndTime());
				formInstance.setSubmittedBy(identityService.getUserByUuid(historicTask.getAssignee()));
	
				TaskFormHandler handler = getTaskFormHandler(formInstance.getTypeId());
				if (handler != null) {
					handler.getHistoricFormInstance(formInstance, historicTask, userId);
				}
				
			}

			if (formInstance != null) {
				formInstance.setActinstId(historicTask.getId());
			}
			
		}
		
		return formInstance;
	}
	
	/**  
	 * initialize a task form instance in activiti engine. set variables by convention.
	 * Get existing FormInstance if form already is initialized.
	 * @return FormInstance of task, new initialized or existing if already initialized.
	 */ 
	public FormInstance getFormInstance(Task task, String userId, FormInstance initialInstance) {
		FormInstance formInstance = null;
		
		Map <String, Object> localVars = task.getTaskLocalVariables();
		Long typeId = (Long)localVars.get(FORM_TYPEID);
		if (typeId != null) {
			// this form is already initialized
			formInstance = getFormInstanceByCommonLocalVars(localVars, initialInstance);
			TaskFormHandler handler = getTaskFormHandler(formInstance.getTypeId());
			if (handler != null) {
				handler.getPendingFormInstance(formInstance, task, userId);
			}
		}
		else {
			// initialize form instance
			// find definition and form handler
			ActivityFormDefinition actformdef = taskFormDb.getActivityFormDefinition(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
			TaskFormHandler handler = null;
			if (actformdef != null) {
				handler = getTaskFormHandler(actformdef.getFormTypeId());
			}
			else {
				handler = getTaskFormHandler(new Long(6)); // no form handler		
			}
			
			if (handler != null) {
				// initialize form and store task local variables
				formInstance = handler.initializeFormInstance(actformdef, task, userId, initialInstance);
				
				localVars.put(FORM_TYPEID, formInstance.getTypeId());
				localVars.put(FORM_DEFINITIONKEY, formInstance.getDefinitionKey());
				localVars.put(FORM_INSTANCEID, formInstance.getInstanceId());
				localVars.put(FORM_DATA_URI, formInstance.getDataUri());
				localVars.put(FORM_ACT_URI, formInstance.getActUri());
				
				activitiEngineService.getEngine().getTaskService().setVariablesLocal(task.getId(), localVars);
			}
		}
		if (formInstance != null && task!=null) {
			formInstance.setActinstId(task.getId());
		}
		return formInstance;
	}
	
	
	/**  
	 * initialize a task form instance in activiti engine. set variables by convention.
	 * Get existing FormInstance if form already is initialized.
	 * @return FormInstance of task, new initialized or existing if already initialized.
	 */ 
	public FormInstance getStartFormInstance(Long formTypeId, String formConnectionKey, String userId, FormInstance initialInstance) {
		FormInstance formInstance = null;
		
		// initialize form instance
			
		// find form handler
		TaskFormHandler handler = getTaskFormHandler(formTypeId);
			
		if (handler != null) {
			// initialize form and store task local variables
			formInstance = handler.initializeStartFormInstance(formTypeId, formConnectionKey, userId, initialInstance);
		}
		
		return formInstance;
	}
	
	
	
	public StartLogItem getStartLogItem(ProcessInstance processInstance, String userId) {
		StartLogItem startFormInstance = null;
		
		Map <String, Object> processVars = processInstance.getProcessVariables();
		
		if (processVars != null) {
			startFormInstance = new StartLogItem();
			startFormInstance.setTypeId((Long)processVars.get(START_FORM_TYPEID));
			startFormInstance.setDefinitionKey((String)processVars.get(START_FORM_DEFINITIONKEY));
			startFormInstance.setInstanceId((String)processVars.get(START_FORM_INSTANCEID));
			startFormInstance.setDataUri((String)processVars.get(START_FORM_DATA_URI));
			startFormInstance.setActUri((String)processVars.get(START_FORM_ACT_URI));
			startFormInstance.setSubmittedBy(identityService.getUserByUuid((String)processVars.get(START_FORM_ASSIGNEE)));
			
			HistoricProcessInstance historicProcessInstance = activitiEngineService.getEngine().getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
			
			if (historicProcessInstance != null) {
				startFormInstance.setSubmitted(historicProcessInstance.getStartTime());
			
				if (startFormInstance.getTypeId()!=null) {				
					TaskFormHandler handler = getTaskFormHandler(startFormInstance.getTypeId());
					if (handler != null) {
						handler.getHistoricStartFormInstance(startFormInstance, historicProcessInstance, userId);
					}
				}
			}
		}
		
		return startFormInstance;
	}
	
	protected FormInstance getFormInstanceByCommonLocalVars(Map <String, Object> localVars, FormInstance initialInstance) {
		FormInstance result = null;
		
		if (localVars != null) {
			result = initialInstance;
			result.setTypeId((Long)localVars.get(FORM_TYPEID));
			result.setDefinitionKey((String)localVars.get(FORM_DEFINITIONKEY));
			result.setInstanceId((String)localVars.get(FORM_INSTANCEID));
			result.setDataUri((String)localVars.get(FORM_DATA_URI));
		}
		
		return result;
	}
	
	public List<StartForm> getStartForms(Locale locale){
		List<StartForm> result = new ArrayList<StartForm>();
		List<StartForm> forms = taskFormDb.getStartForms();
		if (forms != null) {
			for (StartForm form : forms) {
				form.setPage("form");
				String label = getActivitiEngineService().getCoordinatriceDao().getStartFormLabelByStartFormDefinitionKey(form.getDefinitionKey(), locale, form.getDefinitionKey());
				form.setLabel(label);
				
				StartFormDefinition startFormDef;
				try {
					startFormDef = taskFormDb.getStartFormDefinitionByFormPath(form.getDefinitionKey());
					if (startFormDef != null) {
						 ProcessDefinitionState state = getActivitiEngineService().getCoordinatriceDao().getProcessDefinitionState(startFormDef.getProcessDefinitionUuid());
						 if (state != null) {
							 if (state.getStartableCode()==3) {
								 result.add(form);
							 }
						 }
					}
				} catch (Exception e) {
					log.severe("Could not load StartFormDefinition by formConnectionKey: " + form.getDefinitionKey());
				}

				
			}
		}
		return result;
	}

}

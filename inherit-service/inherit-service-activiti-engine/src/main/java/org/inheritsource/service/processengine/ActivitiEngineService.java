package org.inheritsource.service.processengine;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.inheritsource.service.common.domain.ActivityDefinitionInfo;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ActivityInstanceLogItem;
import org.inheritsource.service.common.domain.ActivityInstancePendingItem;
import org.inheritsource.service.common.domain.ActivityWorkflowInfo;
import org.inheritsource.service.common.domain.CandidateInfo;
import org.inheritsource.service.common.domain.CommentFeedItem;
import org.inheritsource.service.common.domain.DashOpenActivities;
import org.inheritsource.service.common.domain.DocBoxFormData;
import org.inheritsource.service.common.domain.FormInstance;
import org.inheritsource.service.common.domain.GroupInfo;
import org.inheritsource.service.common.domain.InboxTaskItem;
import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.common.domain.ProcessDefinitionDetails;
import org.inheritsource.service.common.domain.ProcessDefinitionInfo;
import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.domain.ProcessInstanceListItem;
import org.inheritsource.service.common.domain.StartLogItem;
import org.inheritsource.service.common.domain.Tag;
import org.inheritsource.service.common.domain.Timeline;
import org.inheritsource.service.common.domain.TimelineItem;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.service.common.util.ConfigUtil;
import org.inheritsource.service.coordinatrice.CoordinatriceFacade;
import org.inheritsource.service.delegates.DelegateUtil;
import org.inheritsource.service.docbox.DocBoxFacade;
import org.inheritsource.service.form.FormEngine;
import org.inheritsource.service.identity.IdentityService;
import org.inheritsource.taskform.engine.persistence.TaskFormDb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ActivitiEngineService {

	private CoordinatriceFacade coordinatriceFacade = null;
	
	private ProcessEngine engine = null; 
	private FormEngine formEngine = null;
	private IdentityService identityService = null;
	private TaskFormDb taskFormDb = null;
	
	public static final Logger log = Logger.getLogger(ActivitiEngineService.class.getName());
	public static String docboxBaseUrl = ConfigUtil.getConfigProperties().getProperty("docbox.doc.base.url");
	
	public ActivitiEngineService() {		
	}
	
	public ProcessEngine getEngine() {
		return engine;
	}

	public void setEngine(ProcessEngine engine) {
		this.engine = engine;
	}

	public CoordinatriceFacade getCoordinatriceFacade() {
		return coordinatriceFacade;
	}

	public void setCoordinatriceFacade(CoordinatriceFacade coordinatriceFacade) {
		this.coordinatriceFacade = coordinatriceFacade;
	}

	public FormEngine getFormEngine() {
		return formEngine;
	}

	public void setFormEngine(FormEngine formEngine) {
		this.formEngine = formEngine;
	}

	
	
	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public TaskFormDb getTaskFormDb() {
		return taskFormDb;
	}

	public void setTaskFormDb(TaskFormDb taskFormDb) {
		this.taskFormDb = taskFormDb;
	}

	public void close() {
		if(engine != null) {
			engine.close();
		}
	}
	
	public List<InboxTaskItem> getUserInbox(Locale locale, String userId) {
		List<InboxTaskItem> result = new ArrayList<InboxTaskItem>();
		List<Group> groups = engine.getIdentityService().createGroupQuery().groupMember(userId).list();
		List<String> groupsStr = new ArrayList<String>();
		for (Group group : groups) {
			groupsStr.add(group.getId());
		}
		
		List<Task> groupCandidateTasks = null;
		if (! groupsStr.isEmpty()) {
			groupCandidateTasks = engine.getTaskService().createTaskQuery().taskCandidateGroupIn(groupsStr).list();
		} else {
			groupCandidateTasks = new ArrayList<Task>();
		}
		
		List<Task> tasks = engine.getTaskService().createTaskQuery().taskInvolvedUser(userId).				
				orderByTaskCreateTime().asc().list();
		
		// exclude duplicate tasks when merging		
		for (Task t : groupCandidateTasks) {
			if ( !tasks.contains(t) ) {
				tasks.add(t);
			}
		}
	
		result = taskList2InboxTaskItemList(tasks, locale, userId);
		Collections.sort(result);
		
		return result;
	}
	
	public Set<InboxTaskItem> getUserInboxByProcessInstanceId(String processInstanceId, Locale locale) {
		Set<InboxTaskItem> result = new LinkedHashSet<InboxTaskItem>();
		
		appendInboxTaskItemsFromProcessInstance(result, processInstanceId, locale);
				
		return result;
	}
		
	private void appendInboxTaskItemsFromProcessInstance(Set<InboxTaskItem> items, String processInstanceId, Locale locale) {
		if (items != null && processInstanceId != null) {
			List<Task> tasks = engine.getTaskService().createTaskQuery().
					processInstanceId(processInstanceId).orderByTaskCreateTime().asc().list();
				
				List<InboxTaskItem> inboxTaskItemList = taskList2InboxTaskItemList(tasks, locale, null);
				
				if(inboxTaskItemList != null) {
					for(InboxTaskItem inboxTaskItem : inboxTaskItemList) {
						items.add(inboxTaskItem);
					}
				}
				List<ProcessInstance> subprocesses = engine.getRuntimeService().createProcessInstanceQuery().superProcessInstanceId(processInstanceId).list();
				if (subprocesses != null) {
					for (ProcessInstance subProcInst : subprocesses) {
						appendInboxTaskItemsFromProcessInstance(items, subProcInst.getProcessInstanceId(), locale);
					}
				}
		}
	}
	

	private List<InboxTaskItem> taskList2InboxTaskItemList(List<Task> tasks, Locale locale, String userId) {
		List<InboxTaskItem> result = null;
		if (tasks != null) {
			result = new ArrayList<InboxTaskItem>();
			for (Task task : tasks) {
				result.add(task2InboxTaskItem(task, locale, userId));
			}
	    }
		return result;
	}
	
	private InboxTaskItem task2InboxTaskItem(Task task, Locale locale, String userId) {
		InboxTaskItem item = new InboxTaskItem();
		if (task != null) {
			
			item = (InboxTaskItem) formEngine.getFormInstance(task, userId, item);
			
			item.setActivityCreated(task.getCreateTime());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityLabel(coordinatriceFacade.getLabel(task.getProcessDefinitionId(), task.getName(), locale)); 
			item.setExpectedEndDate(task.getDueDate());
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			
						
			ProcessInstance pI = getMainProcessInstanceByProcessInstanceId
				(task.getProcessInstanceId());
						
		    if(pI != null) {
		    	item.setRootProcessInstanceUuid(pI.getProcessInstanceId());
				item.setRootProcessDefinitionUuid(pI.getProcessDefinitionId());
				item.setProcessLabel(coordinatriceFacade.getStartFormLabel(pI.getProcessInstanceId(), locale));

		    } else {
		    	item.setRootProcessInstanceUuid(task.getProcessInstanceId());
				item.setRootProcessDefinitionUuid(task.getProcessDefinitionId());
				item.setProcessLabel(coordinatriceFacade.getStartFormLabel(task.getProcessInstanceId(), locale));
		    }
		}
		return item;
	}
	
	private ProcessInstance getMainProcessInstanceByProcessInstanceId(String processInstanceId) {
		int turns = 0;
		ProcessInstance processInstance = null;
		ProcessInstance mainProcessInstance = null;
		boolean mainProcessInstanceFound = false;
		
		try {
			while(!mainProcessInstanceFound) {
				turns++;
				processInstance = engine.getRuntimeService().createProcessInstanceQuery().
						subProcessInstanceId(processInstanceId).singleResult();
				
				if(processInstance == null) {
					mainProcessInstanceFound = true;
					mainProcessInstance = engine.getRuntimeService().createProcessInstanceQuery().
						processInstanceId(processInstanceId).includeProcessVariables().singleResult();
				} else {
					processInstanceId = processInstance.getProcessInstanceId();
					
					if(turns > 1000) { // If instance chain is incorrect this limits the loop
						mainProcessInstance = processInstance;
						break;
					}
				}
			}
		} catch (Exception e) {
			log.severe("Unable to getMainProcessInstanceByProcessInstanceId with processInstanceId: " +
					processInstanceId);
		}
		
		return mainProcessInstance;
	}
	
	public Set<InboxTaskItem> getHistoricUserInboxByProcessInstanceId(String processInstanceId, Locale locale) {
		Set<InboxTaskItem> result = new LinkedHashSet<InboxTaskItem>();
		
		List<HistoricTaskInstance> tasks = engine.getHistoryService().createHistoricTaskInstanceQuery().
			processInstanceId(processInstanceId).finished().orderByHistoricTaskInstanceStartTime().asc().includeTaskLocalVariables().list();
		
		List<InboxTaskItem> inboxTaskItemList = historicTaskList2InboxTaskItemList(tasks, locale);
		
		if(inboxTaskItemList != null) {
			for(InboxTaskItem inboxTaskItem : inboxTaskItemList) {
				result.add(inboxTaskItem);
			}
		}
		
		return result;
	}
	
	private List<InboxTaskItem> historicTaskList2InboxTaskItemList(List<HistoricTaskInstance> tasks, Locale locale ) {
		List<InboxTaskItem> result = null;
		if (tasks != null) {
			result = new ArrayList<InboxTaskItem>();
			for (HistoricTaskInstance task : tasks) {
				result.add(historicTask2InboxTaskItem(task, locale));
			}
	    }
		return result;
	}
	
	private InboxTaskItem historicTask2InboxTaskItem(HistoricTaskInstance task, Locale locale) {
		InboxTaskItem item = null;
		if (task != null) {
			item = new InboxTaskItem();
			
			item = (InboxTaskItem) formEngine.getHistoricFormInstance(task, null, item);
			
			item.setActivityCreated(task.getStartTime());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityLabel(coordinatriceFacade.getLabel(task.getProcessDefinitionId(), task.getName(), locale));
			item.setExpectedEndDate(task.getDueDate());
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setProcessLabel(coordinatriceFacade.getStartFormLabel(task.getProcessInstanceId(), locale));

		    HistoricProcessInstance pI = getHistoricMainProcessInstanceByProcessInstanceId
				(task.getProcessInstanceId());
			
		    if(pI != null) {
		    	// Note: id is always the same as processInstanceId in this case
		    	item.setRootProcessInstanceUuid(pI.getId());
				item.setRootProcessDefinitionUuid(pI.getProcessDefinitionId()); 
		    } else {
		    	item.setRootProcessInstanceUuid(task.getProcessInstanceId());
				item.setRootProcessDefinitionUuid(task.getProcessDefinitionId());
		    }
		}
		return item;
	}
	
	private HistoricProcessInstance getHistoricMainProcessInstanceByProcessInstanceId(String processInstanceId) {
		int turns = 0;
		HistoricProcessInstance processInstance = null;
		HistoricProcessInstance mainProcessInstance = null;
		boolean mainProcessInstanceFound = false;
		
		try {
			while(!mainProcessInstanceFound) {
				turns++;
				processInstance = engine.getHistoryService().createHistoricProcessInstanceQuery().
						processInstanceId(processInstanceId).includeProcessVariables().singleResult();
				
				if(processInstance.getSuperProcessInstanceId() == null) {
					mainProcessInstanceFound = true;
					mainProcessInstance = processInstance;
				} else {
					processInstanceId = processInstance.getSuperProcessInstanceId();
					
					if(turns > 1000) { // If instance chain is incorrect this limits the loop
						mainProcessInstance = processInstance;
						break;
					}
				}
			}
		} catch (Exception e) {
			log.severe("Unable to getHistoricMainProcessInstanceByProcessInstanceId with processInstanceId: " +
					processInstanceId);
		}
		
		return mainProcessInstance;
	}
		
	public String getActivityInstanceUuid(String processInstanceId, String taskName) {
		String taskId = null;
		
		try {
			Task task = engine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).
				taskName(taskName).singleResult();
			
			if(task != null) {
				taskId = task.getId();
			} else {
				HistoricTaskInstance historicTask = engine.getHistoryService().
					createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).
						taskName(taskName).singleResult();
				
				if(historicTask != null) {
					taskId = historicTask.getId();
				}
			}
		} catch (Exception e) {
			log.severe("Unable to getActivityInstanceUuid with processInstanceId: " + processInstanceId +
					" and taskName: " + taskName);
		}
			
		return taskId;
	}
	
	public ActivityInstanceItem getActivityInstanceItem(String actinstId, String formInstanceId, Locale locale, String userId) {
		ActivityInstanceItem result = null;
		
		try {
			
			Task task = null;
			if 	(actinstId != null && actinstId.trim().length()>0) {	
				task = engine.getTaskService().createTaskQuery().taskId(actinstId).includeTaskLocalVariables().singleResult();
			}
			else if (task == null && formInstanceId != null && formInstanceId.trim().length()>0 ) {
				task = engine.getTaskService().createTaskQuery().taskVariableValueEquals(FormEngine.FORM_INSTANCEID, formInstanceId).includeTaskLocalVariables().singleResult();
			}
			
			if(task != null) {
				result = task2ActivityInstancePendingItem(task, locale);
			} else {
				HistoricTaskInstance historicTask = engine.getHistoryService().
						createHistoricTaskInstanceQuery().taskId(actinstId).includeTaskLocalVariables().singleResult();
				if(historicTask != null) {
					result = task2ActivityInstanceLogItem(historicTask, locale);
				}
			}
			
		} catch (Exception e) {
			log.severe("Unable to getActivityInstanceItem with taskId: " + actinstId + " Exception: " + e);
		}
		return result;
	}
	
	private ActivityInstancePendingItem task2ActivityInstancePendingItem(Task task, Locale locale) {
		ActivityInstancePendingItem item = null;
		if (task != null) {
			
			item = new ActivityInstancePendingItem();
			
			item = (ActivityInstancePendingItem) formEngine.getFormInstance(task, null, item);
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());
			item.setActivityName(task.getName());
			item.setActivityLabel(getTaskName(task, locale));
			item.setStartDate(task.getCreateTime());
			item.setCurrentState("EXECUTING");
			item.setLastStateUpdate(task.getCreateTime());
			item.setLastStateUpdateByUserId("");
			item.setStartedBy(getStarterByTaskId(task.getId()));
			item.setGuideUri(getGuideUri(task));
			item.setProcessActivityFormInstanceId(new Long(0));
			item.setActivityType(getActivityTypeByExecutionIdTaskId(task.getExecutionId(), task.getId()));
			item.setPriority(task.getPriority());
			item.setExpectedEndDate(task.getDueDate());
			// ActivityInstancePendingItem
			item.setCandidates(getCandidatesByTaskId(task.getId()));
			item.setAssignedUser(userId2UserInfo(task.getAssignee()));

		}
		return item;
	}
	
	private String getGuideUri(Task task) {
		ProcessDefinition procDef = engine.getRepositoryService().getProcessDefinition(task.getProcessDefinitionId());
		return procDef.getKey().toLowerCase() + "/" + task.getTaskDefinitionKey().toLowerCase();
	}
	
	private String getGuideUri(HistoricTaskInstance task) {
		ProcessDefinition procDef = engine.getRepositoryService().getProcessDefinition(task.getProcessDefinitionId());
		return procDef.getKey().toLowerCase() + "/" + task.getTaskDefinitionKey().toLowerCase();
	}
	
	private int getActivityTypeByExecutionIdTaskId(String executionId, String taskId) {
		
		int activityType = 0;
		try {
			List<HistoricActivityInstance> historicActivityInstances = 
				engine.getHistoryService().createHistoricActivityInstanceQuery().executionId(executionId).list();
			
			if(historicActivityInstances != null) {
				for(HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
					if(historicActivityInstance != null) {
						if(historicActivityInstance.getTaskId() != null &&
							historicActivityInstance.getTaskId().equals(taskId)) {
								
							if(historicActivityInstance.getActivityType() != null &&
								historicActivityInstance.getActivityType().equals("userTask")) {
								activityType = ActivityInstanceItem.ACTIVITY_TYPE_USER_TASK;
							} else {
								activityType = ActivityInstanceItem.ACTIVITY_TYPE_SERVICE_TASK;
							}
							
						}
					}
				}
			}
		} catch (Exception e) {
			log.severe("Unable to getActivityTypeByExecutionIdTaskId with executionId: " + executionId +
					" and taskId: " + taskId + "exception: " + e);
		}
		
		return activityType;
	}
	
	private ActivityInstanceLogItem task2ActivityInstanceLogItem(HistoricTaskInstance task, Locale locale) {
		ActivityInstanceLogItem item = null;
		if (task != null) {
			item = new ActivityInstanceLogItem();
			
			item = (ActivityInstanceLogItem) formEngine.getHistoricFormInstance(task, null, item);
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());
			item.setActivityName(task.getName());
			item.setActivityLabel(getHistoricTaskName(task, locale));
			item.setStartDate(task.getStartTime());
			item.setCurrentState("FINISHED");
			item.setLastStateUpdate(task.getDueDate());
			item.setLastStateUpdateByUserId("");
			item.setStartedBy(getHistoricStarterByTaskId(task.getId()));
			item.setGuideUri(getGuideUri(task));
			item.setProcessActivityFormInstanceId(new Long(0));
			item.setActivityType(getActivityTypeByExecutionIdTaskId(task.getExecutionId(), task.getId()));
			item.setPriority(task.getPriority());
			item.setExpectedEndDate(task.getDueDate());
			
			// ActivityInstancelogItem
			item.setEndDate(task.getEndTime());
			item.setPerformedByUser(userId2UserInfo(task.getAssignee()));
		}
		return item;
	}
	
	private String getTaskName(Task task, Locale locale) {
		String result = task.getName();
		String crdName = coordinatriceFacade.getLabel(task.getProcessDefinitionId(), task.getName(), locale);
		if (crdName != null) {
			result = crdName;
		}
		return result;
	}
	
	private String getHistoricTaskName(HistoricTaskInstance task, Locale locale) {
		String result = task.getName();
		String crdName = coordinatriceFacade.getLabel(task.getProcessDefinitionId(), task.getName(), locale);
		if (crdName != null) {
			result = crdName;
		}
		return result;
	}
	
	public DashOpenActivities getDashOpenActivitiesByUserId(String userId, int remainingDays) {
		
		if(remainingDays < 0) {
			remainingDays = 0;
		}

		DashOpenActivities dashOpenActivities = null;
		int onTrack = 0;
		int overdue = 0;
		int atRisk = 0;
		
		try {
			List<InboxTaskItem> inboxTaskItems = getUserInbox(null, userId);
			
			if(inboxTaskItems != null) {
				// Create dates needed in calculation
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date()); // today date.
				Date TODAY = formatter.parse(formatter.format(calendar.getTime()));
				calendar.add(Calendar.DATE, remainingDays); // today date with remaining days added
				Date TODAY_ADDED_WITH_REMAINING_DAYS = formatter.parse(formatter.format(calendar.getTime()));
				
				for (InboxTaskItem inboxTaskItem : inboxTaskItems) {
					
					if(inboxTaskItem.getExpectedEndDate() == null) {
						onTrack++;
					} else if (inboxTaskItem.getExpectedEndDate().after(TODAY_ADDED_WITH_REMAINING_DAYS)) {
						 onTrack++;
					} else if (inboxTaskItem.getExpectedEndDate().before(TODAY)) {
						overdue++;
					} else {
						atRisk++;
					}
				}		
			}
			
			dashOpenActivities = new DashOpenActivities();
			dashOpenActivities.setOnTrack(onTrack);
			dashOpenActivities.setAtRisk(atRisk);
			dashOpenActivities.setOverdue(overdue);
		
		} catch (Exception e) {
			log.severe("Unable to getDashOpenActivitiesByUserId with userId: " + userId +
					" and remaining days: " + remainingDays + 
					" execption: " + e);
			dashOpenActivities = null;
		}
		
		return dashOpenActivities;
	}

	public StartLogItem getStartLogItem(String processInstanceId) {
		HistoricProcessInstance historicProcessInstance = engine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables().singleResult();
			
		return formEngine.getStartLogItem(historicProcessInstance, null);
	}
	
	
	private String getStartFormName(HistoricProcessInstance processInstance, Locale locale) {
		
		String result = coordinatriceFacade.getStartFormLabel(processInstance.getId(), locale);
		
		if (result == null) {
			ProcessDefinition procDef = engine.getRepositoryService().getProcessDefinition(processInstance.getProcessDefinitionId());
			if (procDef != null) {
				result = procDef.getName();
			}
		}
		if (result == null) {
			result = "Ärende";
		}
		return result;
	}
	
	private String getHistoricStartFormName(HistoricProcessInstance processInstance, Locale locale) {
		String result = null;
		if (processInstance != null) {
			String formDefKey = (String)processInstance.getProcessVariables().get(FormEngine.START_FORM_DEFINITIONKEY);
			if (formDefKey != null) {
				result = coordinatriceFacade.getStartFormLabelByStartFormDefinitionKey(formDefKey, locale, null);
			}
		}
		
		if (result == null) {
			
			ProcessDefinition procDef = engine.getRepositoryService().getProcessDefinition(processInstance.getProcessDefinitionId());
			if (procDef != null) {
				result = procDef.getName();
			}
		}
		if (result == null) {
			result = "Ärende";
		}
		return result;
	}
	
	private void appendDetailsFromProcessInstance(ProcessInstanceDetails processInstanceDetails, String processInstanceId, Locale locale) {
	
		// Tasks in this process
		List<Task> tasks = engine.getTaskService().createTaskQuery().
			processInstanceId(processInstanceId).orderByTaskName().includeTaskLocalVariables().asc().list();

		// append historic tasks (i.e. to pending items)
		if(tasks != null && tasks.size() > 0) {
			for(Task task : tasks) {
				processInstanceDetails.addActivityInstanceItem(task2ActivityInstancePendingItem(task, locale));
			}	
		}	
	
		// append historic tasks (i.e. to timeline)
		List<HistoricTaskInstance> historicTasks = engine.getHistoryService().createHistoricTaskInstanceQuery().
				processInstanceId(processInstanceId).finished().includeTaskLocalVariables().orderByHistoricTaskInstanceStartTime().asc().list();
			if(historicTasks != null) {
				for (HistoricTaskInstance historicTask : historicTasks) {
					processInstanceDetails.addActivityInstanceItem(task2ActivityInstanceLogItem(historicTask, locale));
				}
			}

			List<ProcessInstance> subprocesses = engine.getRuntimeService().createProcessInstanceQuery().superProcessInstanceId(processInstanceId).list();
			if (subprocesses != null) {
				// iterate sub processes and add details from them as well
				for (ProcessInstance subProcInst : subprocesses) {
					appendDetailsFromProcessInstance(processInstanceDetails, subProcInst.getProcessInstanceId(), locale);
				}
			}
	}

	
	public ProcessInstanceDetails getProcessInstanceDetails(String processInstanceId, Locale locale) {
		ProcessInstanceDetails processInstanceDetails = null;
		
		String mainProcessInstanceId = processInstanceId;
		try {
			processInstanceDetails = new ProcessInstanceDetails();
			
			// Check if process is found among the active ones 
			
			HistoricProcessInstance processInstance = engine.getHistoryService().createHistoricProcessInstanceQuery().
					processInstanceId(processInstanceId).includeProcessVariables().singleResult();
			
			StartLogItem startLogItem = null;
			
			if(processInstance != null) {
				
				HistoricProcessInstance mainPi = getHistoricMainProcessInstanceByProcessInstanceId(processInstanceId);
				if (mainPi != null) {
					mainProcessInstanceId = mainPi.getId();
					processInstance = mainPi;
				}
				
				String procInstLabel = getStartFormName(processInstance, locale);
				processInstanceDetails.setProcessInstanceLabel(procInstLabel); // TODO is label in use
				processInstanceDetails.setProcessLabel(procInstLabel);
				
				startLogItem = formEngine.getStartLogItem(processInstance, null);
				if (startLogItem != null) {
					startLogItem.setActivityLabel(procInstLabel);
					processInstanceDetails.getTimeline().add(startLogItem);
				}
				
				processInstanceDetails.setStatus(ProcessInstanceListItem.STATUS_PENDING);
				processInstanceDetails.setStartedBy(getStarterByProcessInstanceId(processInstance.getId()));
				processInstanceDetails.setStartDate(getProcessInstanceStartDateByProcessInstanceId(processInstance.getId()));
				processInstanceDetails.setEndDate(null);
				processInstanceDetails.setProcessInstanceUuid(processInstance.getId());
				
				
			} else {
				// Check if process is found among the historic ones 
				HistoricProcessInstance historicProcessInstance = engine.getHistoryService().
					createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
				
				
				
				if(historicProcessInstance != null) {
					
					HistoricProcessInstance mainPi = getHistoricMainProcessInstanceByProcessInstanceId(mainProcessInstanceId);
					if (mainPi != null) {
						mainProcessInstanceId = mainPi.getId();
						historicProcessInstance = mainPi;
					}

					
					String procInstLabel = getHistoricStartFormName(historicProcessInstance, locale);
					processInstanceDetails.setProcessInstanceLabel(procInstLabel);

					// todo historic start log item....this will not work
					startLogItem = formEngine.getStartLogItem(processInstance, null);
					if (startLogItem != null) {
						startLogItem.setActivityLabel(procInstLabel);
						processInstanceDetails.getTimeline().add(startLogItem);
					}
					
					processInstanceDetails.setStatus(ProcessInstanceListItem.STATUS_FINISHED);
					processInstanceDetails.setStartedBy(historicProcessInstance.getStartUserId());
					processInstanceDetails.setStartDate(historicProcessInstance.getStartTime());
					processInstanceDetails.setEndDate(historicProcessInstance.getEndTime());
				} else {
					// No process instances found at all, return null
					return null;
				}
			}
			
			// Append historic tasks in timeline and tasks to pending
			appendDetailsFromProcessInstance(processInstanceDetails, mainProcessInstanceId, locale);
			
			// sort timeline
			processInstanceDetails.getTimeline().sort();
			
		} catch (Exception e) {
			log.severe("Unable to getProcessInstanceDetails with processInstanceId: " + processInstanceId + 
					" execption: " + e);
			processInstanceDetails = null;	
		}
		return processInstanceDetails;
	}

	public ProcessInstanceDetails getProcessInstanceDetailsByActivityInstance(String taskId, Locale locale) {
		ProcessInstanceDetails processInstanceDetails = null;
		
		// Note: Both tasks and historic tasks are represented in the history service. 
		
		try {
			HistoricTaskInstance task = engine.getHistoryService().createHistoricTaskInstanceQuery().taskId(taskId).singleResult();

			if(task != null) {
				processInstanceDetails = getProcessInstanceDetails(task.getProcessInstanceId(), locale);
			}
		} catch (Exception e) {
			log.severe("Unable to getProcessInstanceDetailsByActivityInstance with taskId: " + taskId + 
					" execption: " + e);
			processInstanceDetails = null;	
		}
		return processInstanceDetails;
	}

	public int addComment(String taskId, String comment, String userId) {
		int retVal = -1;
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			Task task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
			
			if(task != null) {
				Comment addedComment = engine.getTaskService().addComment
					(taskId, task.getProcessInstanceId(), comment);

				if(addedComment != null) {
					String msg = addedComment.getFullMessage();
					
					if(msg != null) {
						retVal = 1;
					}
				}
			}
		} catch (Exception e) {
			log.severe("Unable to addComment with taskId: " + taskId + 
				" and userId: " + userId + 
				" execption: " + e);
			retVal = -1;	
		} finally {
			engine.getIdentityService().setAuthenticatedUserId(null);
	    }
		
		return retVal;
	}
	
	public List<CommentFeedItem> getProcessInstanceCommentFeedByActivity(String taskId, Locale locale) {
		List<CommentFeedItem> commentFeedItems = new ArrayList<CommentFeedItem>();
		CommentFeedItem cFItem = null;
		Task task = null;
		HistoricTaskInstance historicTask = null;
		String processDefinitionUuid = null;
		String activityDefinitionUuid = null;
		String processInstanceId = null;
		String setActivityLabel = null;
		String processLabel = null;
		
		try {
			List<Comment> comments = engine.getTaskService().getTaskComments(taskId);

			if(comments != null && comments.size() > 0) {
				
				// Assume task is present first
				task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
				
				if(task != null) {
					processDefinitionUuid = task.getProcessDefinitionId();
					activityDefinitionUuid = task.getTaskDefinitionKey();
					processInstanceId = task.getProcessInstanceId();
					setActivityLabel = task.getName();
					processLabel = coordinatriceFacade.getStartFormLabel(task.getProcessInstanceId(), locale);
				} else {
					historicTask = engine.getHistoryService().createHistoricTaskInstanceQuery().
						taskId(taskId).singleResult();
					
					if(historicTask != null) {
						processDefinitionUuid = historicTask.getProcessDefinitionId();
						activityDefinitionUuid = historicTask.getTaskDefinitionKey();
						processInstanceId = historicTask.getProcessInstanceId();
						setActivityLabel = historicTask.getName();
						processLabel = coordinatriceFacade.getStartFormLabel(historicTask.getProcessInstanceId(), locale);
					} else {
						processDefinitionUuid = "";
						activityDefinitionUuid = "";
						processInstanceId = "";
						setActivityLabel = "";
						processLabel = "";
					}
				}
				
				for(Comment comment : comments) {
					
					cFItem = new CommentFeedItem();
					
					cFItem.setProcessDefinitionUuid(processDefinitionUuid);
					cFItem.setProcessInstanceUuid(processInstanceId);
					cFItem.setProcessLabel(processLabel);
					cFItem.setActivityDefinitionUuid(activityDefinitionUuid);
					cFItem.setActivityInstanceUuid(comment.getTaskId());
					cFItem.setActivityLabel(setActivityLabel);
					cFItem.setTimestamp(comment.getTime());
					cFItem.setMessage(comment.getFullMessage());
					cFItem.setUser(userId2UserInfo(comment.getUserId()));
					
					commentFeedItems.add(cFItem);
				}
			}
		} catch (Exception e) {
			log.severe("Unable to getProcessInstanceCommentFeedByActivity with taskId: " + taskId);
			commentFeedItems = new ArrayList<CommentFeedItem>();
		}
		return commentFeedItems;
	}

	public ActivityWorkflowInfo getActivityWorkflowInfo(String taskId) {
	
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			Task task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
			
			activityWorkflowInfo = new ActivityWorkflowInfo();
			activityWorkflowInfo.setPriority(task.getPriority());
			activityWorkflowInfo.setAssignedUser(userId2UserInfo(task.getAssignee()));
			activityWorkflowInfo.setCandidates(getCandidatesByTaskId(taskId));
		} catch (Exception e) {
			log.severe("Unable to getActivityWorkflowInfo with taskId: " + taskId + 
					" execption: " + e);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}

	public ActivityWorkflowInfo assignTask(String taskId, String userId) {
		
		// FIXME: Check if userId is a candidate for this task or if userId is part of a candidate group

		if(userId != null) {
			try {	
				engine.getTaskService().claim(taskId, userId);
			} catch (Exception e) {
			}
		}
		
		return getActivityWorkflowInfo(taskId);
	}
	
	public ActivityWorkflowInfo unassignTask(String taskId) {
		try {
			engine.getTaskService().setAssignee(taskId, null);
		} catch (Exception e) {
			log.severe("Unable to unassignTask with taskId: " + taskId + 
					" execption: " + e);
		}
		
		return getActivityWorkflowInfo(taskId);
	}
	

	public ActivityWorkflowInfo addCandidate(String taskId, String userId) {
		try {
			engine.getTaskService().addCandidateUser(taskId, userId);
		} catch (Exception e) {
			log.severe("Unable to addCandidate with taskId: " + taskId +
					" and userId: " + userId + 
					" execption: " + e);
		}
		
		return getActivityWorkflowInfo(taskId);
	}
	
	public ActivityWorkflowInfo removeCandidate(String taskId, String userId) {
		try {
			engine.getTaskService().deleteCandidateUser(taskId, userId);
		} catch (Exception e) {
			log.severe("Unable to removeCandidate with taskId: " + taskId +
					" and userId: " + userId + 
					" execption: " + e);
		}
		
		return getActivityWorkflowInfo(taskId);
	}

	public ActivityWorkflowInfo setPriority(String taskId, int priority) {
		try {
			engine.getTaskService().setPriority(taskId, priority);
		} catch (Exception e) {
			log.severe("Unable to setPriority with taskId: " + taskId +
					" and priority: " + priority + 
					" execption: " + e);
		}
		
		return getActivityWorkflowInfo(taskId);
	}

	public FormInstance submitForm(String formInstanceId, String userId, DocBoxFormData docBoxFormData) {
		FormInstance result = null;
		Task task = engine.getTaskService().createTaskQuery().taskVariableValueEquals(FormEngine.FORM_INSTANCEID, formInstanceId).singleResult();
		
		if (task != null) {
			
			Map<String, Object> variables = new HashMap<String, Object>();
			if (docBoxFormData != null) {
				
				if (docBoxFormData.getDocboxRef()!= null && docBoxFormData.getDocboxRef().trim().length()>0) {
					String taskDocRefVarName = DelegateUtil.calcTaskVariableName(FormEngine.FORM_DOCBOXREF, task.getId());
					variables.put(taskDocRefVarName, docBoxFormData.getDocboxRef());
				}
				if (docBoxFormData.getDocUri() != null && docBoxFormData.getDocUri().trim().length()>0) {
					String taskDocActVarName = DelegateUtil.calcTaskVariableName(FormEngine.FORM_ACT_URI, task.getId());
					variables.put(taskDocActVarName, docBoxFormData.getDocUri());
				}
			}	
			if (executeTask(task.getId(), variables, userId)) {
				HistoricTaskInstance historicTask = getEngine().getHistoryService().createHistoricTaskInstanceQuery().taskId(task.getId()).includeTaskLocalVariables().singleResult();
				
				ActivityInstanceLogItem initialInstance = new ActivityInstanceLogItem();	
				result=formEngine.getHistoricFormInstance(historicTask, userId, initialInstance);
			}
		}
		return result;
	}
	
	public String startProcess(String processDefinitionId, Map<String,Object> variables, String userId) {
		String processInstanceId = null;
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			ProcessInstance processInstance = engine.getRuntimeService().startProcessInstanceById(processDefinitionId, variables);
			processInstanceId = processInstance.getId();
		} catch (Exception e) {
			log.severe("Unable to start process instance with processDefinitionId: " + processDefinitionId + 
					" exeception: " + e);
		} finally {
			engine.getIdentityService().setAuthenticatedUserId(null);
	    }
		
		return processInstanceId;
	}

	public boolean executeTask(String taskId, Map<String,Object> variables, String userId) {
		boolean successful = false;
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			engine.getTaskService().claim(taskId, userId);
			engine.getTaskService().complete(taskId, variables);
			successful = true;
		} catch (ActivitiTaskAlreadyClaimedException e) {
			log.fine("Could not claim in executeTask with taskId: " + taskId + 
				" exception: " + e);
			successful = false;
		}  catch (ActivitiObjectNotFoundException e) {
			log.fine("Could not claim task in executeTask with taskId: " + taskId + 
					" exception: " + e);
			successful = false;	
		} catch (Exception e) {
			log.severe("Could not executeTask with taskId: " + taskId + 
				" exception: " + e);
			successful = false;
		} finally {
			engine.getIdentityService().setAuthenticatedUserId(null);
	    }

		return successful;
	}

	
	public PagedProcessInstanceSearchResult getProcessInstancesWithInvolvedUser(
			String involvedUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String filter, Locale locale, String userId) {
		
		if(filter != null) {	
			if(filter.equals("STARTED")) {
				return(getPagedProcessInstanceSearchResult("", involvedUserId, fromIndex, pageSize,
						sortBy, sortOrder, locale, userId));
			}
			else if (filter.equals("FINISHED")){
				return(getHistoricPagedProcessInstanceSearchResult("", involvedUserId, fromIndex, pageSize,
						sortBy, sortOrder, locale, userId));
			}	
		} 	
		
		return(null);
	}
	
	
	public PagedProcessInstanceSearchResult getProcessInstancesStartedBy(
			String startedByUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String filter, Locale locale, String userId) {
		
		if(filter != null) {	
			if(filter.equals("STARTED")) {
				return(getPagedProcessInstanceSearchResult(IdentityLinkType.STARTER, startedByUserId, fromIndex, pageSize,
						sortBy, sortOrder, locale, userId));
			}
			else if (filter.equals("FINISHED")){
				return(getHistoricPagedProcessInstanceSearchResult(IdentityLinkType.STARTER, startedByUserId, fromIndex, pageSize,
						sortBy, sortOrder, locale, userId));
			}	
		} 	
		
		return(null);
	}
		
	private PagedProcessInstanceSearchResult getPagedProcessInstanceSearchResult(String userSearchCriteria,
			String searchForUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, Locale locale, String userId) {
		List<ProcessInstance> processInstances = null;
		PagedProcessInstanceSearchResult pagedProcessInstanceSearchResult = new PagedProcessInstanceSearchResult();
		
		if(fromIndex < 0) {
			fromIndex = 0;
		}
		
		if(pageSize < 0) {
			pageSize = 0;
		}
		
		pagedProcessInstanceSearchResult.setFromIndex(fromIndex);
		pagedProcessInstanceSearchResult.setPageSize(pageSize);
		pagedProcessInstanceSearchResult.setSortBy(sortBy);
		pagedProcessInstanceSearchResult.setSortOrder(sortOrder);
		
		if(searchForUserId == null) {
			return(pagedProcessInstanceSearchResult);
		}
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			
			List<ProcessInstance> processInstancesWithUserInvolved = engine.getRuntimeService().
				createProcessInstanceQuery().involvedUser(searchForUserId).orderByProcessInstanceId().asc().list();
			
			if(processInstancesWithUserInvolved != null && processInstancesWithUserInvolved.size() > 0) {
				
				if(userSearchCriteria != null && userSearchCriteria.equals(IdentityLinkType.STARTER)) {
					processInstances = new ArrayList<ProcessInstance>();
					
					for(ProcessInstance pIWithUserInvolved : processInstancesWithUserInvolved) {
						String starterUserId = getStarterByProcessInstanceId(pIWithUserInvolved.getProcessInstanceId());
						
						if(starterUserId.equals(searchForUserId)) {
							processInstances.add(pIWithUserInvolved);
						}
					}
				} else {
					processInstances = processInstancesWithUserInvolved;
				}
			}
			
			if(processInstances != null) {
				
				// Filter due to fromIndex and pageSize
				// bjmo made a quick fix here, but the number of pages will be wrong because it is based on a array with subprocesses included.
				List<ProcessInstance> pageProcessInstances = pageList(processInstances, fromIndex, pageSize);
				
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
			
				HashMap<String, Date> startDates = getProcessInstanceStartDates(processInstances);
				
				for(ProcessInstance processInstance : pageProcessInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
					
					processInstanceListItem.setProcessInstanceUuid(processInstance.getProcessInstanceId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_PENDING);
					processInstanceListItem.setStartDate(startDates.get(processInstance.getId()));
					processInstanceListItem.setStartedBy(getStarterByProcessInstanceId(processInstance.getProcessInstanceId()));
					processInstanceListItem.setEndDate(null);
					processInstanceListItem.setProcessInstanceLabel("");
					processInstanceListItem.setProcessLabel(coordinatriceFacade.getStartFormLabel(processInstanceListItem.getProcessInstanceUuid(), locale));
					processInstanceListItem.setActivities(getUserInboxByProcessInstanceId
						(processInstance.getProcessInstanceId(), locale));
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
			
		} catch (Exception e) {
			log.severe("Unable to getPagedProcessInstanceSearchResult with startedByUserId: " + searchForUserId +
					" by userId: " + userId + 
					" exeception: " + e);
			pagedProcessInstanceSearchResult = null;	
		} finally {
			engine.getIdentityService().setAuthenticatedUserId(null);
	    }
		
		return pagedProcessInstanceSearchResult;
	}
	
	private HashMap<String, Date> getProcessInstanceStartDates(List<ProcessInstance> processInstances) {
		Set<String> processInstanceIds = new HashSet<String>();
		HashMap<String, Date> startDates = new HashMap<String, Date>();
		
		if(processInstances == null) {
			return(startDates);
		}
		
		for(ProcessInstance pI : processInstances) {
			if(pI != null) {
				processInstanceIds.add(pI.getProcessInstanceId());
			}
		}
		
		List<HistoricProcessInstance> historicProcessInstances = engine.getHistoryService().
				createHistoricProcessInstanceQuery().processInstanceIds(processInstanceIds).list();

		if(historicProcessInstances != null) {
			for(HistoricProcessInstance hPI : historicProcessInstances) {
				if(hPI != null) {
					startDates.put(hPI.getId(), hPI.getStartTime());
				}	
			}
		}
		
		return(startDates);
	}
	
	private Date getProcessInstanceStartDateByProcessInstanceId(String processInstanceId) {
		Date startDate = null;
		
		if(processInstanceId == null) {
			return(startDate);
		}
	
		HistoricProcessInstance historicProcessInstances = engine.getHistoryService().
				createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

		if(historicProcessInstances != null) {
			startDate = historicProcessInstances.getStartTime();
		}
		
		return(startDate);
	}
	
	private PagedProcessInstanceSearchResult getHistoricPagedProcessInstanceSearchResult(String userSearchCriteria,
			String searchForUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, Locale locale, String userId) {
		List<HistoricProcessInstance> processInstances = null;
		PagedProcessInstanceSearchResult pagedProcessInstanceSearchResult = new PagedProcessInstanceSearchResult();
		
		if(fromIndex < 0) {
			fromIndex = 0;
		}
		
		if(pageSize < 0) {
			pageSize = 0;
		}
		
		pagedProcessInstanceSearchResult.setFromIndex(fromIndex);
		pagedProcessInstanceSearchResult.setPageSize(pageSize);
		pagedProcessInstanceSearchResult.setSortBy(sortBy);
		pagedProcessInstanceSearchResult.setSortOrder(sortOrder);
		
		if(searchForUserId == null) {
			return(pagedProcessInstanceSearchResult);
		}
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			
			List<HistoricProcessInstance> processInstancesWithUserInvolved = engine.getHistoryService().
					createHistoricProcessInstanceQuery().involvedUser(searchForUserId).finished().
						orderByProcessInstanceId().asc().list();
		
			if(processInstancesWithUserInvolved != null && processInstancesWithUserInvolved.size() > 0) {
				
				if(userSearchCriteria != null && userSearchCriteria.equals(IdentityLinkType.STARTER)) {
					processInstances = new ArrayList<HistoricProcessInstance>();
					
					for(HistoricProcessInstance pIWithUserInvolved : processInstancesWithUserInvolved) {
						if(searchForUserId != null && searchForUserId.equals(pIWithUserInvolved.getStartUserId())) {
							processInstances.add(pIWithUserInvolved);
						}
					}
				} else {
					processInstances = processInstancesWithUserInvolved;
				}	
			}
			
			if(processInstances != null) {
				
				// Filter due to fromIndex and pageSize	
				// bjmo made a quick fix here, but the number of pages will be wrong because it is based on a array with subprocesses included.
				List<HistoricProcessInstance> pageProcessInstances = pageHistoricList(processInstances, fromIndex, pageSize);
				
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
		
				for(HistoricProcessInstance processInstance : pageProcessInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
				
					processInstanceListItem.setProcessInstanceUuid(processInstance.getId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_FINISHED);
					processInstanceListItem.setStartDate(processInstance.getStartTime());
					processInstanceListItem.setStartedBy(processInstance.getStartUserId());
					processInstanceListItem.setEndDate(processInstance.getEndTime());
					processInstanceListItem.setProcessInstanceLabel("");
					processInstanceListItem.setProcessLabel(coordinatriceFacade.getStartFormLabel(processInstance.getId(), locale));
					processInstanceListItem.setActivities
						(getHistoricUserInboxByProcessInstanceId(processInstance.getId(), locale));
					
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
		} catch (Exception e) {
			log.severe("Unable to getHistoricPagedProcessInstanceSearchResult with searchForUserId: " + searchForUserId +
					" by userId: " + userId + 
					" exeception: " + e);
			pagedProcessInstanceSearchResult = null;	
		} finally {
			engine.getIdentityService().setAuthenticatedUserId(null);
	    }
		
		return pagedProcessInstanceSearchResult;
	}
		
	public PagedProcessInstanceSearchResult getProcessInstancesByUuids(
			List<String> processInstanceIds, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, Locale locale, String userId) {
		
		if(filter != null) {	
			if(filter.equals("STARTED")) {
				return(getPagedProcessInstanceSearchResultByUuids(processInstanceIds, fromIndex, pageSize,
						sortBy, sortOrder, locale, userId));
			}
			else if (filter.equals("FINISHED")){
				return(getHistoricPagedProcessInstanceSearchResultByUuids(processInstanceIds, fromIndex, pageSize,
						sortBy, sortOrder, locale, userId));
			}	
		} 	
		
		return(null);
	}
	
	private PagedProcessInstanceSearchResult getPagedProcessInstanceSearchResultByUuids(List<String> processInstanceIdList,
			int fromIndex, int pageSize, String sortBy, String sortOrder, Locale locale, String userId) {
		List<ProcessInstance> processInstances = null;
		PagedProcessInstanceSearchResult pagedProcessInstanceSearchResult = new PagedProcessInstanceSearchResult();
			
		if(fromIndex < 0) {
			fromIndex = 0;
		}
		
		if(pageSize < 0) {
			pageSize = 0;
		}
		
		pagedProcessInstanceSearchResult.setFromIndex(fromIndex);
		pagedProcessInstanceSearchResult.setPageSize(pageSize);
		pagedProcessInstanceSearchResult.setSortBy(sortBy);
		pagedProcessInstanceSearchResult.setSortOrder(sortOrder);
		
		if(processInstanceIdList != null && processInstanceIdList.size() == 0) {
			return(pagedProcessInstanceSearchResult);
		}
		
		// Convert list to set
		
		Set<String> processInstanceIds = new HashSet<String>();
		for(String pIId : processInstanceIdList) {
			processInstanceIds.add(pIId);
		}
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			
			processInstances = engine.getRuntimeService().createProcessInstanceQuery().
				processInstanceIds(processInstanceIds).orderByProcessInstanceId().asc().list();
			
			if(processInstances != null) {
				
				// Filter due to fromIndex and pageSize	
				// bjmo made a quick fix here, but the number of pages will be wrong because it is based on a array with subprocesses included.
				List<ProcessInstance> pageProcessInstances = pageList(processInstances, fromIndex, pageSize);
				
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
			
				HashMap<String, Date> startDates = getProcessInstanceStartDates(processInstances);
				
				for(ProcessInstance processInstance : pageProcessInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
					
					processInstanceListItem.setProcessInstanceUuid(processInstance.getProcessInstanceId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_PENDING);
					processInstanceListItem.setStartDate(startDates.get(processInstance.getId()));
					processInstanceListItem.setStartedBy(getStarterByProcessInstanceId(processInstance.getProcessInstanceId()));
					processInstanceListItem.setEndDate(null);
					processInstanceListItem.setProcessInstanceLabel("");
					processInstanceListItem.setProcessLabel(coordinatriceFacade.getStartFormLabel(processInstanceListItem.getProcessInstanceUuid(), locale));
					processInstanceListItem.setActivities(getUserInboxByProcessInstanceId
						(processInstance.getProcessInstanceId(), locale));
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
			
		} catch (Exception e) {
			log.severe("Unable to getPagedProcessInstanceSearchResultByUuids with processInstanceIdList: " + processInstanceIdList.toString() +
					" by userId: " + userId  + 
					" exeception: " + e);
			pagedProcessInstanceSearchResult = null;	
		} finally {
			engine.getIdentityService().setAuthenticatedUserId(null);
	    }
		
		return pagedProcessInstanceSearchResult;
	}
	
	private PagedProcessInstanceSearchResult getHistoricPagedProcessInstanceSearchResultByUuids(List<String> processInstanceIdList,
			int fromIndex, int pageSize, String sortBy, String sortOrder, Locale locale, String userId) {
		List<HistoricProcessInstance> processInstances = null;
		PagedProcessInstanceSearchResult pagedProcessInstanceSearchResult = new PagedProcessInstanceSearchResult();
		
		if(fromIndex < 0) {
			fromIndex = 0;
		}
		
		if(pageSize < 0) {
			pageSize = 0;
		}
		
		pagedProcessInstanceSearchResult.setFromIndex(fromIndex);
		pagedProcessInstanceSearchResult.setPageSize(pageSize);
		pagedProcessInstanceSearchResult.setSortBy(sortBy);
		pagedProcessInstanceSearchResult.setSortOrder(sortOrder);
		
		if(processInstanceIdList != null && processInstanceIdList.size() == 0) {
			return(pagedProcessInstanceSearchResult);
		}
		
		// Convert list to set
		
		Set<String> processInstanceIds = new HashSet<String>();
		for(String pIId : processInstanceIdList) {
			processInstanceIds.add(pIId);
		}
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
	
			processInstances = engine.getHistoryService().
				createHistoricProcessInstanceQuery().processInstanceIds(processInstanceIds).finished().
					orderByProcessInstanceId().asc().list();
			
			if(processInstances != null) {
				
				// Filter due to fromIndex and pageSize	
				// bjmo made a quick fix here, but the number of pages will be wrong because it is based on a array with subprocesses included.
				List<HistoricProcessInstance> pageProcessInstances = pageHistoricList(processInstances, fromIndex, pageSize);
				
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
		
				for(HistoricProcessInstance processInstance : pageProcessInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
				
					processInstanceListItem.setProcessInstanceUuid(processInstance.getId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_FINISHED);
					processInstanceListItem.setStartDate(processInstance.getStartTime());
					processInstanceListItem.setStartedBy(processInstance.getStartUserId());
					processInstanceListItem.setEndDate(processInstance.getEndTime());
					processInstanceListItem.setProcessInstanceLabel("");
					processInstanceListItem.setProcessLabel(coordinatriceFacade.getStartFormLabel(processInstance.getId(), locale));
					processInstanceListItem.setActivities(getHistoricUserInboxByProcessInstanceId(processInstance.getId(), locale));
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
		} catch (Exception e) {
			log.severe("Unable to getHistoricPagedProcessInstanceSearchResultByUuids  processInstanceIdList: " + 
					processInstanceIdList.toString() + " by userId: " + userId  + 
					" exeception: " + e);
			pagedProcessInstanceSearchResult = null;	
		} finally {
			engine.getIdentityService().setAuthenticatedUserId(null);
	    }
		return pagedProcessInstanceSearchResult;
	}


	public boolean createUser(String userId) {
		boolean successful = false;
		
		try {
			User user = engine.getIdentityService().newUser(userId);
			engine.getIdentityService().saveUser(user);
			successful = true;
		} catch (Exception e) {
			log.severe("Could not createUser with userId: " + userId +  " exception: " + e);
		}
		
		return successful;
	}

	public Set<ProcessDefinitionInfo> getProcessDefinitions() {
		HashSet<ProcessDefinitionInfo> processDefinitions = new HashSet<ProcessDefinitionInfo>();
		
		try {
			 List<ProcessDefinition> processDefinitionList = engine.getRepositoryService().
				createProcessDefinitionQuery().orderByProcessDefinitionName().asc().list();
			
			 for(ProcessDefinition pDItem : processDefinitionList) {
				 processDefinitions.add(new ProcessDefinitionInfo(pDItem.getId(), pDItem.getName(), pDItem.getName()));
			 }
		} catch (Exception e) {
			log.severe("Unable to getProcessDefinitions" + 
					" exeception: " + e);
			processDefinitions = null;
		}
		
		return processDefinitions;
	}

	public ProcessDefinitionDetails getProcessDefinitionDetailsByUuid(
			String processDefinitionId) {
		
		ProcessDefinitionDetails processDefinitionDetails = null;
		
		try {
			 ProcessDefinition processDefinition = 
				engine.getRepositoryService().getProcessDefinition(processDefinitionId);
			
			 if(processDefinition != null) {
				 processDefinitionDetails = new ProcessDefinitionDetails();
				 
				 // Handle ProcessDefinitionInfo
				
				 ProcessDefinitionInfo pDInfo = 
					new ProcessDefinitionInfo(processDefinition.getId(), processDefinition.getName(), processDefinition.getName());
				 
				 processDefinitionDetails.setProcess(pDInfo);
				 
				 // Handle ActivityDefinitionInfo's
				 
				 Set<ActivityDefinitionInfo> activityDefinitionInfos = 
					new HashSet<ActivityDefinitionInfo>();
				 
				 BpmnModel bpmnModel = engine.getRepositoryService().getBpmnModel(processDefinitionId);
				
				 if(bpmnModel != null) {
					 // Note: bpmnModel.getProcess( ... ) seems to work bad due to a pools issue in activiti
					 List<org.activiti.bpmn.model.Process> processes = bpmnModel.getProcesses();
					 
					 if(processes != null && processes.size() > 0) {
						 
						 for(Process proc : processes) {
							 
							 if(proc != null) {
								 
								 if(proc.getId().equals(processDefinition.getKey())) {
								 
									 for(FlowElement fE : proc.getFlowElements()) {
										
										 if(fE.getClass().getName().contains("UserTask")) {
											 ActivityDefinitionInfo aDInfo = new ActivityDefinitionInfo();
											 aDInfo.setUuid(fE.getId());
											 aDInfo.setName(fE.getName());
											 aDInfo.setLabel(fE.getName());
											 aDInfo.setFormPath("");
											 
											 activityDefinitionInfos.add(aDInfo);
										 }
									 }
								 }
							 }
						 } 
					 }
				 }
				 
				 processDefinitionDetails.setActivities(activityDefinitionInfos);
			 }
		} catch (Exception e) {
			log.severe("Unable to getProcessDefinitionDetailsByUuid with processDefinitionId: " +
					processDefinitionId + " execption: " + e);
			processDefinitionDetails = null;
		}
		
		return processDefinitionDetails;
	}

	
	public String getParentProcessInstanceUuid(String processInstanceId) {
		String parentId = getParentProcessInstanceIdByProcessInstanceId(processInstanceId);
		
		if(parentId == null) {
			parentId = getHistoricParentProcessInstanceIdByProcessInstanceId(processInstanceId);
		}

		return parentId;
	}
	
	private String getParentProcessInstanceIdByProcessInstanceId(String processInstanceId) {
		String parentProcessInstanceId = null;
		
		try {
			ProcessInstance processInstance = engine.getRuntimeService().createProcessInstanceQuery().
					subProcessInstanceId(processInstanceId).singleResult();

			if(processInstance != null) {
				parentProcessInstanceId = processInstance.getProcessInstanceId();
			}
		} catch (Exception e) {
			log.severe("Unable to getParentProcessInstanceIdByProcessInstanceId with processInstanceId: " +
				processInstanceId  + 
				" exeception: " + e);
		}
		
		return parentProcessInstanceId;
	}
	
	private List<String> getParentProcessInstanceIdListByProcessInstanceId(String processInstanceId) {
		List<String> parentProcessInstanceIdList = new ArrayList<String>();
		String parentProcessInstanceId = "";
		
		while(parentProcessInstanceId != null) {
			parentProcessInstanceId = getParentProcessInstanceIdByProcessInstanceId(processInstanceId);
			
			if(parentProcessInstanceId != null) {
				parentProcessInstanceIdList.add(parentProcessInstanceId);
				processInstanceId = parentProcessInstanceId;
			}
		}
		
		return parentProcessInstanceIdList;
	}
	
	private String getHistoricParentProcessInstanceIdByProcessInstanceId(String processInstanceId) {
		String parentProcessInstanceId = null;
		
		try {
			HistoricProcessInstance processInstance = engine.getHistoryService().createHistoricProcessInstanceQuery().
					processInstanceId(processInstanceId).singleResult();

			if(processInstance != null && processInstance.getSuperProcessInstanceId() != null) {
				parentProcessInstanceId = processInstance.getSuperProcessInstanceId();
			}
		} catch (Exception e) {
			log.severe("Unable to getHistoricMainProcessInstanceByProcessInstanceId with processInstanceId: " +
					processInstanceId + 
					" exeception: " + e);
		}
		
		return parentProcessInstanceId;
	}

	public InboxTaskItem getNextInboxTaskItem(String currentProcessInstance, String userId) {
		List<String> parentProcessInstanceList = null;
	
		if (currentProcessInstance == null || userId == null) {
			return null;
		}
		
		try {
			List<InboxTaskItem> inboxTaskItems = getUserInbox(null, userId);
			
			if(inboxTaskItems == null) {
				return null;
			}
			
			// Check if task belongs to a subprocess of currentProcessInstance, 
			// if so return that inboxTaskItem
			for(InboxTaskItem inboxTaskItem : inboxTaskItems) {
				
				parentProcessInstanceList = getParentProcessInstanceIdListByProcessInstanceId
					(inboxTaskItem.getProcessInstanceUuid());
			
				if(parentProcessInstanceList.contains(currentProcessInstance)) {
					return(inboxTaskItem);
				}
			}
			
			// Check if task belongs to currentProcessInstance, 
			// if so return that inboxTaskItem
			for(InboxTaskItem inboxTaskItem : inboxTaskItems) {
				if(inboxTaskItem.equals(currentProcessInstance)) {
					return(inboxTaskItem);
				}
			}
			
			// If any task in the list just return the first			
			if(inboxTaskItems.size() > 0) {
				return(inboxTaskItems.get(0));
			}
			
		} catch (Exception e) {
			log.severe("Unable to getNextInboxTaskItem with currentProcessInstance: " 
					+ currentProcessInstance + " userId: " + userId + " exception: " + e);
		}
		
		// No inboxTaskItems found at all, returning null
		return null;		
	}
	
	public static void main(String[] args) {
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
		ActivitiEngineService activitiEngineService = (ActivitiEngineService)applicationContext.getBean("activitiEngineService");
		
		// run 
		// mvn exec:java
		// in inherit-service/inherit-service-activiti-engine directory
		Deployment deployment;
		
		deployment = activitiEngineService.deployBpmn("../../bpm-processes/kommun/Forenklad_delgivning.bpmn");
		log.severe("Deployment with id: " + deployment.getId() + " (" + deployment.getName() + ")");
		
		deployment = activitiEngineService.deployBpmn("../../bpm-processes/kommun/Hemkompostering.bpmn");
		log.severe("Deployment with id: " + deployment.getId() + " (" + deployment.getName() + ")");
		
		System.exit(0);
	}
	
	private HashSet<CandidateInfo> getCandidatesByTaskId(String taskId) {
		HashSet<CandidateInfo> candidates = new HashSet<CandidateInfo>();
		List<IdentityLink> identityLinks = 
				engine.getTaskService().getIdentityLinksForTask(taskId);
		CandidateInfo candidate = null;
		String userId = null;
		String groupId = null;
		
		if(identityLinks != null) {
			for(IdentityLink iL : identityLinks) {
				if(iL.getType().equals(IdentityLinkType.CANDIDATE)) {

					userId = iL.getUserId();
					
					if(userId != null) {
						candidate = identityService.getUserByUuid(userId);
						if (candidate!=null) {
							candidates.add(candidate);
						}
					}
					
					groupId = iL.getGroupId();
					
					if(groupId != null) {
						candidate = new GroupInfo();
						candidate.setUuid(groupId);
						candidate.setLabel(groupId);
						candidate.setLabelShort(groupId);
						candidates.add(candidate);
					}
				}
			}
		}
	
		return candidates;
	}
	
	private UserInfo userId2UserInfo(String userId) {
		UserInfo userInfo = null;
		
		if(userId == null) {
			userInfo = new UserInfo();
			userInfo.setUuid("");
			userInfo.setLabel("");
			userInfo.setLabelShort("");
		}
		else {
			userInfo = this.identityService.getUserByUuid(userId);
		}
		
		return userInfo;
	}
	
		
	private String getStarterByProcessInstanceId(String processInstanceId) {
		List<HistoricIdentityLink> identityLinks = engine.getHistoryService().getHistoricIdentityLinksForProcessInstance(processInstanceId);		
		if(identityLinks != null) {
			for(HistoricIdentityLink iL : identityLinks) {
				if(iL.getType().equals(IdentityLinkType.STARTER)) {
					return(iL.getUserId());
				}
			}
		}
		
		return "";		
	}
	
	private String getStarterByTaskId(String taskId) {
		List<IdentityLink> identityLinks = 
				engine.getTaskService().getIdentityLinksForTask(taskId);
			
		if(identityLinks != null) {
			for(IdentityLink iL : identityLinks) {
				if(iL.getType().equals(IdentityLinkType.STARTER)) {
					return(iL.getUserId());
				}
			}
		}
		
		return "";
	}
	
	private String getHistoricStarterByTaskId(String taskId) {
		List<HistoricIdentityLink> historicIdentityLinks = 
				engine.getHistoryService().getHistoricIdentityLinksForTask(taskId);
			
		if(historicIdentityLinks != null) {
			for(HistoricIdentityLink iL : historicIdentityLinks) {
				if(iL.getType().equals(IdentityLinkType.STARTER)) {
					return(iL.getUserId());
				}
			}
		}
		
		return "";
	}

    // Methods below is used for testing and developement for the moment

	
	public List<String> getDeployedDeploymentIds() {
		List<Deployment> deployments = engine.getRepositoryService().createDeploymentQuery().list();
		ArrayList<String> deploymentIds = new ArrayList<String>();
		for (Deployment deployment : deployments) {
			deploymentIds.add(deployment.getId());
		}
		return deploymentIds;
	}
	
	public void deleteDeploymentByDeploymentId(String deploymentId, boolean cascade) {
		engine.getRepositoryService().deleteDeployment(deploymentId, cascade);
	}
	
	public Deployment deployBpmn(String bpmnFile) {
		RepositoryService repositoryService = engine.getRepositoryService();
		Deployment deployment = null;
		
		try {
			String resourceName = Paths.get(bpmnFile).getFileName().toString();
			deployment = repositoryService.createDeployment().
				addInputStream(resourceName, new FileInputStream(bpmnFile)).deploy();
			
		} catch (Exception e) {
			log.severe("File '" + bpmnFile + "' not found: " + e.getMessage() + 
					" exeception: " + e);
		}
		return deployment;
	}
	
	public ProcessInstance startProcessInstanceByKey(String key, Map<String, Object> variables) {
		RuntimeService runtimeService = engine.getRuntimeService();
		ProcessInstance processInstance = null;
		
		try {
			processInstance = runtimeService.startProcessInstanceByKey(key, variables);
		} catch (Exception e) {
			log.severe("Unable to start process instance with key: " + key + 
					" exeception: " + e);
		}
		return processInstance;
	}
	
	public ProcessInstance startProcessInstanceByKey(String key, String userId) {
		RuntimeService runtimeService = engine.getRuntimeService();
		ProcessInstance processInstance = null;
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			processInstance = runtimeService.startProcessInstanceByKey(key, new HashMap<String, Object>());
		} catch (Exception e) {
			log.severe("Unable to start process instance with key: " + key + 
					" exeception: " + e);
		} finally {
			engine.getIdentityService().setAuthenticatedUserId(null);
	    }
		
		return processInstance;
	}
	
	private String getProcessDefinitionNameByProcessDefId(String processDefinitionId) {
		String name = null;
		
		try {
			ProcessDefinition processDefinition = engine.getRepositoryService().createProcessDefinitionQuery().
				processDefinitionId(processDefinitionId).singleResult();
			
			if(processDefinition != null) {
				name = processDefinition.getName();
			} else {
				name = "";
			}
		} catch (Exception e) {
			log.severe("Unable to getProcessInstanceByProcessInstanceId with processDefinitionId: " + processDefinitionId + 
					" exeception: " + e);
		}
		
		return name;
	}
	
	private List<ProcessInstance> pageList(List<ProcessInstance> processInstances, int fromIndex, int pageSize) {
		if(processInstances == null) {
			return(null);
		}

		List<ProcessInstance> result = new ArrayList<ProcessInstance>();

		try {
			int index = fromIndex;
			int count = 0;
			int N = processInstances.size();
			while (index<N && count<pageSize) {
				// If exception is thrown for fromIndex element then stop.
				ProcessInstance pi = processInstances.get(index);
				String parentInstanceId = getParentProcessInstanceUuid(pi.getProcessInstanceId());
				if (parentInstanceId==null) {
					// there is not a parent process to this process 
					result.add(pi);
					count++;
				}
				index++;
			}			
		} catch (Exception e) {
			processInstances.clear();
		}
		
		return result;
	}

	
	private List<HistoricProcessInstance> pageHistoricList(List<HistoricProcessInstance> processInstances, int fromIndex, int pageSize) {
		if(processInstances == null) {
			return(null);
		}

		List<HistoricProcessInstance> result = new ArrayList<HistoricProcessInstance>();

		try {
			int index = fromIndex;
			int count = 0;
			int N = processInstances.size();
			while (index<N && count<pageSize) {
				// If exception is thrown for fromIndex element then stop.
				HistoricProcessInstance pi = processInstances.get(index);
				if (pi.getSuperProcessInstanceId()==null) {
					// there is not a super process to this process 
					result.add(pi);
					count++;
				}
				index++;
			}			
		} catch (Exception e) {
			processInstances.clear();
		}
		
		return result;
	}
	
	
	public Tag addTag(String actinstId, Long tagTypeId,
			String value, String userId) {
		Tag tag = null;
		
		Task task = engine.getTaskService().createTaskQuery().taskId(actinstId).singleResult(); 
		if (task != null) {
			String procinstId = task.getProcessInstanceId();
			tag = taskFormDb.addTag(procinstId, actinstId, tagTypeId, value, userId);
		}
		
		return tag;
	}
	
	public DocBoxFormData getDocBoxFormDataToSign(ActivityInstanceItem activity, Locale locale) {
		DocBoxFacade docBox = new DocBoxFacade();
		DocBoxFormData docBoxFormData = null;

		if (activity != null && activity.getActivityInstanceUuid()!=null) {
			String formInstanceIdToSign = null;
			String assetLabelToSign = null;
			String startFormInstanceId = null;
			String signActivityLabel = "activity"; // fall back activity label
			String signProcessLabel = "case"; // fall back case label

			ProcessInstanceDetails piDetails = getProcessInstanceDetailsByActivityInstance(activity.getActivityInstanceUuid(), locale);

			for (TimelineItem item : piDetails.getTimeline().getItems()) {

				if (item instanceof ActivityInstanceLogItem) {
					if (activity.getTypeId()==new Long(3)) {
						ActivityInstanceLogItem logItem = (ActivityInstanceLogItem)item;
						if (logItem.getActivityName() != null && logItem.getActivityName().equals(activity.getDefinitionKey())) {
							// TODO check that docbox pdf/a converter exist...
							formInstanceIdToSign = logItem.getInstanceId();
							assetLabelToSign = logItem.getActivityLabel();
						}
					}
				}

				if (item instanceof StartLogItem) {
					if (activity.getTypeId()==new Long(2)) {
						// sign start form

						// TODO check that docbox pdf/a converter exist...
						StartLogItem startItem = (StartLogItem)item;

						formInstanceIdToSign = startItem.getInstanceId();;
						assetLabelToSign = startItem.getActivityLabel();;
					}
				}
			}


			if (formInstanceIdToSign == null || assetLabelToSign == null) {

				// TODO throw exception and handle in site
			}

			/*
	String docBoxRefVarName = DelegateUtil.calcTaskVariableName(FormEngine.FORM_DOCBOXREF, actinstId);


			Task task = engine.getTaskService().createTaskQuery().taskId(actinstId).includeProcessVariables().includeTaskLocalVariables().singleResult();
			formEngine.getFormInstance(task, userId, initialInstance);


			String docBoxRef = (String)task.getProcessVariables().get(docBoxRefVarName);
			 */

			// TODO diskutera med håkan, hur formInstanceId(docId) och docrefid förhåller sig till varandra och hur de ska lagras
			docBoxFormData = docBox.getDocBoxFormData(formInstanceIdToSign);

			if (docBoxFormData != null) {
				String signText = "Härmed undertecknar jag " + assetLabelToSign + " med dokumentnummer [" + docBoxFormData.getDocNo() + "] och kontrollsumman [" + docBoxFormData.getCheckSum() + "].";

				docBoxFormData.setDocUri(docboxBaseUrl + docBoxFormData.getDocboxRef());
				docBoxFormData.setSignText(signText);
			}
			else {
				// TODO throw exception and handle in site
			}
		}
		return docBoxFormData;
	}
}

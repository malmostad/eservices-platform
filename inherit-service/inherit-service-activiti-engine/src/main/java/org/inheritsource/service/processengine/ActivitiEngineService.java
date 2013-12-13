package org.inheritsource.service.processengine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.security.auth.login.LoginContext;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Pool;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
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
import org.inheritsource.service.common.domain.CommentFeedItem;
import org.inheritsource.service.common.domain.DashOpenActivities;
import org.inheritsource.service.common.domain.InboxTaskItem;
import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.common.domain.ProcessDefinitionDetails;
import org.inheritsource.service.common.domain.ProcessDefinitionInfo;
import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.domain.ProcessInstanceListItem;
import org.inheritsource.service.common.domain.Timeline;
import org.inheritsource.service.common.domain.TimelineItem;
import org.inheritsource.service.common.domain.UserInfo;


public class ActivitiEngineService {

	private static ProcessEngine engine = null; 
	public static final Logger log = Logger.getLogger(ActivitiEngineService.class.getName());
	
	public ActivitiEngineService() {
		initEngine();		
	}
	
	public void close() {
		if(engine != null) {
			engine.close();
		}
	}
	
	private void initEngine() {
		
		if(engine == null) {
			final String ACTIVITI_ENGINE_CONFIG_FILEPATH = "/usr/local/etc/motrice/activiti.cfg.xml";
			ProcessEngineConfiguration engineConfig = loadConfigFromFile(ACTIVITI_ENGINE_CONFIG_FILEPATH);

			if (engineConfig == null) {
				log.severe("The process engine will not be working!");
			}
			else {
				engine =  engineConfig.buildProcessEngine();
			}	
		}
	}
	
	private ProcessEngineConfiguration loadConfigFromFile(String fileName) {
		ProcessEngineConfiguration engineConfig = null;
		try {
			engineConfig = ProcessEngineConfiguration.
				createProcessEngineConfigurationFromInputStream(new FileInputStream(fileName));
		}
		catch (Exception e) {
			log.severe("Could not find config file: " + fileName + e);
		}
		return engineConfig;
	}
	
	public List<InboxTaskItem> getUserInbox(String userId) {
		List<InboxTaskItem> result = new ArrayList<InboxTaskItem>();

		List<Group> groups = engine.getIdentityService().createGroupQuery().groupMember(userId).list();
		List<String> groupsStr = new ArrayList<String>();
		for (Group group : groups) {
			log.severe("Group with id: '" + group.getId()  + "' and name '" + group.getName() + "' added" );
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
		
		//TODO sort tasks by taskCreateTime
		
		result = taskList2InboxTaskItemList(tasks);
		
		return result;
	}
	
	public Set<InboxTaskItem> getUserInboxByProcessInstanceId(String processInstanceId) {
		Set<InboxTaskItem> result = new HashSet<InboxTaskItem>();
		
		List<Task> tasks = engine.getTaskService().createTaskQuery().
			processInstanceId(processInstanceId).orderByTaskCreateTime().asc().list();
		
		List<InboxTaskItem> inboxTaskItemList = taskList2InboxTaskItemList(tasks);
		
		if(inboxTaskItemList != null) {
			for(InboxTaskItem inboxTaskItem : inboxTaskItemList) {
				result.add(inboxTaskItem);
			}
		}
		
		return result;
	}
	

	private List<InboxTaskItem> taskList2InboxTaskItemList(List<Task> tasks) {
		List<InboxTaskItem> result = null;
		if (tasks != null) {
			result = new ArrayList<InboxTaskItem>();
			for (Task task : tasks) {
				result.add(task2InboxTaskItem(task));
			}
	    }
		return result;
	}
	
	private InboxTaskItem task2InboxTaskItem(Task task) {
		InboxTaskItem item = null;
		if (task != null) {
			item = new InboxTaskItem();
			item.setActivityCreated(task.getCreateTime());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityLabel(task.getName()); 
			item.setExpectedEndDate(task.getDueDate());
			item.setExternalUrl(""); // Will be set in TaskFormService
			item.setProcessActivityFormInstanceId(new Long(0)); // Will be set in TaskFormService
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setProcessLabel(getProcessDefinitionNameByProcessDefinitionId(task.getProcessDefinitionId()));
			item.setTaskUuid(task.getId());
			item.setStartedByFormPath(""); // Will be set in TaskFormService
			
		    ProcessInstance pI = getMainProcessInstanceByProcessInstanceId
				(task.getProcessInstanceId());
			
		    if(pI != null) {
		    	item.setRootProcessInstanceUuid(pI.getProcessInstanceId());
				item.setRootProcessDefinitionUuid(pI.getProcessDefinitionId()); 
		    } else {
		    	item.setRootProcessInstanceUuid(task.getProcessInstanceId());
				item.setRootProcessDefinitionUuid(task.getProcessDefinitionId());
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
						processInstanceId(processInstanceId).singleResult();
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
	
	public Set<InboxTaskItem> getHistoricUserInboxByProcessInstanceId(String processInstanceId) {
		Set<InboxTaskItem> result = new HashSet<InboxTaskItem>();
		
		List<HistoricTaskInstance> tasks = engine.getHistoryService().createHistoricTaskInstanceQuery().
			processInstanceId(processInstanceId).finished().orderByHistoricTaskInstanceStartTime().asc().list();
		
		List<InboxTaskItem> inboxTaskItemList = historicTaskList2InboxTaskItemList(tasks);
		
		if(inboxTaskItemList != null) {
			for(InboxTaskItem inboxTaskItem : inboxTaskItemList) {
				result.add(inboxTaskItem);
			}
		}
		
		return result;
	}
	
	private List<InboxTaskItem> historicTaskList2InboxTaskItemList(List<HistoricTaskInstance> tasks) {
		List<InboxTaskItem> result = null;
		if (tasks != null) {
			result = new ArrayList<InboxTaskItem>();
			for (HistoricTaskInstance task : tasks) {
				result.add(historicTask2InboxTaskItem(task));
			}
	    }
		return result;
	}
	
	private InboxTaskItem historicTask2InboxTaskItem(HistoricTaskInstance task) {
		InboxTaskItem item = null;
		if (task != null) {
			item = new InboxTaskItem();
			item.setActivityCreated(task.getStartTime());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityLabel(task.getName());
			item.setExpectedEndDate(task.getDueDate());
			item.setExternalUrl(""); // Will be set in TaskFormService
			item.setProcessActivityFormInstanceId(new Long(0)); // Will be set in TaskFormService
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setProcessLabel(getProcessDefinitionNameByProcessDefinitionId(task.getProcessDefinitionId()));
			item.setStartedByFormPath(""); // Will be set in TaskFormService
			item.setTaskUuid(task.getId());

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
						processInstanceId(processInstanceId).singleResult();
				
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
	
	public ActivityInstanceItem getActivityInstanceItem(String taskId) {
		ActivityInstanceItem result = null;
		
		Task task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		
		if(task != null) {
			result = task2ActivityInstancePendingItem(task);
		} else {
			HistoricTaskInstance historicTask = engine.getHistoryService().
				createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
			if(historicTask != null) {
				result = task2ActivityInstanceLogItem(historicTask);
			}
		}
		
		return result;
	}
	
	private ActivityInstancePendingItem task2ActivityInstancePendingItem(Task task) {
		ActivityInstancePendingItem item = null;
		if (task != null) {
			item = new ActivityInstancePendingItem();
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());
			item.setActivityName(task.getName());
			item.setActivityLabel(task.getName());
			item.setStartDate(task.getCreateTime());
			item.setCurrentState("EXECUTING");
			item.setLastStateUpdate(task.getCreateTime());
			item.setLastStateUpdateByUserId("");
			item.setStartedBy(getStarterByTaskId(task.getId()));
			item.setProcessActivityFormInstanceId(new Long(0));
			item.setFormUrl("");
			item.setFormDocId("");
			item.setActivityType(getActivityTypeByExecutionIdTaskId(task.getExecutionId(), task.getId()));
			item.setPriority(task.getPriority());
			item.setExpectedEndDate(task.getDueDate());
			
			// ActivityInstancePendingItem
			item.setCandidates(getCandidatesByTaskId(task.getId()));
			item.setAssignedUser(userId2UserInfo(task.getAssignee()));
		}
		return item;
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
	
	private ActivityInstanceLogItem task2ActivityInstanceLogItem(HistoricTaskInstance task) {
		ActivityInstanceLogItem item = null;
		if (task != null) {
			item = new ActivityInstanceLogItem();
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());
			item.setActivityName(task.getName());
			item.setActivityLabel(task.getName());
			item.setStartDate(task.getStartTime());
			item.setCurrentState("FINISHED");
			item.setLastStateUpdate(task.getDueDate());
			item.setLastStateUpdateByUserId("");
			item.setStartedBy(getHistoricStarterByTaskId(task.getId()));
			item.setProcessActivityFormInstanceId(new Long(0));
			item.setFormUrl("");
			item.setFormDocId("");
			item.setActivityType(getActivityTypeByExecutionIdTaskId(task.getExecutionId(), task.getId()));
			item.setPriority(task.getPriority());
			item.setExpectedEndDate(task.getDueDate());
			
			// ActivityInstancelogItem
			item.setEndDate(task.getEndTime());
			item.setPerformedByUser(userId2UserInfo(task.getAssignee()));
			item.setViewUrl("");
		}
		return item;
	}
	
	public DashOpenActivities getDashOpenActivitiesByUserId(String userId, int remainingDays) {
		
		if(remainingDays < 0) {
			remainingDays = 0;
		}
		
		DashOpenActivities dashOpenActivities = null;
		
		try {			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date()); // today date.
			Date TODAY = formatter.parse(formatter.format(calendar.getTime()));
			calendar.add(Calendar.DATE, remainingDays); // today date with remaining days added
			Date TODAY_ADDED_WITH_REMAINING_DAYS = formatter.parse(formatter.format(calendar.getTime()));
				
			// onTrack
			// tasks with a dueDate after (TODAY + remainingDays)
			long onTrackLong = engine.getTaskService().createTaskQuery().taskAssignee(userId).dueAfter(TODAY_ADDED_WITH_REMAINING_DAYS).count();
			int onTrack = new Long(onTrackLong).intValue();
			
			// Add tasks without duedate to onTrack
			long withoutDueDateLong = engine.getTaskService().createTaskQuery().taskAssignee(userId).dueDate(null).count();
			int withoutDueDate = new Long(withoutDueDateLong).intValue();
			onTrack = onTrack + withoutDueDate;
	
			// overdue
			// tasks with a duteDate before TODAY.
			long overdueLong = engine.getTaskService().createTaskQuery().taskAssignee(userId).dueBefore(TODAY).count();
			int overdue = new Long(overdueLong).intValue();
			
			// atRisk
			// tasks with a duteDate after TODAY and before TODAY + remaningDays
			long allTasksLong = engine.getTaskService().createTaskQuery().taskAssignee(userId).count();
			int allTasks = new Long(allTasksLong).intValue();
			int atRisk = allTasks - onTrack - overdue;
			
			dashOpenActivities = new DashOpenActivities();
			dashOpenActivities.setOnTrack(onTrack);
			dashOpenActivities.setAtRisk(atRisk);
			dashOpenActivities.setOverdue(overdue);
		
		} catch (Exception e) {
			log.severe("Unable to getDashOpenActivitiesByUserId with userId: " + userId +
					" and remaining days: " + remainingDays);
			dashOpenActivities = null;
		}
		
		return dashOpenActivities;
	}

	
	public ProcessInstanceDetails getProcessInstanceDetails(String processInstanceId) {
		ProcessInstanceDetails processInstanceDetails = null;
		
		try {
			processInstanceDetails = new ProcessInstanceDetails();

			processInstanceDetails.setProcessInstanceLabel("");
			processInstanceDetails.setStartedByFormPath("");
			
			// Check if process is found among the active ones 
			
			//Execution execution = engine.getRuntimeService().createExecutionQuery().
			//	executionId(executionId).singleResult();
			
			ProcessInstance processInstance = engine.getRuntimeService().createProcessInstanceQuery().
				processInstanceId(processInstanceId).singleResult();
			
			if(processInstance != null) {
				processInstanceDetails.setStatus(ProcessInstanceListItem.STATUS_PENDING);
				processInstanceDetails.setStartedBy(getStarterByProcessInstanceId(processInstance.getProcessInstanceId()));
				processInstanceDetails.setStartDate(getProcessInstanceStartDateByProcessInstanceId(processInstance.getProcessInstanceId()));
				processInstanceDetails.setEndDate(null);
				processInstanceDetails.setProcessInstanceUuid(processInstance.getProcessInstanceId());
				
				// Handle pendings
				
				List<Task> tasks = engine.getTaskService().createTaskQuery().
					processInstanceId(processInstance.getProcessInstanceId()).orderByTaskName().asc().list();
							
				if(tasks != null && tasks.size() > 0) {
					List<ActivityInstancePendingItem> activityInstancePendingItems = new ArrayList<ActivityInstancePendingItem>();
					
					for(Task task : tasks) {
						activityInstancePendingItems.add(task2ActivityInstancePendingItem(task));
					}
					
					processInstanceDetails.setPending(activityInstancePendingItems);	
					
					processInstanceDetails.setActivities(new TreeSet<InboxTaskItem>(taskList2InboxTaskItemList(tasks)));
				}	
			} else {
				// Check if process is found among the historic ones 
				
				HistoricProcessInstance historicProcessInstance = engine.getHistoryService().
					createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
				
				if(historicProcessInstance != null) {
					processInstanceDetails.setStatus(ProcessInstanceListItem.STATUS_FINISHED);
					processInstanceDetails.setStartedBy(historicProcessInstance.getStartUserId());
					processInstanceDetails.setStartDate(historicProcessInstance.getStartTime());
					processInstanceDetails.setEndDate(historicProcessInstance.getEndTime());
				} else {
					// No process instances found at all, return null
					return null;
				}
			}
			
			// Handle historic tasks
				
			List<HistoricTaskInstance> historicTasks = engine.getHistoryService().createHistoricTaskInstanceQuery().
				processInstanceId(processInstanceId).finished().orderByHistoricTaskInstanceStartTime().asc().list();
			
			if(historicTasks != null) {
				List<TimelineItem> activityInstanceLogItems = new ArrayList<TimelineItem>();
				
				for (HistoricTaskInstance historicTask : historicTasks) {
					activityInstanceLogItems.add(task2ActivityInstanceLogItem(historicTask));
				}
				
				Timeline timeline = new Timeline();
				timeline.addAndSort(activityInstanceLogItems);
	
				processInstanceDetails.setTimeline(timeline);
			}
		} catch (Exception e) {
			log.severe("Unable to getProcessInstanceDetails with processInstanceId: " + processInstanceId + 
					" execption: " + e);
			processInstanceDetails = null;	
		}
		return processInstanceDetails;
	}

	public ProcessInstanceDetails getProcessInstanceDetailsByActivityInstance(String taskId) {
		ProcessInstanceDetails processInstanceDetails = null;
		
		// Note: Both tasks and historic tasks are represented in the history service. 
		
		try {
			HistoricTaskInstance task = engine.getHistoryService().createHistoricTaskInstanceQuery().taskId(taskId).singleResult();

			if(task != null) {
				processInstanceDetails = getProcessInstanceDetails(task.getProcessInstanceId());
			}
		} catch (Exception e) {
			log.severe("Unable to getProcessInstanceDetailsByActivityInstance with taskId: " + taskId);
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
				Comment addedComment = 
					engine.getTaskService().addComment(taskId, "", comment);

				if(addedComment != null) {
					String msg = addedComment.getFullMessage();
					
					if(msg != null) {
						retVal = 1;
					}
				}
			}
		} catch (Exception e) {
			log.severe("Unable to addComment with taskId: " + taskId + 
				" and userId: " + userId);
			retVal = -1;	
		}
		
		return retVal;
	}
	
	public List<CommentFeedItem> getProcessInstanceCommentFeedByActivity(String taskId) {
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
					processLabel = getProcessDefinitionNameByProcessDefinitionId(task.getProcessDefinitionId());
				} else {
					historicTask = engine.getHistoryService().createHistoricTaskInstanceQuery().
						taskId(taskId).singleResult();
					
					if(historicTask != null) {
						processDefinitionUuid = historicTask.getProcessDefinitionId();
						activityDefinitionUuid = historicTask.getTaskDefinitionKey();
						processInstanceId = historicTask.getProcessInstanceId();
						setActivityLabel = historicTask.getName();
						processLabel = getProcessDefinitionNameByProcessDefinitionId(historicTask.getProcessDefinitionId());
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
			log.severe("Unable to getActivityWorkflowInfo with taskId: " + taskId);
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
			log.severe("Unable to unassignTask with taskId: " + taskId);
		}
		
		return getActivityWorkflowInfo(taskId);
	}
	

	public ActivityWorkflowInfo addCandidate(String taskId, String userId) {
		try {
			engine.getTaskService().addCandidateUser(taskId, userId);
		} catch (Exception e) {
			log.severe("Unable to addCandidate with taskId: " + taskId +
					" and userId: " + userId);
		}
		
		return getActivityWorkflowInfo(taskId);
	}
	
	public ActivityWorkflowInfo removeCandidate(String taskId, String userId) {
		try {
			engine.getTaskService().deleteCandidateUser(taskId, userId);
		} catch (Exception e) {
			log.severe("Unable to removeCandidate with taskId: " + taskId +
					" and userId: " + userId);
		}
		
		return getActivityWorkflowInfo(taskId);
	}

	public ActivityWorkflowInfo setPriority(String taskId, int priority) {
		try {
			engine.getTaskService().setPriority(taskId, priority);
		} catch (Exception e) {
			log.severe("Unable to setPriority with taskId: " + taskId +
					" and priority: " + priority);
		}
		
		return getActivityWorkflowInfo(taskId);
	}

	public String startProcess(String processDefinitionId, String userId) {
		String processInstanceId = null;
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			ProcessInstance processInstance = engine.getRuntimeService().startProcessInstanceById(processDefinitionId);
			processInstanceId = processInstance.getId();
		} catch (Exception e) {
			log.severe("Unable to start process instance with processDefinitionId: " + processDefinitionId);
		}
		
		return processInstanceId;
	}

	public boolean executeTask(String taskId, String userId) {
		boolean successful = false;
			
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
		} catch (Exception e) {
			log.fine("Could not setAuthenticatedUserId in executeTask with taskId: " + taskId + 
				" exception: " + e);
			return successful;
		}
		
		try {
			engine.getTaskService().claim(taskId, userId);
		} catch (Exception e) {
			log.fine("Could not claim in executeTask with taskId: " + taskId + 
				" exception: " + e);
			return successful;
		}
		
		try {
			engine.getTaskService().complete(taskId);
			successful = true;
		} catch (Exception e) {
			log.severe("Could not complete in executeTask with taskId: " + taskId + 
				" exception: " + e);
		}

		return successful;
	}

	
	public PagedProcessInstanceSearchResult getProcessInstancesWithInvolvedUser(
			String involvedUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String filter, String userId) {
		
		if(filter != null) {	
			if(filter.equals("STARTED")) {
				return(getPagedProcessInstanceSearchResult("", involvedUserId, fromIndex, pageSize,
						sortBy, sortOrder, userId));
			}
			else if (filter.equals("FINISHED")){
				return(getHistoricPagedProcessInstanceSearchResult("", involvedUserId, fromIndex, pageSize,
						sortBy, sortOrder, userId));
			}	
		} 	
		
		return(null);
	}
	
	
	public PagedProcessInstanceSearchResult getProcessInstancesStartedBy(
			String startedByUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String filter, String userId) {
		
		if(filter != null) {	
			if(filter.equals("STARTED")) {
				return(getPagedProcessInstanceSearchResult(IdentityLinkType.STARTER, startedByUserId, fromIndex, pageSize,
						sortBy, sortOrder, userId));
			}
			else if (filter.equals("FINISHED")){
				return(getHistoricPagedProcessInstanceSearchResult(IdentityLinkType.STARTER, startedByUserId, fromIndex, pageSize,
						sortBy, sortOrder, userId));
			}	
		} 	
		
		return(null);
	}
		
	private PagedProcessInstanceSearchResult getPagedProcessInstanceSearchResult(String userSearchCriteria,
			String searchForUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String userId) {
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
				processInstances = pageList(processInstances, fromIndex, pageSize);
				
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
			
				HashMap<String, Date> startDates = getProcessInstanceStartDates(processInstances);
				
				for(ProcessInstance processInstance : processInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
					
					processInstanceListItem.setProcessInstanceUuid(processInstance.getProcessInstanceId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_PENDING);
					processInstanceListItem.setStartDate(startDates.get(processInstance.getId()));
					processInstanceListItem.setStartedBy(getStarterByProcessInstanceId(processInstance.getProcessInstanceId()));
					processInstanceListItem.setStartedByFormPath("");
					processInstanceListItem.setEndDate(null);
					processInstanceListItem.setProcessInstanceLabel("");
					processInstanceListItem.setProcessLabel(getProcessDefinitionNameByProcessDefinitionId
						(processInstance.getProcessDefinitionId()));
					processInstanceListItem.setActivities(getUserInboxByProcessInstanceId
						(processInstance.getProcessInstanceId()));
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
			
		} catch (Exception e) {
			log.severe("Unable to getPagedProcessInstanceSearchResult with startedByUserId: " + searchForUserId +
					" by userId: " + userId);
			pagedProcessInstanceSearchResult = null;	
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
			String sortBy, String sortOrder, String userId) {
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
				processInstances = pageHistoricList(processInstances, fromIndex, pageSize);
				
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
		
				for(HistoricProcessInstance processInstance : processInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
				
					processInstanceListItem.setProcessInstanceUuid(processInstance.getId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_FINISHED);
					processInstanceListItem.setStartDate(processInstance.getStartTime());
					processInstanceListItem.setStartedBy(processInstance.getStartUserId());
					processInstanceListItem.setStartedByFormPath("");
					processInstanceListItem.setEndDate(processInstance.getEndTime());
					processInstanceListItem.setProcessInstanceLabel("");
					processInstanceListItem.setProcessLabel(getProcessDefinitionNameByProcessDefinitionId
						(processInstance.getProcessDefinitionId()));
					processInstanceListItem.setActivities
						(getHistoricUserInboxByProcessInstanceId(processInstance.getId()));
					
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
		} catch (Exception e) {
			log.severe("Unable to getHistoricPagedProcessInstanceSearchResult with searchForUserId: " + searchForUserId +
					" by userId: " + userId);
			pagedProcessInstanceSearchResult = null;	
		}
		return pagedProcessInstanceSearchResult;
	}
		
	public PagedProcessInstanceSearchResult getProcessInstancesByUuids(
			List<String> processInstanceIds, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, String userId) {
		
		if(filter != null) {	
			if(filter.equals("STARTED")) {
				return(getPagedProcessInstanceSearchResultByUuids(processInstanceIds, fromIndex, pageSize,
						sortBy, sortOrder, userId));
			}
			else if (filter.equals("FINISHED")){
				return(getHistoricPagedProcessInstanceSearchResultByUuids(processInstanceIds, fromIndex, pageSize,
						sortBy, sortOrder, userId));
			}	
		} 	
		
		return(null);
	}
	
	private PagedProcessInstanceSearchResult getPagedProcessInstanceSearchResultByUuids(List<String> processInstanceIdList,
			int fromIndex, int pageSize, String sortBy, String sortOrder, String userId) {
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
				processInstances = pageList(processInstances, fromIndex, pageSize);
				
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
			
				HashMap<String, Date> startDates = getProcessInstanceStartDates(processInstances);
				
				for(ProcessInstance processInstance : processInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
					
					processInstanceListItem.setProcessInstanceUuid(processInstance.getProcessInstanceId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_PENDING);
					processInstanceListItem.setStartDate(startDates.get(processInstance.getId()));
					processInstanceListItem.setStartedBy(getStarterByProcessInstanceId(processInstance.getProcessInstanceId()));
					processInstanceListItem.setStartedByFormPath("");
					processInstanceListItem.setEndDate(null);
					processInstanceListItem.setProcessInstanceLabel("");
					processInstanceListItem.setProcessLabel(getProcessDefinitionNameByProcessDefinitionId
						(processInstance.getProcessDefinitionId()));
					processInstanceListItem.setActivities(getUserInboxByProcessInstanceId
						(processInstance.getProcessInstanceId()));
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
			
		} catch (Exception e) {
			log.severe("Unable to getPagedProcessInstanceSearchResultByUuids with processInstanceIdList: " + processInstanceIdList.toString() +
					" by userId: " + userId + e);
			pagedProcessInstanceSearchResult = null;	
		}
		return pagedProcessInstanceSearchResult;
	}
	
	private PagedProcessInstanceSearchResult getHistoricPagedProcessInstanceSearchResultByUuids(List<String> processInstanceIdList,
			int fromIndex, int pageSize, String sortBy, String sortOrder, String userId) {
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
				processInstances = pageHistoricList(processInstances, fromIndex, pageSize);
				
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
		
				for(HistoricProcessInstance processInstance : processInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
				
					processInstanceListItem.setProcessInstanceUuid(processInstance.getId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_FINISHED);
					processInstanceListItem.setStartDate(processInstance.getStartTime());
					processInstanceListItem.setStartedBy(processInstance.getStartUserId());
					processInstanceListItem.setStartedByFormPath("");
					processInstanceListItem.setEndDate(processInstance.getEndTime());
					processInstanceListItem.setProcessInstanceLabel("");
					processInstanceListItem.setProcessLabel(getProcessDefinitionNameByProcessDefinitionId
							(processInstance.getProcessDefinitionId()));
					processInstanceListItem.setActivities(getHistoricUserInboxByProcessInstanceId(processInstance.getId()));
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
		} catch (Exception e) {
			log.severe("Unable to getHistoricPagedProcessInstanceSearchResultByUuids  processInstanceIdList: " + 
					processInstanceIdList.toString() + " by userId: " + userId + e);
			pagedProcessInstanceSearchResult = null;	
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
			log.severe("Unable to getProcessDefinitions" + e);
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
				processInstanceId);
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
					processInstanceId);
		}
		
		return parentProcessInstanceId;
	}

	public InboxTaskItem getNextInboxTaskItem(String currentProcessInstance, String userId) {
		List<String> parentProcessInstanceList = null;
		
		if (currentProcessInstance == null || userId == null) {
			return null;
		}
		
		try {
			List<InboxTaskItem> inboxTaskItems = getUserInbox(userId);
			
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
		ActivitiEngineService activitiEngineService = new ActivitiEngineService();

		// run 
		// mvn exec:java
		// in inherit-service/inherit-service-activiti-engine directory
		
		Deployment deployment = activitiEngineService.deployBpmn("../../bpm-processes/Arendeprocess.bpmn20.xml");
		log.severe("Deployment with id: " + deployment.getId() + " (" + deployment.getName() + ")");
		
		//deployment = activitiEngineService.deployBpmn("../../bpm-processes/TestFunctionProcess1.bpmn20.xml");
		//log.severe("Deployment with id: " + deployment.getId() + " (" + deployment.getName() + ")");		
		
		System.exit(0);
	}
	
	private HashSet<UserInfo> getCandidatesByTaskId(String taskId) {
		HashSet<UserInfo> candidates = new HashSet<UserInfo>();
		List<IdentityLink> identityLinks = 
				engine.getTaskService().getIdentityLinksForTask(taskId);
		UserInfo candidate = null;
		String userId = null;
		
		if(identityLinks != null) {
			for(IdentityLink iL : identityLinks) {
				if(iL.getType().equals(IdentityLinkType.CANDIDATE)) {
					userId = iL.getUserId();
					
					if(userId != null) {
						candidate = new UserInfo();
						candidate.setUuid(userId);
						candidate.setLabel(userId);
						candidate.setLabelShort(userId);
						candidates.add(candidate);
					}
				}
			}
		}
	
		return candidates;
	}
	
	private UserInfo userId2UserInfo(String userId) {
		UserInfo userInfo = new UserInfo();
		
		if(userId == null) {
			userId = "";
		}
		
		userInfo.setUuid(userId);
		userInfo.setLabel(userId);
		userInfo.setLabelShort(userId);
		
		return userInfo;
	}
	
		
	private String getStarterByProcessInstanceId(String processInstanceId) {
		List<IdentityLink> identityLinks = engine.getRuntimeService().
			getIdentityLinksForProcessInstance(processInstanceId);
				
		if(identityLinks != null) {
			for(IdentityLink iL : identityLinks) {
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
    
	private void listDeployedProcesses() {
		List<ProcessDefinition> processes = engine.getRepositoryService().createProcessDefinitionQuery().list();
		for (ProcessDefinition process : processes) {
			log.severe("Process: " + process.getId() + ": " + process + "START");
			log.severe("getDeploymentId: " + process.getDeploymentId());
			log.severe("getDescription: " + process.getDescription());
			log.severe("getDiagramResourceName: " + process.getDiagramResourceName());
			log.severe("getId: " + process.getId());
			log.severe("getKey: " + process.getKey());
			log.severe("getName: " + process.getName());
			log.severe("getResourceName: " + process.getResourceName());
			log.severe("version: " + process.getVersion());
			log.severe("END");
		}
	}
	
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
	
	private void logTableSizes() {
		Map<String, Long> counts = engine.getManagementService().getTableCount();
		
		for (String key : counts.keySet()) {
			log.info("Key: " + key + " count: " + counts.get(key));
		}
	}
	
	private ProcessEngineConfiguration loadConfigFromResource(String resource) {
		ProcessEngineConfiguration engineConfig = null;
		try {
			engineConfig = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(resource)
					  .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
					  .setJobExecutorActivate(true);
		}
		catch (Exception e) {
			log.severe("Could not find config resource: " + resource);
		}
		return engineConfig;
	}
	
	public Deployment deployBpmn(String bpmnFile) {
		RepositoryService repositoryService = engine.getRepositoryService();
		Deployment deployment = null;
		
		try {
			String resourceName = Paths.get(bpmnFile).getFileName().toString();
			deployment = repositoryService.createDeployment().
				addInputStream(resourceName, new FileInputStream(bpmnFile)).deploy();
			
		} catch (Exception e) {
			log.severe("File '" + bpmnFile + "' not found: " + e.getMessage());
		}
		return deployment;
	}
	
	public ProcessInstance startProcessInstanceByKey(String key, Map<String, Object> variables) {
		RuntimeService runtimeService = engine.getRuntimeService();
		ProcessInstance processInstance = null;
		
		try {
			processInstance = runtimeService.startProcessInstanceByKey(key, variables);
		} catch (Exception e) {
			log.severe("Unable to start process instance with key: " + key);
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
			log.severe("Unable to start process instance with key: " + key);
		}
		return processInstance;
	}
	
	private String getProcessDefinitionNameByProcessDefinitionId(String processDefinitionId) {
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
			log.severe("Unable to getProcessInstanceByProcessInstanceId with processDefinitionId: " + processDefinitionId);
		}
		
		return name;
	}
	
	private List<ProcessInstance> pageList(List<ProcessInstance> processInstances, int fromIndex, int pageSize) {
		int len;
		int toIndex;
		
		if(processInstances == null) {
			return(null);
		}
		
		try {
			// If exception is thrown for fromIndex element then list is cleared.
			processInstances.get(fromIndex);
			
			len = processInstances.size();
			
			if((fromIndex + pageSize) <= len) {
				toIndex = fromIndex + pageSize;
				processInstances = processInstances.subList(fromIndex, toIndex);
			} else {
				processInstances = processInstances.subList(fromIndex, len);
			}	
		} catch (Exception e) {
			processInstances.clear();
		}
		
		return processInstances;
	}

	private List<HistoricProcessInstance> pageHistoricList(List<HistoricProcessInstance> processInstances, int fromIndex, int pageSize) {
		int len;
		int toIndex;
		
		if(processInstances == null) {
			return(null);
		}
		
		try {
			// If exception is thrown for fromIndex element then list is cleared.
			processInstances.get(fromIndex);
			
			len = processInstances.size();
			
			if((fromIndex + pageSize) <= len) {
				toIndex = fromIndex + pageSize;
				processInstances = processInstances.subList(fromIndex, toIndex);
			} else {
				processInstances = processInstances.subList(fromIndex, len);
			}	
		} catch (Exception e) {
			processInstances.clear();
		}
		
		return processInstances;
	}

}

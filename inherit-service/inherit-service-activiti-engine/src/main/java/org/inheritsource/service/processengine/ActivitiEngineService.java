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
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
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
import org.inheritsource.service.common.domain.UserInfo;


// FIXME: Should be added everywhere a userID is sent to activiti?
// engine.getIdentityService().setAuthenticatedUserId(userId); 

public class ActivitiEngineService {

	private ProcessEngine engine = null; 
	public static final Logger log = Logger.getLogger(ActivitiEngineService.class.getName());
	
	public ActivitiEngineService() {
		initEngine();		
	}
	
	private void initEngine() {
		
		final String ACTIVITI_ENGINE_CONFIG_FILEPATH = "/usr/local/etc/motrice/activiti.cfg.xml";
		ProcessEngineConfiguration engineConfig = loadConfigFromFile(ACTIVITI_ENGINE_CONFIG_FILEPATH);
		
		if (engineConfig == null) {
			log.severe("The process engine will not be working!");
		}
		else {
			engine =  engineConfig.buildProcessEngine();
		}	
	}
	
	private ProcessEngineConfiguration loadConfigFromFile(String fileName) {
		ProcessEngineConfiguration engineConfig = null;
		try {
			engineConfig = ProcessEngineConfiguration.
				createProcessEngineConfigurationFromInputStream(new FileInputStream(fileName));
		}
		catch (Exception e) {
			log.severe("Could not find config file: " + fileName);
		}
		return engineConfig;
	}
	
	public List<InboxTaskItem> getUserInbox(String userId) {
		List<InboxTaskItem> result = new ArrayList<InboxTaskItem>();
		
		List<Task> tasks = engine.getTaskService().createTaskQuery().taskAssignee(userId).
			orderByTaskCreateTime().asc().list();
		
		result = taskList2InboxTaskItemList(tasks);
		
		return result;
	}
	
	private Set<InboxTaskItem> getUserInboxByInvolvedUserInProcess(String involvedUserId, String executionId) {
		Set<InboxTaskItem> result = new HashSet<InboxTaskItem>();
		
		List<Task> tasks = engine.getTaskService().createTaskQuery().taskInvolvedUser(involvedUserId).
				executionId(executionId).orderByTaskCreateTime().asc().list();
		
		List<InboxTaskItem> inboxTaskItemList = taskList2InboxTaskItemList(tasks);
		
		if(inboxTaskItemList != null) {
			for(InboxTaskItem inboxTaskItem : inboxTaskItemList) {
				result.add(inboxTaskItem);
			}
		}
		
		return result;
	}

	private Set<InboxTaskItem> getHistoricUserInboxByInvolvedUserInProcess(String involvedUserId, String executionId) {
		Set<InboxTaskItem> result = new HashSet<InboxTaskItem>();
		
		List<HistoricTaskInstance> tasks = engine.getHistoryService().createHistoricTaskInstanceQuery().
		taskInvolvedUser(involvedUserId).executionId(executionId).orderByHistoricTaskInstanceStartTime().asc().list();
		
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
	
	// FIXME: label fields are not set
	
	private InboxTaskItem task2InboxTaskItem(Task task) {
		InboxTaskItem item = null;
		if (task != null) {
			item = new InboxTaskItem();
			item.setActivityCreated(task.getCreateTime());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityLabel(""); // FIXME
			item.setExpectedEndDate(task.getDueDate());
			item.setExternalUrl(""); // Will be set in TaskFormService
			item.setProcessActivityFormInstanceId(new Long(0)); // Will be set in TaskFormService
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getExecutionId());
			item.setRootProcessDefinitionUuid(task.getProcessDefinitionId()); // TODO fix support for sub processes
			item.setRootProcessInstanceUuid(task.getProcessInstanceId()); // TODO fix support for sub processes
			item.setProcessLabel(""); // FIXME
			item.setStartedByFormPath(""); // Will be set in TaskFormService
			item.setTaskUuid(task.getId());
		}
		return item;
	}
	
	// FIXME: label fields are not set
	
	private InboxTaskItem historicTask2InboxTaskItem(HistoricTaskInstance task) {
		InboxTaskItem item = null;
		if (task != null) {
			item = new InboxTaskItem();
			item.setActivityCreated(task.getStartTime());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityLabel(""); // FIXME
			item.setExpectedEndDate(task.getDueDate());
			item.setExternalUrl(""); // Will be set in TaskFormService
			item.setProcessActivityFormInstanceId(new Long(0)); // Will be set in TaskFormService
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getExecutionId());
			item.setProcessLabel(""); // FIXME
			item.setStartedByFormPath(""); // Will be set in TaskFormService
			item.setTaskUuid(task.getId());
		}
		return item;
	}
	
	// FIXME: Should historic tasks be returned?
	
	public String getActivityInstanceUuid(String executionId, String taskName) {
		String taskId = null;
		Task task = null;
		
		try {
			task = engine.getTaskService().createTaskQuery().executionId(executionId).
					taskName(taskName).singleResult();
		} catch (Exception e) {
			log.severe("Unable to getActivityInstanceUuid with executionId: " + executionId +
					" and taskName: " + taskName);
		}
		
		if(task != null) {
			taskId = task.getId();
		}
		
		return taskId;
	}

	// FIXME: Here is historicTask searched if no current task was found! Wrong?
	
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
	
	// FIXME: label is not set
	// FIXME: current state is not set
	// FIXME: lastStateUpdate is not set
	// FIXME: lastStateUpdateByUserId is not set
	// FIXME: activityType is not set
	
	private ActivityInstancePendingItem task2ActivityInstancePendingItem(Task task) {
		ActivityInstancePendingItem item = null;
		if (task != null) {
			item = new ActivityInstancePendingItem();
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getExecutionId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());
			item.setActivityName(task.getName());
			item.setActivityLabel("");
			item.setStartDate(task.getCreateTime());
			item.setCurrentState("");
			item.setLastStateUpdate(null);
			item.setLastStateUpdateByUserId("");
			item.setStartedBy(getStarterByTaskId(task.getId()));
			item.setProcessActivityFormInstanceId(new Long(0));
			item.setFormUrl("");
			item.setFormDocId("");
			item.setActivityType(0);
			item.setPriority(task.getPriority());
			item.setExpectedEndDate(task.getDueDate());
			
			// ActivityInstancePendingItem
			item.setCandidates(getCandidatesByTaskId(task.getId()));
			
			UserInfo assignedUser = new UserInfo();
			assignedUser.setUuid(task.getAssignee());
			item.setAssignedUser(assignedUser);
		}
		return item;
	}
	
	// FIXME: label is not set
	// FIXME: current state is not set
	// FIXME: lastStateUpdate is not set
	// FIXME: lastStateUpdateByUserId is not set
	// FIXME: activityType is not set
	
	private ActivityInstanceLogItem task2ActivityInstanceLogItem(HistoricTaskInstance task) {
		ActivityInstanceLogItem item = null;
		if (task != null) {
			item = new ActivityInstanceLogItem();
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getExecutionId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());
			item.setActivityName(task.getName());
			item.setActivityLabel("");
			item.setStartDate(task.getStartTime());
			item.setCurrentState("");
			item.setLastStateUpdate(null);
			item.setLastStateUpdateByUserId("");
			item.setStartedBy(getHistoricStarterByTaskId(task.getId()));
			item.setProcessActivityFormInstanceId(new Long(0));
			item.setFormUrl("");
			item.setFormDocId("");
			item.setActivityType(0);
			item.setPriority(task.getPriority());
			item.setExpectedEndDate(task.getDueDate());
			
			// ActivityInstancelogItem
			item.setEndDate(task.getDueDate());
			
			UserInfo performedByUser = new UserInfo();
			performedByUser.setUuid(task.getAssignee());
			item.setPerformedByUser(performedByUser);
			item.setViewUrl("");
		}
		return item;
	}
	
	// FIXME NOTE: If due date for a task is not given it will always be classified as a 'atRisk'. 
	
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

	// FIXME: Several fields are not set correct in the method!
	// FIXME: Should historic ProcessInstanceDetails be returned?
	// FIXME: timeline is not implemented!
	
	public ProcessInstanceDetails getProcessInstanceDetails(String executionId) {
		ProcessInstanceDetails processInstanceDetails = null;
		
		try {
			Execution execution = engine.getRuntimeService().createExecutionQuery().
				executionId(executionId).singleResult();

			processInstanceDetails = new ProcessInstanceDetails();
			
			processInstanceDetails.setProcessInstanceLabel(""); // FIXME
			processInstanceDetails.setStatus(ProcessInstanceListItem.STATUS_PENDING); // FIXME
			
			processInstanceDetails.setStartDate(null);
			processInstanceDetails.setStartedBy(getStarterByProcessInstanceId(execution.getProcessInstanceId()));
			processInstanceDetails.setEndDate(null);
			processInstanceDetails.setProcessInstanceLabel(""); //FIXME
			processInstanceDetails.setStartedByFormPath("");
				
			// Handle pendings
			
			List<Task> tasks = engine.getTaskService().createTaskQuery().
				executionId(execution.getId()).orderByTaskName().asc().list();
						
			if(tasks != null && tasks.size() > 0) {
				List<ActivityInstancePendingItem> activityInstancePendingItems = new ArrayList<ActivityInstancePendingItem>();
				
				for(Task task : tasks) {
					activityInstancePendingItems.add(task2ActivityInstancePendingItem(task));
				}
				
				processInstanceDetails.setPending(activityInstancePendingItems);
				processInstanceDetails.setTimeline(new Timeline()); //  FIXME: Nothing is done here now!
				
				
				// Handle activities as a set of InboxTaskItem
				// FIXME: Feels like overlap with activityInstancePendingItems!
				
				processInstanceDetails.setActivities(new TreeSet<InboxTaskItem>(taskList2InboxTaskItemList(tasks)));
			}	
		} catch (Exception e) {
			log.severe("Unable to getProcessInstanceDetails with executionId: " + executionId);
			processInstanceDetails = null;	
		}
		return processInstanceDetails;
	}

	public ProcessInstanceDetails getProcessInstanceDetailsByActivityInstance(String taskId) {
		ProcessInstanceDetails processInstanceDetails = null;
		
		try {
			Task task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();

			if(task != null) {
				processInstanceDetails = getProcessInstanceDetails(task.getExecutionId());
			}
		} catch (Exception e) {
			log.severe("Unable to getProcessInstanceDetailsByActivityInstance with taskId: " + taskId);
			processInstanceDetails = null;	
		}
		return processInstanceDetails;
	}

	// FIXME: returning number of characters in message. What should be returned?
	
	public int addComment(String taskId, String comment, String userId) {
		int retVal = -1;
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId); // FIXME: should checkPassword be used?
			Task task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
			
			if(task != null) {
				Comment addedComment = 
					engine.getTaskService().addComment(taskId, "", comment);

				if(addedComment != null) {
					String msg = addedComment.getFullMessage();
					
					if(msg != null) {
						retVal = msg.length();
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
	
	// FIXME: Historic comments will not have processDefinitionUuid and activityDefinitionUuid set!
	// Could be fixed.
	// FIXME: Two label fields are set to blank for the moment.

	public List<CommentFeedItem> getProcessInstanceCommentFeedByActivity(String taskId) {
		List<CommentFeedItem> commentFeedItems = new ArrayList<CommentFeedItem>();
		CommentFeedItem cFItem = null;
		UserInfo userInfo = null;
		Task task = null;
		String processDefinitionUuid = null;
		String activityDefinitionUuid = null;
		String executionId = null;
		
		try {
			List<Comment> comments = engine.getTaskService().getTaskComments(taskId);

			if(comments != null && comments.size() > 0) {
				task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
				
				if(task != null) {
					processDefinitionUuid = task.getProcessDefinitionId();
					activityDefinitionUuid = task.getTaskDefinitionKey();
					executionId = task.getExecutionId();
				} else {
					processDefinitionUuid = "";
					activityDefinitionUuid = "";
					executionId = "";
				}
				
				for(Comment comment : comments) {
					
					cFItem = new CommentFeedItem();
					
					cFItem.setProcessDefinitionUuid(processDefinitionUuid);
					cFItem.setProcessInstanceUuid(executionId);
					cFItem.setProcessLabel(""); // FIXME
					cFItem.setActivityDefinitionUuid(activityDefinitionUuid);
					cFItem.setActivityInstanceUuid(comment.getTaskId());
					cFItem.setActivityLabel(""); // FIXME
					cFItem.setTimestamp(comment.getTime());
					cFItem.setMessage(comment.getFullMessage());
					
					userInfo = new UserInfo();
					userInfo.setUuid(comment.getUserId());
					cFItem.setUser(userInfo);
					
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
			UserInfo assignedUser = new UserInfo();
			assignedUser.setUuid(task.getAssignee());
			activityWorkflowInfo.setAssignedUser(assignedUser);
			activityWorkflowInfo.setCandidates(getCandidatesByTaskId(taskId));
		} catch (Exception e) {
			log.severe("Unable to getActivityWorkflowInfo with taskId: " + taskId);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}

	public ActivityWorkflowInfo assignTask(String taskId, String userId) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			Task task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
			task.setAssignee(userId);
			engine.getTaskService().saveTask(task);
			
			activityWorkflowInfo = new ActivityWorkflowInfo();
			activityWorkflowInfo.setPriority(task.getPriority());
			UserInfo assignedUser = new UserInfo();
			assignedUser.setUuid(userId);
			activityWorkflowInfo.setAssignedUser(assignedUser);
			activityWorkflowInfo.setCandidates(getCandidatesByTaskId(taskId));
		} catch (Exception e) {
			log.severe("Unable to assignTask with taskId: " + taskId);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}

	public ActivityWorkflowInfo addCandidate(String taskId, String userId) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			engine.getTaskService().addCandidateUser(taskId, userId);
			activityWorkflowInfo = getActivityWorkflowInfo(taskId);
		} catch (Exception e) {
			log.severe("Unable to addCandidate with taskId: " + taskId +
					" and userId: " + userId);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}
	
	public ActivityWorkflowInfo removeCandidate(String taskId, String userId) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			engine.getTaskService().deleteCandidateUser(taskId, userId);
			activityWorkflowInfo = getActivityWorkflowInfo(taskId);
		} catch (Exception e) {
			log.severe("Unable to removeCandidate with taskId: " + taskId +
					" and userId: " + userId);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}

	public ActivityWorkflowInfo setPriority(String taskId, int priority) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			engine.getTaskService().setPriority(taskId, priority);
			activityWorkflowInfo = getActivityWorkflowInfo(taskId);
		} catch (Exception e) {
			log.severe("Unable to setPriority with taskId: " + taskId +
					" and priority: " + priority);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}

	public String startProcess(String processDefinitionId, String userId) {
		String executionId = null;
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId); // FIXME: should checkPassword be used here?
			ProcessInstance processInstance = engine.getRuntimeService().startProcessInstanceById(processDefinitionId);
			executionId = processInstance.getId();
		} catch (Exception e) {
			log.severe("Unable to start process instance with processDefinitionId: " + processDefinitionId);
		}
		
		return executionId;
	}

	public boolean executeTask(String taskId, String userId) {
		boolean successful = false;
			
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId); // FIXME: should checkPassword be used here?
			// Note: ActivitiTaskAlreadyClaimedException - when the task is already claimed by another user.
			engine.getTaskService().claim(taskId, userId);
			
			// Note: ActivitiException is thrown when this task is DelegationState.PENDING delegation.
			engine.getTaskService().complete(taskId);
			successful = true;
		} catch (Exception e) {
			log.severe("Could not executeTask with taskId: " + taskId + 
				" exception: " + e);
		}

		return successful;
	}

	
	public PagedProcessInstanceSearchResult getProcessInstancesStartedBy(
			String startedByUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String filter, String userId) {
		
		if(filter != null) {	
			if(filter.equals("STARTED")) {
				return(processInstancesStartedBy(startedByUserId, fromIndex, pageSize,
						sortBy, sortOrder, userId));
			}
			else if (filter.equals("FINISHED")){
				return(historicProcessInstancesStartedBy(startedByUserId, fromIndex, pageSize,
						sortBy, sortOrder, userId));
			}	
		} 	
		
		return(null);
	}
	
	
	// FIXME: Shall pageSize for returned objekt be adjusted if answer is shorter than the requested pageSize?
	// FIXME: sortBy is always processInstanceId for the moment.
	// FIXME: sortOrder is always asc for the moment.
	// FIXME: userId is set as setAuthenticatedUserId, but it is maybe not necessary?
	// FIXME: set status is hardcoded to PENDING.
	
	private PagedProcessInstanceSearchResult processInstancesStartedBy(
			String startedByUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String userId) {
		List<ProcessInstance> processInstances = null;
		int len = 0;
		int toIndex = 0;
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
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			
			List<ProcessInstance> processInstancesWithUserInvolved = engine.getRuntimeService().
				createProcessInstanceQuery().involvedUser(startedByUserId).orderByProcessInstanceId().asc().list();
			
			if(processInstancesWithUserInvolved != null && processInstancesWithUserInvolved.size() > 0) {
				processInstances = new ArrayList<ProcessInstance>();
				
				for(ProcessInstance pIWithUserInvolved : processInstancesWithUserInvolved) {
					String starterUserId = getStarterByProcessInstanceId(pIWithUserInvolved.getProcessInstanceId());
					
					if(starterUserId.equals(startedByUserId)) {
						processInstances.add(pIWithUserInvolved);
					}
				}
			}
		
			// Filter due to fromIndex and pageSize
			
			if(processInstances != null) {
				
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
			}
			
			if(processInstances != null) { // FIXME: Refactoring is needed...
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
			
				for(ProcessInstance processInstance : processInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
					
					processInstanceListItem.setProcessInstanceUuid(processInstance.getId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_PENDING);
					processInstanceListItem.setStartDate(null); // FIXME
					processInstanceListItem.setStartedBy(getStarterByProcessInstanceId(processInstance.getProcessInstanceId()));
					processInstanceListItem.setStartedByFormPath("");
					processInstanceListItem.setEndDate(null); // FIXME
					processInstanceListItem.setProcessInstanceLabel(""); // FIXME
					processInstanceListItem.setProcessLabel(""); // FIXME
					processInstanceListItem.setActivities
						(getUserInboxByInvolvedUserInProcess(startedByUserId, processInstance.getProcessInstanceId()));
					// FIXME: Should getUserInboxByInvolvedUserInProcess be used here? should tartedByUserId be used?
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
		} catch (Exception e) {
			log.severe("Unable to getProcessInstancesStartedBy with startedByUserId: " + startedByUserId +
					" by userId: " + userId);
			pagedProcessInstanceSearchResult = null;	
		}
		return pagedProcessInstanceSearchResult;
	}
	
	// FIXME: set status is hardcoded to STATUS_FINISHED.
	// FIXME: Historic identity links are coupled with processInstanceId and not executionid.
	//        Historic id is processInstanceId ? or?
	
	private PagedProcessInstanceSearchResult historicProcessInstancesStartedBy(
			String startedByUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String userId) {
		List<HistoricProcessInstance> processInstances = null;
		int len = 0;
		int toIndex = 0;
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
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			
			List<HistoricProcessInstance> processInstancesWithUserInvolved = engine.getHistoryService().
					createHistoricProcessInstanceQuery().involvedUser(startedByUserId).
						orderByProcessInstanceId().asc().list();
		
			if(processInstancesWithUserInvolved != null && processInstancesWithUserInvolved.size() > 0) {
				processInstances = new ArrayList<HistoricProcessInstance>();
				
				for(HistoricProcessInstance pIWithUserInvolved : processInstancesWithUserInvolved) {
					// FIXME: Better if processInstanceId could be used but maybe impossible for historic data
					String starterUserId = getHistoricStarterByProcessInstanceId(pIWithUserInvolved.getId());
					
					if(starterUserId.equals(startedByUserId)) {
						processInstances.add(pIWithUserInvolved);
					}
				}
			}
			
			// Filter due to fromIndex and pageSize
			
			if(processInstances != null) {
				
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
			}
			
			if(processInstances != null) { // FIXME: Refactoring is needed...
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
			
				for(HistoricProcessInstance processInstance : processInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();

					processInstanceListItem.setProcessInstanceUuid(processInstance.getId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_FINISHED);
					processInstanceListItem.setStartDate(null); // FIXME
					// FIXME: Better if processInstanceId could be used but maybe impossible for historic data
					processInstanceListItem.setStartedBy(getHistoricStarterByProcessInstanceId(processInstance.getId()));
					processInstanceListItem.setStartedByFormPath("");
					processInstanceListItem.setEndDate(null); // FIXME
					processInstanceListItem.setProcessInstanceLabel(""); // FIXME
					processInstanceListItem.setProcessLabel(""); // FIXME
					processInstanceListItem.setActivities
						(getHistoricUserInboxByInvolvedUserInProcess(startedByUserId, processInstance.getId()));
					// FIXME: Should getUserInboxByInvolvedUserInProcess be used here? should tartedByUserId be used?
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
		} catch (Exception e) {
			log.severe("Unable to historicProcessInstancesStartedBy with startedByUserId: " + startedByUserId +
					" by userId: " + userId);
			pagedProcessInstanceSearchResult = null;	
		}
		return pagedProcessInstanceSearchResult;
	}
	
	// FIXME: sortBy is always processInstanceId for the moment.
	// FIXME: sortOrder is always asc for the moment.
    // FIXME: filter is not used for the moment.
    // FIXME: userId is set as setAuthenticatedUserId, but it is maybe not necessary?
	// FIXME: set status is hardcoded to PENDING.
	
	public PagedProcessInstanceSearchResult getProcessInstancesWithInvolvedUser(
			String searchForUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String filter, String userId) {
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
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			
			List<ProcessInstance> processInstances = engine.getRuntimeService().
				createProcessInstanceQuery().involvedUser(searchForUserId).orderByProcessInstanceId().asc().
					listPage(fromIndex, pageSize);
			
			if(processInstances != null) {  // FIXME: Refactoring is needed...
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
			
				for(ProcessInstance processInstance : processInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
					
					processInstanceListItem.setProcessInstanceUuid(processInstance.getId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_PENDING);
					processInstanceListItem.setStartDate(null); // FIXME
					processInstanceListItem.setStartedBy(getStarterByProcessInstanceId(processInstance.getProcessInstanceId()));
					processInstanceListItem.setStartedByFormPath("");
					processInstanceListItem.setEndDate(null); // FIXME
					processInstanceListItem.setProcessInstanceLabel(""); // FIXME
					processInstanceListItem.setProcessLabel(""); // FIXME
					processInstanceListItem.setActivities
						(getUserInboxByInvolvedUserInProcess(searchForUserId, processInstance.getId()));
					// FIXME: Should getUserInboxByInvolvedUserInProcess be used here? should startedByUserId be used?
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
		} catch (Exception e) {
			log.severe("Unable to getProcessInstancesWithInvolvedUser with searchForUserId: " + searchForUserId +
					" by userId: " + userId);
			pagedProcessInstanceSearchResult = null;	
		}
		return pagedProcessInstanceSearchResult;
	}
	
	// FIXME: sortBy is always processInstanceId for the moment.
	// FIXME: sortOrder is always asc for the moment.
	// FIXME: filter is not used for the moment.
	// FIXME: userId is set as setAuthenticatedUserId, but it is maybe not necessary?
	// FIXME: set status is hardcoded to PENDING.
	// FIXME: userId is not used for filtering on UserInbox (setActivities). Wrong?

	public PagedProcessInstanceSearchResult getProcessInstancesByUuids(
			List<String> executionIds, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, String userId) {
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
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId);
			
			// convert to a set of processInstanceIds
			Set<String> processInstanceIds = new HashSet<String>();
			String processInstanceId = null;
			if(executionIds != null) {
				for(String executionId : executionIds) {
					processInstanceId = engine.getRuntimeService().createExecutionQuery().
							executionId(executionId).singleResult().getProcessInstanceId();
					
					processInstanceIds.add(processInstanceId);	
				}
			}	
			
			List<ProcessInstance> processInstances = engine.getRuntimeService().createProcessInstanceQuery().
				processInstanceIds(processInstanceIds).orderByProcessInstanceId().asc().
					listPage(fromIndex, pageSize);
			
			if(processInstances != null) { // FIXME: Refactoring is needed...
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
			
				for(ProcessInstance processInstance : processInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
					
					processInstanceListItem.setProcessInstanceUuid(processInstance.getId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_PENDING);
					processInstanceListItem.setStartDate(null); // FIXME
					processInstanceListItem.setStartedBy(getStarterByProcessInstanceId(processInstance.getProcessInstanceId())); 
					processInstanceListItem.setStartedByFormPath("");
					processInstanceListItem.setEndDate(null); // FIXME
					processInstanceListItem.setProcessInstanceLabel(""); // FIXME
					processInstanceListItem.setProcessLabel(""); // FIXME
					processInstanceListItem.setActivities
						(getUserInboxByProcessInstanceId(processInstance.getId()));
					// FIXME: Should getUserInboxByInvolvedUser be used here? should tartedByUserId be used?
					processInstanceListItems.add(processInstanceListItem);
				}

				pagedProcessInstanceSearchResult.setHits(processInstanceListItems);
			}
		} catch (Exception e) {
			log.severe("Unable to getProcessInstancesByUuids with executionIds: " + executionIds +
					" by userId: " + userId);
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

	public ActivityWorkflowInfo unassignTask(String taskId) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			Task task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
			task.setAssignee(null);
			engine.getTaskService().saveTask(task);
			
			activityWorkflowInfo = new ActivityWorkflowInfo();
			activityWorkflowInfo.setPriority(task.getPriority());
			activityWorkflowInfo.setAssignedUser(null);
			activityWorkflowInfo.setCandidates(getCandidatesByTaskId(taskId));	
		} catch (Exception e) {
			log.severe("Unable to assignTask with taskId: " + taskId);
		}
		
		return activityWorkflowInfo;
	}
	
	// FIXME: Label for ProcessDefinitionInfo is not set! Maybe key can be used?
	
	public Set<ProcessDefinitionInfo> getProcessDefinitions() {
		HashSet<ProcessDefinitionInfo> processDefinitions = new HashSet<ProcessDefinitionInfo>();
		
		try {
			 List<ProcessDefinition> processDefinitionList = engine.getRepositoryService().
				createProcessDefinitionQuery().orderByProcessDefinitionName().asc().list();
			
			 for(ProcessDefinition pDItem : processDefinitionList) {
				 processDefinitions.add(new ProcessDefinitionInfo(pDItem.getId(), pDItem.getName(), ""));
			 }
		} catch (Exception e) {
			log.severe("Unable to getProcessDefinitions" + e);
			processDefinitions = null;
		}
		
		return processDefinitions;
	}
	
	// FIXME: Label for ProcessDefinitionInfo is not set! Maybe key can be used?
	// FIXME: Label for ActivityDefinitionInfo is not set! Maybe description can be used? 

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
					new ProcessDefinitionInfo(processDefinition.getId(), processDefinition.getName(), "");
				 
				 processDefinitionDetails.setProcess(pDInfo);
				 
				 // Handle ActivityDefinitionInfo's
				 
				 Set<ActivityDefinitionInfo> activityDefinitionInfos = 
					new HashSet<ActivityDefinitionInfo>();
				 
				 List<Task> tasks = engine.getTaskService().createTaskQuery().
					processDefinitionId(processDefinition.getId()).orderByProcessInstanceId().asc().list();
				 
				 for(Task task : tasks) {
					 ActivityDefinitionInfo aDInfo = new ActivityDefinitionInfo();
					 aDInfo.setUuid(task.getId());
					 aDInfo.setName(task.getName());
					 aDInfo.setLabel("");
					 aDInfo.setFormPath("");
					 
					 activityDefinitionInfos.add(aDInfo);
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

	public String getParentProcessInstanceUuid(String executionId) {
		
		String parentId = null;

		try {
			parentId = engine.getRuntimeService().createExecutionQuery().
					executionId(executionId).singleResult().getParentId();
		} catch (Exception e) {
			log.severe("Unable to getParentProcessInstanceUuid with exectionId: " + executionId +
					" exception: "+ e);
		}

		return parentId;
	}

	// FIXME: No idea for the moment how to solve this...
	
	public InboxTaskItem getNextInboxTaskItem(String currentProcessInstance, String userId) {
		return null;
	 // TODO		
	}
	
	public static void main(String[] args) {
		ActivitiEngineService activitiEngineService = new ActivitiEngineService();
		
		PagedProcessInstanceSearchResult p = activitiEngineService.
				getProcessInstancesStartedBy("admin", 0, 100, null, null, "FINISHED", "admin");

		log.severe("p:" + p);
		
		
/*
		log.severe(activitiEngineService.getActivityInstanceItem("4204").toString());
		*/
		//activitiEngineService.startProcess("Arendeprocess:1:3904", "ulric");
		/*
		List<String> uuids = new ArrayList<String>();
		
		uuids.add("3905");
		uuids.add("4001");
		uuids.add("4101");
		uuids.add("6501");
		uuids.add("7001");
		
		PagedProcessInstanceSearchResult p = activitiEngineService.
				getProcessInstancesByUuids(uuids, 0, 100, null, null, null, "ulric");

		log.severe("p:" + p);
		*/
		//log.severe("p:" + p.getHits());
		
		/*
		List<CommentFeedItem> comments =  activitiEngineService.getProcessInstanceCommentFeedByActivity("4204");
		
		for (CommentFeedItem c : comments) {
			log.severe("c:" + c);
			log.severe("userid:" + c.getUser().getUuid());
		}
		*/
		/*
		 ProcessDefinitionDetails details = 
				activitiEngineService.getProcessDefinitionDetailsByUuid("Arendeprocess:1:3904");
		
		 log.severe("details" + details);
		 */
		/*
		Set<ProcessDefinitionInfo> pDInfos = activitiEngineService.getProcessDefinitions();
		for(ProcessDefinitionInfo pDInfo : pDInfos) {
			log.severe(" pDInfo: " + pDInfo);
		}
		*/
		//ActivityWorkflowInfo aWI = activitiEngineService.setPriority("4204", 50);
		//log.severe(" assigned User ID: " + aWI.getAssignedUser().getUuid());
		//log.severe(" candidates: " + ((UserInfo)aWI.getCandidates().toArray()[0]).getUuid()     );
		//log.severe(" prio: " + aWI.getPriority()   );
		
		//String taskId = activitiEngineService.getActivityInstanceUuid("4201", "Registrering");
		//log.severe("taskId: " + taskId);
		
		//ActivityWorkflowInfo aWI = activitiEngineService.unassignTask("4204");
		
		//activitiEngineService.engine.getTaskService().addCandidateUser("4204", "KALLE STROPP");
		
		//ActivityWorkflowInfo aWI = activitiEngineService.assignTask("4204", "GRODAN BOLL");
		
		//log.severe(aWI.toString());
		//log.severe(" assigned User ID: " + aWI.getAssignedUser().getUuid());
		//log.severe(" candidates: " + ((UserInfo)aWI.getCandidates().toArray()[0]).getUuid()     );
		
		//activitiEngineService.createUser("nya usern lasse");
		//activitiEngineService.executeTask("4502", "dont care");
		
		/*
		log.severe("Number of process instances Before: " + 
				activitiEngineService.engine.getRuntimeService().createProcessInstanceQuery().count());     
		
		String userId = "kermit";
		String processDefinitionUUIDStr = "Arendeprocess:1:3904";
		
		activitiEngineService.startProcess(processDefinitionUUIDStr, userId);
		
		// Verify that we started a new process instance
		log.severe("Number of process instances After: " + 
				activitiEngineService.engine.getRuntimeService().createProcessInstanceQuery().count());   
*/
		
		
		/*
		// getDeployedDeploymentIds
		List<String> deploymentIds = activitiEngineService.getDeployedDeploymentIds();
		// deleteDeploymentByDeploymentId
		for(String deploymentId : deploymentIds) {
			activitiEngineService.deleteDeploymentByDeploymentId(deploymentId, true);
		}
		
		
		
		// getDeployedDeploymentIds
		//deploymentIds = activitiEngineService.getDeployedDeploymentIds();
		//log.severe("after deploy: " + deploymentIds.size());
		//for(String deploymentId : deploymentIds) {
		//	log.severe("deloymentId: " + deploymentId);
		//}
				
		
		log.severe("Number of process definitions: " + 
				activitiEngineService.engine.getRepositoryService().createProcessDefinitionQuery().count());
			
		Map<String, Object> variables = new HashMap<String, Object>();
		ProcessInstance processInstance = activitiEngineService.startProcessInstanceByKey("Arendeprocess", variables);
		
		// Verify that we started a new process instance
		log.severe("Number of process instances: " + 
			activitiEngineService.engine.getRuntimeService().createProcessInstanceQuery().count());           
		
		// set assignee for the created task with the processinstanceid above.
		
		Task task = activitiEngineService.engine.getTaskService().createTaskQuery().
			processInstanceId(processInstance.getProcessInstanceId()).singleResult();
		task.setAssignee("kermit");
		task.setOwner("kermit");
		activitiEngineService.engine.getTaskService().saveTask(task);
		*/
	
		/*
		ActivityInstanceItem activityInstanceItem = activitiEngineService.getActivityInstanceItem("3908");
		log.severe("activityInstanceItem: " + activityInstanceItem);
		
		//activitiEngineService.listDeployedProcesses();
		  
		  
		List<InboxTaskItem> tasks = activitiEngineService.getUserInbox("kermit");
		log.severe("Inbox item count: " + tasks.size());
		for (InboxTaskItem t : tasks) {
			log.severe("Inbox item: " + t);
		}
		*/
		/*
		Deployment deployment = activitiEngineService.deployBpmn
				("/home/pama/workspace/motrice/pawap/bpm-processes/Arendeprocess.bpmn20.xml");
		
		log.severe("Deployment with id: " + deployment.getId());
		*/
		System.exit(0);
	}
	
	private Set<InboxTaskItem> getUserInboxByProcessInstanceId(String processInstanceId) {
		Set<InboxTaskItem> result = new HashSet<InboxTaskItem>();
		
		List<Task> tasks = engine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).
			orderByTaskCreateTime().asc().list();
		
		List<InboxTaskItem> inboxTaskItemList = taskList2InboxTaskItemList(tasks);
		
		if(inboxTaskItemList != null) {
			for(InboxTaskItem inboxTaskItem : inboxTaskItemList) {
				result.add(inboxTaskItem);
			}
		}
		
		return result;
	}
	
	private HashSet<UserInfo> getCandidatesByTaskId(String taskId) {
		HashSet<UserInfo> candidates = new HashSet<UserInfo>();
		List<IdentityLink> identityLinks = 
				engine.getTaskService().getIdentityLinksForTask(taskId);
		UserInfo candidate = null;
		
		if(identityLinks != null) {
			for(IdentityLink iL : identityLinks) {
				if(iL.getType().equals(IdentityLinkType.CANDIDATE)) {
					candidate = new UserInfo();
					candidate.setUuid(iL.getUserId());
					candidates.add(candidate);
				}
			}
		}
	
		return candidates;
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
	
	private String getHistoricStarterByProcessInstanceId(String processInstanceId) {
		List<HistoricIdentityLink> identityLinks = engine.getHistoryService().
				getHistoricIdentityLinksForProcessInstance(processInstanceId);
				
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
	
	private List<String> getDeployedDeploymentIds() {
		List<Deployment> deployments = engine.getRepositoryService().createDeploymentQuery().list();
		ArrayList<String> deploymentIds = new ArrayList<String>();
		for (Deployment deployment : deployments) {
			deploymentIds.add(deployment.getId());
		}
		return deploymentIds;
	}
	
	private void deleteDeploymentByDeploymentId(String deploymentId, boolean cascade) {
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
	
	private Deployment deployBpmn(String bpmnFile) {
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
	
	private ProcessInstance startProcessInstanceByKey(String key, Map<String, Object> variables) {
		RuntimeService runtimeService = engine.getRuntimeService();
		ProcessInstance processInstance = null;
		
		try {
			processInstance = runtimeService.startProcessInstanceByKey(key, variables);
		} catch (Exception e) {
			log.severe("Unable to start process instance with key: " + key);
		}
		return processInstance;
	}
}

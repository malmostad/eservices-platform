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
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
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

// FIXME: Execution klassen skall diskuteras med de andra hur den kommer in i det här...

public class ActivitiEngineService {

	private ProcessEngine engine = null; 
	public static final Logger log = Logger.getLogger(ActivitiEngineService.class.getName());
	
	public ActivitiEngineService() {
		initEngine();		
	}

	public void logTableSizes() {
		Map<String, Long> counts = engine.getManagementService().getTableCount();
		
		for (String key : counts.keySet()) {
			log.info("Key: " + key + " count: " + counts.get(key));
		}
	}
	
	private void initEngine() {

		ProcessEngineConfiguration engineConfig = null;
		
		// try to find config 
		engineConfig = loadConfigFromFile("/usr/local/etc/motrice/activiti.cfg.xml");
		
		if (engineConfig == null) {
			// fall back to default - activiti.cfg.xml resource in classpath (src/main/resources) 
			engineConfig = loadConfigFromResource("activiti.cfg.xml");
		}
		
		if (engineConfig == null) {
			log.severe("Could not find default config - the process engine will not be working");
		}
		else {
			engine =  engineConfig.buildProcessEngine();
		}
		
	}
	
	
	private ProcessEngineConfiguration loadConfigFromFile(String fileName) {
		ProcessEngineConfiguration engineConfig = null;
		try {
			engineConfig = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(new FileInputStream(fileName))
					  .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
					  .setJobExecutorActivate(true);
		}
		catch (Exception e) {
			log.severe("Could not find config file: " + fileName);
		}
		return engineConfig;
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
		
	public void listDeployedProcesses() {
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
	
	public List<InboxTaskItem> getUserInbox(String userId) {
		List<InboxTaskItem> result = new ArrayList<InboxTaskItem>();
		
		List<Task> tasks = engine.getTaskService().createTaskQuery().taskAssignee(userId).
			orderByTaskCreateTime().asc().list();
		
		result = taskList2InboxTaskItemList(tasks);
		
		return result;
	}
	
	public Set<InboxTaskItem> getUserInboxByInvolvedUser(String involvedUserId, String processInstanceId) {
		Set<InboxTaskItem> result = new HashSet<InboxTaskItem>();
		
		List<Task> tasks = engine.getTaskService().createTaskQuery().taskInvolvedUser(involvedUserId).
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
	
	// FIXME: label is not set
	
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
			item.setProcessLabel("");
			item.setStartedByFormPath(""); // Will be set in TaskFormService
			item.setTaskUuid(task.getId());
		}
		return item;
	}
	
	public String getActivityInstanceUuid(String processInstanceUuid,
			String activityName) {
		String taskId = null;
		Task task = null;
		
		try {
			task = engine.getTaskService().createTaskQuery().
				processInstanceId(processInstanceUuid).taskName(activityName).singleResult();
		} catch (Exception e) {
			log.severe("More than one task with processInstanceUuid: " + processInstanceUuid + 
					" and name: " + activityName + "exists! ... returning taskId = null");
		}
		
		if(task != null) {
			taskId = task.getId();
		}
		
		return taskId;
	}

	public ActivityInstanceItem getActivityInstanceItem(
			String activityInstanceUuid) {
		ActivityInstanceItem result = null;
		
		//activityInstanceUuid is taskId
		Task task = engine.getTaskService().createTaskQuery().taskId(activityInstanceUuid).singleResult();
		
		if(task != null) {
			result = task2ActivityInstancePendingItem(task);
		} else {
			HistoricTaskInstance historicTask = engine.getHistoryService().createHistoricTaskInstanceQuery().taskId(activityInstanceUuid).singleResult();
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
	// FIXME: startedBy might not be correct?
	// FIXME: activityType might not be correct?
	
	private ActivityInstancePendingItem task2ActivityInstancePendingItem(Task task) {
		ActivityInstancePendingItem item = null;
		if (task != null) {
			item = new ActivityInstancePendingItem();
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());
			item.setActivityName(task.getName());
			item.setActivityLabel("");
			item.setStartDate(task.getCreateTime());
			item.setCurrentState("");
			item.setLastStateUpdate(null);
			item.setLastStateUpdateByUserId("");
			item.setStartedBy(task.getOwner());
			item.setProcessActivityFormInstanceId(new Long(0));
			item.setFormUrl("");
			item.setFormDocId("");
			item.setActivityType(99);
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
	// FIXME: startedBy might not be correct?
	// FIXME: activityType might not be correct?
	
	private ActivityInstanceLogItem task2ActivityInstanceLogItem(HistoricTaskInstance task) {
		ActivityInstanceLogItem item = null;
		if (task != null) {
			item = new ActivityInstanceLogItem();
			
			item.setProcessDefinitionUuid(task.getProcessDefinitionId());
			item.setProcessInstanceUuid(task.getProcessInstanceId());
			item.setActivityDefinitionUuid(task.getTaskDefinitionKey());
			item.setActivityInstanceUuid(task.getId());
			item.setActivityName(task.getName());
			item.setActivityLabel("");
			item.setStartDate(task.getStartTime());
			item.setCurrentState("");
			item.setLastStateUpdate(null);
			item.setLastStateUpdateByUserId("");
			item.setStartedBy(task.getOwner());
			item.setProcessActivityFormInstanceId(new Long(0));
			item.setFormUrl("");
			item.setFormDocId("");
			item.setActivityType(99);
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
	
	// FIXME NOTE: If due date is not given it will always be classified as a 'atRisk'. 
	
	public DashOpenActivities getDashOpenActivitiesByUserId(String userId,
			int remainingDays) {
		
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
			log.severe("Unable to getDashOpenActivitiesByUserId with taskId: " + dashOpenActivities);
			dashOpenActivities = null;
		}
		
		return dashOpenActivities;
	}

	// FIXME: Several fields are not set correct in the method!
	
	public ProcessInstanceDetails getProcessInstanceDetails(
			String processInstanceUuid) {
		ProcessInstanceDetails processInstanceDetails = null;
		
		try {
			ProcessInstance processInstance = engine.getRuntimeService().
				createProcessInstanceQuery().processInstanceId(processInstanceUuid).singleResult();

			processInstanceDetails = new ProcessInstanceDetails();
			
			processInstanceDetails.setProcessInstanceLabel(""); // FIXME
			processInstanceDetails.setStatus(ProcessInstanceListItem.STATUS_PENDING); // FIXME
			
			processInstanceDetails.setStartDate(null);
			processInstanceDetails.setStartedBy(""); // FIXME: This could be fetched if it is earlier set with addUserIdentityLink
			processInstanceDetails.setEndDate(null);
			processInstanceDetails.setProcessInstanceLabel(processInstanceUuid);
			processInstanceDetails.setStartedByFormPath("");
				
			// Handle pendings
			
			List<Task> tasks = engine.getTaskService().createTaskQuery().
				processInstanceId(processInstance.getId()).orderByTaskName().asc().list();
						
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
			log.severe("Unable to getProcessInstanceDetails with processInstanceUuid: " + processInstanceUuid);
			processInstanceDetails = null;	
		}
		return processInstanceDetails;
	}

	public ProcessInstanceDetails getProcessInstanceDetailsByActivityInstance(
			String activityInstanceUuid) {
		ProcessInstanceDetails processInstanceDetails = null;
		
		try {
			Task task = engine.getTaskService().createTaskQuery().taskId(activityInstanceUuid).singleResult();

			if(task != null) {
				processInstanceDetails = getProcessInstanceDetails(task.getProcessInstanceId());
			}
		} catch (Exception e) {
			log.severe("Unable to getProcessInstanceDetailsByActivityInstance with activityInstanceUuid: " + activityInstanceUuid);
			processInstanceDetails = null;	
		}
		return processInstanceDetails;
	}

	// FIXME: returning number of characters in message. What should be returned?
	
	public int addComment(String activityInstanceUuid, String comment,
			String userId) {
		int retVal = -1;
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId); // FIXME: should login be used?
			Task task = engine.getTaskService().createTaskQuery().taskId(activityInstanceUuid).singleResult();
			
			if(task != null) {
				Comment addedComment = 
					engine.getTaskService().addComment(activityInstanceUuid, task.getProcessInstanceId(), comment);

				if(addedComment != null) {
					String msg = addedComment.getFullMessage();
					
					if(msg != null) {
						retVal = msg.length();
					}
				}
			}
		} catch (Exception e) {
			log.severe("Unable to addComment with activityInstanceUuid: " + activityInstanceUuid + 
				" and userId: " + userId);
			retVal = -1;	
		}
		
		return retVal;
	}
	
	// FIXME: Historic comments will not have processDefinitionUuid and activityDefinitionUuid set!
	// Could be fixed.
	// FIXME: Two label fields are set to blank for the moment.

	public List<CommentFeedItem> getProcessInstanceCommentFeedByActivity(
			String activityInstanceUuid) {
		List<CommentFeedItem> commentFeedItems = new ArrayList<CommentFeedItem>();
		CommentFeedItem cFItem = null;
		UserInfo userInfo = null;
		Task task = null;
		String processDefinitionUuid = null;
		String activityDefinitionUuid = null;
		
		try {
			List<Comment> comments = engine.getTaskService().getTaskComments(activityInstanceUuid);

			if(comments != null && comments.size() > 0) {
				task = engine.getTaskService().createTaskQuery().taskId(activityInstanceUuid).singleResult();
				
				if(task != null) {
					processDefinitionUuid = task.getProcessDefinitionId();
					activityDefinitionUuid = task.getTaskDefinitionKey();
				} else {
					processDefinitionUuid = "";
					activityDefinitionUuid = "";
				}
				
				for(Comment comment : comments) {
					
					cFItem = new CommentFeedItem();
					
					cFItem.setProcessDefinitionUuid(processDefinitionUuid);
					cFItem.setProcessInstanceUuid(comment.getProcessInstanceId());
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
			log.severe("Unable to getProcessInstanceCommentFeedByActivity with activityInstanceUuid: " + activityInstanceUuid);
			commentFeedItems = new ArrayList<CommentFeedItem>();
		}
		return commentFeedItems;
	}

	public ActivityWorkflowInfo getActivityWorkflowInfo(
			String activityInstanceUuid) {
	
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			Task task = engine.getTaskService().createTaskQuery().taskId(activityInstanceUuid).singleResult();
			
			activityWorkflowInfo = new ActivityWorkflowInfo();
			activityWorkflowInfo.setPriority(task.getPriority());
			UserInfo assignedUser = new UserInfo();
			assignedUser.setUuid(task.getAssignee());
			activityWorkflowInfo.setAssignedUser(assignedUser);
			activityWorkflowInfo.setCandidates(getCandidatesByTaskId(activityInstanceUuid));
		} catch (Exception e) {
			log.severe("Unable to getActivityWorkflowInfo with taskId: " + activityInstanceUuid);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}

	public ActivityWorkflowInfo assignTask(String activityInstanceUuid,
			String userId) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			Task task = engine.getTaskService().createTaskQuery().taskId(activityInstanceUuid).singleResult();
			task.setAssignee(userId);
			engine.getTaskService().saveTask(task);
			
			activityWorkflowInfo = new ActivityWorkflowInfo();
			activityWorkflowInfo.setPriority(task.getPriority());
			UserInfo assignedUser = new UserInfo();
			assignedUser.setUuid(userId);
			activityWorkflowInfo.setAssignedUser(assignedUser);
			activityWorkflowInfo.setCandidates(getCandidatesByTaskId(activityInstanceUuid));
		} catch (Exception e) {
			log.severe("Unable to assignTask with taskId: " + activityInstanceUuid);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}

	public ActivityWorkflowInfo addCandidate(String activityInstanceUuid,
			String userId) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			engine.getTaskService().addCandidateUser(activityInstanceUuid, userId);
			activityWorkflowInfo = getActivityWorkflowInfo(activityInstanceUuid);
		} catch (Exception e) {
			log.severe("Unable to addCandidate with taskId: " + activityInstanceUuid +
					" and userId: " + userId);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}
	
	public ActivityWorkflowInfo removeCandidate(String activityInstanceUuid,
			String userId) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			engine.getTaskService().deleteCandidateUser(activityInstanceUuid, userId);
			activityWorkflowInfo = getActivityWorkflowInfo(activityInstanceUuid);
		} catch (Exception e) {
			log.severe("Unable to removeCandidate with taskId: " + activityInstanceUuid +
					" and userId: " + userId);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}

	public ActivityWorkflowInfo setPriority(String activityInstanceUuid,
			int priority) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			engine.getTaskService().setPriority(activityInstanceUuid, priority);
			activityWorkflowInfo = getActivityWorkflowInfo(activityInstanceUuid);
		} catch (Exception e) {
			log.severe("Unable to setPriority with taskId: " + activityInstanceUuid +
					" and priority: " + priority);
			activityWorkflowInfo = null;
		}
		
		return activityWorkflowInfo;
	}

	public String startProcess(String processDefinitionId, String userId) {
		String processInstanceId = null;
		
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId); // FIXME: should user be validated?
			ProcessInstance processInstance = engine.getRuntimeService().startProcessInstanceById(processDefinitionId);
			processInstanceId = processInstance.getProcessInstanceId();
		} catch (Exception e) {
			log.severe("Unable to start process instance with processDefinitionId: " + processDefinitionId);
		}
		
		return processInstanceId;
	}

	public boolean executeTask(String activityInstanceUuid, String userId) {
		
		// FIXME: Does clame the job?
		
		boolean successful = false;
			
		try {
			engine.getIdentityService().setAuthenticatedUserId(userId); // FIXME: should login be used?
			// Note: ActivitiTaskAlreadyClaimedException - when the task is already claimed by another user.
			engine.getTaskService().claim(activityInstanceUuid, userId);
			
			// Note: ActivitiException is thrown when this task is DelegationState.PENDING delegation.
			engine.getTaskService().complete(activityInstanceUuid);
			successful = true;
		} catch (Exception e) {
			log.severe("Could not executeTask with taskId: " + activityInstanceUuid + 
				" exception: " + e);
		}

		return successful;
	}

	// FIXME: Shall pageSize for returned objekt be adjusted if answer is shorter than the requested pageSize?
	
	public PagedProcessInstanceSearchResult getProcessInstancesStartedBy(
			String startedByUserId, int fromIndex, int pageSize,
			String sortBy, String sortOrder, String filter, String userId) {
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
					
					// FIXME: Unclear if getId() or getInstanceId() should be used below
					List<IdentityLink> identityLinks = engine.getRuntimeService().
						getIdentityLinksForProcessInstance(pIWithUserInvolved.getProcessInstanceId());
					
					if(identityLinks != null && identityLinks.size() > 0) {
						for(IdentityLink identityLink : identityLinks) {
							if(identityLink.getUserId().equals(startedByUserId) &&
								identityLink.getType().equals(IdentityLinkType.STARTER)) {
								processInstances.add(pIWithUserInvolved);
								break;
							}
						}
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
			
			if(processInstances != null) {
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
			
				for(ProcessInstance processInstance : processInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
					
					processInstanceListItem.setProcessInstanceUuid(processInstance.getProcessInstanceId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_PENDING);
					processInstanceListItem.setStartDate(null); // FIXME
					processInstanceListItem.setStartedBy(""); // FIXME: Could be fetched from LinkedIdentity if it was set.
					processInstanceListItem.setStartedByFormPath("");
					processInstanceListItem.setEndDate(null); // FIXME
					processInstanceListItem.setProcessInstanceLabel(""); // FIXME
					processInstanceListItem.setProcessLabel(""); // FIXME
					processInstanceListItem.setActivities
						(getUserInboxByInvolvedUser(startedByUserId, processInstance.getProcessInstanceId()));
					
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
	
	// FIXME: sortBy is always processInstanceId for the moment.
	// FIXME: sortOrder is always asc for the moment.
    // FIXME: filter is not used for the moment.
    // FIXME: userId is set as setAuthenticatedUserId, but it is maybe not necessary?
	// FIXME: setProcessInstanceUuid uses processInstanceId but could use id? This differs for subprocesses and
	// embedded processes. This has to be clear but it is not for the moment.
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
			
			if(processInstances != null) {
				pagedProcessInstanceSearchResult.setNumberOfHits(processInstances.size());
				
				List<ProcessInstanceListItem> processInstanceListItems = new ArrayList<ProcessInstanceListItem>();
			
				for(ProcessInstance processInstance : processInstances) {
					ProcessInstanceListItem processInstanceListItem = new ProcessInstanceListItem();
					
					processInstanceListItem.setProcessInstanceUuid(processInstance.getProcessInstanceId());
					processInstanceListItem.setStatus(ProcessInstanceListItem.STATUS_PENDING);
					processInstanceListItem.setStartDate(null); // FIXME
					processInstanceListItem.setStartedBy(""); // FIXME: Could be fetched from LinkedIdentity if it was set.
					processInstanceListItem.setStartedByFormPath("");
					processInstanceListItem.setEndDate(null); // FIXME
					processInstanceListItem.setProcessInstanceLabel(""); // FIXME
					processInstanceListItem.setProcessLabel(""); // FIXME
					processInstanceListItem.setActivities
						(getUserInboxByInvolvedUser(searchForUserId, processInstance.getProcessInstanceId()));
					
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

	public PagedProcessInstanceSearchResult getProcessInstancesByUuids(
			List<String> uuids, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean createUser(String uuid) {
		boolean successful = false;
		
		try {
			User user = engine.getIdentityService().newUser(uuid);
			engine.getIdentityService().saveUser(user);
			successful = true;
		} catch (Exception e) {
			log.severe("Could not createUser with uuid: " + uuid +  " exception: " + e);
		}
		
		return successful;
	}

	public ActivityWorkflowInfo unassignTask(String activityInstanceUuid) {
		ActivityWorkflowInfo activityWorkflowInfo = null;
		
		try {
			Task task = engine.getTaskService().createTaskQuery().taskId(activityInstanceUuid).singleResult();
			task.setAssignee(null);
			engine.getTaskService().saveTask(task);
			
			activityWorkflowInfo = new ActivityWorkflowInfo();
			activityWorkflowInfo.setPriority(task.getPriority());
			activityWorkflowInfo.setAssignedUser(null);
			activityWorkflowInfo.setCandidates(getCandidatesByTaskId(activityInstanceUuid));	
		} catch (Exception e) {
			log.severe("Unable to assignTask with taskIs: " + activityInstanceUuid);
		}
		
		return activityWorkflowInfo;
	}
	
	// FIXME: Label for ProcessDefinitionInfo is not set!
	
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
	
	// FIXME: Label for ProcessDefinitionInfo is not set!
	// FIXME: Label for ActivityDefinitionInfo is not set!

	public ProcessDefinitionDetails getProcessDefinitionDetailsByUuid(
			String processDefinitionUUIDStr) {
		
		ProcessDefinitionDetails processDefinitionDetails = null;
		
		try {
			 ProcessDefinition processDefinition = 
				engine.getRepositoryService().getProcessDefinition(processDefinitionUUIDStr);
			
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
			log.severe("Unable to getProcessDefinitionDetailsByUuid with processDefinitionUUIDStr: " +
				processDefinitionUUIDStr + " execption: " + e);
			processDefinitionDetails = null;
		}
		
		return processDefinitionDetails;
	}
	
	public static void main(String[] args) {
		ActivitiEngineService activitiEngineService = new ActivitiEngineService();

		//activitiEngineService.startProcess("Arendeprocess:1:3904", "ulric");
		
		/*
		PagedProcessInstanceSearchResult p = activitiEngineService.
				getProcessInstancesStartedBy("ulric", 3, 100, null, null, null, "ulric");

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
			
		Deployment deployment = activitiEngineService.deployBpmn
				("/home/pama/workspace/motrice/pawap/bpm-processes/Arendeprocess.bpmn20.xml");
		
		log.severe("Deployment with id: " + deployment.getId());
		
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
		
		System.exit(0);
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

    public String getParentProcessInstanceUuid(String processInstanceUuid) {
	String parentProcessInstanceUuid = null;

	return parentProcessInstanceUuid;
    }

    public InboxTaskItem getNextInboxTaskItem(String currentProcessInstance, String userId) {
	return null;
    }
}

package se.inherit.service.bonita;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.security.auth.login.LoginContext;

import org.inherit.bonita.client.util.BonitaUtil;
import org.inherit.service.common.domain.InboxTaskItem;
import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.ow2.bonita.facade.QueryDefinitionAPI;
import org.ow2.bonita.facade.def.majorElement.ProcessDefinition;
import org.ow2.bonita.facade.exception.ProcessNotFoundException;
import org.ow2.bonita.facade.runtime.ActivityState;
import org.ow2.bonita.facade.runtime.InstanceState;
import org.ow2.bonita.facade.runtime.ProcessInstance;
import org.ow2.bonita.facade.runtime.TaskInstance;
import org.ow2.bonita.facade.uuid.ProcessDefinitionUUID;
import org.ow2.bonita.util.AccessorUtil;

public class BonitaEngineServiceImpl {

	public static final Logger log = Logger.getLogger(BonitaEngineServiceImpl.class.getName());
	
	HashMap<String, String> defUuid2LabelCache = new HashMap<String,String>();
	
	public BonitaEngineServiceImpl() {
		
	}
	
	public ArrayList<ProcessInstanceListItem> getUserInstancesList(String user) { // ArrayList<ProcessInstanceListItem>
		ArrayList<ProcessInstanceListItem> result = new ArrayList<ProcessInstanceListItem>();

		try {
			
			
			Set<ProcessInstance> piList = new HashSet<ProcessInstance>();
			LoginContext loginContext = BonitaUtil.loginWithUser(user); 
			piList = AccessorUtil.getQueryRuntimeAPI().getUserInstances();
			
			for (ProcessInstance pi : piList) {
	
				ProcessInstanceListItem item = new ProcessInstanceListItem();
	
				// find out process label
				item.setProcessLabel(getProcessLabel(pi.getProcessDefinitionUUID()));
	
				// find out process instance status
				if (InstanceState.FINISHED.equals(pi.getInstanceState())) {
					item.setStatus("Avslutad");
				}
				else if (InstanceState.CANCELLED.equals(pi.getInstanceState()) || InstanceState.ABORTED.equals(pi.getInstanceState())) {
					item.setStatus("Avbruten");
				} 
				else {
					StringBuffer sb = new StringBuffer();
					Set<TaskInstance> tasks = pi.getTasks();
					for (TaskInstance task : tasks) {
						if (ActivityState.READY.equals(task.getState()) || ActivityState.EXECUTING.equals(task.getState())) {
							sb.append(task.getActivityLabel());
						}
					}
					item.setStatus(sb.toString());
				}
				
				item.setEndDate(pi.getEndedDate());
				item.setStartDate(pi.getStartedDate());
				
				result.add(item);
			}
	
			BonitaUtil.logoutWithUser(loginContext);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}

		return result;
	}
	
	public ArrayList<InboxTaskItem> getUserInbox(String userId) { // ArrayList<ProcessInstanceListItem>
		ArrayList<InboxTaskItem> result = new ArrayList<InboxTaskItem>();

		try {
			LoginContext loginContext = BonitaUtil.login(); 
			Collection<TaskInstance> taskList = AccessorUtil.getQueryRuntimeAPI().getTaskList(userId, ActivityState.READY);
			
			for (TaskInstance taskInstance : taskList) {
				InboxTaskItem taskItem = new InboxTaskItem();
				
				taskItem.setActivityCreated(taskInstance.getStartedDate());
				taskItem.setActivityLabel(taskInstance.getActivityLabel());
				taskItem.setProcessLabel(getProcessLabel(taskInstance.getProcessDefinitionUUID()));;
				taskItem.setProcessActivityFormInstanceId(new Long(0)); // TODO
				
				result.add(taskItem);
			}
			BonitaUtil.logout(loginContext);
		}
		catch (Exception e) {
			log.severe("Exception: " + e);
		}
		return result;
	}
		
	private String getProcessLabel(ProcessDefinitionUUID processDefinitionUUID) {
		String result = defUuid2LabelCache.get(processDefinitionUUID.getValue());

		if (result == null) {
			QueryDefinitionAPI definitionAPI = AccessorUtil.getQueryDefinitionAPI();
			try {
				ProcessDefinition processDefinition = definitionAPI.getProcess(processDefinitionUUID);
				result = processDefinition.getLabel();
			} catch (ProcessNotFoundException e) {
				result = "n/a";
			}
			defUuid2LabelCache.put(processDefinitionUUID.getValue(), result);
		}

		return result;
	}
	
}

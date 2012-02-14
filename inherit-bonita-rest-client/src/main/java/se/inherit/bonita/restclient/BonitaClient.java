package se.inherit.bonita.restclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.LoginContext;

import org.ow2.bonita.facade.QueryDefinitionAPI;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.def.element.impl.ConnectorDefinitionImpl;
import org.ow2.bonita.facade.def.majorElement.ProcessDefinition;
import org.ow2.bonita.facade.def.majorElement.impl.ActivityDefinitionImpl;
import org.ow2.bonita.facade.def.majorElement.impl.DataFieldDefinitionImpl;
import org.ow2.bonita.facade.def.majorElement.impl.DescriptionElementImpl;
import org.ow2.bonita.facade.def.majorElement.impl.EventProcessDefinitionImpl;
import org.ow2.bonita.facade.def.majorElement.impl.NamedElementImpl;
import org.ow2.bonita.facade.def.majorElement.impl.ParticipantDefinitionImpl;
import org.ow2.bonita.facade.def.majorElement.impl.ProcessDefinitionImpl;
import org.ow2.bonita.facade.def.majorElement.impl.ProcessElementImpl;
import org.ow2.bonita.facade.def.majorElement.impl.TransitionDefinitionImpl;
import org.ow2.bonita.facade.exception.ProcessNotFoundException;
import org.ow2.bonita.facade.runtime.ActivityState;
import org.ow2.bonita.facade.runtime.InstanceState;
import org.ow2.bonita.facade.runtime.ProcessInstance;
import org.ow2.bonita.facade.runtime.TaskInstance;
import org.ow2.bonita.facade.runtime.impl.ActivityInstanceImpl;
import org.ow2.bonita.facade.runtime.impl.AssignUpdateImpl;
import org.ow2.bonita.facade.runtime.impl.AttachmentInstanceImpl;
import org.ow2.bonita.facade.runtime.impl.CaseImpl;
import org.ow2.bonita.facade.runtime.impl.CatchingEventImpl;
import org.ow2.bonita.facade.runtime.impl.CategoryImpl;
import org.ow2.bonita.facade.runtime.impl.CommentImpl;
import org.ow2.bonita.facade.runtime.impl.DocumentImpl;
import org.ow2.bonita.facade.runtime.impl.InitialAttachmentImpl;
import org.ow2.bonita.facade.runtime.impl.InstanceStateUpdateImpl;
import org.ow2.bonita.facade.runtime.impl.LabelImpl;
import org.ow2.bonita.facade.runtime.impl.ObjectVariable;
import org.ow2.bonita.facade.runtime.impl.ProcessInstanceImpl;
import org.ow2.bonita.facade.runtime.impl.RuntimeRecordImpl;
import org.ow2.bonita.facade.runtime.impl.StateUpdateImpl;
import org.ow2.bonita.facade.runtime.impl.UpdateImpl;
import org.ow2.bonita.facade.runtime.impl.VariableUpdateImpl;
import org.ow2.bonita.facade.runtime.impl.WebTemporaryTokenImpl;
import org.ow2.bonita.facade.uuid.ProcessDefinitionUUID;
import org.ow2.bonita.light.LightActivityInstance;
import org.ow2.bonita.light.LightTaskInstance;
import org.ow2.bonita.light.impl.LightActivityInstanceImpl;
import org.ow2.bonita.util.AccessorUtil;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.bonita.domain.InboxTaskItem;
import se.inherit.bonita.domain.ProcessDefinitionInfo;
import se.inherit.bonita.domain.ProcessInstanceListItem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.ArrayConverter;

public class BonitaClient {
	
	public static final Logger log = LoggerFactory.getLogger(BonitaClient.class);

	String serverBaseUrl;
	String restUser;
	String restPassword;
	
	String customServerBaseUrl;
	
	XStream xstream;
	
	HashMap<String, ProcessDefinitionInfo> defUuid2ProcessDefinitionInfo = new HashMap<String,ProcessDefinitionInfo>();
	
	public BonitaClient() {
		this.serverBaseUrl = "http://localhost:58080/bonita-server-rest/";
		this.restUser = "restuser";
		this.restPassword = "restbpm";
		this.customServerBaseUrl = "http://localhost:58080/inherit-bonita-rest-server-custom-1.0-SNAPSHOT/";
		
		xstream = initXStream();
	}
	
	public BonitaClient(String serverBaseUrl, String customServerBaseUrl, String restUser,
			String restPassword) {
		super();
		this.serverBaseUrl = serverBaseUrl;
		this.restUser = restUser;
		this.restPassword = restPassword;
		this.customServerBaseUrl = customServerBaseUrl;
		
		xstream = initXStream();
	}

	private XStream initXStream() {
		XStream xstream = new XStream();
        
		xstream.alias("set", HashSet.class);
		xstream.alias("Object-array", Object[].class);
        
		// definition api 
        xstream.alias("ProcessDefinition", ProcessDefinitionImpl.class);
        xstream.alias("DataFieldDefinition", DataFieldDefinitionImpl.class);
        xstream.alias("ActivityDefinition", ActivityDefinitionImpl.class);
        xstream.alias("ParticipantDefinition", ParticipantDefinitionImpl.class);
        xstream.alias("DescriptionElement", DescriptionElementImpl.class);
        xstream.alias("ProcessElement", ProcessElementImpl.class);
        xstream.alias("EventProcessDefinition", EventProcessDefinitionImpl.class);
        xstream.alias("NamedElement", NamedElementImpl.class);
        xstream.alias("TransitionDefinition", TransitionDefinitionImpl.class);
        xstream.alias("roleMapper", ConnectorDefinitionImpl.class);
        
        // runtime api
        xstream.alias("ActivityInstance", ActivityInstanceImpl.class);
        xstream.alias("AssignUpdate", AssignUpdateImpl.class);
        xstream.alias("AttachmentInstance", AttachmentInstanceImpl.class);
        xstream.alias("Case", CaseImpl.class);
        xstream.alias("CatchingEvent", CatchingEventImpl.class);
        xstream.alias("Category", CategoryImpl.class);
        xstream.alias("Comment", CommentImpl.class);
        xstream.alias("Document", DocumentImpl.class);
        xstream.alias("InitialAttachment", InitialAttachmentImpl.class);
        xstream.alias("InstanceStateUpdate", InstanceStateUpdateImpl.class);
        xstream.alias("Label", LabelImpl.class);
        xstream.alias("ObjectVariable", ObjectVariable.class);
        xstream.alias("ProcessInstance", ProcessInstanceImpl.class);
        xstream.alias("RuntimeRecord", RuntimeRecordImpl.class);
        xstream.alias("StateUpdate", StateUpdateImpl.class);
        xstream.alias("Update", UpdateImpl.class);
        xstream.alias("VariableUpdate", VariableUpdateImpl.class);
        xstream.alias("WebTemporaryToken", WebTemporaryTokenImpl.class);
        
        xstream.alias("LightActivityInstance", LightActivityInstanceImpl.class);
        //xstream.alias("roleMapper", ConnectorDefinitionImpl.class);
        
        // inherit bonita custom api
        //xstream.alias("", type)
        
        return xstream;
	}

	private String call(String uri, String bonitaUser) {
		String result = null;
		try {
			Client client = new Client(new Context(), Protocol.HTTP);
			
			ClientResource cr = new ClientResource(uri);
			cr.setNext(client);
			cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, restUser, restPassword);
				
			final String RESTLET_HTTP_HEADERS = "org.restlet.http.headers";
	        Map<String, Object> reqAttribs = cr.getRequestAttributes();
	        Form headers = (Form)reqAttribs.get(RESTLET_HTTP_HEADERS);
	        if (headers == null) {
	            headers = new Form();
	            reqAttribs.put(RESTLET_HTTP_HEADERS, headers);
	        }
	        headers.add("options", "user:" + bonitaUser); 

	        result = (String)cr.post(null, String.class);
	        			
		} catch (ResourceException e) {
			log.error("ResourceException: " + e);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public Set<ProcessDefinition> getProcesses (String bonitaUser) {
		Set<ProcessDefinition> result = null;
		String uri = serverBaseUrl + "API/queryDefinitionAPI/getProcesses";
		String response = call(uri, bonitaUser);
		if (response != null) {
			result = (Set<ProcessDefinition>)xstream.fromXML(response);
		}
		return result;
	}
	

	@SuppressWarnings("unchecked")
	public ProcessDefinition getProcess (ProcessDefinitionUUID processDefinitionUUID) {
		ProcessDefinition result = null;
		String uri = serverBaseUrl + "API/queryDefinitionAPI/getProcesses";
		String response = call(uri, "admin");
		if (response != null) {
			result = (ProcessDefinition)xstream.fromXML(response);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ProcessDefinitionInfo getProcessDefinitionInfo (String processDefinitionUUID) {
		ProcessDefinitionInfo result = null;
		String response = null;
		String uri = customServerBaseUrl + "processDefinition/" + processDefinitionUUID;
		response = call(uri, "admin");
		if (response != null) {
			result = (ProcessDefinitionInfo)xstream.fromXML(response);
		}
		return result;
	}

	

	@SuppressWarnings("unchecked")
	public Set<ProcessInstance> getUserInstances (String bonitaUser) {
		Set<ProcessInstance> result = null;
		String uri = serverBaseUrl + "API/queryRuntimeAPI/getUserInstances";
		String response = call(uri, bonitaUser);
		if (response != null) {
			result = (Set<ProcessInstance>)xstream.fromXML(response);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<LightTaskInstance> getLightTaskListByUserId(String bonitaUser, String activityState) {
		Collection<LightTaskInstance> result = null; 	
		String uri = serverBaseUrl + "API/queryRuntimeAPI/getLightTaskListByUserId/" + bonitaUser + "/" + activityState;
		String response = call(uri, bonitaUser);
		if (response != null) {
			result = (Collection<LightTaskInstance>)xstream.fromXML(response);
		}
		return result;
	}
	
	public String getFormIdentityKey(String bonitaUser, String password) {
		String result = null;
		String uri = customServerBaseUrl + "bonita/" + bonitaUser + "/" + password;
		result = call(uri, bonitaUser);
		return result;
	}
	
	public String getStatusByUserId(String bonitaUser) {
		String result = null;
		String uri = customServerBaseUrl + "bonitaStatusByUserId/" + bonitaUser;
		result = call(uri, bonitaUser);
		return result;
	}

	@SuppressWarnings("unchecked")
	public Collection<TaskInstance> getTaskList(String bonitaUser, String activityState) {
		Collection<TaskInstance> result = null; 	
		String uri = serverBaseUrl + "API/queryRuntimeAPI/getTaskListByUserIdAndActivityState/" + bonitaUser + "/" + activityState;
		String response = call(uri, bonitaUser);
		if (response != null) {
			result = (Collection<TaskInstance>)xstream.fromXML(response);
		}
		return result;
	}

	public Collection<InboxTaskItem> getUserTaskList(String userId) {
		Collection<InboxTaskItem> result = new ArrayList<InboxTaskItem>();
		
		
		Collection<TaskInstance> taskInstances = getTaskList(userId, "READY");
		
		for (TaskInstance taskInstance: taskInstances) {
			
			//ProcessDefinition processDefinition;
			
				//processDefinition = definitionAPI.getProcess(taskInstance.getProcessDefinitionUUID());
			
				InboxTaskItem item = new InboxTaskItem();

				//item.setProcessName(processDefinition.getName() + "--" + processDefinition.getVersion());
				item.setProcessName(getProcessLabel(taskInstance.getProcessDefinitionUUID().getValue()).getName());
				item.setTaskUUID(taskInstance.getUUID().getValue());
				//item.setProcessLabel(processDefinition.getLabel());
				item.setProcessLabel(getProcessLabel(taskInstance.getProcessDefinitionUUID().getValue()).getLabel());
				item.setActivityLabel(taskInstance.getActivityLabel());
				item.setCreatedDate(taskInstance.getCreatedDate());
				item.setActivityDefinitionUUID(taskInstance.getActivityDefinitionUUID().getValue());
	
				result.add(item);
			

		}

		return result;
	}

	
	public ArrayList<ProcessInstanceListItem> getUserInstancesList(String userid) { // ArrayList<ProcessInstanceListItem>
		ArrayList<ProcessInstanceListItem> result = new ArrayList<ProcessInstanceListItem>();

		Set<ProcessInstance> piList = new HashSet<ProcessInstance>();

		piList = getUserInstances(userid);
		
		for (ProcessInstance pi : piList) {

			ProcessInstanceListItem item = new ProcessInstanceListItem();

			// find out process label
			item.setProcessLabel(getProcessLabel(pi.getProcessDefinitionUUID().getValue()).getLabel());

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

		return result;
	}
	
	private ProcessDefinitionInfo getProcessLabel(String processDefinitionUUID) {
		
		ProcessDefinitionInfo result = defUuid2ProcessDefinitionInfo.get(processDefinitionUUID);

		if (result == null) {
			result = getProcessDefinitionInfo(processDefinitionUUID);
			defUuid2ProcessDefinitionInfo.put(processDefinitionUUID, result);
		}
		return result;
	}
	
	
}

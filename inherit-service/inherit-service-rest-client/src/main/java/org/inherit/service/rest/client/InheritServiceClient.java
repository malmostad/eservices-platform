package org.inherit.service.rest.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import org.inherit.service.common.domain.ActivityInstanceListItem;
import org.inherit.service.common.domain.InboxTaskItem;
import org.inherit.service.common.domain.ProcessDefinitionInfo;
import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.thoughtworks.xstream.XStream;

public class InheritServiceClient {

	public static final Logger log = Logger.getLogger(InheritServiceClient.class.getName());
	
	String serverBaseUrl;
	XStream xstream;
	
	public InheritServiceClient() {
		serverBaseUrl = "http://localhost:58080/inherit-service-rest-server-1.0-SNAPSHOT/";
		xstream = initXStream();
	}

	public InheritServiceClient(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
		xstream = initXStream();
	}
	
	private XStream initXStream() {
		XStream xstream = new XStream();
		
		xstream.alias("Date", Date.class);
		
		xstream.alias("set", HashSet.class);
		xstream.alias("list", ArrayList.class);
		
		xstream.alias("InboxTaskItem", InboxTaskItem.class);
		xstream.alias("ProcessDefinitionInfo", ProcessDefinitionInfo.class);
		xstream.alias("ProcessInstanceListItem", ProcessInstanceListItem.class);
		xstream.alias("ActivityInstanceListItem", ActivityInstanceListItem.class);
		
		return xstream;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ProcessInstanceListItem> getStatusByUserId(String bonitaUser) {
		 ArrayList<ProcessInstanceListItem> result = null;
		String uri = serverBaseUrl + "statusByUserId/" + bonitaUser + "?media=xml";
		String response = call(uri);
		System.out.println(response);
		if (response != null) {
			result = (ArrayList<ProcessInstanceListItem>)xstream.fromXML(response);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<InboxTaskItem> getInboxByUserId(String bonitaUser) {
		 ArrayList<InboxTaskItem> result = null;
		String uri = serverBaseUrl + "inboxByUserId/" + bonitaUser + "?media=xml";
		String response = call(uri);
		System.out.println(response);
		if (response != null) {
			result = (ArrayList<InboxTaskItem>)xstream.fromXML(response);
		}
		return result;
	}

	private String call(String uri) {
		String result = null;
		try {
			Client client = new Client(new Context(), Protocol.HTTP);
			
			ClientResource cr = new ClientResource(uri);
			cr.setNext(client);
		
			cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "restuser", "restbpm");
				
			final String RESTLET_HTTP_HEADERS = "org.restlet.http.headers";
	        Map<String, Object> reqAttribs = cr.getRequestAttributes();
	        Form headers = (Form)reqAttribs.get(RESTLET_HTTP_HEADERS);
	        if (headers == null) {
	            headers = new Form();
	            reqAttribs.put(RESTLET_HTTP_HEADERS, headers);
	        } 
	        //headers.add("options", "user:" + bonitaUser); 

	        result = (String)cr.post(null, String.class);
	        			
		} catch (ResourceException e) {
			log.severe("call ResourceException: " + e);
		}
		
		return result;
	}
	
	public static void main(String args[]) {
		System.out.println("Testa InheritServiceClient");
		
		InheritServiceClient c = new InheritServiceClient();
		ArrayList<ProcessInstanceListItem> list = c.getStatusByUserId("john");
		for (ProcessInstanceListItem item : list) {
			System.out.println("item: " + item);
		}

		ArrayList<InboxTaskItem> listInbx = c.getInboxByUserId("john");
		for (InboxTaskItem item : listInbx) {
			System.out.println("listInbx item: " + item);
		}

		System.out.println("InheritServiceClient avslutas");
	}
	
}

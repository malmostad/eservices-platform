package se.inherit.bonita;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.ow2.bonita.facade.runtime.ProcessInstance;
import org.ow2.bonita.light.LightTaskInstance;

import se.inherit.bonita.domain.InboxTaskItem;
import se.inherit.bonita.domain.ProcessInstanceListItem;
import se.inherit.bonita.restclient.BonitaClient;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        
        System.out.println( "Hello World!" );
        
        //BonitaClient client = new BonitaClient();
        BonitaClient client = new BonitaClient("http://localhost:58081/bonita-server-rest/", "http://localhost:58081/inherit-bonita-rest-server-custom-1.0-SNAPSHOT/", "restuser", "restbpm");
        
        Collection<InboxTaskItem> userTaskList = client.getUserTaskList("admin");
        
        for (InboxTaskItem task : userTaskList) {
        	String taskUUID = task.getTaskUUID();
        	System.out.println("taskName:" + task.getActivityLabel() + " : " + taskUUID);
        }   
 
        
        Collection<LightTaskInstance> todoList = client.getLightTaskListByUserId("admin", "READY");
        
        for (LightTaskInstance task : todoList) {
        	String taskUUID = task.getUUID().getValue();
        	System.out.println("taskName:" + task.getActivityLabel() + " : " + taskUUID);
        	
        	System.out.println(client.getProcessDefinitionInfo(task.getProcessDefinitionUUID().getValue()));
        }   
        
        Set<ProcessInstance> instances = client.getUserInstances("admin");
        for (ProcessInstance pi : instances) {
        	System.out.println("pi: " + pi.getProcessInstanceUUID().getValue());
        }
        
        ArrayList<ProcessInstanceListItem> instanceListItem = client.getUserInstancesList("admin");
        for (ProcessInstanceListItem pi : instanceListItem) {
        	System.out.println("instance list item: " + pi.getProcessLabel() + ", " + pi.getStatus());
        }
        
        
        System.out.println("id key: " + client.getFormIdentityKey("admin", "bpm"));
        //System.out.println("id key: " + client.getStatusByUserId("admin"));
    }
}

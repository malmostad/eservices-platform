/* 
 *  Process Aware Web Application Platform 
 * 
 *  Copyright (C) 2011-2013 Inherit S AB 
 * 
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Affero General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details. 
 * 
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 *  e-mail: info _at_ inherit.se 
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 *  phone: +46 8 641 64 14 
 */ 
 
package org.inheritsource.test.service.processengine;

import java.util.List;
import java.util.Set;

import org.activiti.engine.repository.Deployment;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.InboxTaskItem;
import org.inheritsource.service.processengine.ActivitiEngineService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivitiEngineServiceTest {

	ActivitiEngineService activitiEngineService = null;
	
	@Before
	public void before() {
		activitiEngineService = new ActivitiEngineService();
	}
	
	@After
	public void after() {
		activitiEngineService.close();
	}


	@Test
	public void userInbox() {
		String userId = "admin";
		clearDatabase();
		
		// Deploy a BPMN and start a process with a certain user
		activitiEngineService.deployBpmn("../../bpm-processes/TestFunctionProcess1.bpmn20.xml");
		activitiEngineService.startProcessInstanceByKey("TestFunctionProcess1", userId);

		// Get the inbox and verify some data
		List<InboxTaskItem> inbox = activitiEngineService.getUserInbox("admin");
		Assert.assertNotNull(inbox);
		Assert.assertEquals(inbox.size(), 1);
		Assert.assertEquals(inbox.get(0).getActivityLabel(), "Registrera");
		
		// Get the inbox from another method and verify inboxes are equal
		Set<InboxTaskItem> inbox2 = activitiEngineService.getUserInboxByProcessInstanceId(inbox.get(0).getProcessInstanceUuid());
		Assert.assertNotNull(inbox2);
		Assert.assertEquals(inbox2.size(), 1);
		Assert.assertEquals(inbox2.toArray()[0], inbox.get(0));
				
		Set<InboxTaskItem> historicInbox = activitiEngineService.getHistoricUserInboxByProcessInstanceId(inbox.get(0).getProcessInstanceUuid());
		Assert.assertEquals(historicInbox.size(), 0);
		
		activitiEngineService.executeTask(inbox.get(0).getTaskUuid(), "admin");
		historicInbox = activitiEngineService.getHistoricUserInboxByProcessInstanceId(inbox.get(0).getProcessInstanceUuid());
		Assert.assertNotNull(historicInbox);
		Assert.assertEquals(historicInbox.size(), 1);
		Assert.assertEquals(inbox.get(0), historicInbox.toArray()[0]);
	}
	
	@Test
	public void taskId() {
		String userId = "admin";
		clearDatabase();
		
		// Deploy a BPMN and start a process with a certain user
		activitiEngineService.deployBpmn("../../bpm-processes/TestFunctionProcess1.bpmn20.xml");
		activitiEngineService.startProcessInstanceByKey("TestFunctionProcess1", userId);

		// Get the inbox and verify some data
		List<InboxTaskItem> inbox = activitiEngineService.getUserInbox("admin");
		Assert.assertNotNull(inbox);
		Assert.assertEquals(inbox.size(), 1);
		
		String taskId = activitiEngineService.getActivityInstanceUuid
			(inbox.get(0).getProcessInstanceUuid(), inbox.get(0).getActivityLabel());
		Assert.assertEquals(taskId, inbox.get(0).getTaskUuid());
		
		// One executeTask should make the task passed into history
		activitiEngineService.executeTask(inbox.get(0).getTaskUuid(), "admin");
		
		String historicTaskId = activitiEngineService.getActivityInstanceUuid
				(inbox.get(0).getProcessInstanceUuid(), inbox.get(0).getActivityLabel());
		Assert.assertEquals(historicTaskId, taskId);
		
	}
	
	@Test
	public void taskItem() {
		String userId = "admin";
		clearDatabase();
		
		// Deploy a BPMN and start a process with a certain user
		activitiEngineService.deployBpmn("../../bpm-processes/TestFunctionProcess1.bpmn20.xml");
		activitiEngineService.startProcessInstanceByKey("TestFunctionProcess1", userId);

		// Get the inbox and verify some data
		List<InboxTaskItem> inbox = activitiEngineService.getUserInbox("admin");
		Assert.assertNotNull(inbox);
		Assert.assertEquals(inbox.size(), 1);
		
		String taskId = activitiEngineService.getActivityInstanceUuid
			(inbox.get(0).getProcessInstanceUuid(), inbox.get(0).getActivityLabel());
		Assert.assertEquals(taskId, inbox.get(0).getTaskUuid());
		
		ActivityInstanceItem taskItem = activitiEngineService.getActivityInstanceItem(inbox.get(0).getTaskUuid()); 
		Assert.assertEquals(taskItem.getActivityLabel(), inbox.get(0).getActivityLabel());
		
		// One executeTask should make the task passed into history
		activitiEngineService.executeTask(inbox.get(0).getTaskUuid(), "admin");
		
		ActivityInstanceItem historicTaskItem = activitiEngineService.getActivityInstanceItem(inbox.get(0).getTaskUuid()); 
		Assert.assertEquals(historicTaskItem.getActivityLabel(), inbox.get(0).getActivityLabel());
	}

	@Test
	public void taskItem() {
		String userId = "admin";
		clearDatabase();
		
		// Deploy a BPMN and start a process with a certain user
		activitiEngineService.deployBpmn("../../bpm-processes/TestFunctionProcess1.bpmn20.xml");
		activitiEngineService.startProcessInstanceByKey("TestFunctionProcess1", userId);

		// Get the inbox and verify some data
		List<InboxTaskItem> inbox = activitiEngineService.getUserInbox("admin");
		Assert.assertNotNull(inbox);
		Assert.assertEquals(inbox.size(), 1);
		
		ActivityInstanceItem taskItem = activitiEngineService.getActivityInstanceItem(inbox.get(0).getTaskUuid()); 
		Assert.assertEquals(taskItem.getActivityLabel(), inbox.get(0).getActivityLabel());

		// One executeTask should make the task passed into history
		activitiEngineService.executeTask(inbox.get(0).getTaskUuid(), "admin");
		
		ActivityInstanceItem historicTaskItem = activitiEngineService.getActivityInstanceItem(inbox.get(0).getTaskUuid()); 
		Assert.assertEquals(historicTaskItem.getActivityLabel(), inbox.get(0).getActivityLabel());
	}
	
	private void clearDatabase() {
		
		// Clear the deployment, processdefinitions, processinstances, task and history
		for(String deploymentId : activitiEngineService.getDeployedDeploymentIds()) {
			activitiEngineService.deleteDeploymentByDeploymentId(deploymentId, true);
		}		
	}
	
}

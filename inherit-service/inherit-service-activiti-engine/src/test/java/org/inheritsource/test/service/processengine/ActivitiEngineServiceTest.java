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

import org.activiti.engine.repository.Deployment;
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
		
		// Clear the deploymnent, processdefinitions, processinstances, task and history
		for(String deploymentId : activitiEngineService.getDeployedDeploymentIds()) {
			activitiEngineService.deleteDeploymentByDeploymentId(deploymentId, true);
		}
		
		// Deploy a BPMN
		String BPMN_FILE = "../../bpm-processes/TestFunctionProcess1.bpmn20.xml";
		String PROCDEF_NAME = "TestFunctionProcess1";
		String startUserId = "admin2";
		
		activitiEngineService.deployBpmn(BPMN_FILE);
		activitiEngineService.startProcessInstanceByKey(PROCDEF_NAME, startUserId);
	}
	
	@After
	public void after() {
		activitiEngineService.close();
	}

	
	@Test
	public void tags() {
		List<InboxTaskItem> inbox = activitiEngineService.getUserInbox("admin");
		System.out.println("inbox: " + inbox.toString());
		Assert.assertNotNull(inbox);
		
		activitiEngineService.executeTask(inbox.get(0).getTaskUuid(), "admin2");
		
	}
	
}

package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestScriptprocess {

	private String filename = "/home/tostman/workspace20140410/pawap/inherit-service/inherit-service-activiti-engine/src/main/resources/ScriptProcess.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule(
			"activiti.cfg-mem-fullhistory.xml");

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule
				.getRepositoryService();
		repositoryService
				.createDeployment()
				.addInputStream("scriptprocess.bpmn20.xml",
						new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();

		TaskService taskService = activitiRule.getTaskService();
		List<Long> currentGroupList = new ArrayList<Long>();
		//
		// run the process with a number of values of currentgroup
		//
		currentGroupList.add(5L);
		currentGroupList.add(3L);
		currentGroupList.add(1L);
		currentGroupList.add(4L);
		currentGroupList.add(6L);
		int nVariables = 0 ; 
		for (long currentgroup : currentGroupList) {
			Map<String, Object> variableMap = new HashMap<String, Object>();
			variableMap.put("motrice_form_data_name", "Activiti");
			variableMap.put("startevent1_start_forename", "Fozzie");
			variableMap.put("startevent1_start_lastname", "The Bear");
			variableMap.put("startevent1_start_emailaddress", "fozzie@nowhere");
			variableMap.put("startevent1_start_phonenumber", "47114711");
			variableMap
					.put("startevent1_start_currentgroup_long", currentgroup);
			variableMap.put("startevent1_start_info", "test info");
			ProcessInstance processInstance = runtimeService
					.startProcessInstanceByKey("scriptprocess", variableMap);
			nVariables += variableMap.size(); 
			assertNotNull(processInstance.getId());
			System.out.println("id " + processInstance.getId() + " "
					+ processInstance.getProcessDefinitionId());

			//

			System.out.println("processInstance.getActivityId() ="
					+ processInstance.getActivityId());

			System.out.println("=======================================");
			System.out.println("== complete tasks======================");

			Map<String, Object> variableMap2 = processInstance
					.getProcessVariables();

			System.out.println("variableMap2 = " + variableMap2);

			List<Task> availableTaskList = taskService.createTaskQuery()
					.taskName("Add comment").list();

			for (Task task : availableTaskList) {
				System.out.println("Completing task with id :" + task.getId());
				taskService.setVariable(task.getId(),
						"motrice_form_data_comment", "Min kommentar");
				nVariables ++; 
				taskService.complete(task.getId());
			}
			nVariables ++; // myVar is set in the script so more variable is created 
			System.out.println("=======================================");
			//
		}
		List<HistoricDetail> historyVariables = activitiRule
				.getHistoryService().createHistoricDetailQuery()
				.variableUpdates().orderByVariableName().asc().list();
		for (HistoricDetail mHistoricDetail : historyVariables) {
			System.out.println(": " + mHistoricDetail.getActivityInstanceId()
					+ ": " + mHistoricDetail.getExecutionId() + ": "
					+ mHistoricDetail.getId() + ": "
					+ mHistoricDetail.getProcessInstanceId() + ": "
					+ mHistoricDetail.getTaskId() + ": "
					+ mHistoricDetail.getTime());
		}
		assertNotNull(historyVariables);
		assertEquals(nVariables, historyVariables.size());
		// HistoricVariableUpdate update0 = ((HistoricVariableUpdate)
		// historyVariables
		// .get(5));
		// assertEquals("myVar", update0.getVariableName());
		// LoanApplication la = (LoanApplication) loanAppUpdate.getValue();
		// assertEquals(true, la.isCreditCheckOk());
		HistoricVariableUpdate update;
		System.out.println("=======================================");
		int iter = 0;
		for (int index = 0; index < historyVariables.size(); index++) {
			update = ((HistoricVariableUpdate) historyVariables.get(index));

			System.out.println(update.getVariableName() + " : "
					+ update.getValue());

			// this checks that the assignment based on the process was correct
			// might be nicer way to do this
			if (update.getVariableName().equals("myVar")) {
				String myVar = (String) update.getValue();
				System.out.println("checking group");

				if (currentGroupList.get(iter) >= 5) {
					assertEquals(myVar, "test456");
				} else {
					assertEquals(myVar, "test789");
				}
				iter++;
			}

		}
	}
}

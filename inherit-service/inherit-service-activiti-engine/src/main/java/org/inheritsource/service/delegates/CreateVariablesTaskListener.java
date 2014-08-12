/* == Motrice Copyright Notice == 
 * 
 * Motrice Service Platform 
 * 
 * Copyright (C) 2011-2014 Motrice AB 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Affero General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Affero General Public License for more details. 
 * 
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>. 
 * 
 * e-mail: info _at_ motrice.se 
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 * phone: +46 8 641 64 14 
 */

package org.inheritsource.service.delegates;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.inheritsource.service.form.FormEngine;
import org.inheritsource.service.orbeon.OrbeonService;

public class CreateVariablesTaskListener implements TaskListener {

	/**
	 * 
	 */
	public static final Logger log = LoggerFactory
			.getLogger(CreateVariablesTaskListener.class.getName());
	private static final long serialVersionUID = -3696696981293315576L;

	public void notify(DelegateTask delegateTask) {
		String taskName = delegateTask.getName();
		log.info("taskName = " + taskName);
		log.info("CreateVariablesTaskListener called from "
				+ delegateTask.getProcessInstanceId() + " at " + new Date());

		log.warn("activityId = null     ");

		String persistenceApiBaseUrl = "http://localhost:8080/exist/rest/db/orbeon-pe/fr/";

		Map<String, Object> localVariables = delegateTask.getVariablesLocal();

		String taskFormDataUri = (String) localVariables
				.get(FormEngine.FORM_DATA_URI);
		String taskFormPath = (String) localVariables
				.get(FormEngine.FORM_DEFINITIONKEY);
		String taskFormInstanceId = (String) localVariables
				.get(FormEngine.FORM_INSTANCEID);
		FormEngine.log.info("taskFormDataUri = " + taskFormDataUri);
		log.info("taskFormPath = " + taskFormPath);
		log.info("taskFormInstanceId" + taskFormInstanceId);

		String dataUri = persistenceApiBaseUrl + taskFormPath + "/data/"
				+ taskFormInstanceId + "/data.xml";
		log.info("dataUri = " + dataUri);

		if ((taskFormPath == null) || (taskFormInstanceId == null)) {
			// something went wrong, or this is called from a test program
			// where the
			// orbeon form is not available
			log.info("taskFormPath = " + taskFormPath);
			log.info("taskFormInstanceId = " + taskFormInstanceId);
		} else {
			OrbeonService service = new OrbeonService();

			HashMap<String, String> variableMap = service.getVariableMap(
					dataUri, 2);
			log.info("data: " + variableMap);

			for (Map.Entry<String, String> entry : variableMap.entrySet()) {
				log.info("Key : " + entry.getKey() + " Value : "
						+ entry.getValue());
				String newVariableNameUnfiltered = taskName + "_"
						+ entry.getKey();
				// String newVariableName = activityId + "_" + entry.getKey();
				// temporary solution to get the type of the
				// variable from the orbeon form.
				// Use the name of the field.
				// Only long implemented so far

				// regexp to remove some unwanted characters
				String regex = "[^a-zA-Z0-9_]"; //
				String newVariableName = newVariableNameUnfiltered.replaceAll(
						"-", "_").replaceAll(regex, "");

				String typeDescription = "_long";
				int indexFirst = newVariableName.length()
						- typeDescription.length();
				int indexLast = newVariableName.length();
				if (indexFirst >= 0) {
					String typeCandidate = newVariableName.substring(
							indexFirst, indexLast);
					if (typeDescription.equals(typeCandidate)) {
						delegateTask.setVariable(newVariableName,
								Long.valueOf(entry.getValue()));
						log.info("new process variable (Long) : "
								+ newVariableName + " created.");
					} else {
						delegateTask.setVariable(newVariableName,
								entry.getValue());
						log.info("new process variable : " + newVariableName
								+ " created.");
					}

				} else {
					delegateTask.setVariable(newVariableName, entry.getValue());
					log.info("new process variable : " + newVariableName
							+ " created.");
				}
			}
		}
	}
} // delegateTask.setVariableLocal("mylocalVariable", "Variable value");


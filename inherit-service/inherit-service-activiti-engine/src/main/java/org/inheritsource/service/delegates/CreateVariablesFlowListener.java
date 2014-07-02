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

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.inheritsource.service.form.FormEngine;
import org.inheritsource.service.orbeon.OrbeonService;

public class CreateVariablesFlowListener implements ExecutionListener {

	public static final Logger log = LoggerFactory.getLogger(CreateVariablesFlowListener.class.getName());
	private static final long serialVersionUID = 593974194188510980L;

	public void notify(DelegateExecution execution) {
		String activityName = execution.getCurrentActivityName();
		String activityId = execution.getCurrentActivityId();
		log.info(
		"CreateVariablesFlowListener called from "
				+ activityName + " with id " + activityId + " in process "
				+ execution.getProcessInstanceId() + " at " + new Date());

		if (activityId == null) {
			log.warn("activityId = null     ");
		} else {
			String persistenceApiBaseUrl = "http://localhost:8080/exist/rest/db/orbeon-pe/fr/";

			Map<String, Object> localVariables = execution.getVariablesLocal();

			String startFormDataUri = (String) localVariables
					.get(FormEngine.START_FORM_DATA_URI);
			String startFormPath = (String) localVariables
					.get(FormEngine.START_FORM_DEFINITIONKEY);
			String startFormInstanceId = (String) localVariables
					.get(FormEngine.START_FORM_INSTANCEID);

			log.info( "startFormDataUri = " + startFormDataUri);
			log.info( "startFormPath = " + startFormPath);
			log.info( "startFormInstanceId" + startFormInstanceId);

			String dataUri = persistenceApiBaseUrl + startFormPath + "/data/"
					+ startFormInstanceId + "/data.xml";
			log.info("dataUri = " + dataUri);

			if ((startFormPath == null) || (startFormInstanceId == null)) {
				// something went wrong, or this is called from a test program
				// where the
				// orbeon form is not available
				log.info(          "startFormPath = " + startFormPath);
				log.info(          "startFormInstanceId = "
						+ startFormInstanceId);
			} else {
				OrbeonService service = new OrbeonService();

				HashMap<String, String> variableMap = service.getVariableMap(
						dataUri, 2);
				log.info(          "data: " + variableMap);

				for (Map.Entry<String, String> entry : variableMap.entrySet()) {
					log.info(          "Key : " + entry.getKey() + " Value : "
							+ entry.getValue());
					String newVariableName = activityId + "_" + entry.getKey();
					// temporary solution to get the type of the
					// variable from the orbeon form.
					// Use the name of the field.
					// Only long implemented so far

					String typeDescription = "_long";
					int indexFirst = newVariableName.length()
							- typeDescription.length();
					int indexLast = newVariableName.length();
					if (indexFirst >= 0) {
						String typeCandidate = newVariableName.substring(
								indexFirst, indexLast);
						if (typeDescription.equals(typeCandidate)) {
							execution.setVariable(newVariableName,
									Long.valueOf(entry.getValue()));
							log.info(          "new process variable (Long) : "
									+ newVariableName + " created.");
						} else {
							execution.setVariable(newVariableName,
									entry.getValue());
							log.info(          "new process variable : "
									+ newVariableName + " created.");
						}

					} else {
						execution
								.setVariable(newVariableName, entry.getValue());
						log.info(          "new process variable : "
								+ newVariableName + " created.");
					}
				}
			}
		}
	}

}

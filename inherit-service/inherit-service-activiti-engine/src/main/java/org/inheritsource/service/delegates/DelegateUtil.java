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
 *
 */ 
 
package org.inheritsource.service.delegates;

import org.inheritsource.service.form.FormEngine;

public class DelegateUtil {
	
	/*
	 * calc a process variable name with the taskId included, use this in order
	 * to store a task variable as a process variable. Activiti does not work
	 * properly if task variables is set at an ExecutionListener's end event 
	 */
	public static String calcTaskVariableName(String variableName, String taskId) {
		return variableName + "[" + taskId + "]";
	}

}

package org.inheritsource.service.common.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

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

@XStreamAlias("ProcessDefinitionDetails")
public class ProcessDefinitionDetails implements Serializable {

	private static final long serialVersionUID = 6783807984503688411L;

	private ProcessDefinitionInfo process = null;
	private Set<ActivityDefinitionInfo> activities = new HashSet<ActivityDefinitionInfo>();
	
	public ProcessDefinitionDetails() {
		
	}
	
	public ProcessDefinitionInfo getProcess() {
		return process;
	}

	public void setProcess(ProcessDefinitionInfo process) {
		this.process = process;
	}

	public Set<ActivityDefinitionInfo> getActivities() {
		return activities;
	}

	public void setActivities(Set<ActivityDefinitionInfo> activities) {
		this.activities = activities;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activities == null) ? 0 : activities.hashCode());
		result = prime * result + ((process == null) ? 0 : process.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessDefinitionDetails other = (ProcessDefinitionDetails) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcessDefinitionDetails [process=" + process + ", activities="
				+ activities + "]";
	}

		
	
		
}

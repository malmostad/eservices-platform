package org.inheritsource.service.common.domain;

import java.io.Serializable;

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

@XStreamAlias("ProcessDefinitionInfo")
public class ProcessDefinitionInfo implements Serializable {
	
	private static final long serialVersionUID = -6992366367865872101L;
	
	private String uuid;
	private String name;
	private String label;
	
	public ProcessDefinitionInfo() {
		
	}
	
	public ProcessDefinitionInfo(String uuid, String name, String label) {
		this.uuid = uuid;
		this.name = name;
		this.label = label;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		ProcessDefinitionInfo other = (ProcessDefinitionInfo) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcessDefinitionInfo [uuid=" + uuid + ", name=" + name
				+ ", label=" + label + "]";
	}
		
}

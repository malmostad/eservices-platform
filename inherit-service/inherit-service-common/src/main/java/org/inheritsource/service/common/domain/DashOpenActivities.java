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
 
package org.inheritsource.service.common.domain;

import java.io.Serializable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DashOpenActivities")
public class DashOpenActivities implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	int onTrack;
	int atRisk;	
	int overdue;
	
	public DashOpenActivities() {
		
	}

	public int getOnTrack() {
		return onTrack;
	}

	public void setOnTrack(int onTrack) {
		this.onTrack = onTrack;
	}

	public int getAtRisk() {
		return atRisk;
	}

	public void setAtRisk(int atRisk) {
		this.atRisk = atRisk;
	}

	public int getOverdue() {
		return overdue;
	}

	public void setOverdue(int overdue) {
		this.overdue = overdue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + atRisk;
		result = prime * result + onTrack;
		result = prime * result + overdue;
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
		DashOpenActivities other = (DashOpenActivities) obj;
		if (atRisk != other.atRisk)
			return false;
		if (onTrack != other.onTrack)
			return false;
		if (overdue != other.overdue)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DashOpenActivities [onTrack=" + onTrack + ", atRisk=" + atRisk
				+ ", overdue=" + overdue + "]";
	}
	
	
}

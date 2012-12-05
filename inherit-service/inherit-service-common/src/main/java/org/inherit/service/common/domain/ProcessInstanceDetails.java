package org.inherit.service.common.domain;

import java.util.ArrayList;
import java.util.List;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
Copyright (C) 2012 Inherit S AB

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

@XStreamAlias("ProcessInstanceDetails")
public class ProcessInstanceDetails extends ProcessInstanceListItem {

	private static final long serialVersionUID = 5176472811760050085L;

	private List<ActivityInstancePendingItem> pending = new ArrayList<ActivityInstancePendingItem>();
	private Timeline timeline = new Timeline();
	
	public ProcessInstanceDetails() {
		
	}
	
	public List<ActivityInstancePendingItem> getPending() {
		return pending;
	}

	public void setPending(List<ActivityInstancePendingItem> pending) {
		this.pending = pending;
	}
	
	public void addActivityInstanceItem(ActivityInstanceItem item) {
		if (item instanceof ActivityInstancePendingItem) {
			pending.add((ActivityInstancePendingItem)item);
		}
		else if (item instanceof ActivityInstanceLogItem) {
			timeline.add((ActivityInstanceLogItem)item);
		}
	}

	public Timeline getTimeline() {
		return timeline;
	}

	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((pending == null) ? 0 : pending.hashCode());
		result = prime * result
				+ ((timeline == null) ? 0 : timeline.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessInstanceDetails other = (ProcessInstanceDetails) obj;
		if (pending == null) {
			if (other.pending != null)
				return false;
		} else if (!pending.equals(other.pending))
			return false;
		if (timeline == null) {
			if (other.timeline != null)
				return false;
		} else if (!timeline.equals(other.timeline))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcessInstanceDetails [pending=" + pending + ", timeline="
				+ timeline + "]";
	}

	
}

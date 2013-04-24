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

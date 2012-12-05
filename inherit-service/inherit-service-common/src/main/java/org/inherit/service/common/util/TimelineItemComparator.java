package org.inherit.service.common.util;

import java.util.Comparator;
import java.util.Date;
import java.util.logging.Logger;

import org.inherit.service.common.domain.TimelineItem;

public class TimelineItemComparator<T> implements Comparator<T> {
	
	public static final Logger log = Logger.getLogger(TimelineItemComparator.class.getName());
	
	@Override
	public int compare(T o1, T o2) {
		//log.severe("======> o1=" + o1 + " cmp with o2=" + o2);
		int result = 0;
	
		Date t1 = null;
		Date t2 = null;
		
		if (o1 != null && o1 instanceof TimelineItem) {
			t1 = ((TimelineItem)o1).getTimestamp();
		}

		if (o2 != null && o2 instanceof TimelineItem) {
			t2 = ((TimelineItem)o2).getTimestamp();
		}

		if (t1!=null) {
			if (t2==null) {
				result = 1;
			}
			else {
				result = -t1.compareTo(t2);
			}
		}
		else if (t2!=null) {
			if (t1 == null) {
				result = -1;
			} 
		}
		
		return result;
	}

}

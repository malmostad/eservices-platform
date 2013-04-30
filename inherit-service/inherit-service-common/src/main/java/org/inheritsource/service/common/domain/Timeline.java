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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.apache.commons.lang3.time.DateUtils;
import org.inheritsource.service.common.util.TimelineItemComparator;

/**
 * Not thread safe Timeline 
 * @author bjmo
 *
 */
public class Timeline {
	public static final Logger log = Logger.getLogger(Timeline.class.getName());
	
	private List<TimelineItem> items = new ArrayList<TimelineItem>();
	private boolean sorted;
	
	/**
	 * Sort timeline items by timestamp, most recent timestamp first
	 */
	public void sort() {
		TimelineItemComparator<TimelineItem> comparator = new TimelineItemComparator<TimelineItem>();
		Collections.sort(items, comparator);
		sorted = true;
	}
	
	public void add(List<TimelineItem> newItems) {
		items.addAll(newItems);
		sorted = false;
	}
	
	public void add(TimelineItem newItem) {
		items.add(newItem);
		sorted = false;
	}
	
	public void addAndSort(List<TimelineItem> newItems) {
		items.addAll(newItems);
		sort();
	}
	
	public void addAndSort(TimelineItem newItem) {
		items.add(newItem);
		sort();
	}
	
	public List<TimelineItem> getItems() {
		return items;
	}

	public void setItems(List<TimelineItem> items) {
		this.items = items;
	}

	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	public TreeMap<Date, List<TimelineItem>> getTimelineByDay() {
		TreeMap<Date, List<TimelineItem>> timelineByDay = new TreeMap<Date, List<TimelineItem>>(new Comparator<Date>() {
			@Override
			public int compare(Date o1, Date o2) {
				int result = 0;
				if (o1==null) {
					if (o2==null) {
						return 0;
					}
					else {
						result = 1;
					}
				}
				else {
					if (o2==null) {
						return -1;
					}
					else {
						result = -o1.compareTo(o2);
					}
				}
				return result;
			}
		} );
		
		if (!sorted) {
			sort();
		} 
		
		Date keyDay = null;
		List<TimelineItem> dayItems = null;
		if (items != null) {
			for (TimelineItem item : items) {
				if (item != null) {
					if (item.getTimestamp()==null) {
						log.severe("Timestamp in TimelineItem is not expected to be null: " + item);
					}
					else {
						if (keyDay==null || !DateUtils.isSameDay(keyDay, item.getTimestamp())) {
							// new day in Map => create new day in map and a new list for that day
							keyDay = DateUtils.truncate(item.getTimestamp(), Calendar.DAY_OF_MONTH);
							dayItems = new ArrayList<TimelineItem>();
							timelineByDay.put(keyDay, dayItems);
						}
						// always add item to current day in map
						if (dayItems != null) {
							dayItems.add(item);
						}
						else {
							log.severe("dayItems should never be null on add");
						}
					}
				}
			}
		}
		
		return timelineByDay;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + (sorted ? 1231 : 1237);
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
		Timeline other = (Timeline) obj;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (sorted != other.sorted)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Timeline [items=" + items + ", sorted=" + sorted + "]";
	}
	
}

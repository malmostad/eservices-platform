package org.inherit.service.common.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.apache.commons.lang3.time.DateUtils;
import org.inherit.service.common.util.TimelineItemComparator;

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
		TreeMap<Date, List<TimelineItem>> timelineByDay = new TreeMap<Date, List<TimelineItem>>();
		
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

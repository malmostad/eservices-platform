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
 
package org.inheritsource.service.common.util;

import java.util.Comparator;
import java.util.Date;
import java.util.logging.Logger;

import org.inheritsource.service.common.domain.TimelineItem;

public class TimelineItemComparator<T> implements Comparator<T> {
	
	public static final Logger log = Logger.getLogger(TimelineItemComparator.class.getName());
	
	@Override
	public int compare(T o1, T o2) {
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

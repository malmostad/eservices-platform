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

import java.util.Date;

public interface TimelineItem {
	
	public static final int TYPE_UNKNOWN = 0;	
	public static final int TYPE_CREATE = 1;
	public static final int TYPE_EDIT = 2;
	public static final int TYPE_DELETE = 3;
	public static final int TYPE_FINISHED = 4;
	public static final int TYPE_COMMENT = 5;
	
	/**
	 * used to sort timeline
	 * @return
	 */
	public Date getTimestamp();
	
	/**
	 * Can be used to view text description or icon in gui
	 * @return
	 */
	public int getType();
	
	/**
	 * Brief description which can be viewed in timeline. Keep it short i.e. <150 characters (max a line or two)
	 * @return
	 */
	public String getBriefDescription();
	
	/**
	 * Description which can be viewed in timeline as first level in-depth information, before viewUrl info. 
	 * @return
	 */
	public String getDescription();

	/**
	 * URL to view more information about this item. 
	 * @return
	 */
	public String getViewUrl();
	
	public UserInfo getUser();	
	
}

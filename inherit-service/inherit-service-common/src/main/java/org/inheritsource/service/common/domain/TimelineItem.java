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

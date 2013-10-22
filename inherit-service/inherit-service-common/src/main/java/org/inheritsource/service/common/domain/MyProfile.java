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

@XStreamAlias("MyProfile")
public class MyProfile implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1882549459619270243L;

	private String uuid;
	private String email;
	
	public MyProfile() {
	}

	public MyProfile(String myUuid) {
		uuid=myUuid;
	}

	public String getUuid() {
		return uuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

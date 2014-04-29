/* == Motrice Copyright Notice == 
 * 
 * Motrice Service Platform 
 * 
 * Copyright (C) 2011-2014 Motrice AB 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Affero General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Affero General Public License for more details. 
 * 
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>. 
 * 
 * e-mail: info _at_ motrice.se 
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 * phone: +46 8 641 64 14 
 
 */ 
 
package org.inheritsource.service.common.domain;

import javax.naming.*;
import javax.naming.directory.*;
import java.io.Serializable;

public class UserDirectoryEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3570218626429848060L;

	private String cn;
	private String givenName;
	private String sn;
	private String mail;
	private String department;
	private String company;

	public UserDirectoryEntry() {
	}

	public UserDirectoryEntry(String cn,
			String givenName,
			String sn,
			String mail,
			String department,
			String company)    {
		this.cn = cn;
		this.givenName = givenName;
		this.sn = sn;
		this.mail = mail;
		this.department = department;
		this.company = company;
	}

	public String getCn()         {return cn;}
	public String getGivenName()  {return givenName;}
	public String getSn()         {return sn;}
	public String getMail()       {return mail;}
	public String getDepartment() {return department;}
	public String getCompany()    {return company;}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public void setCn(Attribute cn) {
		if ( cn == null ) {
			this.cn = null;
		} else {
			try {
				this.cn = cn.get().toString();
			} catch ( NamingException ne ) {
				ne.printStackTrace();
			}
		}
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public void setGivenName(Attribute givenName)    {
		if (givenName== null) {
			this.givenName = null;
		} else {
			try {
				this.givenName = givenName.get().toString();
			} catch ( NamingException ne ) {
				ne.printStackTrace();
			}
		}
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public void setSn(Attribute sn) {
		if (sn==null) {
			this.sn = null;
		} else {
			try {
				this.sn = sn.get().toString();
			} catch ( NamingException ne ) {
				ne.printStackTrace();
			}
		}
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setMail(Attribute mail)              {
		if (mail == null) {
			this.mail = null;
		} else {
			try {
				this.mail = mail.get().toString();
			} catch ( NamingException ne ) {
				ne.printStackTrace();
			}
		}
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setDepartment(Attribute department)  {
		if (department == null) {
			this.department = null;
		} else {
			try {
				this.department = department.get().toString();
			} catch ( NamingException ne ) {
				ne.printStackTrace();
			}
		}
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setCompany(Attribute company)        {
		if (company == null) {
			this.company = null;
		} else {
			try {
				this.company = company.get().toString();
			} catch ( NamingException ne ) {
				ne.printStackTrace();
			}
		}
	}

	public String getLabel() {
		return givenName + " " + sn;
	}
	
	public String getShortLabel() {
		return cn;
	}
	
	public void print() {
		System.out.println( "User Entry: ") ;
		System.out.println( "  cn:        \t" + ( cn == null ? "null": cn )) ;
		System.out.println( "  givenName: \t" + ( givenName == null ? "null" : givenName )) ;
		System.out.println( "  sn:        \t" + ( sn == null ? "null" : sn )) ;
		System.out.println( "  mail:      \t" + ( mail == null ? "null": mail )) ;
		System.out.println( "  department:\t" + ( department == null ? "null" : department )) ;
		System.out.println( "  company:   \t" + ( company == null ? "null" : company )) ;
		System.out.println("") ;
	}
}

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
 
package org.inheritsource.taskform.engine.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mtf_tag_type")
public class TagType {

	// system defined tag types
	
	/**
	 * Id number in diary/journal
	 */
	public static final Long TAG_TYPE_DIARY_NO = new Long(1);
	
	/**
	 * Use this tag to mark that an userId has submitted an application 
	 * in order to make a fast search possibly
	 */
	public static final Long TAG_APPLICATION_BY = new Long(2);

	/**
	 * Use this tag to mark that an userId has submitted an application 
	 * in order to make a fast search possibly
	 */
	public static final Long TAG_OTHER = new Long(10000);

	@Id
	Long tagTypeId;
	
	@Column(unique=true, nullable=false)
	String name;

	// TODO should be multi lingual?
	@Column(nullable=false)
	String label;
	
	public TagType() {
		
	}

	public Long getTagTypeId() {
		return tagTypeId;
	}

	public void setTagTypeId(Long tagTypeId) {
		this.tagTypeId = tagTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((tagTypeId == null) ? 0 : tagTypeId.hashCode());
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
		TagType other = (TagType) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tagTypeId == null) {
			if (other.tagTypeId != null)
				return false;
		} else if (!tagTypeId.equals(other.tagTypeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TagType [tagTypeId=" + tagTypeId + ", name=" + name
				+ ", label=" + label + "]";
	}
	
	
}

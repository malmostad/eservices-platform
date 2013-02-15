package org.inherit.taskform.engine.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
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

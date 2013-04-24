package org.inheritsource.taskform.engine.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserEntity {
	
	public static final int CATEGORY_UNKNOWN = 0;

	@Id
	@GeneratedValue
	Long userId;

	/**
	 * platform generated uuid which identifies the user in the system 
	 */
	@Column(unique=true, nullable=false)
	String uuid;
	
	@Column(unique=false, nullable=false)
	int category = CATEGORY_UNKNOWN;
	
	/**
	 * commonName
	 */
	@Column(unique=false, nullable=false)
	String cn;
	
	/**
	 * Distinguished Name
	 */
	@Column(unique=false, nullable=true)
	String dn;

	/**
	 * E-leg Serial
	 */
	@Column(unique=false, nullable=true)
	String serial;

	/**
	 * First or given name
	 */
	@Column(unique=false, nullable=true)
	String gn;
	
	/**
	 * surname or family name  
	 */
	@Column(unique=false, nullable=true)
	String sn;
	
	public UserEntity() {
		
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getGn() {
		return gn;
	}

	public void setGn(String gn) {
		this.gn = gn;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 
	 * @return A label to be presented in GUI etc
	 */
	public String getLabelShort() {
		String result = cn;
		
		/**
		 * Append serial if it exists (personnr from eleg)
		 */
		if (serial != null && serial.trim().length()>0) {
			result = serial;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @return A label to be presented in GUI etc
	 */
	public String getLabel() {
		String result = cn;
		
		/**
		 * Append serial if it exists (personnr from eleg)
		 */
		if (serial != null && serial.trim().length()>0) {
			result += " (" + serial + ")";
		}
		
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + category;
		result = prime * result + ((cn == null) ? 0 : cn.hashCode());
		result = prime * result + ((dn == null) ? 0 : dn.hashCode());
		result = prime * result + ((gn == null) ? 0 : gn.hashCode());
		result = prime * result + ((serial == null) ? 0 : serial.hashCode());
		result = prime * result + ((sn == null) ? 0 : sn.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		UserEntity other = (UserEntity) obj;
		if (category != other.category)
			return false;
		if (cn == null) {
			if (other.cn != null)
				return false;
		} else if (!cn.equals(other.cn))
			return false;
		if (dn == null) {
			if (other.dn != null)
				return false;
		} else if (!dn.equals(other.dn))
			return false;
		if (gn == null) {
			if (other.gn != null)
				return false;
		} else if (!gn.equals(other.gn))
			return false;
		if (serial == null) {
			if (other.serial != null)
				return false;
		} else if (!serial.equals(other.serial))
			return false;
		if (sn == null) {
			if (other.sn != null)
				return false;
		} else if (!sn.equals(other.sn))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", uuid=" + uuid + ", category="
				+ category + ", cn=" + cn + ", dn=" + dn + ", serial=" + serial
				+ ", gn=" + gn + ", sn=" + sn + "]";
	}
	
	
}

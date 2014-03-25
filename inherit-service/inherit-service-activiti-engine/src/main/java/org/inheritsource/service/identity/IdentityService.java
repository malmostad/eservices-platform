package org.inheritsource.service.identity;

import java.util.Set;

import org.inheritsource.service.common.domain.UserInfo;

public interface IdentityService {
	public Set<String> getUsersByRoleAndActivity(String roleName, String activityInstanceUuid);

	public UserInfo getUserByUuid(String uuid);

	public UserInfo getUserByDn(String dn);

	public UserInfo getUserBySerial(String serial,	String certificateSubject);
}

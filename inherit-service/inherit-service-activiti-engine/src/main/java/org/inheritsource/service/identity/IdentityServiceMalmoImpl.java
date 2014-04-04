package org.inheritsource.service.identity;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.inheritsource.service.common.domain.UserDirectoryEntry;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.taskform.engine.persistence.TaskFormDb;
import org.inheritsource.taskform.engine.persistence.entity.UserEntity;

public class IdentityServiceMalmoImpl implements IdentityService {
	
	public static final Logger log = Logger.getLogger(IdentityServiceMalmoImpl.class.getName());
			
	ActorSelectorDirUtils aSelectorDirUtils;
	TaskFormDb taskFormDb;
	UserDirectoryService userDirectoryService;

	public UserDirectoryService getUserDirectoryService() {
		return userDirectoryService;
	}

	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}


	public ActorSelectorDirUtils getaSelectorDirUtils() {
		return aSelectorDirUtils;
	}

	public void setaSelectorDirUtils(ActorSelectorDirUtils aSelectorDirUtils) {
		this.aSelectorDirUtils = aSelectorDirUtils;
	}

	public TaskFormDb getTaskFormDb() {
		return taskFormDb;
	}

	public void setTaskFormDb(TaskFormDb taskFormDb) {
		this.taskFormDb = taskFormDb;
	}

	/* (non-Javadoc)
	 * @see org.inheritsource.service.identity.Tmp#getUsersByRoleAndActivity(java.lang.String, java.lang.String)
	 */
	@Override
	public Set<String> getUsersByRoleAndActivity(String roleName,
			String activityInstanceUuid) {
		// TODO implement getDepartmentByactivityInstanceUuid, for now just
		// hardwire "Miljöförvaltningen"
		Set<String> result = null;
		try {
			result = aSelectorDirUtils.getUsersByDepartmentAndRole(
					"Miljöförvaltningen", roleName);
		} catch (Exception e) {
			log.severe("getUsersByRoleAndActivity roleName" + 	roleName + 
					" activityInstanceUuid=" + activityInstanceUuid + 
					"Exception: " + e.toString());
		}

		return result;
	}

	@Override
	public UserInfo getUserByUuid(String uuid) {
		UserInfo userInfo = taskFormDb.getUserByUuid(uuid);
		log.info("getUserByUuid: " + uuid);
		if ( userInfo == null && userDirectoryService != null && uuid!=null) {
			UserDirectoryEntry ue = userDirectoryService.lookupUserByCn(uuid);
			userInfo = new UserInfo();
			userInfo.setUuid(ue.getCn());
			userInfo.setLabel(ue.getLabel());
			userInfo.setLabelShort(ue.getCn());
		}
		if (userInfo == null) {
			userInfo = new UserInfo();
			userInfo.setUuid(UserInfo.ANONYMOUS_UUID);
			userInfo.setLabel(UserInfo.ANONYMOUS_UUID);
			userInfo.setLabelShort(UserInfo.ANONYMOUS_UUID);
			
		}		
		return userInfo;
	}
	
	/* (non-Javadoc)
	 * @see org.inheritsource.service.identity.Tmp#getUserByDn(java.lang.String)
	 */
	@Override
	public UserInfo getUserByDn(String dn) {
		UserInfo userInfo = taskFormDb.getUserByDn(dn);

		if (userInfo == null) {
			// new user in system

			String gn = null, sn = null, cn = null;
			String uuid = java.util.UUID.randomUUID().toString();
			String serial = null;

			// try to parse CN
			StringTokenizer st = new StringTokenizer(dn, ",");
			while (st.hasMoreTokens()) {
				String s = st.nextToken();

				StringTokenizer valuePairToken = new StringTokenizer(s, "=");
				String key = null;
				String value = null;
				if (valuePairToken.hasMoreTokens()) {
					key = valuePairToken.nextToken().trim();
				}
				if (valuePairToken.hasMoreTokens()) {
					value = valuePairToken.nextToken().trim();
				}
				if (!valuePairToken.hasMoreTokens()) {
					if ("CN".equalsIgnoreCase(key)) {
						cn = value;
					}
				}

			}

			if (cn != null && cn.trim().length() > 0) {
				// use cn as uuid...
				uuid = cn;
			}

			// store user
			UserEntity user = new UserEntity();
			user.setCategory(UserInfo.CATEGORY_INTERNAL);
			user.setSerial(serial);
			user.setCn(cn);
			user.setGn(gn);
			user.setSn(sn);
			user.setDn(dn);
			user.setUuid(uuid);
			userInfo = taskFormDb.createUser(user);
		}

		return userInfo;
	}

	/* (non-Javadoc)
	 * @see org.inheritsource.service.identity.Tmp#getUserBySerial(java.lang.String, java.lang.String)
	 */
	@Override
	public UserInfo getUserBySerial(String serial, String certificateSubject) {
		UserInfo userInfo = taskFormDb.getUserBySerial(serial);

		if (userInfo == null) {
			// new user in system

			String gn = null, sn = null, cn = null;
			String uuid = java.util.UUID.randomUUID().toString();
			String dn = null;

			// try to parse certificateSubject
			StringTokenizer st = new StringTokenizer(certificateSubject, ",");
			while (st.hasMoreTokens()) {
				String s = st.nextToken();

				StringTokenizer valuePairToken = new StringTokenizer(s, "=");
				String key = null;
				String value = null;
				if (valuePairToken.hasMoreTokens()) {
					key = valuePairToken.nextToken().trim();
				}
				if (valuePairToken.hasMoreTokens()) {
					value = valuePairToken.nextToken().trim();
				}
				if (!valuePairToken.hasMoreTokens()) {
					if ("CN".equalsIgnoreCase(key)) {
						cn = value;
					}
					if ("GIVENNAME".equalsIgnoreCase(key)) {
						gn = value;
					}
					if ("SURNAME".equalsIgnoreCase(key)) {
						sn = value;
					}
				}

			}
			// store user
			UserEntity user = new UserEntity();
			user.setCategory(UserInfo.CATEGORY_EXTERNAL);
			user.setSerial(serial);
			user.setCn(cn);
			user.setGn(gn);
			user.setSn(sn);
			user.setDn(dn);
			user.setUuid(uuid);
			userInfo = taskFormDb.createUser(user);


		}

		return userInfo;
	}
}

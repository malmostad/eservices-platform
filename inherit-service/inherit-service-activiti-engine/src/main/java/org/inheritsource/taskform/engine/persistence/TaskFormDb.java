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
 
 
package org.inheritsource.taskform.engine.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.inheritsource.service.common.domain.FormInstance;
import org.inheritsource.service.common.domain.StartForm;
import org.inheritsource.service.common.domain.Tag;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.taskform.engine.persistence.entity.ActivityFormDefinition;
import org.inheritsource.taskform.engine.persistence.entity.ProcessActivityFormInstance;
import org.inheritsource.taskform.engine.persistence.entity.ProcessActivityTag;
import org.inheritsource.taskform.engine.persistence.entity.StartFormDefinition;
import org.inheritsource.taskform.engine.persistence.entity.TagType;
import org.inheritsource.taskform.engine.persistence.entity.UserEntity;

public class TaskFormDb {
	
	public static final Logger log = LoggerFactory.getLogger(TaskFormDb.class.getName());
		
	public TaskFormDb() {
		
	}

	public StartFormDefinition getStartFormDefinition(Session session, Long id) {
		return (StartFormDefinition)session.load(StartFormDefinition.class, id);
	}
	
	public StartFormDefinition getStartFormDefinition(Long id) {
		StartFormDefinition result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			result = getStartFormDefinition(session, id);
		}
		catch (Exception e) {
			log.error("id=[" + id + "] Exception: " + e.toString());
		}
		finally {		
			session.close();
		}
		return result;
	}

	public List<StartForm> getStartForms() {
		List<StartForm> result = new ArrayList<StartForm>();
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			List<StartFormDefinition> startForms = (List<StartFormDefinition>) session.createCriteria(StartFormDefinition.class)
				    .list();
			
			for (StartFormDefinition startFormDef : startForms) {
				StartForm form = new StartForm();
				form.setTypeId(startFormDef.getFormTypeId());
				form.setDefinitionKey(startFormDef.getFormConnectionKey());
				result.add(form);
			}
		}
		catch (Exception e) {
			log.error("Exception: " + e.toString());
		}
		finally {		
			session.close();
		}
		return result;
	}

	
	public StartFormDefinition getStartFormDefinitionByFormPath(Session session, String formPath) {
		List<StartFormDefinition> result =  (List<StartFormDefinition>)session.createCriteria(StartFormDefinition.class).add(Restrictions.eq("formConnectionKey", formPath)).add(Restrictions.eq("formTypeId", new Long(1))).list();
		
		return this.filterUniqueStartFormDefinitionFromList(result);
	}
	
	public StartFormDefinition getStartFormDefinitionByFormPath(String formPath) throws Exception {
		StartFormDefinition result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			result = getStartFormDefinitionByFormPath(session, formPath);
		}
		catch (Exception e) {
			log.error("formPath=[" + formPath + "] Exception: " + e.toString());
			throw e;
		}
		finally {		
			session.close();
		}
		return result;
	}	
	
	public ProcessActivityFormInstance getStartProcessActivityFormInstanceByFormPathAndUser(String formPath, String userId) {
		List<ProcessActivityFormInstance> result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			result = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
					.add(Restrictions.eq("formConnectionKey", formPath))// identifies the start form
					.add(Restrictions.eq("formTypeId", new Long(1))) // orbeon
				    .add( Restrictions.isNull("activityInstanceUuid")) // only start forms
				    .add( Restrictions.isNull("submitted")) // only not submitted forms
				    .add( Restrictions.eq("userId", userId) ) // only forms that belongs to user
				    .list();
		}
		catch (Exception e) {
			log.error("formPath=[" + formPath + "] Exception: " + e.toString());
		}
		finally {		
			session.close();
		}
		return filterUniqueProcessActivityFormInstanceFromList(result);
	}
	
	public ProcessActivityFormInstance getProcessStartFormInstanceById(Session session, String processInstanceUuid) {
		List<ProcessActivityFormInstance> result = null;
				
		try {
			//result = (List<ProcessDefinition>)session.createQuery(hql).list();
			result = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
				    .add( Restrictions.eq("processInstanceUuid", processInstanceUuid) )
				    .add( Restrictions.isNull("activityInstanceUuid"))
				    .list();
		}
		catch (Exception e) {
			log.error("processInstanceUuid=[" + processInstanceUuid + "] Exception: " + e.toString());
		}

		return filterUniqueProcessActivityFormInstanceFromList(result);
	}

	public ProcessActivityFormInstance getProcessActivityFormInstanceByFormDocId(String formDocId) {
		ProcessActivityFormInstance result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			result = (ProcessActivityFormInstance) session.createCriteria(ProcessActivityFormInstance.class)
				    .add( Restrictions.eq("formDocId", formDocId) ).uniqueResult();
		}
		catch (Exception e) {
			log.error("formDocId=[" + formDocId + "] Exception: " + e.toString());
		}
		finally {		
			session.close();
		}
		return result;
	}

	private StartFormDefinition filterUniqueStartFormDefinitionFromList(List<StartFormDefinition> list) {
		StartFormDefinition result = null;
		if (list != null) {
			if (list.size() > 0 ) {
				result = list.get(0);
			}
			if (list.size() > 1) {
				log.error("Unique value is expected");
				for (StartFormDefinition o : list) {
					log.error(" value: " + o);
				}
			}
		}
		return result;
	}


	private ProcessActivityFormInstance filterUniqueProcessActivityFormInstanceFromList(List<ProcessActivityFormInstance> list) {
		ProcessActivityFormInstance result = null;
		if (list != null) {
			if (list.size() > 0 ) {
				result = list.get(0);
			}
			if (list.size() > 1) {
				log.error("Unique value is expected");
				for (ProcessActivityFormInstance o : list) {
					log.error(" value: " + o);
				}
			}
		}
		return result;
	}

	public void saveProcessActivityFormInstance(Session session, ProcessActivityFormInstance processActivityFormInstance) {
		session.saveOrUpdate(processActivityFormInstance);		
	}


	public void saveProcessActivityFormInstance(ProcessActivityFormInstance processActivityFormInstance) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			
			saveProcessActivityFormInstance(session, processActivityFormInstance);
			
			session.getTransaction().commit();
		}
		catch (Exception e) {
			log.error("Exception in saveProcessActivityFormInstance: Exception " + e.toString());
		}
		finally {
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessActivityFormInstance> getPendingStartformFormInstances(String userId) {
		List<ProcessActivityFormInstance> result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			//result = (List<ProcessDefinition>)session.createQuery(hql).list();
			result = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
					.add( Restrictions.isNull("processInstanceUuid") ) // is not started start form
					.add( Restrictions.eq("userId", userId) )  // this user is last writer
					.add(Restrictions.isNull("submitted"))     // not submitted
				    .list();
		}
		catch (Exception e) {
			log.error("Exception in getPendingStartformFormInstances: userId="  + userId + " exception: " + e.toString());
		}
		finally {
			session.close();
		}
		return result;
	}
	
	/**
	 * Returns 
	 * 1) General form for actdefId
	 * 2) No form defined (return null).  
	 * @return 
	 */
	public ActivityFormDefinition getActivityFormDefinition( String procdefId, String actdefId) {
		ActivityFormDefinition result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			result = getActivityFormDefinition(session, procdefId, actdefId);
		}
		catch (Exception e) {
			log.error("actdefId=[" + actdefId + "] procdefId=[" + procdefId + "] Exception: " + e.toString());
		}
		finally {
			session.close();
		}
		return result;
	}
		
	@SuppressWarnings("unchecked")
	public ActivityFormDefinition getActivityFormDefinition(Session session, String procdefId, String actdefId) {
		List<ActivityFormDefinition> result = null;
				
		//result = (List<ProcessDefinition>)session.createQuery(hql).list();
		result = (List<ActivityFormDefinition>) session.createCriteria(ActivityFormDefinition.class)
			    .add( Restrictions.eq("procdefId", procdefId) )
			    .add( Restrictions.eq("actdefId", actdefId) )
			    .list();

		return filterUniqueActivityDefinitionFromList(result);
	}
		
    private ActivityFormDefinition filterUniqueActivityDefinitionFromList(List<ActivityFormDefinition> list) {
    	ActivityFormDefinition result = null;
    			
		if (list != null) {
			if (list.size() > 0 ) {
				result = list.get(0);
			}
			if (list.size() > 1) {
				log.error("Unique value is expected");
				for (ActivityFormDefinition o : list) {
					log.error(" value: " + o);
				}
			}
		}
		 
		return result;
	}

	public void save(Object o) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(o);
		session.getTransaction().commit();
		session.close();
	}
	
	private void saveProcessActivityTag(Session session, ProcessActivityTag processActivityTag) {
		session.saveOrUpdate(processActivityTag);		
	}


	public void saveProcessActivityTag(ProcessActivityTag processActivityTag) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		saveProcessActivityTag(session, processActivityTag);
		
		session.getTransaction().commit();
		session.close();
	}
	
	private TagType getTagType(Session session, Long id) {
		TagType result = null;
		
		result = (TagType)session.load(TagType.class, id);
		
		return result;
	}
	
	public Tag addTag(String procinstId, String actinstId, Long tagTypeId, String value, String userId) {
		Tag result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			ProcessActivityTag processActivityTag = new ProcessActivityTag();
			processActivityTag.setType(getTagType(session, tagTypeId));
			processActivityTag.setProcinstId(procinstId);
			processActivityTag.setActinstId(actinstId);
			processActivityTag.setValue(value);
			processActivityTag.setUserId(userId);
			processActivityTag.setTimestamp(new Date());
			saveProcessActivityTag(session, processActivityTag);
			
			result = processActivityTag2Tag(processActivityTag);
			session.getTransaction().commit();
		}
		catch (Exception e) {
			log.error("actinstId=[" + actinstId + "] tagTypeId=[" + tagTypeId + "] value=[" + value  + "] userId=[" + userId + "] Exception: " + e.toString());
		}
		finally {		
			session.close();
		}
		
		return result;
	}
	
	public boolean deleteTag(Long processActivityTagId, String userId) {
		boolean result = false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			session.beginTransaction();
			Object o = session.load(ProcessActivityTag.class, processActivityTagId);
			System.out.println("Delete: " + o);
			session.delete(o);
			session.getTransaction().commit();
			result = true;
		}
		catch (Exception e) {
			log.error("processActivityTagId=[" + processActivityTagId + "] userId=[" + userId + "] Exception: " + e.toString());
		}
		finally {		
			session.close();
		}
		return result;
	}
	
	public boolean deleteTag(String procinstId, String value, String userId) {
		boolean result = false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			session.beginTransaction();
			List<ProcessActivityTag> tags = (List<ProcessActivityTag>) session.createCriteria(ProcessActivityTag.class)
					.add( Restrictions.eq("procinstId", procinstId) )
					.add( Restrictions.eq("value", value) )
				    .list();
			
			if (tags != null && tags.size()>0) {
				for (ProcessActivityTag tag : tags) {
					session.delete(tag);				
				}
				result = true;
			}
			session.getTransaction().commit();
		}
		catch (Exception e) {
			log.error("procinstId=[" + procinstId + "] value=[" + value + "] userId=[" + userId + "] Exception: " + e.toString());
		}
		finally {		
			session.close();
		}
		return result;
	}
	
	public List<Tag> getTagsByProcessInstance(String procinstId) {
		List<Tag> tags = new ArrayList<Tag>();
		
		if (procinstId != null && procinstId.length()>0) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			try {
				List<ProcessActivityTag> result = (List<ProcessActivityTag>) session.createCriteria(ProcessActivityTag.class)
						.add( Restrictions.eq("procinstId", procinstId) ) 
					    .list();
				
				for (ProcessActivityTag pafi : result) {
					tags.add(processActivityTag2Tag(pafi));
				}
			}
			catch (Exception e) {
				log.error("procinstId=[" + procinstId + "] Exception: " + e.toString());
			}
			finally {		
				session.close();
			}
		}
		
		return tags;
	}
	
	public List<String> getProcessInstancesByTag(String tagValue) {
		List<String>  result = new ArrayList<String> ();
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			List<ProcessActivityTag> hitItems = (List<ProcessActivityTag>) session.createCriteria(ProcessActivityTag.class)
					.add( Restrictions.eq("value", tagValue) ) 
				    .list();
			
			if (hitItems != null) {
				for (ProcessActivityTag hitItem : hitItems) {
					result.add(hitItem.getProcinstId());
				}
			}
		}
		catch (Exception e) {
			log.error("tagValue=[" + tagValue + "] Exception: " + e.toString());
		}
		finally {		
			session.close();
		}
		
		return result;
	}
	
	
	
	private Tag processActivityTag2Tag(ProcessActivityTag processActivityTag) {
		Tag tag = new Tag();
		tag.setProcessActivityTagId(processActivityTag.getProcessActivityTagId());
		tag.setTypeId(processActivityTag.getType().getTagTypeId());
		tag.setTypeLabel(processActivityTag.getType().getLabel());
		tag.setValue(processActivityTag.getValue());
		tag.setActinstId(processActivityTag.getActinstId());
		tag.setProcinstId(processActivityTag.getProcinstId());
		return tag;
	} 
	
	public String getUserSerialByUuid(String uuid) {
		String result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			UserEntity user = getUserByUuid(session, uuid);
			if (user != null) {
				result = user.getSerial();
			}
		}
		catch (Exception e) {
			log.error("uuid=[" + uuid + "] Exception: " + e.toString());
		}
		finally {
			session.close();
		}
		return result;
	}
	
	public UserInfo getUserByUuid(String uuid) {
		UserInfo result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			UserEntity user = getUserByUuid(session, uuid);
			result = convertUser(user);
		}
		catch (Exception e) {
			log.error("uuid=[" + uuid + "] Exception: " + e.toString());
		}
		finally {
			session.close();
		}
		return result;
	}
	
	public UserInfo getUserByDn(String dn) {
		UserInfo result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			UserEntity user = getUserByDn(session, dn);
			result = convertUser(user);
		}
		catch (Exception e) {
			log.error("dn=[" + dn + "] Exception: " + e.toString());
		}
		finally {
			session.close();
		}
		return result;
	}
	
	public UserInfo getUserBySerial(String serial) {
		UserInfo result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			UserEntity user = getUserBySerial(session, serial);
			result = convertUser(user);
		}
		catch (Exception e) {
			log.error("serial=[" + serial + "] Exception: " + e.toString());
		}
		finally {
			session.close();
		}
		return result;
	}
	
	private UserInfo convertUser(UserEntity user) {
		UserInfo result = null;
		if (user != null) {
			result = new UserInfo();
			result.setCategory(user.getCategory());
			result.setLabel(user.getLabel());
			result.setLabelShort(user.getLabelShort());
			result.setUuid(user.getUuid());
		}
		return result;
	}
	
	private UserEntity getUserByUuid(Session session, String uuid) throws Exception {
		UserEntity user = null;
		try {
			user = (UserEntity) session.createCriteria(UserEntity.class).add(Restrictions.eq("uuid", uuid)).uniqueResult();
		}
		catch (Exception e) {
			log.error("uuid=[" + uuid + "] Exception: " + e.toString());
			throw e;
		}
		return user;
	}
	
	private UserEntity getUserByDn(Session session, String dn) throws Exception {
		UserEntity user = null;
		try {
			List<UserEntity> users = (List<UserEntity>) session.createCriteria(UserEntity.class)
					.add( Restrictions.eq("dn", dn) ) 
				    .list();
			
			if (users != null) {
				if (users.size() > 0) {
					user = users.get(0);
				}
				if (users.size()>1) {
					log.error("dn should identify a unique user. dn=[" + dn + "] identifies " + users.size() + " users");
				}
			}
		}
		catch (Exception e) {
			log.error("dn=[" + dn + "] Exception: " + e.toString());
			throw e;
		}
		
		return user;
	}
	
	private UserEntity getUserBySerial(Session session, String serial) throws Exception {
		UserEntity user = null;
		try {
			List<UserEntity> users = (List<UserEntity>) session.createCriteria(UserEntity.class)
					.add( Restrictions.eq("serial", serial) ) 
				    .list();
			
			if (users != null) {
				if (users.size() > 0) {
					user = users.get(0);
				}
				if (users.size()>1) {
					log.error("serial should identify a unique user. serial=[" + serial + "] identifies " + users.size() + " users");
				}
			}
		}
		catch (Exception e) {
			log.error("serial=[" + serial + "] Exception: " + e.toString());
			throw e;
		}
		
		return user;
	}
	
	public UserInfo createUser(UserEntity user) {
		save(user);
		UserInfo result = this.convertUser(user);
		return result;
	}

	public static void main(String args[]) {
				
		System.out.println("start main load initial data to InheritPlatform database");
		
		TaskFormDb db = new TaskFormDb();
		//db.getStartProcessActivityFormInstanceByFormPathAndUser("miljoforvaltningen/inventeringsprotokoll_pcb_fogmassor--v002", "bjmo");
	    
		System.out.println("2: " + db.getProcessActivityFormInstanceByFormDocId("dba3e535-a5a3-4b02-806c-6a26ef51d4c0").getFormDocId());
		
		System.out.println("end main");
		
	}
	
	
}

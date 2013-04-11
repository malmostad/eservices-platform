package org.inherit.taskform.engine.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.inherit.service.common.domain.Tag;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.taskform.engine.persistence.entity.ActivityFormDefinition;
import org.inherit.taskform.engine.persistence.entity.ProcessActivityFormInstance;
import org.inherit.taskform.engine.persistence.entity.ProcessActivityTag;
import org.inherit.taskform.engine.persistence.entity.StartFormDefinition;
import org.inherit.taskform.engine.persistence.entity.TagType;
import org.inherit.taskform.engine.persistence.entity.UserEntity;

public class TaskFormDb {
	
	public static final Logger log = Logger.getLogger(TaskFormDb.class.getName());
		
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
			log.severe("id=[" + id + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		return result;
	}
	
	public StartFormDefinition getStartFormDefinitionByFormPath(Session session, String formPath) {
		StartFormDefinition result = null;
		result = (StartFormDefinition) session.createCriteria(StartFormDefinition.class).add(Restrictions.eq("formPath", formPath)).uniqueResult();
		return result;
	}
	
	public StartFormDefinition getStartFormDefinitionByFormPath(String formPath) {
		StartFormDefinition result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			result = getStartFormDefinitionByFormPath(session, formPath);
		}
		catch (Exception e) {
			log.severe("formPath=[" + formPath + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		return result;
	}	

	public ProcessActivityFormInstance getSubmittedStartProcessActivityFormInstanceByProcessInstanceUuid(String processInstanceUuid) {
		List<ProcessActivityFormInstance> result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			result = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
				    .add( Restrictions.eq("processInstanceUuid", processInstanceUuid) ) // identifies the start form
				    .add( Restrictions.isNull("activityInstanceUuid")) // only start forms
				    .list();
		}
		catch (Exception e) {
			log.severe("processInstanceUuid=[" + processInstanceUuid + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		return filterUniqueProcessActivityFormInstanceFromList(result);
	}

	
	public ProcessActivityFormInstance getStartProcessActivityFormInstanceByFormPathAndUser(String formPath, String userId) {
		List<ProcessActivityFormInstance> result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			result = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
				    .add( Restrictions.eq("formPath", formPath) ) // identifies the start form
				    .add( Restrictions.isNull("activityInstanceUuid")) // only start forms
				    .add( Restrictions.isNull("submitted")) // only not submitted forms
				    .add( Restrictions.eq("userId", userId) ) // only forms that belongs to user
				    .list();
		}
		catch (Exception e) {
			log.severe("formPath=[" + formPath + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		return filterUniqueProcessActivityFormInstanceFromList(result);
	}

	public ProcessActivityFormInstance getStartProcessActivityFormInstanceByProcessInstanceUuid(String processInstanceUuid) {
		List<ProcessActivityFormInstance> result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			result = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
				    .add( Restrictions.eq("processInstanceUuid", processInstanceUuid) ) // identifies the start form
				    .add( Restrictions.isNull("activityInstanceUuid")) // only start forms
				    .add( Restrictions.isNotNull("submitted")) // only submitted forms
				    .list();
		}
		catch (Exception e) {
			log.severe("processInstanceUuid=[" + processInstanceUuid + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		return filterUniqueProcessActivityFormInstanceFromList(result);
	}
	
   @SuppressWarnings("unchecked")
   public List<ProcessActivityFormInstance> getProcessActivityFormInstances(String processInstanceUuid) {
           List<ProcessActivityFormInstance> result = null;
           
           Session session = HibernateUtil.getSessionFactory().openSession();
           
           try {
                   //result = (List<ProcessDefinition>)session.createQuery(hql).list();
                   result = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
                                   .add( Restrictions.eq("processInstanceUuid", processInstanceUuid) )  // this user is last writer
                                   .list();
           }
           catch (Exception e) {
                   log.severe("Exception in getProcessActivityFormInstances: processInstanceUuid="  + processInstanceUuid + " exception: " + e);
           }
           finally {
                   session.close();
           }
           return result;
   }

	
	public ProcessActivityFormInstance getProcessActivityFormInstanceByActivityInstanceUuid(String activityInstanceUuid) {
		List<ProcessActivityFormInstance> result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			//result = (List<ProcessDefinition>)session.createQuery(hql).list();
			result = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
				    .add( Restrictions.eq("activityInstanceUuid", activityInstanceUuid) )
				    .list();
		}
		catch (Exception e) {
			log.severe("activityInstanceUuid=[" + activityInstanceUuid + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		return filterUniqueProcessActivityFormInstanceFromList(result);
	}

	
	public ProcessActivityFormInstance getProcessActivityFormInstanceById(Long id) {
		List<ProcessActivityFormInstance> result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			result = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
					                                   .add( Restrictions.eq("processActivityFormInstanceId", id) )
				                                       .list();

		}
		catch (Exception e) {
			log.severe("id=[" + id + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		return filterUniqueProcessActivityFormInstanceFromList(result);
	}
	
	public ProcessActivityFormInstance getProcessActivityFormInstanceById(Session session, Long id) {
		ProcessActivityFormInstance result = null;
		result = (ProcessActivityFormInstance)session.load(ProcessActivityFormInstance.class, id);
		return result;
	}


	public ProcessActivityFormInstance getProcessStartFormInstanceById(String processInstanceUuid) {
		ProcessActivityFormInstance result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		result = getProcessStartFormInstanceById(session, processInstanceUuid);
		
		session.close();
		
		return result;
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
			log.severe("processInstanceUuid=[" + processInstanceUuid + "] Exception: " + e);
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
			log.severe("formDocId=[" + formDocId + "] Exception: " + e);
		}
		finally {		
			session.close();
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
				log.severe("Unique value is expected");
				for (ProcessActivityFormInstance o : list) {
					log.severe(" value: " + o);
				}
			}
		}
		log.severe("===> result=" + result);
		return result;
	}

	public void saveProcessActivityFormInstance(Session session, ProcessActivityFormInstance processActivityFormInstance) {
		session.saveOrUpdate(processActivityFormInstance);		
	}


	public void saveProcessActivityFormInstance(ProcessActivityFormInstance processActivityFormInstance) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		saveProcessActivityFormInstance(session, processActivityFormInstance);
		
		session.getTransaction().commit();
		session.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessActivityFormInstance> getPendingProcessActivityFormInstances(String userId) {
		List<ProcessActivityFormInstance> result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			//result = (List<ProcessDefinition>)session.createQuery(hql).list();
			result = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
					.add( Restrictions.eq("userId", userId) )  // this user is last writer
					.add(Restrictions.isNull("submitted"))     // not submitted
				    .list();
		}
		catch (Exception e) {
			log.severe("Exception in getProcessActivityFormInstances: userId="  + userId + " exception: " + e);
		}
		finally {
			session.close();
		}
		return result;
	}

	
	public StartFormDefinition getStartFormDefinition(String activityDefinitionUuid, String processInstanceUuid) {
		StartFormDefinition result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		ProcessActivityFormInstance startForm = getProcessStartFormInstanceById(session, processInstanceUuid);
		log.severe("startForm=" + startForm);
		if (startForm != null) {
			String startFormPath = startForm.getFormPath();
			log.severe("startFormPath=" + startFormPath);
			result = getStartFormDefinitionByFormPath(session, startFormPath);
		}
		session.close();
		log.severe("result=" + result);
		return result;
	}
	
	
	/**
	 * Returns 
	 * 1) Special form depending on activityDefinitionUuid and startFormDefinitionId
	 * 2) General form for activityDefinitionUuid
	 * 3) No form defined (return null) Probably is best fall back to use the bonita form 
	 * @return 
	 */
	public ActivityFormDefinition getActivityFormDefinition(String activityDefinitionUuid, Long startFormDefinitionId) {
		ActivityFormDefinition result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		log.severe("==> activityDefinitionUuid=" + activityDefinitionUuid + ", startFormDefinitionId=" + startFormDefinitionId);
		try {
			result = getActivityFormDefinition(session, activityDefinitionUuid, startFormDefinitionId);
		}
		catch (Exception e) {
			log.severe("activityDefinitionUuid=[" + activityDefinitionUuid + "] startFormDefinitionId=[" + startFormDefinitionId + "] Exception: " + e);
		}
		finally {
			session.close();
		}
		return result;
	}
		
	@SuppressWarnings("unchecked")
	public ActivityFormDefinition getActivityFormDefinition(Session session, String activityDefinitionUuid, Long startFormDefinitionId) {
		List<ActivityFormDefinition> result = null;
				
		//result = (List<ProcessDefinition>)session.createQuery(hql).list();
		result = (List<ActivityFormDefinition>) session.createCriteria(ActivityFormDefinition.class)
			    .add( Restrictions.eq("activityDefinitionUuid", activityDefinitionUuid) )
			    .list();

		return filterUniqueActivityDefinitionFromList(result, startFormDefinitionId);
	}
	
	public ActivityFormDefinition getActivityFormDefinitionByFormPath(Session session, String formPath) {
		ActivityFormDefinition result = null;
		result = (ActivityFormDefinition) session.createCriteria(ActivityFormDefinition.class).add(Restrictions.eq("formPath", formPath)).uniqueResult();
		return result;
	}
	
	public ActivityFormDefinition getActivityFormDefinitionByFormPath(String formPath) {
		ActivityFormDefinition result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			result = getActivityFormDefinitionByFormPath(session, formPath);
		}
		catch (Exception e) {
			log.severe("formPath=[" + formPath + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		return result;
	}	
		
    private ActivityFormDefinition filterUniqueActivityDefinitionFromList(List<ActivityFormDefinition> list, Long startFormDefinitionId) {
    	ActivityFormDefinition result = null;
    	
    	ActivityFormDefinition specialForm = null;
		ActivityFormDefinition defaultForm = null;
		
		if (list != null) {
			for (ActivityFormDefinition afDef : list) {
				if (afDef.getStartFormDefinition() == null) {
					if (defaultForm != null) {
						log.severe("Unique defaultForm is expected for a specific activity. ActivityDefinitionUuid=[" + 
									defaultForm.getActivityDefinitionUuid() + "]");
					}
					defaultForm = afDef;
				}
				else {
					if (afDef.getStartFormDefinition().getStartFormDefinitionId().equals(startFormDefinitionId)) {
						if (defaultForm != null) {
							log.severe("Unique specialForm is expected for a specific activity and startFormDefinitionId. ActivityDefinitionUuid=[" + 
										defaultForm.getActivityDefinitionUuid() + "] startFormDefinitionId=[" + startFormDefinitionId + "]");
						}
						specialForm = afDef;
					}
				}
			}
		}
		
		log.severe("====> specialForm=" + specialForm);
		log.severe("====> defaultForm=" + defaultForm);
		
		if (specialForm == null) {
			result = defaultForm;
		}
		else {
			result = specialForm;
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


	private void saveProcessActivityTag(ProcessActivityTag processActivityTag) {
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
	
	public Tag addTag(Long processActivityFormInstanceId, Long tagTypeId, String value, String userId) {
		Tag result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			ProcessActivityTag processActivityTag = new ProcessActivityTag();
			processActivityTag.setType(getTagType(session, tagTypeId));
			processActivityTag.setProcessActivityFormInstance(getProcessActivityFormInstanceById(session, processActivityFormInstanceId));
			processActivityTag.setValue(value);
			processActivityTag.setUserId(userId);
			processActivityTag.setTimestamp(new Date());
			saveProcessActivityTag(session, processActivityTag);
			
			result = processActivityTag2Tag(processActivityTag);
			session.getTransaction().commit();
		}
		catch (Exception e) {
			log.severe("processActivityFormInstanceId=[" + processActivityFormInstanceId + "] tagTypeId=[" + tagTypeId + "] value=[" + value  + "] userId=[" + userId + "] Exception: " + e);
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
			log.severe("processActivityTagId=[" + processActivityTagId + "] userId=[" + userId + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		return result;
	}
	
	public boolean deleteTag(String processInstanceUuid, String value, String userId) {
		boolean result = false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			session.beginTransaction();
			List<ProcessActivityTag> tags = (List<ProcessActivityTag>) session.createCriteria(ProcessActivityTag.class)
					.add( Restrictions.eq("value", value) )
					.createCriteria("processActivityFormInstance")
					.add( Restrictions.eq("processInstanceUuid", processInstanceUuid) )
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
			log.severe("processInstanceUuid=[" + processInstanceUuid + "] value=[" + value + "] userId=[" + userId + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		return result;
	}
	
	public List<Tag> getTagsByProcessInstance(String processInstanceUuid) {
		List<Tag> tags = new ArrayList<Tag>();
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			List<ProcessActivityTag> result = (List<ProcessActivityTag>) session.createCriteria(ProcessActivityTag.class)
					.createCriteria("processActivityFormInstance")
					.add( Restrictions.eq("processInstanceUuid", processInstanceUuid) ) 
				    .list();
			
			for (ProcessActivityTag pafi : result) {
				tags.add(processActivityTag2Tag(pafi));
			}
		}
		catch (Exception e) {
			log.severe("processInstanceUuid=[" + processInstanceUuid + "] Exception: " + e);
		}
		finally {		
			session.close();
		}
		
		return tags;
	}
	
	public List<String> getProcessInstancesByTag(String tagValue) {
		List<String>  result = new ArrayList<String> ();
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			List<ProcessActivityFormInstance> hitItems = (List<ProcessActivityFormInstance>) session.createCriteria(ProcessActivityFormInstance.class)
					.createCriteria("processActivityTags")
					.add( Restrictions.eq("value", tagValue) ) 
				    .list();
			
			if (hitItems != null) {
				for (ProcessActivityFormInstance hitItem : hitItems) {
					result.add(hitItem.getProcessInstanceUuid());
				}
			}
		}
		catch (Exception e) {
			log.severe("tagValue=[" + tagValue + "] Exception: " + e);
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
		tag.setProcessActivityFormInstanceId(processActivityTag.getProcessActivityFormInstance().getProcessActivityFormInstanceId());
		return tag;
	}
	
	
	public UserInfo getUserByUuid(String uuid) {
		UserInfo result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			UserEntity user = getUserByUuid(session, uuid);
			result = convertUser(user);
		}
		catch (Exception e) {
			log.severe("uuid=[" + uuid + "] Exception: " + e);
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
			log.severe("dn=[" + dn + "] Exception: " + e);
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
			log.severe("serial=[" + serial + "] Exception: " + e);
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
			log.severe("uuid=[" + uuid + "] Exception: " + e);
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
					log.severe("dn should identify a unique user. dn=[" + dn + "] identifies " + users.size() + " users");
				}
			}
		}
		catch (Exception e) {
			log.severe("dn=[" + dn + "] Exception: " + e);
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
					log.severe("serial should identify a unique user. serial=[" + serial + "] identifies " + users.size() + " users");
				}
			}
		}
		catch (Exception e) {
			log.severe("serial=[" + serial + "] Exception: " + e);
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
		
		Configuration cfg = new AnnotationConfiguration()
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.StartFormDefinition.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ActivityFormDefinition.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ProcessActivityFormInstance.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.TagType.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ProcessActivityTag.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.UserEntity.class)
	    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
	    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
	    .setProperty("hibernate.connection.url", " jdbc:postgresql://localhost/InheritPlatform")
	    .setProperty("hibernate.connection.username", "inherit")
	    .setProperty("hibernate.connection.password", "inh3rit")
	    .setProperty("hibernate.hbm2ddl.auto", "create")
//	    .setProperty("hibernate.hbm2ddl.auto", "update")
	    .setProperty("show_sql", "true");

		HibernateUtil.overrideProperties(cfg);
		
	    SessionFactory sessionFactory = cfg.buildSessionFactory();
	    Session session = sessionFactory.openSession();
	    log.fine("Init hibernate finished");
		// initialize db with demo data
		
		StartFormDefinition spridning = new StartFormDefinition();
		spridning.setFormPath("start/demo-ansokan");
		spridning.setProcessDefinitionUuid("Spridning_bekampningsmedel");
		
		StartFormDefinition pcb_arende = new StartFormDefinition();
		pcb_arende.setFormPath("miljoforvaltningen/inventeringsprotokoll_pcb_fogmassor");
		pcb_arende.setProcessDefinitionUuid("Arendeprocess");
		
		ActivityFormDefinition granskaAnsokan = new ActivityFormDefinition();
		granskaAnsokan.setFormPath("Demo/Granska_ansokan");
		granskaAnsokan.setActivityDefinitionUuid("Spridning_bekampningsmedel--5.0--Granska_ansokan");
		granskaAnsokan.setStartFormDefinition(null);

		ActivityFormDefinition remissA = new ActivityFormDefinition();
		remissA.setFormPath("Demo/Remissyttrande");
		remissA.setActivityDefinitionUuid("Spridning_bekampningsmedel--5.0--Remissyttrande_A");
		remissA.setStartFormDefinition(null);
		
		ActivityFormDefinition remissB = new ActivityFormDefinition();
		remissB.setFormPath("Demo/Remissyttrande");
		remissB.setActivityDefinitionUuid("Spridning_bekampningsmedel--5.0--Remissyttrande_B");
		remissB.setStartFormDefinition(null);

		ActivityFormDefinition decision = new ActivityFormDefinition();
		decision.setFormPath("Demo/Beslut");
		decision.setActivityDefinitionUuid("Spridning_bekampningsmedel--5.0--Beslut");
		decision.setStartFormDefinition(null);
//===		
		ActivityFormDefinition registrering = new ActivityFormDefinition();
		registrering.setFormPath("basprocess/registrera");
		registrering.setActivityDefinitionUuid("Arendeprocess--1.0--Registrering");
		registrering.setStartFormDefinition(null);

		ActivityFormDefinition handlaggning = new ActivityFormDefinition();
		handlaggning.setFormPath("basprocess/handlagga");
		handlaggning.setActivityDefinitionUuid("Arendeprocess--1.0--Handlaggning");
		handlaggning.setStartFormDefinition(null);
		
		ActivityFormDefinition beslut = new ActivityFormDefinition();
		beslut.setFormPath("basprocess/beslut");
		beslut.setActivityDefinitionUuid("Arendeprocess--1.0--Beslut");
		beslut.setStartFormDefinition(null);
				
		TagType diaryTagType = new TagType();
		diaryTagType.setTagTypeId(TagType.TAG_TYPE_DIARY_NO);
		diaryTagType.setName("diary_no");
		diaryTagType.setLabel("Diarienr");

		TagType applicationByTagType = new TagType();
		applicationByTagType.setTagTypeId(TagType.TAG_APPLICATION_BY);
		applicationByTagType.setName("application_by");
		applicationByTagType.setLabel("Ansökan av");
		
		TagType otherTagType = new TagType();
		otherTagType.setTagTypeId(TagType.TAG_OTHER);
		otherTagType.setName("other");
		otherTagType.setLabel("Annan");

		session.beginTransaction();
		session.save(spridning);
		session.save(pcb_arende);
		session.save(granskaAnsokan);
		session.save(remissA);
		session.save(remissB);
		session.save(decision);
		session.save(registrering);
		session.save(handlaggning);
		session.save(beslut);
		session.save(diaryTagType);
		session.save(applicationByTagType);
		session.save(otherTagType);
		
		session.getTransaction().commit();
		session.close();
		
		/*
		TaskFormDb db = new TaskFormDb();
		
		User user = new User();
		user.setCategory(User.CATEGORY_UNKNOWN);
		user.setDn("testdn");
		user.setCn("test cn");
		user.setUuid("uuid");
		db.createUser(user);
		*/
		System.out.println("end main");
		
	}
	
	
}

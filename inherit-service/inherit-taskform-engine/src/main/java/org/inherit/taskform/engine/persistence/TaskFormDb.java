package org.inherit.taskform.engine.persistence;

import java.util.List;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.inherit.taskform.engine.persistence.entity.ActivityFormDefinition;
import org.inherit.taskform.engine.persistence.entity.ProcessActivityFormInstance;
import org.inherit.taskform.engine.persistence.entity.ProcessActivityTag;
import org.inherit.taskform.engine.persistence.entity.StartFormDefinition;
import org.inherit.taskform.engine.persistence.entity.TagType;

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
			//result = (List<ProcessDefinition>)session.createQuery(hql).list();
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
	
	public void saveProcessActivityTag(Session session, ProcessActivityTag processActivityTag) {
		session.saveOrUpdate(processActivityTag);		
	}


	public void saveProcessActivityTag(ProcessActivityTag processActivityTag) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		saveProcessActivityTag(session, processActivityTag);
		
		session.getTransaction().commit();
		session.close();
	}

	public static void main(String args[]) {
		System.out.println("start main load initial data to InheritPlatform database");
		
		Configuration cfg = new AnnotationConfiguration()
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.StartFormDefinition.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ActivityFormDefinition.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ProcessActivityFormInstance.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.TagType.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ProcessActivityTag.class)
	    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
	    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
	    .setProperty("hibernate.connection.url", " jdbc:postgresql://localhost/InheritPlatform")
	    .setProperty("hibernate.connection.username", "inherit")
	    .setProperty("hibernate.connection.password", "inh3rit")
	    .setProperty("hibernate.hbm2ddl.auto", "create")
//	    .setProperty("hibernate.hbm2ddl.auto", "update")
	    .setProperty("show_sql", "true");
	    
		
	    SessionFactory sessionFactory = cfg.buildSessionFactory();
	    Session session = sessionFactory.openSession();
	    log.fine("Init hibernate finished");
		// initialize db with demo data
		
		StartFormDefinition spridning = new StartFormDefinition();
		spridning.setFormPath("start/demo-ansokan");
		spridning.setProcessDefinitionUuid("Spridning_bekampningsmedel");
		
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
		
		TagType diaryTagType = new TagType();
		diaryTagType.setTagTypeId(TagType.TAG_TYPE_DIARY_NO);
		diaryTagType.setName("diary_no");
		diaryTagType.setLabel("Diarienr");

		TagType applicationByTagType = new TagType();
		applicationByTagType.setTagTypeId(TagType.TAG_APPLICATION_BY);
		applicationByTagType.setName("application_by");
		applicationByTagType.setLabel("Ansökan av");

		session.beginTransaction();
		session.save(spridning);
		session.save(granskaAnsokan);
		session.save(remissA);
		session.save(remissB);
		session.save(decision);
		session.save(diaryTagType);
		session.save(applicationByTagType);
		
		session.getTransaction().commit();
		session.close();
		
		System.out.println("end main");
		
	}
	
	
}

package org.inherit.taskform.engine.persistence;

import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.login.LoginContext;

import org.hibernate.Session;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import org.inherit.bonita.client.util.BonitaUtil;
import org.inherit.taskform.engine.bonitautil.BonitaObjectConverter;
import org.inherit.taskform.engine.persistence.entity.ActivityFormDefinition;
import org.inherit.taskform.engine.persistence.entity.ProcessActivityFormInstance;
import org.inherit.taskform.engine.persistence.entity.StartFormDefinition;
import org.ow2.bonita.facade.def.majorElement.ActivityDefinition.Type;
import org.ow2.bonita.facade.uuid.ProcessDefinitionUUID;
import org.ow2.bonita.util.AccessorUtil;


public class TaskFormDb {
	
	public static final Logger log = Logger.getLogger(TaskFormDb.class.getName());
	
	//public void void addActivityFormMapping()
	
	public TaskFormDb() {
		
	}

	/**
	 * TODO refaktorisera senare för att få snyggare beroende
	 
	public void syncDefsWithExecutionEngine() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
	    	
    		LoginContext loginContext = BonitaUtil.login();

    		Set<org.ow2.bonita.facade.def.majorElement.ProcessDefinition> procDefs;
    		procDefs = AccessorUtil.getQueryDefinitionAPI().getProcesses();
    		for (org.ow2.bonita.facade.def.majorElement.ProcessDefinition procDef : procDefs) {
    			
    			String processUuid = procDef.getUUID().getValue();
    			ProcessDefinition pd = this.getProcessDefinitionsByUuid(session, processUuid);
    			if (pd == null) {
    				// new process definition
    				pd = new ProcessDefinition();
    				BonitaObjectConverter.convert(procDef, pd);
    				
    				// new process => all activites are new
    				for (org.ow2.bonita.facade.def.majorElement.ActivityDefinition actDef : procDef.getActivities()) {
    					if (actDef.isTask() && !actDef.isAutomatic()) {
    						if (actDef.getType() == Type.Human) {
		    					ActivityFormDefinition ad = new ActivityFormDefinition();
		    					BonitaObjectConverter.convert(actDef, ad);
		    					
		    					pd.addActivityDefinition(ad);
    						}
    					}
    				}    				
    				
    			}
    			else {
    				BonitaObjectConverter.convert(procDef, pd);
    				
    				for (org.ow2.bonita.facade.def.majorElement.ActivityDefinition actDef : procDef.getActivities()) {
    					// TODO loop activities....
    				}
    			}

    			session.beginTransaction();
    			saveProcessDefinition(session, pd);
    			session.getTransaction().commit();
    		}
            
	        BonitaUtil.logout(loginContext);

    	} catch (Exception e) {
        	log.severe("Could not create a proper bonita form identity key: " + e); // instance=TestaCheckboxlist--1.0--8
        } 
		finally {
			session.close();
		}
	}
*/
	
	
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
		return result;
	}

	public void saveProcessActivityFormInstance(Session session, ProcessActivityFormInstance processActivityFormInstance) {
		session.save(processActivityFormInstance);		
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

	public static void main(String args[]) {
		System.out.println("start main");
		
		ActivityFormDefinition ad = new ActivityFormDefinition();
		ad.setFormPath("form path");
		ad.setStartFormDefinition(null);
		ad.setActivityDefinitionUuid("act instance uuid");

		ActivityFormDefinition adExtra = new ActivityFormDefinition();
		adExtra.setFormPath("form path extra");
		adExtra.setStartFormDefinition(null);
		adExtra.setActivityDefinitionUuid("act instance uuid extra");
		
		ProcessActivityFormInstance afi = new ProcessActivityFormInstance();
		afi.setFormDocId("formDocIdsdfghjklkjhg");
		afi.setFormPath("demo/form1");
		afi.setProcessInstanceUuid("procInstanceUuidhg678578t");
		afi.setUserId("eva_extern");
			
		TaskFormDb db = new TaskFormDb();
		db.save(ad);
		db.save(adExtra);
		db.save(afi);
		
		List<ProcessActivityFormInstance> list = db.getPendingProcessActivityFormInstances("eva_extern");
		for (ProcessActivityFormInstance item : list) {
			System.out.println(item);
		}
		
		System.out.println("end main");
		
	}
	
	
}

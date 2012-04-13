package org.inherit.taskform.engine.persistence;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.security.auth.login.LoginContext;

import org.hibernate.Session;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import org.inherit.bonita.client.util.BonitaUtil;
import org.inherit.taskform.engine.bonitautil.BonitaObjectConverter;
import org.inherit.taskform.engine.persistence.entity.ActivityDefinition;
import org.inherit.taskform.engine.persistence.entity.ProcessDefinition;
import org.ow2.bonita.facade.uuid.ProcessDefinitionUUID;
import org.ow2.bonita.util.AccessorUtil;


public class TaskFormDb {
	
	public static final Logger log = Logger.getLogger(TaskFormDb.class.getName());
	
	//public void void addActivityFormMapping()
	
	public TaskFormDb() {
		HibernateUtil.loadConfig();
	}

	/**
	 * TODO refaktorisera senare för att få snyggare beroende
	 */
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
    					ActivityDefinition ad = new ActivityDefinition();
    					BonitaObjectConverter.convert(actDef, ad);
    					
    					pd.addActivityDefinition(ad);
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

	
	public void saveProcessDefinition(Session session, ProcessDefinition processDefinition) {
		session.save(processDefinition);		
		Set<ActivityDefinition> activityDefinitions = processDefinition.getActivityDefinitions();
		if (activityDefinitions != null) {
			for (ActivityDefinition activityDefinition : activityDefinitions) {
				session.save(activityDefinition);
			}
		}
	}

	public void saveProcessDefinition(ProcessDefinition processDefinition) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		saveProcessDefinition(session, processDefinition);
		
		session.getTransaction().commit();
		session.close();
	}

	private ProcessDefinition filterUniqueFromList(List<ProcessDefinition> list) {
		ProcessDefinition result = null;
		if (list != null) {
			if (list.size() > 0 ) {
				result = list.get(0);
			}
			if (list.size() > 1) {
				log.severe("Unique value is expected");
				for (ProcessDefinition o : list) {
					log.severe(" value: " + o);
				}
			}
		}
		return result;
	}
	
	public ProcessDefinition getProcessDefinitionsById(Long id) {
		List<ProcessDefinition> result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			//result = (List<ProcessDefinition>)session.createQuery(hql).list();
			result = (List<ProcessDefinition>) session.createCriteria(ProcessDefinition.class)
				    .add( Restrictions.eq("processDefinitionId", id) )
				    .setFetchMode("activityDefinitions", FetchMode.JOIN)
				    .list();
		}
		catch (Exception e) {
			
		}
		finally {
			session.close();
		}
		return filterUniqueFromList(result);
	}

	// TODO ändra till unik uuid
	public ProcessDefinition getProcessDefinitionsByUuid(String uuid) {
		ProcessDefinition result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			//result = (List<ProcessDefinition>)session.createQuery(hql).list();
			result = getProcessDefinitionsByUuid(session, uuid);
		}
		catch (Exception e) {
			
		}
		finally {
			session.close();
		}
		return result;
	}
	
	// TODO ändra till unik uuid
	private ProcessDefinition getProcessDefinitionsByUuid(Session session, String uuid) {
		List<ProcessDefinition> result = null;
				
		//result = (List<ProcessDefinition>)session.createQuery(hql).list();
		result = (List<ProcessDefinition>) session.createCriteria(ProcessDefinition.class)
			    .add( Restrictions.eq("uuid", uuid) )
			    .setFetchMode("activityDefinitions", FetchMode.JOIN)
			    .list();

		return filterUniqueFromList(result);
	}
	

	@SuppressWarnings("unchecked")
	public List<ProcessDefinition> getProcessDefinitions() {
		List<ProcessDefinition> result = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			//result = (List<ProcessDefinition>)session.createQuery(hql).list();
			result = (List<ProcessDefinition>) session.createCriteria(ProcessDefinition.class)
				    .setFetchMode("activityDefinitions", FetchMode.JOIN)
				    .list();
		}
		catch (Exception e) {
			
		}
		finally {
			session.close();
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
		ProcessDefinition pd = new ProcessDefinition();
		pd.setLabel("Test");
		pd.setUuid("ghjgjhgj2");
		
		ActivityDefinition ad = new ActivityDefinition();
		ad.setLabel("Test");
		ad.setUuid("ghjgjhgj");

		ActivityDefinition adExtra = new ActivityDefinition();
		ad.setLabel("Test extra");
		ad.setUuid("ghjgjhgj extra");
		
		pd.addActivityDefinition(ad);

		TaskFormDb db = new TaskFormDb();
		db.saveProcessDefinition(pd);
		
		List<ProcessDefinition> pafs  = db.getProcessDefinitions();
		for (ProcessDefinition paf : pafs) {
			System.out.println(paf);
		}

		System.out.println("By uuid");
		ProcessDefinition paf  = db.getProcessDefinitionsByUuid("ghjgjhgj2");
		System.out.println(paf);
		
		System.out.println("By id");
	    paf  = db.getProcessDefinitionsById(new Long(56));
		System.out.println(paf);
		
		
		
	}
	
	
}

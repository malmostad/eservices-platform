package org.inherit.taskform.engine.persistence;

import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	static {
		log = Logger.getLogger(HibernateUtil.class.getName());
		init();
	}
	
	public static final Logger log;
	
	private static SessionFactory sessionFactory;
	
	private static void init() {
	    // A SessionFactory is set up once for an application
		log.fine("Init hibernate");
		try {
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
//		    .setProperty("hibernate.hbm2ddl.auto", "create")
//		    .setProperty("hibernate.hbm2ddl.auto", "update")
		    .setProperty("show_sql", "true");
		    
			
		    sessionFactory = cfg.buildSessionFactory();
		    
		    log.fine("Init hibernate finished");
		} 
		catch (Exception e) {
			log.severe("Could not initialize Hibernate SessionFactory: " + e.getMessage());
			log.severe("Could not initialize Hibernate SessionFactory: " + e.toString());
		}
	}
	
	public HibernateUtil() {
		
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtil.sessionFactory = sessionFactory;
	}
	
	
	
}

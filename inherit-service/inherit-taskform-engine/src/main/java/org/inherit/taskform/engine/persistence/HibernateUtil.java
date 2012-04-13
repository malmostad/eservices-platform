package org.inherit.taskform.engine.persistence;

import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	public static final Logger log = Logger.getLogger(HibernateUtil.class.getName());
	
	private static SessionFactory sessionFactory = null;
	
	private static void init() {
	    // A SessionFactory is set up once for an application
		try {
		    Configuration cfg = new Configuration()
		    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ProcessDefinition.class)
		    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ActivityDefinition.class)
		    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
		    .setProperty("hibernate.connection.url", " jdbc:postgresql://localhost/InheritPlatform")
		    .setProperty("hibernate.connection.username", "inherit")
		    .setProperty("hibernate.connection.password", "inh3rit")
		    .setProperty("hibernate.hbm2ddl.auto", "update");
		    
		    sessionFactory = cfg.buildSessionFactory();
		    
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
	
	public static void loadConfig() {
		init();
	}
	
	
}

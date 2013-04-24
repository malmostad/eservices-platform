package org.inheritsource.taskform.engine.persistence;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
    // Environment variable pointing to Hibernate properties file, used in init()
    // Neither can be null.
    public static final String DB_PROPERTIES_ENV = "INHERIT_PLATFORM_DB_PROPS";
    public static final String DB_PROPERTIES_DEFAULT_PATH = "/usr/local/etc/inherit/platform-db-connect.properties";

    public static final Logger log;

	static {
		log = Logger.getLogger(HibernateUtil.class.getName());
		init();
	}
	
	private static SessionFactory sessionFactory;
	
	private static void init() {
	    // A SessionFactory is set up once for an application
		log.fine("Init hibernate");
		try {
		    Configuration cfg = new AnnotationConfiguration()
			.addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.StartFormDefinition.class)
			.addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.ActivityFormDefinition.class)
			.addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.ProcessActivityFormInstance.class)
			.addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.TagType.class)
			.addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.ProcessActivityTag.class)
			.addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.UserEntity.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
			.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
			.setProperty("hibernate.connection.url", " jdbc:postgresql://localhost/InheritPlatform")
			.setProperty("hibernate.connection.username", "inherit")
			.setProperty("hibernate.connection.password", "inh3rit");
		    overrideProperties(cfg);
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

    /**
     * Override properties in a Hibernate configuration if an environment variable is set.
     * No exceptions are propagated, this step is just omitted in such case.
     * SIDE EFFECT: Properties are set in the configuration, possibly overriding those
     * already set.
     */
    public static void overrideProperties(Configuration cfg) {
	try {
	    Properties props = doOverrideProperties();
	    cfg.addProperties(props);
	} catch (Exception exc) {
	    // Warn and ignore
	    log.warning("Db default connect params (got " + exc.getClass().getName() + ")");
	}

	String url = cfg.getProperty("hibernate.connection.url");
 	log.info("Db connect url: " + ((url != null)? url : "*NO URL*"));
    }

    private static Properties doOverrideProperties() throws IOException {
	String filePath = null;

	try {
	    filePath = System.getenv(DB_PROPERTIES_ENV);
	} catch (NullPointerException exc) {
	    // Ignore
	}

	if (filePath != null) {
	    log.info("Db connect -- using env var " + DB_PROPERTIES_ENV);
	} else {
	    log.info("Db connect -- no path from env variable, use file " + DB_PROPERTIES_DEFAULT_PATH);
	    File file = new File(DB_PROPERTIES_DEFAULT_PATH);
	    if (file.exists()) filePath = DB_PROPERTIES_DEFAULT_PATH;
	}

	Properties props = new Properties();

	if (filePath != null) {
	    FileReader fr = null;
	    try {
		fr = new FileReader(filePath);
		props.load(fr);
		log.info("Db connect properties loaded from " + filePath);
	    } finally {
		if (fr != null) {
		    fr.close();
		}
	    }
	}

	return props;
    }

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtil.sessionFactory = sessionFactory;
	}
	
	
	
}

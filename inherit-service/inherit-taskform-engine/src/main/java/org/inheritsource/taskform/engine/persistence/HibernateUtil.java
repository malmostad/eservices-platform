/* 
 *  Process Aware Web Application Platform 
 * 
 *  Copyright (C) 2011-2013 Inherit S AB 
 * 
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Affero General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details. 
 * 
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 *  e-mail: info _at_ inherit.se 
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 *  phone: +46 8 641 64 14 
 */ 
 
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
			.setProperty("hibernate.connection.url", " jdbc:postgresql://localhost/InheritPlatform");
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

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
 
package org.inheritsource.service.rest.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.restlet.resource.ServerResource;

public abstract class Emailer extends ServerResource {

	public static final String MOTRICE_PROPERTIES_DEFAULT_PATH = "/usr/local/etc/inherit/motrice.properties";
	public static final String MOTRICE_HOME = "MOTRICE_HOME";
	public static final Logger log = Logger.getLogger(Emailer.class.getName());

	protected static String SENDERADDRESS  = null;
	protected static String SMTPSERVER     = null;
	protected static String ERRORRECIPIENT = null;
	protected static String ERRORMESSAGE1  = null;
	protected static String ERRORMESSAGE2  = null;
	protected static String ERRORMESSAGE3  = null;

	protected Properties cfgProps = null;

	public Emailer() {
		super();
		loadconfiguration();
	}

	private void loadconfiguration() {
		cfgProps=getConfigProperties();
		if (cfgProps != null) {
			SENDERADDRESS     = cfgProps.getProperty("email.senderaddress");
			SMTPSERVER        = cfgProps.getProperty("email.smtpserver");
			ERRORRECIPIENT    = cfgProps.getProperty("email.errorrecipient");
			ERRORMESSAGE1    = cfgProps.getProperty("email.errormessage1");
			ERRORMESSAGE2    = cfgProps.getProperty("email.errormessage2");
			ERRORMESSAGE3    = cfgProps.getProperty("email.errormessage3");
			//System.out.println("SENDERADDRESS:  "  + SENDERADDRESS);
			//System.out.println("SMTPSERVER:     "  + SMTPSERVER);
			//System.out.println("ERRORRECIPIENT: "  + ERRORRECIPIENT);
			//System.out.println("ERRORMESSAGE1:  "  + ERRORMESSAGE1);
			//System.out.println("ERRORMESSAGE2:  "  + ERRORMESSAGE2);
			//System.out.println("ERRORMESSAGE3:  "  + ERRORMESSAGE3);
		} else {
			log.info("Cannot load configuration properties");
		}

	}
	
	protected Properties getConfigProperties() {
		Properties result = null;
		String filePath = null;

		try {
			filePath = System.getenv(MOTRICE_HOME);
		} catch (NullPointerException exc) {
			// Ignore
		}

		if (filePath != null) {
			log.info("Trying to set properties -- using env var $" + MOTRICE_HOME + "/conf/motrice.properties" );
			filePath = filePath + "/conf/motrice.properties";
			File file = new File(filePath);
			if (file.exists()) {
				result = loadPropsFromFile(filePath);	
			} else {
				log.info("Config file: $" + MOTRICE_HOME + "/conf/motrice.properties does not exist, revert to default config file: " + MOTRICE_PROPERTIES_DEFAULT_PATH);
				file = new File(MOTRICE_PROPERTIES_DEFAULT_PATH);
				if (file.exists()){
					result = loadPropsFromFile(MOTRICE_PROPERTIES_DEFAULT_PATH);	
				}
			}
		} else {
			log.info("Db connect -- no path from env variable, use default config file: " + MOTRICE_PROPERTIES_DEFAULT_PATH);
			File file = new File(MOTRICE_PROPERTIES_DEFAULT_PATH);
			if (file.exists()){
				result = loadPropsFromFile(MOTRICE_PROPERTIES_DEFAULT_PATH);	
			}
		}
		return result;
	}	

	private Properties loadPropsFromFile(String fPath) {
		//assuming fPath != null and that file exists
		Properties result  = new Properties();
		FileReader fr=null;
		try {
			fr = new FileReader(fPath);
			result.load(fr);
			log.info("Config properties loaded from " + fPath);
		} catch (FileNotFoundException fe ) {
			// ignore
		} catch (IOException fe ) {
			// ignore
		} finally {
			try {
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
package org.inheritsource.service.common.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigUtil {
	public static final Logger log;

	public static final String MOTRICE_HOME_ENV = "MOTRICE_HOME";
	public static final String MOTRICE_CONFIG_FILE = "/conf/motrice.properties";

	public static final String MOTRICE_CONF_DEFAULT_PATH = "/usr/local/etc/motrice/motrice.properties";

	private static Properties props;
	
	static {
		log = Logger.getLogger(ConfigUtil.class.getName());
		init();
	}

	public static Properties getConfigProperties() {
		return props;
	}
	
	private static void init() {
		try {
			props = loadProperties();
		} catch (IOException e) {
			log.severe("Could not read configuration: " + e.getMessage());
		}
		
		if (props == null) {
			props = new Properties();
		}
		
		// set default values here
	}
	
	private static Properties loadProperties() throws IOException {
		String filePath = null;

		try {
			filePath = System.getenv(MOTRICE_HOME_ENV);
		} catch (NullPointerException exc) {
			// Ignore
		}

		if (filePath != null) {
			filePath = filePath + MOTRICE_CONFIG_FILE;
			log.info("Motrice configuration -- using env var " + MOTRICE_HOME_ENV
					+ " => config file: '" + filePath + "'");
		} else {
			log.info("Motrice configuration -- no path from env variable, use dafault config file "
					+ MOTRICE_CONF_DEFAULT_PATH);
			File file = new File(MOTRICE_CONF_DEFAULT_PATH);
			if (file.exists())
				filePath = MOTRICE_CONF_DEFAULT_PATH;
		}

		Properties props = new Properties();

		if (filePath != null) {
			FileReader fr = null;
			try {
				fr = new FileReader(filePath);
				props.load(fr);
				log.info("Motrice configuration properties loaded from " + filePath);
			} finally {
				if (fr != null) {
					fr.close();
				}
			}
		}
		else {
			log.info("Motrice could not load configuration file. Motrice looks for configuration in 1) ${" + MOTRICE_HOME_ENV + "}" + MOTRICE_CONFIG_FILE + " 2) " + MOTRICE_CONF_DEFAULT_PATH);
		}

		return props;
	}

	
	public static void main(String[] args) {
		Properties p = ConfigUtil.getConfigProperties();
		for (Object key : p.keySet()) {
			if (key instanceof String) {
				String property = (String) key;
				System.out.println("Property " + property + " = ["  + p.getProperty(property, "not found in config file") + "]");
			}
		}
		
		
	}
}

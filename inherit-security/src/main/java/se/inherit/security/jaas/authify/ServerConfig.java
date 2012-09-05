package se.inherit.security.jaas.authify;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;

public class ServerConfig {

	private static ServerConfigBean config = null;
	
	private static Logger logger = Logger.getLogger(ServerConfig.class.getName());
	
	static {
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			config = (ServerConfigBean)envContext.lookup("bean/ServerConfigFactory");
			
			logger.setLevel(Level.INFO);						
			logger.info("Authify ServerConfig config=[" + config + "]");
		}
		catch (Exception e) {
			logger.severe("Could not read ServerConfig, verify your context.xml, exception: " + e );
		}

	}

	public static ServerConfigBean getConfig() {
		return config;
	}

	public static void setConfig(ServerConfigBean config) {
		ServerConfig.config = config;
	}
	
}

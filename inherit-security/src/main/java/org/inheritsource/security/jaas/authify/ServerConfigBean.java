package org.inheritsource.security.jaas.authify;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;

/*
Copyright (C) 2011 Inherit S AB

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

public class ServerConfigBean {
	
	private static Logger logger = Logger.getLogger(ServerConfigBean.class.getName());
	
	private String host;
	private String port;
	private String authifyLandingPage;
	private String authifyAppId;
	private String authifySecretKey;
	
		
	public ServerConfigBean() {


	}
	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ServerConfigBean.logger = logger;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setAuthifyLandingPage(String authifyLandingPage) {
		this.authifyLandingPage = authifyLandingPage;
	}

	public String getBaseUrl() {
		String result =  "http://" + host;
		if (port!=null && port.trim().length() > 0 && !"80".equals(port.trim())) {
			result += ":" + port;
		}
		result += "/";
		return result;
	}
	
	public String getAuthifyLandingPage() {
		return getBaseUrl() + authifyLandingPage;
	}


	
	public String getAuthifyAppId() {
		return authifyAppId;
	}

	public void setAuthifyAppId(String authifyAppId) {
		this.authifyAppId = authifyAppId;
	}

	public String getAuthifySecretKey() {
		return authifySecretKey;
	}

	public void setAuthifySecretKey(String authifySecretKey) {
		this.authifySecretKey = authifySecretKey;
	}

	@Override
	public String toString() {
		return "ServerConfig [host=" + host + 
				", port=" + port + 
				", authifyLandingPage=" + authifyLandingPage + 
				", authifyAppId=" + authifyAppId + 
				", authifySecretKey=" + authifySecretKey + "]";
	}
	
	
}

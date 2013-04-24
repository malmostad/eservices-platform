package org.inheritsource.service.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

public class UrlUtil {

	public static final Logger log = Logger.getLogger(UrlUtil.class.getName());
			
	public static String loadFromUrl(String urlStr) {
		log.info("loadFromUrl: " + urlStr);
		String result = null;
		URL url;
		try {
			url = new URL(urlStr);
	        URLConnection con = url.openConnection();
	        result = IOUtils.toString(con.getInputStream(), "UTF-8");
		} 
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//log.severe("loadFromUrl: " + urlStr + " => " + result);
		return result;
	}
}

package org.inherit.service.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.logging.Logger;

public class ParameterEncoder {

		public static final String SLASH_REPLACE_STR = "¤#";
		
		public static final String NULL_REPLACE_STR = "---NULL---";
	
		public static final Logger log = Logger.getLogger(ParameterEncoder.class.getName());
	
		public static String encode(String s) {
			String result = s;
			try {
				
				if (s == null) {
					s = NULL_REPLACE_STR;
				}
				
				// replace slashes
				String tmpStr = s.replaceAll("/", SLASH_REPLACE_STR);
				
				result = URLEncoder.encode(tmpStr, "UTF-8");
				
			} catch (UnsupportedEncodingException e) {
				log.severe("This should never happen....: " + e.toString());
			}
			
			return result;
		}
		
		public static String decode(String s) {
			String result = s;
			try {
				if (s != null) {
					
					result = URLDecoder.decode(s, "UTF-8");
					
					// replace slashes
					result = result.replaceAll(SLASH_REPLACE_STR, "/");
					
					if (NULL_REPLACE_STR.equals(result)) {
						result = null;
					}
				}
				
				
			} catch (UnsupportedEncodingException e) {
				log.severe("This should never happen....: " + e.toString());
			}
			return result;
		}
		
		public static void main(String args[]) {
			System.out.println("Test URL enc");
			
			String[] strings = {"kalle", "kal/e", null};
			for (String s : strings) {
				System.out.println("Compare decoded encoded string");
				System.out.println("s                 = [" + s + "]");
				String e = encode(s);
				System.out.println("encode(s)         = [" + e + "]");
				String d = decode(e);
				System.out.println("decode(encode(s)) = [" + d + "]");
				
				if (s==null) {
					if (d!=null) {
						System.out.println("FAIL");
					}
				}
				else if (!s.equals(d)) {
					System.out.println("FAIL");
				}
			}
			
		}
		
}

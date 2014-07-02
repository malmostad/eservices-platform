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
 
 
package org.inheritsource.service.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParameterEncoder {

		public static final String SLASH_REPLACE_STR = "¤#";
		
		public static final String NULL_REPLACE_STR = "---NULL---";
	
		public static final Logger log = LoggerFactory.getLogger(ParameterEncoder.class.getName());
	
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
				log.error("This should never happen....: " + e.toString());
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
				log.error("This should never happen....: " + e.toString());
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

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

package org.inheritsource.portal.mycases.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.taskform.engine.TaskFormService;
import org.inheritsource.service.common.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class ServletUserNameUtil {

	public static final Logger log = LoggerFactory
			.getLogger(ServletUserNameUtil.class);

	private static TaskFormService engine;

	private static void checkEngine() {
		if (engine == null) {
			ApplicationContext ctx = ContextLoader
					.getCurrentWebApplicationContext();
			engine = (TaskFormService) ctx.getBean("engine");
		}
	}

	public static UserInfo getUserName(final HttpServletRequest request) {
		UserInfo result = null;

		/*
		 * log.error("ServletUserNameUtil:error");
		 * log.warn("ServletUserNameUtil:warn");
		 * log.info("ServletUserNameUtil:info");
		 * log.debug("ServletUserNameUtil:debug");
		 * log.trace("ServletUserNameUtil:trace");
		 */
		checkEngine();
		// / trying shibboleth
		String Shib_Identity_Provider = (String) request
				.getAttribute("Shib-Identity-Provider");
		log.info("request.getAttribute(Shib-Identity-Provider) = "
				+ Shib_Identity_Provider);
                String Shib_Application_ID =  (String) request.getAttribute("Shib-Application-ID")  ; 
		log.info("request.getAttribute(Shib-Application-ID) = "
				+ Shib_Application_ID);
		// log.info("request.getHeader(Shib-Identity-Provider)  = "
		// + request.getHeader("Shib-Identity-Provider"));
		if ((Shib_Identity_Provider != null) && ( Shib_Application_ID.equals("default" ))) {
			// the names of the attributes are set in attribute-*.xml files in
			// /etc/shibboleth for the SP
			// Different IdPs might privide different attributes, it probable
			// makes sence
			// to make the mapping in the attribute-*.xml to the same attribute name for the different IdPs.
			String Subject_SerialNumber = (String) request
					.getAttribute("Subject_SerialNumber");
			// String gn = (String) request.getAttribute("GivenName");
		        String gn = getAttributeShibboleth("GivenName" , request) ; 
                        String sn = getAttributeShibboleth("Subject_Surname" , request) ;
			//String sn_id = (String) request.getAttribute("sn_id");
			String sn_id = null ; 
			String cn =  getAttributeShibboleth("Subject_CommonName" , request) ;  
			log.info("Subject_SerialNumber = " + Subject_SerialNumber + " gn = "
					+ gn + " sn = " + sn + " cn = " + cn);
			result = engine.getUserBySerial(Subject_SerialNumber, gn, sn,
					sn_id, cn);
		}

		if ((Shib_Identity_Provider != null) && ( Shib_Application_ID.equals("internal" ))) {
			// the names of the attributes are set in attribute-*.xml files in
			// /etc/shibboleth for the SP
			// Different IdPs might privide different attributes, it probable
			// makes sence
			// to make the mapping in the attribute-*.xml to the same attribute name for the different IdPs.
			String cn =  getAttributeShibboleth("Subject_CommonName" , request) ;  
			log.info(" cn = " + cn);
		        String userBaseDn 	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.userBaseDn");
		        String baseDn      	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.baseDn");
                        // String dn ="cn="+cn+",ou=Personal,ou=Organisation,ou=Malmo,dc=adm,dc=malmo,dc=se" ; // NOTE  
                        String dn ="cn="+cn+","+userBaseDn+","+ baseDn ; // 
			log.info(" dn = " + dn);
			// log.info("Subject_SerialNumber = " + Subject_SerialNumber + " gn = "
		// 			+ gn + " sn = " + sn + " cn = " + cn);
			result = engine.getUserByDn( dn);
		}





		if (result == null) {

			log.info("Trying openAM");
			// OPEN AM
			String dn = request.getHeader("x-ipl-dn");
			String ser = request.getHeader("x-ipl-ser");
			String certificateSubject = request.getHeader("x-ipl-cer-sub");

			if (dn == null) {
				if (ser != null) {
					result = engine.getUserBySerial(ser, certificateSubject);
				}
			} else {
				if (ser == null) {
					result = engine.getUserByDn(dn);
				} else {
					log.debug("Only one of header x-ipl-dn and x-ipl-ser should be used");
					log.debug("x-ipl-dn=[  {} ]", dn);
					log.debug("x-ipl-ser=[  {} ]", ser);

					/**
					 * TODO workaround to detect by path komin/extern
					 */
					String pathInfo = request.getPathInfo();
					if (pathInfo != null && pathInfo.indexOf("komin") > 0) {
						result = engine.getUserByDn(dn);
					} else {
						result = engine
								.getUserBySerial(ser, certificateSubject);
					}

				}
			}

			if (result == null) {

				log.info("userName header not found, get user principal instead");
				log.info("Only one of header x-ipl-dn and x-ipl-ser should be used");
				log.info("x-ipl-dn=[{} ]", dn);
				log.info("x-ipl-ser=[{}  ]", ser);
				log.info("x-ipl-cer-sub=[{}]", certificateSubject);

				Principal principal = request.getUserPrincipal();
				if (principal != null) {
					String hippoDn = "CN="
							+ principal.getName()
							+ ",OU=Personal,OU=Organisation,OU=Hippo Internal User,DC=adm,DC=inherit,DC=se";
					result = engine.getUserByDn(hippoDn);
					// "CN=tesetj,OU=Personal,OU=Organisation,OU=Malmo,DC=adm,DC=malmo,DC=se"

				}

			}

			log.info("Render page with userInfo=[ {} ]", result);

			Enumeration attributes = request.getAttributeNames();
			while (attributes.hasMoreElements()) {
				String attr_name = (String) attributes.nextElement();
				Object attr_val = request.getAttribute(attr_name);
				log.info(attr_name + " = " + attr_val);

			}
		}
			log.info("request.getAttribute(GivenName) = {} "
					, getAttributeShibboleth("GivenName", request));
			log.info("request.getAttribute(sn_id) = {} "
					, getAttributeShibboleth("sn_id", request));
			log.info("request.getAttribute(SecurityLevelDescription) = {} "
					, getAttributeShibboleth("SecurityLevelDescription", request));
			log.info("request.getAttribute(Subject_CountryName) = {} ", 
					getAttributeShibboleth("Subject_CountryName", request));
			log.info("request.getAttribute(Subject_CommonName) = {} "
					, getAttributeShibboleth("Subject_CommonName", request));
			log.info("request.getAttribute(CertificateSerialNumber) = {} "
					, getAttributeShibboleth("CertificateSerialNumber", request));
			log.info("request.getAttribute(dateOfBirth) = {} "
					, getAttributeShibboleth("dateOfBirth", request));
			log.info("request.getAttribute(Subject_OrganisationName) = {} "
					, getAttributeShibboleth("Subject_OrganisationName", request));
			log.info("request.getAttribute(Issuer_OrganizationName) = {} "
					, getAttributeShibboleth("Issuer_OrganizationName", request));
			log.info("request.getAttribute(sn_type) = {} "
					, getAttributeShibboleth("sn_type", request));
			log.info("request.getAttribute(Subject_Surname) = {} "
					, getAttributeShibboleth("Subject_Surname", request));
			log.info("request.getAttribute(Subject_SerialNumber) = {} "
					, getAttributeShibboleth("Subject_SerialNumber", request));
			log.info("request.getAttribute(Gender) = "
					, getAttributeShibboleth("Gender", request));
			log.info("request.getAttribute(ValidationOcspResponse, request) = {} "
					, getAttributeShibboleth("ValidationOcspResponse", request));
			log.info("request.getAttribute(SecurityLevel) = {} "
					, getAttributeShibboleth("SecurityLevel", request));
			log.info("request.getAttribute(Issuer_CommonName) = {} "
					, getAttributeShibboleth("Issuer_CommonName", request));
			log.info("request.getAttribute(age) = {} "
					, getAttributeShibboleth("age", request));
			log.info("request.getAttribute(affiliation) = {} "
					, getAttributeShibboleth("affiliation", request));
			log.info("request.getAttribute(entitlement) = {} "
					, getAttributeShibboleth("entitlement", request));
			log.info("request.getAttribute(eppn) = {} "
					, getAttributeShibboleth("eppn", request));
			log.info("request.getAttribute(persistent-id) = {} "
					, getAttributeShibboleth("persistent-id", request));
			log.info("request.getAttribute(telephoneNumber) = {} "
					, getAttributeShibboleth("telephoneNumber", request));
			log.info("request.getAttribute(unscoped-affiliation) = {} "
					, getAttributeShibboleth("unscoped-affiliation", request));
			return result;
	}
		static String getAttributeShibboleth(String name, final  HttpServletRequest request)
		{
			//Encoding issue. See 
			//https://wiki.shibboleth.net/confluence/display/SHIB2/NativeSPAttributeAccess
			String value =  (String) request.getAttribute(name);
                        if (value != null)  {
			try {
				value= new String( value.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		return value  ;	
		}
		
	
}

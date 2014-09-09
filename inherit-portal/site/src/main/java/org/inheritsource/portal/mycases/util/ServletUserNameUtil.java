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
			String gn = (String) request.getAttribute("GivenName");
			String sn = (String) request.getAttribute("Subject_Surname");
			//String sn_id = (String) request.getAttribute("sn_id");
			String sn_id = null ; 
			String cn = (String) request.getAttribute("Subject_CommonName");
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
			/// String Subject_SerialNumber = (String) request
		/// 			.getAttribute("Subject_SerialNumber");
		/// 	String gn = (String) request.getAttribute("GivenName");
		/// 	String sn = (String) request.getAttribute("Subject_Surname");
		/// 	String sn_id = (String) request.getAttribute("sn_id");
			String cn = (String) request.getAttribute("Subject_CommonName");
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
			log.info("request.getAttribute(GivenName) = "
					+ request.getAttribute("GivenName"));
			log.info("request.getAttribute(sn_id) = "
					+ request.getAttribute("sn_id"));
			log.info("request.getAttribute(SecurityLevelDescription) = "
					+ request.getAttribute("SecurityLevelDescription"));
			log.info("request.getAttribute(Subject_CountryName) = "
					+ request.getAttribute("Subject_CountryName"));
			log.info("request.getAttribute(Subject_CommonName) = "
					+ request.getAttribute("Subject_CommonName"));
			log.info("request.getAttribute(CertificateSerialNumber) = "
					+ request.getAttribute("CertificateSerialNumber"));
			log.info("request.getAttribute(dateOfBirth) = "
					+ request.getAttribute("dateOfBirth"));
			log.info("request.getAttribute(Subject_OrganisationName) = "
					+ request.getAttribute("Subject_OrganisationName"));
			log.info("request.getAttribute(Issuer_OrganizationName) = "
					+ request.getAttribute("Issuer_OrganizationName"));
			log.info("request.getAttribute(sn_type) = "
					+ request.getAttribute("sn_type"));
			log.info("request.getAttribute(Subject_Surname) = "
					+ request.getAttribute("Subject_Surname"));
			log.info("request.getAttribute(Subject_SerialNumber) = "
					+ request.getAttribute("Subject_SerialNumber"));
			log.info("request.getAttribute(Gender) = "
					+ request.getAttribute("Gender"));
			log.info("request.getAttribute(ValidationOcspResponse) = "
					+ request.getAttribute("ValidationOcspResponse"));
			log.info("request.getAttribute(SecurityLevel) = "
					+ request.getAttribute("SecurityLevel"));
			log.info("request.getAttribute(Issuer_CommonName) = "
					+ request.getAttribute("Issuer_CommonName"));
			log.info("request.getAttribute(age) = "
					+ request.getAttribute("age"));
			log.info("request.getAttribute(affiliation) = "
					+ request.getAttribute("affiliation"));
			log.info("request.getAttribute(entitlement) = "
					+ request.getAttribute("entitlement"));
			log.info("request.getAttribute(eppn) = "
					+ request.getAttribute("eppn"));
			log.info("request.getAttribute(persistent-id) = "
					+ request.getAttribute("persistent-id"));
			log.info("request.getAttribute(telephoneNumber) = "
					+ request.getAttribute("telephoneNumber"));
			log.info("request.getAttribute(unscoped-affiliation) = "
					+ request.getAttribute("unscoped-affiliation"));
		return result;
	}

}

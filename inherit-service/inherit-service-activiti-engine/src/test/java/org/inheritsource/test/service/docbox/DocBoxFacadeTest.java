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

package org.inheritsource.test.service.docbox;


import org.activiti.engine.impl.util.json.JSONObject;
import org.inheritsource.service.docbox.DocBoxFacade;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.commons.codec.binary.Base64;

public class DocBoxFacadeTest {

	DocBoxFacade docBox;

	@Before
	public void before() {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"/applicationContext.xml");

		docBox = (DocBoxFacade) applicationContext.getBean("docBoxFacade");

	}

	@Test
	public void testRequestDocBoxSignature() {
	
		
		// doc box number needs to be a existing one 
		String docBoxRef = "765ebfcf-fade-4773-b28f-18513a41680e"; // 
		String personalId = "197307290259"; // NOTE
	// personalId = null; 
		// base 64
		// text may be whatever 
		// at least with 
		// docbox.signed.text.check.strict = false
		String signTextBase64 = "";
		String signText = "hello world";
		if (signText != null) {
			signTextBase64 = new String(
					Base64.encodeBase64(signText.getBytes()));
		}
		System.out.println("hello world 1 ");

		JSONObject response = null;
		try {
			response = docBox.requestDocBoxSignature(docBoxRef,
					signTextBase64, personalId);
		} catch (Exception e) {
		
			e.printStackTrace();
		}

		System.out.println("hello world 2 ");
		
		
	//	transactionId = (String) response.get("transactionId");
		if ( response == null) {
			System.out.println("response == null"   ) ; 
		} else {
		String autoStartToken = (String) response.get("autoStartToken");
		String transactionId = (String) response.get("transactionId")  ; 
		//log.info("transactionId = {} ", transactionId);
		//log.info("autoStartToken = {} ", autoStartToken);
		System.out.println("autoStartToken = " +  autoStartToken ) ; 
		System.out.println("transactionId = " +  transactionId ) ;
		System.out.println(response ) ; 
		System.out.println("hello world 3");
		}
	}

}

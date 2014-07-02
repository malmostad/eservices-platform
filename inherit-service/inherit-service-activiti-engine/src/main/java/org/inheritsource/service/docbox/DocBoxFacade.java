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
 
package org.inheritsource.service.docbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.inheritsource.service.common.domain.DocBoxFormData;
import org.inheritsource.service.common.util.ParameterEncoder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class DocBoxFacade {
	
	public static final Logger log = LoggerFactory.getLogger(DocBoxFacade.class.getName());
	
	public DocBoxFacade() {
		
	}
		
	public DocBoxFormData getDocBoxFormData(String formInstanceId) {
		DocBoxFormData label = null;
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource service = client.resource("http://localhost:8080/docbox/doc/formdata/");
			label = 
					service
					.path(formInstanceId)
					.accept(MediaType.APPLICATION_JSON)
					.put(DocBoxFormData.class);
		}
		catch (Exception e) {
			log.error("Exception: {}" , e.toString());
		}
		return label;
	}
	
	public DocBoxFormData addDocBoxSignature(String docBoxRef, String signature) {
		DocBoxFormData label = null;
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource service = client.resource("http://localhost:8080/docbox/doc/sig/");
			label = 
					service
					.path(docBoxRef)
					.accept(MediaType.APPLICATION_JSON)
					.post(DocBoxFormData.class, signature);
		}
		catch (UniformInterfaceException e) {
			log.error("Exception: {}" , e.toString());
			switch (e.getResponse().getStatus()) {
			case 409:
				// 409 (Conflict) on concurrent update conflict 
				log.info("optimistic lock conflict:  signature of old version cannot be added. docBoxRef=[" +
						docBoxRef + "] signature=[" + signature + "] and response = [" + label + "]");
				label = null;
				break;
			
			case 403:
				// 409 (Conflict) on concurrent update conflict 
				log.warn("the document number and/or the checksum in the signed text do not agree with the document being signed. docBoxRef=[" +
						docBoxRef + "] signature=[" + signature + "] and response = [" + label + "]");
				label = null;
				break;
			
			case 404:
				// 409 (Conflict) on concurrent update conflict 
				log.error("the document was not found. docBoxRef=[" +
						docBoxRef + "] signature=[" + signature + "] and response = [" + label + "]");
				label = null;
				break;
			
			default:
				log.error("Exception docBoxRef=[" +
						docBoxRef + "] signature=[" + signature + "] and response = [" + label + "]");
				label = null;
				break;
			}
		}
		return label;
	}
	
	public static void main(String args[] ) {
		DocBoxFacade f = new DocBoxFacade();
		System.out.println(f.getDocBoxFormData("d6b04342-46e0-4b49-87f3-fe89fbce8281"));
	}
	
}

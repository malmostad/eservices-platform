package org.inheritsource.service.docbox;

import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.inheritsource.service.common.util.ParameterEncoder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class DocBoxFacade {
	
	public static final Logger log = Logger.getLogger(DocBoxFacade.class.getName());
	
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
			log.severe("Exception: " + e);
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
					.put(DocBoxFormData.class, signature);
		}
		catch (UniformInterfaceException e) {
			log.severe("Exception: " + e);
			switch (e.getResponse().getStatus()) {
			case 409:
				// 409 (Conflict) on concurrent update conflict 
				log.info("optimistic lock conflict:  signature of old version cannot be added. docBoxRef=[" +
						docBoxRef + "] signature=[" + signature + "] and response = [" + label + "]");
				label = null;
				break;
			
			case 403:
				// 409 (Conflict) on concurrent update conflict 
				log.warning("the document number and/or the checksum in the signed text do not agree with the document being signed. docBoxRef=[" +
						docBoxRef + "] signature=[" + signature + "] and response = [" + label + "]");
				label = null;
				break;
			
			case 404:
				// 409 (Conflict) on concurrent update conflict 
				log.severe("the document was not found. docBoxRef=[" +
						docBoxRef + "] signature=[" + signature + "] and response = [" + label + "]");
				label = null;
				break;
			
			default:
				log.severe("Exception docBoxRef=[" +
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

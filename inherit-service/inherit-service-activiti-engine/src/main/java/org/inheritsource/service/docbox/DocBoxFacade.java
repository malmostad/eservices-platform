package org.inheritsource.service.docbox;

import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
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
	
	public static void main(String args[] ) {
		DocBoxFacade f = new DocBoxFacade();
		System.out.println(f.getDocBoxFormData("d6b04342-46e0-4b49-87f3-fe89fbce8281"));
	}
	
}

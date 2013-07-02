package org.inheritsource.service.orbeon;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Hello world!
 *
 */
public class OrbeonService 
{
	
	public static final Logger log = Logger.getLogger(OrbeonService.class.getName());
	
	private String persistenceApiBaseUrl = "http://localhost:8080/orbeon/fr/service/exist/crud/";
	
	
	private String parseUniqueXPathExpr(String xmlDataUri, String uniqueXPathExpr) {
		String retVal = null;
		DocumentBuilderFactory domFactory = 
				  DocumentBuilderFactory.newInstance();
				  domFactory.setNamespaceAware(true); 
				  DocumentBuilder builder;
		try {
			builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlDataUri);
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			XPathExpression expr = xpath.compile(uniqueXPathExpr);

            Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			
			if (nodes.getLength()>0) {
				retVal = nodes.item(0).getTextContent();
			}
			if (nodes.getLength()>1) {
				log.warning("uniqueXPathExpr: " + uniqueXPathExpr + "is not unique in uri: " + xmlDataUri + " => Using first node's data: " + retVal);
			}
		} catch (ParserConfigurationException e) {
			log.severe("uri=" + xmlDataUri + ", uniqueXPathExpr: " + uniqueXPathExpr + " Exception: " + e.toString());
		} catch (SAXException e) {
			log.severe("uri=" + xmlDataUri + ", uniqueXPathExpr: " + uniqueXPathExpr + " Exception: " + e.toString());
		} catch (IOException e) {
			log.severe("uri=" + xmlDataUri + ", uniqueXPathExpr: " + uniqueXPathExpr + " Exception: " + e.toString());
		} catch (XPathExpressionException e) {
			log.severe("uri=" + xmlDataUri + ", uniqueXPathExpr: " + uniqueXPathExpr + " Exception: " + e.toString());
		}
		  
	    return retVal;
	}
	
	private String parseXPathExpr(String xmlDataUri, String xPathExpr) {
		String retVal = null;
		DocumentBuilderFactory domFactory = 
				  DocumentBuilderFactory.newInstance();
				  domFactory.setNamespaceAware(true); 
				  DocumentBuilder builder;
		try {
			builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlDataUri);
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			XPathExpression expr = xpath.compile(xPathExpr);
			

            Object result = expr.evaluate(doc, XPathConstants.NODESET);
			
            
            
            
            if (result != null && result instanceof NodeList) {
            	
            	NodeList nl = (NodeList)result;
            	StringWriter sw = new StringWriter();
            	Transformer serializer = TransformerFactory.newInstance().newTransformer();
            	serializer.transform(new DOMSource(nl.item(0)), new StreamResult(sw));
            	retVal = sw.toString(); 
            	retVal = retVal.replaceFirst("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
            }
            
		} catch (ParserConfigurationException e) {
			log.severe("uri=" + xmlDataUri + ", xPathExpr: " + xPathExpr + " Exception: " + e.toString());
		} catch (SAXException e) {
			log.severe("uri=" + xmlDataUri + ", xPathExpr: " + xPathExpr + " Exception: " + e.toString());
		} catch (IOException e) {
			log.severe("uri=" + xmlDataUri + ", xPathExpr: " + xPathExpr + " Exception: " + e.toString());
		} catch (XPathExpressionException e) {
			log.severe("uri=" + xmlDataUri + ", xPathExpr: " + xPathExpr + " Exception: " + e.toString());
		} catch (TransformerException e) {
			log.severe("uri=" + xmlDataUri + ", xPathExpr: " + xPathExpr + " Exception: " + e.toString());
		}
		  
	    return retVal;
	}
	
	public String getFormDataValue(String formPath, String dataUuid, String uniqueXPathExpr) {
		String uri = persistenceApiBaseUrl + formPath + "/data/" + dataUuid + "/data.xml";
		String response = parseUniqueXPathExpr(uri, uniqueXPathExpr);
		System.out.println("response: " + response);
		
		return response;
	}
	
	public String getFormData(String formPath, String dataUuid) {
		String uri = persistenceApiBaseUrl + formPath + "/data/" + dataUuid + "/data.xml";
		String response = parseXPathExpr(uri, "//form");
		System.out.println("response: " + response);
		
		return response;
	}

	private String callGetAndCatchRE(String uri) {
		String result = null;
		try {
			Client client = new Client(new Context(), Protocol.HTTP);
			
			ClientResource cr = new ClientResource(uri);
			cr.setNext(client);
		
			cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "restuser", "restbpm");
				
			final String RESTLET_HTTP_HEADERS = "org.restlet.http.headers";
	        Map<String, Object> reqAttribs = cr.getRequestAttributes();
	        Form headers = (Form)reqAttribs.get(RESTLET_HTTP_HEADERS);
	        if (headers == null) {
	            headers = new Form();
	            reqAttribs.put(RESTLET_HTTP_HEADERS, headers);
	        } 
	        //headers.add("options", "user:" + bonitaUser); 

	        result = (String)cr.get(String.class);
	        			
		} catch (ResourceException e) {
			System.out.println("call ResourceException: " + e);
		}
		
		return result;
	}
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        OrbeonService service = new OrbeonService();
        System.out.println("data: " + service.getFormDataValue("miljoforvaltningen/inventeringsprotokoll_pcb_fogmassor", "059eba3c-c91b-47b7-ac28-b590caa1c073", "//section-1/control-1"));
        System.out.println("data: " + service.getFormData("miljoforvaltningen/inventeringsprotokoll_pcb_fogmassor", "059eba3c-c91b-47b7-ac28-b590caa1c073"));
        System.out.println("data: " + service.getFormDataValue("malmo/profil", "john", "//section-1/email"));
        System.out.println("data: " + service.getFormData("malmo/profil", "john"));
        
    }
}

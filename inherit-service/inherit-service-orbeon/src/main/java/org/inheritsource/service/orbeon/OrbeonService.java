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
 
package org.inheritsource.service.orbeon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
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
public class OrbeonService {

	public static final Logger log = Logger.getLogger(OrbeonService.class
			.getName());

	private String persistenceApiBaseUrl = "http://localhost:8080/exist/rest/db/orbeon-pe/fr/";

	// 3.9 "http://localhost:8080/orbeon/fr/service/exist/crud/";

	private String parseUniqueXPathExpr(String xmlDataUri,
			String uniqueXPathExpr) {
		String retVal = null;
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlDataUri);

			XPath xpath = XPathFactory.newInstance().newXPath();

			XPathExpression expr = xpath.compile(uniqueXPathExpr);

			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;

			if (nodes.getLength() > 0) {
				retVal = nodes.item(0).getTextContent();
			}
			if (nodes.getLength() > 1) {
				log.warning("uniqueXPathExpr: " + uniqueXPathExpr
						+ "is not unique in uri: " + xmlDataUri
						+ " => Using first node's data: " + retVal);
			}
		} catch (ParserConfigurationException e) {
			log.severe("uri=" + xmlDataUri + ", uniqueXPathExpr: "
					+ uniqueXPathExpr + " Exception: " + e.toString());
		} catch (SAXException e) {
			log.severe("uri=" + xmlDataUri + ", uniqueXPathExpr: "
					+ uniqueXPathExpr + " Exception: " + e.toString());
		} catch (IOException e) {
			log.severe("uri=" + xmlDataUri + ", uniqueXPathExpr: "
					+ uniqueXPathExpr + " Exception: " + e.toString());
		} catch (XPathExpressionException e) {
			log.severe("uri=" + xmlDataUri + ", uniqueXPathExpr: "
					+ uniqueXPathExpr + " Exception: " + e.toString());
		}

		return retVal;
	}

	private String parseXPathExpr(String xmlDataUri, String xPathExpr) {
		String retVal = null;
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlDataUri);

			XPath xpath = XPathFactory.newInstance().newXPath();

			XPathExpression expr = xpath.compile(xPathExpr);

			Object result = expr.evaluate(doc, XPathConstants.NODESET);

			if (result != null && result instanceof NodeList) {

				NodeList nl = (NodeList) result;
				StringWriter sw = new StringWriter();
				Transformer serializer = TransformerFactory.newInstance()
						.newTransformer();
				serializer.transform(new DOMSource(nl.item(0)),
						new StreamResult(sw));
				retVal = sw.toString();
				retVal = retVal.replaceFirst(
						"<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
			}

		} catch (ParserConfigurationException e) {
			log.severe("uri=" + xmlDataUri + ", xPathExpr: " + xPathExpr
					+ " Exception: " + e.toString());
		} catch (SAXException e) {
			log.severe("uri=" + xmlDataUri + ", xPathExpr: " + xPathExpr
					+ " Exception: " + e.toString());
		} catch (IOException e) {
			log.severe("uri=" + xmlDataUri + ", xPathExpr: " + xPathExpr
					+ " Exception: " + e.toString());
		} catch (XPathExpressionException e) {
			log.severe("uri=" + xmlDataUri + ", xPathExpr: " + xPathExpr
					+ " Exception: " + e.toString());
		} catch (TransformerException e) {
			log.severe("uri=" + xmlDataUri + ", xPathExpr: " + xPathExpr
					+ " Exception: " + e.toString());
		}

		return retVal;
	}

	public String getFormDataValue(String formPath, String dataUuid,
			String uniqueXPathExpr) {
		String uri = persistenceApiBaseUrl + formPath + "/data/" + dataUuid
				+ "/data.xml";
		String response = parseUniqueXPathExpr(uri, uniqueXPathExpr);
		System.out.println("response: " + response);

		return response;
	}

	public String getFormData(String formPath, String dataUuid) {
		String uri = persistenceApiBaseUrl + formPath + "/data/" + dataUuid
				+ "/data.xml";
		// get form data except for pawap-activities element
		String response = parseXPathExpr(uri,
				"//form/*[not(self::pawap-activities)]");
		System.out.println("response: " + response);

		return response;
	}
	
	public String getFormData(String uri ) {
		
		// get form data except for pawap-activities element
		String response = parseXPathExpr(uri,
				"//form/*[not(self::pawap-activities)]");
		System.out.println("response: " + response);

		return response;
	}

	@SuppressWarnings("unused")
	private String callGetAndCatchRE(String uri) {
		String result = null;
		try {
			Client client = new Client(new Context(), Protocol.HTTP);

			ClientResource cr = new ClientResource(uri);
			cr.setNext(client);

			cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "restuser",
					"restbpm");

			final String RESTLET_HTTP_HEADERS = "org.restlet.http.headers";
			Map<String, Object> reqAttribs = cr.getRequestAttributes();
			Form headers = (Form) reqAttribs.get(RESTLET_HTTP_HEADERS);
			if (headers == null) {
				headers = new Form();
				reqAttribs.put(RESTLET_HTTP_HEADERS, headers);
			}
			// headers.add("options", "user:" + bonitaUser);

			result = (String) cr.get(String.class);

		} catch (ResourceException e) {
			System.out.println("call ResourceException: " + e);
		}

		return result;
	}

	// getVariableMap   : extract the variable names and values from 
	//                    the xml into a map. The map might then be used to 
	//                    get process variables in a BPMN 2.0 process

	public HashMap<String, String> getVariableMap(String uri , int xmlTargetLevel) {

		HashMap<String, String> variableMap = new HashMap<String, String>();

		String inputForm = getFormData(uri);

		// convert String into InputStream
		InputStream is = new ByteArrayInputStream(inputForm.getBytes());

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader streamReader;
		try {
			streamReader = factory.createXMLStreamReader(is);
			
			int xmlLevel = 0;
			List<String> variableName = new ArrayList<String>();
			while (streamReader.hasNext()) {
				streamReader.next();

				if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
					String elementName = streamReader.getLocalName();
					xmlLevel++;
					if (variableName.size() >= xmlLevel) {
						variableName.set(xmlLevel - 1, elementName);
					} else {
						variableName.add(elementName);
					}

				}
				if (streamReader.getEventType() == XMLStreamReader.CHARACTERS) {
					if (xmlLevel == xmlTargetLevel) {
						String elementValue = streamReader.getText();						
						String fullVariableName = "";
						for (String str : variableName) {
							if (fullVariableName != "") {
								fullVariableName = fullVariableName + "_" + str;
							} else {
								fullVariableName = str;
							}
						}
						variableMap.put(fullVariableName, elementValue);
					}
					if (xmlLevel > xmlTargetLevel) {
						log.warning("This xml form seems to have more levels than expected.");
						log.warning("xmlLevel =" + xmlLevel
								+ " xmlTargetLevel =" + xmlTargetLevel);
					}

				}

				if (streamReader.getEventType() == XMLStreamReader.END_ELEMENT) {
					xmlLevel--;

				}

			}
		} catch (XMLStreamException e) {
			log.severe("uri =" + uri 
					+ " Exception: " + e.toString());

		}
		return variableMap;

	}


	public static void main(String[] args) {
		System.out.println("Hello World!");

		OrbeonService service = new OrbeonService();
		System.out.println("data: "
				+ service.getFormData("scriptprocess/scriptprocess--v002_01",
						"d32f9984-dd67-4e52-9c09-99c004c8650b"));
		// System.out.println("data: (section-1)"
		// + service.getFormDataValue(
		// "scriptprocess/scriptprocess--v002_01",
		// "d32f9984-dd67-4e52-9c09-99c004c8650b", "section-1"));
		// System.out.println("data: (control-1)"
		// + service.getFormDataValue(
		// "scriptprocess/scriptprocess--v002_01",
		// "d32f9984-dd67-4e52-9c09-99c004c8650b", "control-1"));
		// System.out.println("data: (control-3)"
		// + service.getFormDataValue(
		// "scriptprocess/scriptprocess--v002_01",
		// "d32f9984-dd67-4e52-9c09-99c004c8650b", "control-3"));
		// System.out.println("data: (control)"
		// + service.getFormDataValue(
		// "scriptprocess/scriptprocess--v002_01",
		// "d32f9984-dd67-4e52-9c09-99c004c8650b",
		// "//section-1/control-1"));
		// System.out.println("data: (control)"
		// + service.getFormDataValue(
		// "scriptprocess/scriptprocess--v002_01",
		// "d32f9984-dd67-4e52-9c09-99c004c8650b",
		// "//section-1/control-3"));
		int xmlTargetLevel = 2;
		String formPath = "scriptprocess/scriptprocess--v002_01" ; 
		String dataUuid ="d32f9984-dd67-4e52-9c09-99c004c8650b" ; 
		String dataUri = service.persistenceApiBaseUrl + formPath + "/data/" + dataUuid
				+ "/data.xml";
		System.out
				.println("data: "
						+ service.getVariableMap(dataUri
								,
								xmlTargetLevel));

		// http://localhost:8080/orbeon/fr/test/fdelgivning-ta-del-beslut--v004/edit/8c7d8218-a4b2-4713-9a7d-4abfa0484c74?orbeon-embeddable=true&pawap-mode=load-deps
		// System.out.println("data: "
		// + service.getFormDataValue(
		// "test/fdelgivning-ta-del-beslut--v004",
		// "8c7d8218-a4b2-4713-9a7d-4abfa0484c74",
		// "//beslutsdokumentref"));
		// System.out.println("data: " +
		// service.getFormDataValue("miljoforvaltningen/inventeringsprotokoll_pcb_fogmassor",
		// "059eba3c-c91b-47b7-ac28-b590caa1c073", "//section-1/control-1"));
		// System.out.println("data: " +
		// service.getFormData("miljoforvaltningen/inventeringsprotokoll_pcb_fogmassor",
		// "059eba3c-c91b-47b7-ac28-b590caa1c073"));
		// System.out.println("data: " +
		// service.getFormData("basprocess/registrera",
		// "259b09b0-0303-4222-a368-13368f0a5ae8"));
		// System.out.println("data: " +
		// service.getFormDataValue("malmo/profil", "john",
		// "//section-1/email"));
		// System.out.println("data: " + service.getFormData("malmo/profil",
		// "john"));
	}
}

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
package org.motrice.postxdb;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Extracts metadata from an Orbeon form definition.
 * Metadata is available in a public map after parsing.
 * The Groovy equivalent is much shorter, but too slow to be included
 * in a database request.
 * This implementation uses SAX.
 * It parses just enough to extract the metadata.
 * Typical execution time less than 1.5 ms.
 */
public class MetaExtractor extends DefaultHandler {
    // Tags etc
    public static final String INSTANCE_TAG = "instance";
    public static final String METADATA_TAG = "metadata";
    public static final String TITLE_TAG = "title";
    public static final String ID_ATTR = "id";
    public static final String METADATA_ID = "fr-form-metadata";

    private static final int INIT_STATE = 0;
    private static final int IN_INSTANCE = 1;
    private static final int IN_METADATA = 2;

    private static final String LANGUAGE_ATTR = "xml:lang";
    private static final String LANGUAGE_KEY = "lang";

    // Parsing state
    private int state = INIT_STATE;

    // Text collected between tags
    private String text;

    /**
     * The outcome of parsing, maps tag name to text (String, String),
     * except language (see key definition above).
     */
    public Map<String,String> metadata = new HashMap<String,String>();

    /**
     * Extract metadata from an Orbeon form definition.
     * @param formdef Form definition XML
     * @returns Some items may be null in the returned result.
     * However, if app/form are null this is not a form definition.
     */
    public static FormdefMeta extract(String formdef) throws IOException {
	SAXParserFactory spfac = SAXParserFactory.newInstance();
	spfac.setNamespaceAware(true);
	SAXParser sp = null;
	MetaExtractor handler = null;

	// Parse, using an instance of this class as handler
	// Stop on catching our special exception
	StringReader sr = null;
	try {
	    sp = spfac.newSAXParser();
	    handler = new MetaExtractor();
	    sr = new StringReader(formdef);
	    InputSource input = new InputSource(sr);
	    sp.parse(input, handler);
	} catch (StopParsingException exc) {
	    // No action, we just want to stop the parser
	} catch (SAXException exc) {
	    throw new IOException("Terminated with parsing problem", exc);
	} catch (ParserConfigurationException exc) {
	    throw new IOException("Terminated with parser config problem", exc);
	} finally {
	    unconditionalClose(sr);
	}

	return new FormdefMeta(handler.metadata);
    }

    /**
     * Main method for testing and timing.
     * Command line arguments: One or more form definition files to parse.
     * Prints metadata along with timing to standard out.
     */
    public static void main(String[] args) throws SAXException, ParserConfigurationException {
	SAXParserFactory spfac = SAXParserFactory.newInstance();
	spfac.setNamespaceAware(true);
	SAXParser sp = spfac.newSAXParser();
	MetaExtractor handler = new MetaExtractor();

	// Parse, using an instance of this class as handler
	// Stop on catching our special exception
	for (int idx = 0; idx < args.length; idx++) {
	    String fileName = args[idx];
	    FileInputStream fis = null;
	    InputStreamReader irs = null;
	    long startTime = 0;
	    long stopTime = 0;

	    try {
		fis = new FileInputStream(fileName);
		irs = new InputStreamReader(fis, "UTF-8");
		InputSource input = new InputSource(irs);
		handler.init();
		startTime = System.nanoTime();
		stopTime = 0;
		sp.parse(input, handler);
	    } catch (StopParsingException exc) {
		// We just want to stop the parser
		stopTime = System.nanoTime();
	    } catch (SAXException exc) {
		System.out.println(fileName + " terminated with parsing problem: " + exc);
	    } catch (IOException exc) {
		System.out.println(fileName + " terminated with i/o problem: " + exc);
	    } finally {
		unconditionalClose(irs);
		unconditionalClose(fis);
	    }

	    if (stopTime > 0) {
		System.out.println("*** " + fileName + ": elapsed time " +
				   (stopTime - startTime)/1000000.0D + " ms");
		TreeMap<String,String> sortedMeta = new TreeMap<String,String>(handler.metadata);
		System.out.println("Metadata:");
		for (Map.Entry entry : sortedMeta.entrySet()) {
		    System.out.println("  " + entry.getKey() + " = " + entry.getValue());
		}
	    }
	}
    }

    /**
     * Helper for the main method. Not needed otherwise.
     */
    private static void unconditionalClose(Closeable whatever) {
	if (whatever != null) {
	    try {
		whatever.close();
	    } catch (IOException exc) {
		// Never mind
	    }
	}
    }

    /**
     * Initialize the handler for parsing.
     * Necessary only when reusing an instance.
     */
    public void init() {
	state = INIT_STATE;
	metadata = new HashMap<String,String>();
    }

    /**
     * The parser collects text content between tags.
     */
    public void characters(char[] buffer, int start, int length) {
	if (state >= IN_METADATA) {
	    String fragment = new String(buffer, start, length);
	    if (text == null) {
		text = fragment;
	    } else {
		text += fragment;
	    }
	}
    }
      
    /**
     * The parser has encountered start of element.
     */ 
    public void startElement(String uri, String localName, String qName, Attributes attributes)
	throws SAXException
    {
	text = null;
	if (INSTANCE_TAG.equals(localName)) {
	    // This is THE instance if we also find the right id attribute
	    for (int idx = 0; idx < attributes.getLength(); idx++) {
		String localAttr = attributes.getLocalName(idx);
		if (ID_ATTR.equals(localAttr)) {
		    if (METADATA_ID.equals(attributes.getValue(idx))) {
			state = IN_INSTANCE;
			// DEBUG
			// System.out.println("--- found metadata instance");
		    }
		}
	    }
	} else if (state >= IN_INSTANCE && METADATA_TAG.equals(localName)) {
	    // DEBUG
	    // System.out.println("--- metadata");
	    state = IN_METADATA;
	} else if (state >= IN_METADATA && TITLE_TAG.equals(localName)) {
	    metadata.put(LANGUAGE_KEY, attributes.getValue(LANGUAGE_ATTR));
	}
    }

    /**
     * The parser has encountered end of element.
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
	if (state >= IN_METADATA) {
	    // Stop parsing at the end of metadata
	    if (METADATA_TAG.equals(localName)) throw new StopParsingException();

	    metadata.put(localName, text);
	}
    }

    /**
     * Special exception to stop the parser.
     */
    public static class StopParsingException extends SAXException {}
}

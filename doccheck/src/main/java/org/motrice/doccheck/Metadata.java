package org.motrice.doccheck;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XMP metadata extracted from a PDF file.
 * ASSUMES the PDF is Motrice-generated.
 */
public class Metadata extends DefaultHandler {
    // Namespace uris
    public static final String DC_NS = "http://purl.org/dc/elements/1.1/";
    public static final String PDF_NS = "http://ns.adobe.com/pdf/1.3/";
    public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    // Tag local names
    private static final String DC_DATE_TAG = "date";
    private static final String DC_DESCRIPTION_TAG = "description";
    private static final String DC_RELATION_TAG = "relation";
    private static final String DC_TITLE_TAG = "title";
    private static final String RDF_DESCRIPTION_TAG = "Description";
    private static final String RDF_LI_TAG = "li";

    // Attribute local names
    private static final String DC_FORMAT_ATTR = "format";
    private static final String DC_IDENTIFIER_ATTR = "identifier";
    private static final String DC_SOURCE_ATTR = "source";
    private static final String PDF_PDFVERSION_ATTR = "PDFVersion";
    private static final String PDF_PRODUCER_ATTR = "Producer";

    // Parsing states
    private static final int INIT_STATE = 0;
    private static final int IN_RDF_DESCRIPTION_STATE = 1;
    private static final int IN_DC_COMPOUND_STATE = 2;
    private static final int ANY_INNER_STATE = 3;

    // Parsing state
    private Stack<String> state;

    // Text collected between tags
    private String text;

    // Text from an li tag
    private String liText;

    // Extracted metadata
    private List<String> docDate;
    private String docDescription;
    private String docFormat;
    private String docIdentifier;
    private String docRelation;
    private String docSource;
    private String docTitle;
    private String pdfPdfVersion;
    private String pdfProducer;

    // Preferred date format
    private static final String DATEFMT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Internal users only, others use parse()
     */
    private Metadata() {
	super();
	state = new Stack<String>();
	docDate = new ArrayList<String>();
    }

    public Map<String,Object> getMetaMap(I18n i18n) {
	Map<String,Object> map = new TreeMap<String,Object>();
	Date date = null;
	for (String dateString : docDate) {
	    SimpleDateFormat fmt = new SimpleDateFormat(DATEFMT);
	    try {
		date = fmt.parse(dateString);
	    } catch (ParseException exc) {
		// Ignore
	    }
	}

	if (date != null) map.put(i18n.m("doc.date.label"), date);
	map.put(i18n.m("doc.description.label"), docDescription);
	map.put(i18n.m("doc.format.label"), docFormat);
	map.put(i18n.m("doc.id.label"), docIdentifier);
	map.put(i18n.m("doc.form.id.label"), docRelation);
	map.put(i18n.m("doc.data.uuid.label"), docSource);
	map.put(i18n.m("doc.title.label"), docTitle);
	map.put(i18n.m("doc.pdf.version"), pdfPdfVersion);
	map.put(i18n.m("doc.pdf.producer"), pdfProducer);
	return map;
    }

    public String toString() {
	StringBuilder sb = new StringBuilder("[Metadata ");
	sb.append("Dates:");
	for (String date : docDate) {
	    sb.append(' ').append(date);
	}
	sb.append("; Description: ").append(docDescription);
	sb.append("; Format: ").append(docFormat);
	sb.append("; DocId: ").append(docIdentifier);
	sb.append("; Relation: ").append(docRelation);
	sb.append("; Title: ").append(docTitle);
	sb.append("; PDF version: ").append(pdfPdfVersion);
	sb.append("; PDF producer: ").append(pdfProducer);
	sb.append("]");
	return sb.toString();
    }

    /**
     * Parse XML supposedly containing XMP metadata
     */
    public static Metadata parse(String xml) {
	SAXParserFactory spfac = SAXParserFactory.newInstance();
	spfac.setNamespaceAware(true);
	SAXParser sp = null;
	try {
	    sp = spfac.newSAXParser();
	} catch (ParserConfigurationException exc) {
	    throw new AppException("metadata.setup.problem", exc);
	} catch (SAXException exc) {
	    throw new AppException("metadata.setup.problem", exc);
	}
	Metadata handler = new Metadata();
	StringReader reader = null;

	// Parse using an instance of this class as handler
	try {
	    reader = new StringReader(xml);
	    InputSource input = new InputSource(reader);
	    sp.parse(input, handler);
	} catch (SAXException exc) {
	    throw new AppException("metadata.parsing.problem", exc);
	} catch (IOException exc) {
	    throw new AppException("metadata.reading.problem", exc);
	} finally {
	    unconditionalClose(reader);
	}

	return handler;
    }

    /**
     * Helper for the parse method. Not needed otherwise.
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
     * The parser has encountered start of element.
     */ 
    public void startElement(String uri, String ln, String qName, Attributes attrs)
	throws SAXException
    {
	text = null;
	state.push(ln);

	if (isRdf(uri, ln, RDF_DESCRIPTION_TAG)) {
	    // Pick up attributes from the rdf:Description tag
	    docFormat = attrs.getValue(DC_NS, DC_FORMAT_ATTR);
	    docIdentifier = attrs.getValue(DC_NS, DC_IDENTIFIER_ATTR);
	    docSource = attrs.getValue(DC_NS, DC_SOURCE_ATTR);
	    pdfPdfVersion = attrs.getValue(PDF_NS, PDF_PDFVERSION_ATTR);
	    pdfProducer = attrs.getValue(PDF_NS, PDF_PRODUCER_ATTR);
	}
    }

    /**
     * The parser has encountered end of element.
     */
    public void endElement(String uri, String ln, String qName) throws SAXException {
	if (isRdf(uri, ln, RDF_LI_TAG)) {
	    liText = text;
	    // Date is special, there may be more than one
	    if (state.search(DC_DATE_TAG) > 0) docDate.add(liText);
	} else if (isDc(uri, ln, DC_DESCRIPTION_TAG)) {
	    docDescription = liText;
	} else if (isDc(uri, ln, DC_RELATION_TAG)) {
	    docRelation = liText;
	} else if (isDc(uri, ln, DC_TITLE_TAG)) {
	    docTitle = liText;
	}

	popState();
    }

    /**
     * The parser collects text content between tags.
     */
    public void characters(char[] buffer, int start, int length) {
	String fragment = new String(buffer, start, length);
	if (text == null) {
	    text = fragment;
	} else {
	    text += fragment;
	}
    }

    private String popState() {
	String result;
	try {
	    result = state.pop();
	} catch (java.util.EmptyStackException exc) {
	    throw new AppException("metadata.processing.problem");
	}

	return result;
    }

    /**
     * Check for a "dc:" tag
     */
    private boolean isDc(String uri, String localName, String testName) {
	return DC_NS.equals(uri) && testName.equals(localName);
    }

    /**
     * Check for a "pdf:" tag
     */
    private boolean isPdf(String uri, String localName, String testName) {
	return PDF_NS.equals(uri) && testName.equals(localName);
    }

    /**
     * Check for an "rdf:" tag
     */
    private boolean isRdf(String uri, String localName, String testName) {
	return RDF_NS.equals(uri) && testName.equals(localName);
    }

}

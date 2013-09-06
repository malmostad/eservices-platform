package org.motrice.postxdb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.EventReaderDelegate;

/**
 * Modifies Orbeon form metadata: version number and/or language.
 * Usage: Construct, then call the appropriate Op methods to indicate the
 * operation to be performed on the form.
 * Then call edit.
 * The underlying assumption is that we insert a version number in the form name.
 * The Groovy equivalent is much shorter but too slow to be included in
 * a database request. Editing occurs in every form write.
 * This implementation uses StAX.
 * Typical execution time including one edit: 40 ms.
 * WARNING: Whatever you do, do NOT use the XMLEvent.writeAsEncodedUnicode() method.
 * Use an event writer.
 */
public class MetaEditor {
    // Should the event writer attempt to repair namespaces?
    public static final Boolean REPAIR_NAMESPACES = Boolean.FALSE;

    // Tags etc
    public static final QName INSTANCE_TAG =
	new QName("http://www.w3.org/2002/xforms", "instance", "xforms");
    public static final QName METADATA_TAG = new QName("metadata");
    public static final QName TITLE_TAG = new QName("title");
    public static final QName FORM_NAME_TAG = new QName("form-name");
    public static final QName ID_ATTR = new QName("id");
    public static final String METADATA_ID = "fr-form-metadata";

    private static final int INIT_STATE = 0;
    private static final int IN_PROGRESS = 1;
    private static final int IN_INSTANCE = 2;
    private static final int IN_METADATA = 3;
    private static final int EDIT_FINISHED = 4;

    private static final QName LANGUAGE_ATTR =
	new QName("http://www.w3.org/XML/1998/namespace", "lang", "xml");
    private static final String LANGUAGE_KEY = "lang";

    // Type of version number increment
    private static final int INCR_NONE = 0;
    private static final int INCR_DRAFT = 1;
    private static final int INCR_VERSION = 2;
    private static final int ASSIGN_VERSION = 3;
    private static final int PUBLISH = 4;
    private static final int WITHDRAW = 5;

    // Parsing state
    private int state = INIT_STATE;

    // For collecting text during parse
    private String text = null;

    // Event factory used during parse
    private XMLEventFactory ef = null;

    // Type of version number increment
    private int versionIncrOp = INCR_NONE;

    // Version number to assign (if ASSIGN_VERSION)
    private int versionToAssign = 0;

    // Draft number to assign (if ASSIGN_VERSION)
    private int draftToAssign = 0;

    // Language to insert
    // Two-letter abbreviation or null
    private String langEditOp = null;

    // Form definition input, an XML document
    private final String formdefIn;

    // Form definition output, an XML document
    public String formdefOut = null;

    // After running edit, contains metadata of the transformed form definition
    private Map<String,String> metadata = null;

    /**
     * Construct from a form definition
     */
    public MetaEditor(String formdef) {
	formdefIn = formdef;
    }

    /**
     * Indicate that the draft number should be incremented
     */
    public void opIncrDraft() {
	versionIncrOp = INCR_DRAFT;
    }

    /**
     * Indicate that the version number should be incremented
     */
    public void opIncrVersion() {
	versionIncrOp = INCR_VERSION;
    }

    /**
     * Indicate that definite version and draft numbers should be assigned
     */
    public void opAssignVersion(int version, int draft) {
	versionIncrOp = ASSIGN_VERSION;
	versionToAssign = version;
	draftToAssign = draft;
    }

    /**
     * Indicate that the version number should be changed to published
     */
    public void opPublish() {
	versionIncrOp = PUBLISH;
    }

    /**
     * Indicate that the version number should be withdrawn
     */
    public void opWithdraw() {
	versionIncrOp = WITHDRAW;
    }

    /**
     * Indicate that the language should be changed
     * @param targetLanguage must be a two-letter language abbreviation
     */
    public void opLangEdit(String targetLanguage) {
	langEditOp = targetLanguage;
    }

    /**
     * Get form metadata after parsing.
     */
    public FormdefMeta getMetadata() {
	return (metadata != null)? new FormdefMeta(metadata) : null;
    }

    /**
     * Do the transformation while collecting metadata in a map.
     * SIDE EFFECT: formdefOut will contain the edited form definition
     * @throws RuntimeException in case of conflict
     */
    public void edit() {
	XMLEventReader evr = null;
	StringReader input = new StringReader(formdefIn);
	XMLEventWriter evw = null;
	StringWriter output = new StringWriter(formdefIn.length());

	try {
	    XMLInputFactory xif = XMLInputFactory.newFactory();
	    evr = xif.createXMLEventReader(input);
	    XMLOutputFactory xof = XMLOutputFactory.newFactory();
	    xof.setProperty("javax.xml.stream.isRepairingNamespaces", REPAIR_NAMESPACES);
	    evw = xof.createXMLEventWriter(output);
	    doEdit(evr, evw);
	    evw.flush();
	    output.flush();
	    formdefOut = output.toString();
	} catch (XMLStreamException exc) {
	    throw new RuntimeException(exc);
	} finally {
	    doClose(evr);
	    doClose(evw);
	}
    }

    /**
     * Contain messy closing of event reader
     */
    private void doClose(XMLEventReader evr) {
	try {
	    if (evr != null) evr.close();
	} catch (XMLStreamException exc) {
	    // Ignore
	}
    }

    private void doClose(XMLEventWriter evw) {
	try {
	    if (evw != null) evw.close();
	} catch (XMLStreamException exc) {
	    // Ignore
	}
    }

    /**
     * Do the transformation.
     * Needs an event writer because we might add more than one event.
     */
    private void doEdit(XMLEventReader evr, XMLEventWriter evw) throws XMLStreamException {
	XMLEvent event = null;
	state = INIT_STATE;
	text = "";
	ef = XMLEventFactory.newFactory();

	while (evr.hasNext()) {
	    event = evr.nextEvent();
	    // If the source document does not have an XML declaration the XML version
	    // and encoding will be null and cause parsing errors later
	    switch (state) {
	    case INIT_STATE:
		// Fix start of document
		event = doInitState(event);
		break;
	    case IN_PROGRESS:
		// Find the right instance element
		event = doInProgress(event);
		break;
	    case IN_INSTANCE:
		event = doInInstance(event);
		break;
	    case IN_METADATA:
		event = doInMetadata(evw, event);
		break;
		// In all other cases, pass the event unchanged
	    }

	    if (event != null) evw.add(event);
	}

	// DEBUG
	// System.out.println("State on exit: " + state);
    }

    /**
     * Make sure the document starts in the right way.
     */
    private XMLEvent doInitState(XMLEvent event) {
	if (event.isStartDocument()) {
	    StartDocument startDoc = (StartDocument)event;
	    // Happens if the original document does not have an XML declaration
	    if (startDoc.getVersion() == null) {
		event = ef.createStartDocument("UTF-8", "1.0");
	    }

	    state = IN_PROGRESS;
	}

	return event;
    }

    /**
     * Parsing has started, find the right instance tag
     */
    private XMLEvent doInProgress(XMLEvent event) {
	if (event.isStartElement()) {
	    StartElement elem = event.asStartElement();

	    // Find instance with the right id
	    if (INSTANCE_TAG.equals(elem.getName())) {
		Attribute id = elem.getAttributeByName(ID_ATTR);
		if (id != null && METADATA_ID.equals(id.getValue()))
		    state = IN_INSTANCE;
	    }
	}

	return event;
    }

    /**
     * Find the metadata tag
     */
    private XMLEvent doInInstance(XMLEvent event) {
	if (event.isStartElement()) {
	    StartElement elem = event.asStartElement();

	    // We are looking for metadata
	    if (METADATA_TAG.equals(elem.getName())) {
		// Prepare to extract metadata
		metadata = new HashMap<String,String>();
		state = IN_METADATA;
	    }
	}

	return event;
    }

    /**
     * Extract and edit metadata
     * Characters are not returned until we reach the end element because
     * we must know the element they belong to
     */
    private XMLEvent doInMetadata(XMLEventWriter evw, XMLEvent event)
	throws XMLStreamException
    {
	XMLEvent result = event;

	if (event.isStartElement()) {
	    // Special treatment of title
	    StartElement elem = event.asStartElement();
	    text = "";

	    // Conditionally edit the language attribute
	    // We must output a new start element in such case
	    // ASSUMES there are no other attributes
	    // ASSUMES the start element has default namespace
	    // This is Orbeon implementation stuff and might change
	    Attribute srcLang = elem.getAttributeByName(LANGUAGE_ATTR);

	    if (srcLang != null) {
		String langName = srcLang.getValue();

		if (langEditOp != null) {
		    Attribute tgtLang = ef.createAttribute(LANGUAGE_ATTR, langEditOp);
		    List<XMLEvent> eventList = new ArrayList<XMLEvent>(1);
		    eventList.add(tgtLang);
		    result = ef.createStartElement(elem.getName(), eventList.iterator(), null);
		    langName = langEditOp;
		}

		metadata.put(LANGUAGE_KEY, langName);
	    }
	} else if (event.isEndElement()) {
	    EndElement elem = event.asEndElement();
	    QName name = elem.getName();
	    if (METADATA_TAG.equals(name)) {
		// Exit from metadata
		state = EDIT_FINISHED;
	    } else {
		Characters chars = null;
		if (FORM_NAME_TAG.equals(name)) {
		    chars = doFormName(text);
		} else {
		    chars = ef.createCharacters(text);
		}

		evw.add(chars);
		metadata.put(name.getLocalPart(), chars.getData());
	    }
	} else if (event.isCharacters()) {
	    Characters chars = event.asCharacters();
	    // There will be whitespace characters events between elements
	    // We assume characters are coalesced into a single event
	    // between start and end elements
	    if (!chars.isWhiteSpace()) {
		text += chars.getData();
		// Don't return this just yet (but do return whitespace)
		result = null;
	    }
	}

	return result;
    }

    /**
     * Process the form name, possibly editing it
     */
    private Characters doFormName(String srcFormName) {
	String tgtText = srcFormName;

	if (versionIncrOp > INCR_NONE) {
	    FormdefPath path = new FormdefPath("dummy/" + srcFormName);
	    switch (versionIncrOp) {
	    case INCR_DRAFT:
		path.nextDraft();
		break;
	    case INCR_VERSION:
		path.nextVersion();
		break;
	    case ASSIGN_VERSION:
		path = new FormdefPath(path.getAppName(), path.getFormName(),
				       versionToAssign, draftToAssign);
		break;
	    case PUBLISH:
		path.publish();
		break;
	    case WITHDRAW:
		path.withdraw();
		break;
	    }

	    tgtText = path.toString(false);
	}

	return ef.createCharacters(tgtText);
    }

    /**
     * Main method for testing and timing.
     * Command line arguments: One or more form definition files to parse.
     * Prints metadata along with timing to standard out.
     */
    public static void main(String[] args) {
	// Parse, using an instance of this class as handler
	// Stop on catching our special exception
	for (int idx = 0; idx < args.length; idx++) {
	    String fileName = args[idx];
	    long startTime = 0;
	    long stopTime = 0;
	    // Do a single edit
	    MetaEditor editor = new MetaEditor(readFile(fileName));
	    editor.opIncrDraft();
	    startTime = System.nanoTime();
	    editor.edit();
	    stopTime = System.nanoTime();

	    // Present the outcome
	    System.out.println("*** " + fileName + ": elapsed time " +
			       (stopTime - startTime)/1000000.0D + " ms");
	}
    }

    private static String readFile(String fileName) {
	String result = null;
	BufferedReader br = null;
	try {
	    br = new BufferedReader(new FileReader(fileName));
	    StringBuilder sb = new StringBuilder();
	    for (String line = br.readLine(); line != null; line = br.readLine()) {
		sb.append(line).append('\n');
	    }

	    result = sb.toString();
	} catch (FileNotFoundException exc) {
	    throw new RuntimeException(exc);
	} catch (IOException exc) {
	    throw new RuntimeException(exc);
	} finally {
	    try {
		br.close();
	    } catch (Exception exc2) {
		// Never mind
	    }
	}

	return result;
    }
}

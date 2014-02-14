package org.motrice.doccheck;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.DatatypeConverter;

/**
 * Processes a PDF file.
 * This implementation stores the entire PDF file in a byte array, implying
 * a 2 GB maximum file size.
 * Enough for our purposes at this time.
 */
public class Processor {
    private static final int BUFSIZE = 1024 * 4;
    private static final String XMPID = "W5M0MpCehiHzreSzNTczkc9d";

    final String fileName;
    final byte[] pdf;
    final I18n i18n;
    final Options opts;
    final LimitedPdfReader reader;

    public Processor(String fileName, byte[] pdf, I18n i18n, Options opts) {
	this.fileName = fileName;
	this.pdf = pdf;
	this.i18n = i18n;
	this.opts = opts;
	reader = new LimitedPdfReader(pdf);
    }

    /**
     * Does the hard work.
     */
    public void process() {
	i18n.p("filename.label", fileName);
	Metadata meta = processMetadata();
	if (meta != null) {
	    i18n.p("metadata.label");
	    Map<String,Object> metaMap = meta.getMetaMap(i18n);
	    for (Map.Entry<String,Object> entry : metaMap.entrySet()) {
		i18n.p(entry.getKey(), entry.getValue().toString());
	    }
	} else {
	    i18n.p("metadata.not.available");
	}

	if (meta != null) {
	    List<byte[]> digestList = processDigests();
	    
	    i18n.p("checksums.label");
	    for (int idx = 0; idx < digestList.size(); idx++) {
		byte[] hash = digestList.get(idx);
		String label = i18n.m("step.label") + ' ' + idx + ' ';
		if (opts.getBool(Options.BASE64)) {
		    i18n.s(label + "(B64): " + DatatypeConverter.printBase64Binary(hash));
		}

		if (opts.getBool(Options.HEX)) {
		    i18n.s(label + "(HEX): " + DatatypeConverter.printHexBinary(hash));
		}
	    }
	}
    }

    /**
     * Find XMP metadata in the document.
     * @return a Map of metadata, or null if metadata not found
     */
    private Metadata processMetadata() {
	reader.reset();
	boolean xmpFound = false;
	List<Integer> idxList = new ArrayList<Integer>();
	StringBuilder sb = new StringBuilder();
	for (String line = reader.readLine(); line != null; line = reader.readLine()) {
	    if (xmpFound) {
		sb.append(line);
		if ((line.indexOf("</") >= 0) && (line.indexOf("xmpmeta") >= 0)) {
		    idxList.add(reader.getPos());
		    break;
		}
	    } else {
		xmpFound = (line.indexOf("xpacket") >= 0) && (line.indexOf(XMPID) >= 0);
		if (xmpFound) idxList.add(reader.getPos());
	    }
	}

	if (opts.isDebug()) {
	    i18n.s("-- XMP Offsets");
	    for (Integer offset : idxList) {
		i18n.s("  " + offset);
	    }
	}

	Metadata result = null;
	if (xmpFound) {
	    byte[] bytes = reader.getSlice(idxList.get(0), idxList.get(1));
	    String xml = null;
	    try {
		xml = new String(bytes, "UTF-8");
	    } catch (java.io.UnsupportedEncodingException exc) {
		// Should not happen, give up if it does
		throw new AppException("unexpected.conflict", exc);
	    }

	    result = Metadata.parse(xml);
	}

	return result;
    }

    /**
     * Compute SHA-256 digest of each document step.
     * @return a list of digests
     */
    private List<byte[]> processDigests() {
	// Get offsets to document step end bytes
	List<Integer> idxList = getChecksumOffsets();
	if (opts.isDebug()) {
	    i18n.s("-- EOF Offsets");
	    for (Integer offset : idxList) {
		i18n.s("  " + offset);
	    }
	}

	// Second pass: compute check sums for document steps
	List<byte[]> hashList = new ArrayList<byte[]>();
	for (int idx : idxList) {
	    reader.reset(idx);
	    // Must be allocated here because the last reader.read() may return a
	    // different buffer
	    byte[] buf = new byte[BUFSIZE];
	    MessageDigest md = null;
	    try {
		md = MessageDigest.getInstance("SHA-256");
	    } catch (java.security.NoSuchAlgorithmException exc) {
		// Should never happen, give up if it does
		throw new AppException("unexpected.conflict", exc);
	    }

	    buf = reader.read(buf);
	    while (buf != null) {
		md.update(buf);
		buf = reader.read(buf);
	    }

	    hashList.add(md.digest());
	}

	return hashList;
    }

    private List<Integer> getChecksumOffsets() {
	List<Integer> ilist = new ArrayList<Integer>();

	reader.reset();
	for (String line = reader.readLine(); line != null; line = reader.readLine()) {
	    if (line.indexOf("startxref") >= 0) {
		line = reader.readLine();
		if (line != null && line.trim().matches("\\d+")) {
		    line = reader.readLine();
		    if (line != null && "%%EOF".equals(line.trim())) {
			ilist.add(reader.getPos());
		    }
		}
	    }
	}

	return ilist;
    }

}

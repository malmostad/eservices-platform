package org.motrice.docbox.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Tiny utility for extracting fields from text.
 */
public class Exxtractor {
    // Delimiters
    private static final char LDELIM = '[';
    private static final char RDELIM = ']';

    // Source to extract from
    private final String text;

    public Exxtractor(String text) {
	this.text = text;
    }

    /**
     * Extract all occurrences of text between paired delimiters.
     */
    public List<String> extract() {
	List<String> list = new ArrayList<String>();
	int ridx = 0;
	int lidx = text.indexOf(LDELIM);
	while (lidx >= 0 && ridx >= 0) {
	    ridx = text.indexOf(RDELIM, lidx);
	    if (ridx >= 0) {
		list.add(text.substring(lidx + 1, ridx));
		lidx = text.indexOf(LDELIM, ridx);
	    }
	}

	return list;
    }
}

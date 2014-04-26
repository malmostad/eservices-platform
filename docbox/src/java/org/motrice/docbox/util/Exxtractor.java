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

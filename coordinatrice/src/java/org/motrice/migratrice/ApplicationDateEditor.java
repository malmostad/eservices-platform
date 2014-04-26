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
package org.motrice.migratrice;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A somewhat flexible Date editor for Grails data binding
 */
class ApplicationDateEditor extends CustomDateEditor {
    private static final String FMT_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String FMT_SECONDS = "yyyy-MM-dd HH:mm:ss";
    private static final String FMT_MINUTES = "yyyy-MM-dd HH:mm";

    public ApplicationDateEditor() {
	super(new SimpleDateFormat(FMT_SECONDS), true);
    }

    public ApplicationDateEditor(DateFormat dateFormat, boolean allowEmpty) {
	super(dateFormat, allowEmpty);
    }

    public ApplicationDateEditor(DateFormat dateFormat, boolean allowEmpty,
				 int exactDateLength)
    {
	super(dateFormat, allowEmpty);
    }

    public void setAsText(String text) throws IllegalArgumentException {
	try {
	    super.setAsText(text);
	} catch (IllegalArgumentException exc) {
	    Date date = doParseText(text);
	    if (date == null) {
		throw exc;
	    } else {
		setValue(date);
	    }
	}
    }

    private Date doParseText(String text) throws IllegalArgumentException {
	Date date = null;
	SimpleDateFormat fmt = new SimpleDateFormat(FMT_MILLIS);
	try {
	    date = fmt.parse(text);
	} catch (ParseException exc) {
	    // Ignore
	}

	if (date == null) {
	    fmt = new SimpleDateFormat(FMT_SECONDS);
	    try {
		date = fmt.parse(text);
	    } catch (ParseException exc) {
		// Ignore
	    }
	}

	if (date == null) {
	    fmt = new SimpleDateFormat(FMT_MINUTES);
	    try {
		date = fmt.parse(text);
	    } catch (ParseException exc) {
		// Ignore
	    }
	}

	return date;
    }

}

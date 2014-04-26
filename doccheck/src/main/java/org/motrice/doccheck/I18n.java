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
package org.motrice.doccheck;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Handles i18n messages.
 */
public class I18n {
    Locale locale = null;
    ResourceBundle messages = null;

    public I18n(String language) {
	locale = new Locale(language);
	messages = ResourceBundle.getBundle("i18n.Messages", locale);
    }

    /**
     * Get an i18n message given a key.
     * @return the message string or the key if no message is found.
     */
    public String m(String key) {
	String message = key;

	try {
	    message = messages.getString(key);
	    // Neutralize stupid ISO-8859-1 encoding
	    message = new String(message.getBytes("ISO-8859-1"), "UTF-8");
	} catch (MissingResourceException exc) {
	    // Ignore
	} catch (java.io.UnsupportedEncodingException exc) {
	    // Very strange if this happens
	    throw new RuntimeException("Unexpected conflict.", exc);
	}

	return message;
    }

    /**
     * Print an i18n message on standard out.
     */
    public void p(String key) {
	System.out.println(m(key));
    }

    public void p(String key, String extra) {
	System.out.println(m(key) + ": " + extra);
    }

    public void s(String msg) {
	System.out.println(msg);
    }

}

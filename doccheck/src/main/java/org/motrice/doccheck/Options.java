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

import java.util.Properties;

/**
 * Container for application options.
 */
public class Options extends Properties  {
    public static final String BASE64 = "base64";
    public static final String DEBUG = "debug";
    public static final String HEX = "hex";
    public static final String META = "meta";
    public static final String LANGUAGE = "language";

    public static final String BASE64_OPT = "-base64";
    public static final String NOBASE64_OPT = "-nobase64";
    public static final String DEBUG_OPT = "-debug";
    public static final String HEX_OPT = "-hex";
    public static final String NOHEX_OPT = "-nohex";
    public static final String META_OPT = "-meta";
    public static final String NOMETA_OPT = "-nometa";
    public static final String ENGLISH_OPT = "-en";
    public static final String SWEDISH_OPT = "-sv";

    public Options(String[] args) {
	super();
	setProperty(BASE64, "true");
	setProperty(DEBUG, "false");
	setProperty(LANGUAGE, "sv");

	for (String arg : args) {
	    if (BASE64_OPT.equals(arg)) {
		setProperty(BASE64, "true");
	    } else if (NOBASE64_OPT.equals(arg)) {
		setProperty(BASE64, "false");
	    } else if (ENGLISH_OPT.equals(arg)) {
		setProperty(LANGUAGE, "en");
	    } else if (HEX_OPT.equals(arg)) {
		setProperty(HEX, "true");
	    } else if (NOHEX_OPT.equals(arg)) {
		setProperty(HEX, "false");
	    } else if (META_OPT.equals(arg)) {
		setProperty(META, "true");
	    } else if (NOMETA_OPT.equals(arg)) {
		setProperty(META, "false");
	    } else if (SWEDISH_OPT.equals(arg)) {
		setProperty(LANGUAGE, "sv");
	    } else if (DEBUG_OPT.equals(arg)) {
		setProperty(DEBUG, "true");
	    }
	}
    }

    public boolean getBool(String name) {
	return new Boolean(getProperty(name, ""));
    }

    public boolean isDebug() {
	return getBool(DEBUG);
    }

}

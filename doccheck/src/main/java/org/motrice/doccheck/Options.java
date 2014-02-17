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

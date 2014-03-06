package org.motrice.coordinatrice;

/**
 * Support for guide uri, a simple string substitution.
 * Given a string and arguments, substitute the following placeholders.
 * 
 * %H root URL as defined in the Coordinatrice config
 * %P BPMN process definition key (i.e. without version number).
 * %V process definition version (an Activiti concept).
 * Formatted as two digits, padded with leading zeros if necessary.
 * %A activity name as defined in the BPMN process.
 * The name defined in the deployment is used, not locale-dependent.
 * %L locale.
 */
public class UriFormatter {
    // Formatting states
    private final static int INIT_STATE = 1;
    private final static int REPL_STATE = 2;

    // Process definition version format (String.format)
    private final static String INTEGER_FMT = "%02d";

    // URL-encoded per cent character
    private final static String URL_PERCENT = encode(Character.toString('%'));

    // A pattern containing placeholders
    private final String pattern;

    /**
     * Construct from a pattern
     */
    public UriFormatter(String pattern) {
	this.pattern = pattern;
    }

    /**
     * Format the pattern, substitute the placeholders.
     * The name parameters are URL-encoded before being inserted.
     * RETURN the formatted string.
     */
    public String format(String baseUri, String procdefKey, Integer procdefVer,
			 String actdefName, String locale) {
	if (procdefKey == null || locale == null)
	    throw new IllegalArgumentException("Process definition key and locale may not be null.");

	StringBuilder sb = new StringBuilder();
	int state = INIT_STATE;
	for (int idx = 0; idx < pattern.length(); idx++) {
	    char ch = pattern.charAt(idx);

	    switch (ch) {
	    case '%':
		if (state == INIT_STATE) {
		    state = REPL_STATE;
		} else if (state == REPL_STATE) {
		    sb.append(URL_PERCENT);
		    state = INIT_STATE;
		}
		break;
	    case 'H':
		if (state == REPL_STATE) {
		    // Do not URL-encode
		    if (baseUri != null) sb.append(baseUri);
		    state = INIT_STATE;
		} else {
		    sb.append(ch);
		}
		break;
	    case 'P':
		if (state == REPL_STATE) {
		    condAppend(sb, procdefKey);
		    state = INIT_STATE;
		} else {
		    sb.append(ch);
		}
		break;
	    case 'V':
		if (state == REPL_STATE) {
		    condAppend(sb, procdefVer);
		    state = INIT_STATE;
		} else {
		    sb.append(ch);
		}
		break;
	    case 'A':
		if (state == REPL_STATE) {
		    condAppend(sb, actdefName);
		    state = INIT_STATE;
		} else {
		    sb.append(ch);
		}
		break;
	    case 'L':
		if (state == REPL_STATE) {
		    condAppend(sb, locale);
		    state = INIT_STATE;
		} else {
		    sb.append(ch);
		}
		break;
	    default:
		if (state == REPL_STATE) {
		    sb.append(URL_PERCENT);
		    state = INIT_STATE;
		}

		sb.append(ch);
	    }
	}

	// A lone percent sign at the end is taken as itself.
	if (state == REPL_STATE) sb.append(encode("%"));
	return sb.toString();
    }

    /**
     * Convenience without process definition version and activity name
     */
    public String format(String baseUri, String procdefKey, String locale) {
	return format(baseUri, procdefKey, null, null, locale);
    }

    /**
     * Convenience without process definition version
     */
    public String format(String baseUri, String procdefKey, String actdefName, String locale) {
	return format(baseUri, procdefKey, null, actdefName, locale);
    }

    /**
     * Conditional append.
     * The text is URL-encoded before being appended.
     */
    private void condAppend(StringBuilder sb, String text) {
	if (text != null) sb.append(encode(text));
    }

    /**
     * Conditional append
     */
    private void condAppend(StringBuilder sb, Integer value) {
	if (value != null) sb.append(String.format(INTEGER_FMT, value));
    }

    /**
     * URL-encode a string to be part of the formatted uri
     */
    private static String encode(String text) {
	String result = null;
	try {
	    result = java.net.URLEncoder.encode(text, "UTF-8");
	} catch (java.io.UnsupportedEncodingException exc) {
	    // Ignore
	}

	return result;
    }

    private static String encode(char ch) {
	return encode(Character.toString(ch));
    }
	
}

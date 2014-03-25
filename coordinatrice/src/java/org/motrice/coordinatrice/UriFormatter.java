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
 * Characters supplied through placeholders, except %H, are URL-encoded.
 * Characters from the pattern are not URL-encoded.
 */
public class UriFormatter {
    // Formatting states
    private final static int INIT_STATE = 1;
    private final static int REPL_STATE = 2;
    private final static int COND_STATE = 3;
    private final static int PREP_STATE = 4;
    private final static int EXPR_TRUE_STATE = 5;
    private final static int EXPR_FALSE_STATE = 6;

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
	StringBuilder sb = new StringBuilder();
	int state = INIT_STATE;
	Boolean condition = null;
	for (int idx = 0; idx < pattern.length(); idx++) {
	    char ch = pattern.charAt(idx);
	    // DEBUG
	    //System.out.println("'" + ch + "': pos=" + idx + "|state=" + state + "|cond=" + condition);

	    switch (ch) {
	    case '%':
		if (state == INIT_STATE) {
		    state = REPL_STATE;
		} else if (state == REPL_STATE) {
		    sb.append(URL_PERCENT);
		    state = INIT_STATE;
		}
		break;
	    case '?':
		if (state == REPL_STATE) {
		    state = COND_STATE;
		} else {
		    append(sb, ch, state, condition);
		}
		break;
	    case '{':
		if (state == PREP_STATE) {
		    state = EXPR_TRUE_STATE;
		} else {
		    append(sb, ch, state, condition);
		}
		break;
	    case '|':
		if (state == EXPR_TRUE_STATE) {
		    state = EXPR_FALSE_STATE;
		} else {
		    append(sb, ch, state, condition);
		}
		break;
	    case '}':
		if (state == EXPR_TRUE_STATE || state == EXPR_FALSE_STATE) {
		    condition = null;
		    state = INIT_STATE;
		} else {
		    append(sb, ch, state, condition);
		}
		break;
	    case 'H':
	    case 'P':
	    case 'V':
	    case 'A':
	    case 'L':
		if (state == COND_STATE) {
		    switch (ch) {
		    case 'H': condition = baseUri != null;
			break;
		    case 'P': condition = procdefKey != null;
			break;
		    case 'V': condition = procdefVer != null;
			break;
		    case 'A': condition = actdefName != null;
			break;
		    case 'L': condition = locale != null;
			break;
		    }

		    state = PREP_STATE;
		} else if (state == REPL_STATE) {
		    switch (ch) {
		    case 'H': if (baseUri != null) sb.append(baseUri);
			// Do not URL-encode the %H placeholder
			break;
		    case 'P': condAppend(sb, procdefKey);
			break;
		    case 'V': condAppend(sb, procdefVer);
			break;
		    case 'A': condAppend(sb, actdefName);
			break;
		    case 'L': condAppend(sb, locale);
			break;
		    }

		    state = INIT_STATE;
		} else {
		    append(sb, ch, state, condition);
		}
		break;
	    default:
		append(sb, ch, state, condition);
		if (state == REPL_STATE) sb.append(URL_PERCENT);
	    }
	}

	// A lone percent sign at the end is taken as itself.
	if (state == REPL_STATE) sb.append(URL_PERCENT);
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
     * Default case of appending a character while honoring the formatter states.
     */
    private void append(StringBuilder sb, char ch, int state, Boolean condition) {
	if (state == EXPR_TRUE_STATE) {
	    if (Boolean.TRUE.equals(condition)) sb.append(ch);
	} else if (state == EXPR_FALSE_STATE) {
	    if (Boolean.FALSE.equals(condition)) sb.append(ch);
	} else if (state != PREP_STATE) {
	    sb.append(ch);
	}
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

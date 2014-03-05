package org.motrice.postxdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A form definition path and helper methods.
 * The string format of a complete form definition path is: app/form--vNNN_DD
 * where NNN is the version number and DD an optional draft number.
 * If the version is withdrawn the string is app/form-vNNN_X
 */
public class FormdefPath {
    public static final char APP_FORM_SEP = '/';
    public static final String VERSION_SEP = "--v";
    public static final char DRAFT_SEP = '_';
    public static final String LIBRARY_NAME = "library";
    public static final int VERSION_SEP_LEN = VERSION_SEP.length();
    // Use Matcher.matches to match the entire path
    public static final Pattern VERSION_PAT = Pattern.compile("(\\d+)(?:_(X|\\d+))?");
    public static final int PUBLISHED = 9999;
    public static final int WITHDRAWN = 10000;
    public static final String WITHDRAWN_SUFFIX = "X";
    public static final int MIN_VERSION = 1;
    public static final int MAX_VERSION = 999;
    public static final int MIN_DRAFT = 1;
    public static final int MAX_DRAFT = 999;

    private static final String V_OUT_OF_RANGE = "Version number out of range in '";
    private static final String D_OUT_OF_RANGE = "Draft number out of range in '";

    // A form definition path with or without version.
    private String path;

    // Application name.
    private String appName;

    // Form name without version.
    private String formName;

    // Form version number or null if the path has no version.
    // The first version number is 1.
    private Integer fvno;

    // Draft number or null if there is no version.
    // The first draft number is 1.
    // The magic draft number 9999 means this version is published.
    // 10000 (magic number) means the version has been explicitly withdrawn.
    // Note that there is an intentional progression in draft numbers.
    private Integer draft;

    /**
     * Construct from a path.
     * @throw IllegalArgumentException if the path does not have an app and
     * form name separated by the APP_FORM_SEP separator, or if the version
     * or draft number are out of bounds.
     * (The application will probably prevent this from happening anyway.)
     * Paths that do not match the versioning pattern are considered unversioned
     * even if they contain the VERSION_SEP separator.
     * The constructor does not examine app and form names with respect to
     * the characters they contain.
     * This is up to the application, but Orbeon Forms actually accepts names that
     * it rejects in later steps.
     */
    public FormdefPath(String path) {
	this.path = path;
	int idx1 = path.indexOf(APP_FORM_SEP);
	if (idx1 < 0) throw new IllegalArgumentException("Invalid path: '" + path + "'");
	appName = path.substring(0, idx1);
	idx1++;
	int idx2 = path.lastIndexOf(VERSION_SEP);
	formName = (idx2 < 0)? path.substring(idx1) : path.substring(idx1, idx2);
	if (idx2 >= 0) {
	    Matcher m = VERSION_PAT.matcher(path.substring(idx2 + VERSION_SEP_LEN));
	    fvno = null;
	    draft = null;
	    if (m.matches()) {
		try {
		    fvno = Integer.parseInt(m.group(1));
		    checkVersion(fvno, path);
		} catch (NumberFormatException exc) {
		    // The pattern guarantees the version number contains only digits.
		    // But there may be too many to fit in an Integer.
		    throw new IllegalArgumentException(V_OUT_OF_RANGE + path + "'");
		}

		if (fvno != null) {
		    if (WITHDRAWN_SUFFIX.equals(m.group(2))) {
			draft = WITHDRAWN;
		    } else if (m.group(2) == null) {
			draft = PUBLISHED;
		    } else {
			try {
			    draft = Integer.parseInt(m.group(2));
			    checkDraft(draft, path);
			} catch (NumberFormatException exc) {
			    // The pattern guarantees the draft number contains only digits.
			    // But there may be too many to fit in an Integer.
			    throw new IllegalArgumentException(D_OUT_OF_RANGE + path + "'");
			}
		    }
		}
	    } else {
		// Pattern does not match, change the form name to contain everything
		// from the app/form separator
		formName = path.substring(idx1);
	    }
	}
    }

    private static void checkDraft(Integer draft, String path) {
	if (draft < 1 || draft > MAX_DRAFT)
	    throw new IllegalArgumentException(D_OUT_OF_RANGE + path + "'");
    }

    private static void checkVersion(Integer version, String path) {
	if (version < 1 || version > MAX_VERSION)
	    throw new IllegalArgumentException(V_OUT_OF_RANGE + path + "'");
    }

    /**
     * Construct from path components.
     */
    public FormdefPath(String appName, String formName, Integer fvno, Integer draft) {
	this.appName = appName;
	this.formName = formName;
	this.fvno = fvno;
	this.draft = (this.fvno == null)? null : draft;
    }

    /**
     * Construct a published formdef path.
     */
    public FormdefPath(String appName, String formName, Integer fvno) {
	this(appName, formName, fvno, PUBLISHED);
    }

    public FormdefPath(String appName, String formName) {
	this(appName, formName, null, null);
    }

    /**
     * Copy constructor
     */
    public FormdefPath(FormdefPath other) {
	this(other.appName, other.formName, other.fvno, other.draft);
    }

    /**
     * Get the application name part of a path.
     */
    public static String app(String path) {
	int idx = path.indexOf(APP_FORM_SEP);
	return path.substring(0, idx);
    }

    /**
     * Get the form name part of a path.
     */
    public static String form(String path) {
	int idx1 = path.indexOf(APP_FORM_SEP) + 1;
	int idx2 = path.lastIndexOf(VERSION_SEP);
	return (idx2 < 0)? path.substring(idx1) : path.substring(idx1, idx2);
    }

    public boolean isLibrary() {
	return LIBRARY_NAME.equals(formName);
    }

    /**
     * Get the unversioned app/form path.
     */
    public String getUnversioned() {
	return appName + APP_FORM_SEP + formName;
    }

    public String getAppName() {return appName;}
    // Form name without version
    public String getFormName() {return formName;}
    public boolean isVersioned() {return fvno != null;}
    public Integer getVersion() {return fvno;}
    public boolean isPublished() {return (fvno != null) && (draft != null) && (draft == PUBLISHED);}
    public boolean isDraftState() {
	return (fvno != null) && (draft != null) && (draft > 0) && (draft < PUBLISHED);
    }
    public boolean isWithdrawn() {return (fvno != null) && (draft != null) && (draft == WITHDRAWN);}
    public Integer getDraft() {return draft;}

    public FormdefPath nextDraft() {
	if (!isVersioned()) {
	    fvno = MIN_VERSION;
	    draft = MIN_DRAFT;
	} else if (isDraftState()) {
	    draft++;
	} else {
	    fvno++;
	    draft = 1;
	}

	return this;
    }

    public FormdefPath nextVersion() {
	fvno = isVersioned()? fvno + 1 : MIN_VERSION;
	draft = 1;
	return this;
    }

    public void publish() {
	if (!isVersioned())
	    throw new IllegalStateException("Publish on unversioned form path");
	draft = PUBLISHED;
    }

    public void withdraw() {
	if (!isVersioned())
	    throw new IllegalStateException("Withdraw on unversioned form path");
	draft = WITHDRAWN;
    }

    public String toString() {
	return toString(true);
    }

    /**
     * Combine components to a string with or without the app name
     */
    public String toString(boolean withAppName) {
	StringBuilder sb = new StringBuilder();
	if (withAppName) sb.append(appName).append(APP_FORM_SEP);
	sb.append(formName);

	if (isVersioned()) {
	    sb.append(VERSION_SEP);
	    int len = sb.length();
	    sb.append(fvno);
	    while (sb.length() < len + 3) sb.insert(len, '0');

	    if (isDraftState()) {
		sb.append(DRAFT_SEP);
		len = sb.length();
		sb.append(draft);
		while (sb.length() < len + 2) sb.insert(len, '0');
	    } else if (isWithdrawn()) {
		sb.append(DRAFT_SEP);
		sb.append(WITHDRAWN_SUFFIX);
	    }
	}

	return sb.toString();
    }
}

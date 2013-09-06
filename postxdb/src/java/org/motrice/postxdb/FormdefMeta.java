package org.motrice.postxdb;

import java.util.Map;

/**
 * Form definition metadata, usually extracted from XML.
 * All items may be null.
 * TODO: Complete form logo information (mediatype, filename, size).
 */
public class FormdefMeta {
    // Tag names used for metadata (language is an attribute)
    public static final String APP_TAG = "application-name";
    public static final String AUTHOR_TAG = "author";
    public static final String DESCRIPTION_TAG = "description";
    public static final String FORM_TAG = "form-name";
    public static final String LANGUAGE_KEY = "lang";
    public static final String LOGO_TAG = "logo";
    public static final String TITLE_TAG = "title";

    private static final int MULT = 17;

    // App name
    public String app;

    // Form name
    public String form;

    // Form title
    public String title;

    // Form description
    public String description;

    // Form author (username?)
    public String author;

    // Form language (two-letter abbreviation)
    public String language;

    // Logo url
    public String logo;

    /**
     * Construct from a map containing tag names and values produced by MetaExtractor
     */
    public FormdefMeta(Map<String,String> map) {
	app = nonEmptyString(map.get(APP_TAG));
	author = nonEmptyString(map.get(AUTHOR_TAG));
	description = nonEmptyString(map.get(DESCRIPTION_TAG));
	form = nonEmptyString(map.get(FORM_TAG));
	language = nonEmptyString(map.get(LANGUAGE_KEY));
	logo = nonEmptyString(map.get(LOGO_TAG));
	title = nonEmptyString(map.get(TITLE_TAG));
    }

    private static String nonEmptyString(String str) {
	return (str == null || str.length() == 0)? null : str;
    }

    public int hashCode() {
	int hash = 0;
	if (app != null) hash = MULT * hash + app.hashCode();
	if (form != null) hash = MULT * hash + form.hashCode();
	if (title != null) hash = MULT * hash + title.hashCode();
	if (description != null) hash = MULT * hash + description.hashCode();
	if (author != null) hash = MULT * hash + author.hashCode();
	if (language != null) hash = MULT * hash + language.hashCode();
	if (logo != null) hash = MULT * hash + logo.hashCode();
	return hash;
    }

    public boolean equals(Object obj) {
	if (obj == null || !(obj instanceof FormdefMeta)) return false;
	FormdefMeta other = (FormdefMeta)obj;
	return stringCompare(app, other.app) && stringCompare(form, other.form) &&
	    stringCompare(title, other.title) && stringCompare(description, other.description) &&
	    stringCompare(author, other.author) && stringCompare(language, other.language) &&
	    stringCompare(logo, other.logo);
    }

    public String diff(FormdefMeta other) {
	StringBuilder sb = new StringBuilder();
	sb.append('{');
	if (!stringCompare(app, other.app)) sb.append("app (" + app + ", " + other.app + ") ");
	if (!stringCompare(form, other.form)) sb.append("form (" + form + ", " + other.form + ") ");
	if (!stringCompare(title, other.title)) sb.append("title (" + title + ", " + other.title + ") ");
	if (!stringCompare(description, other.description))
	    sb.append("description (" + description + ", " + other.description + ") ");
	if (!stringCompare(author, other.author)) sb.append("author (" + author + ", " + other.author + ") ");
	if (!stringCompare(language, other.language))
	    sb.append("language (" + language + ", " + other.language + ") ");
	if (!stringCompare(logo, other.logo)) sb.append("logo (" + logo + ", " + other.logo + ") ");
	sb.append('}');
	return sb.toString();
    }

    private boolean stringCompare(String s1, String s2) {
	boolean result = false;
	if (s1 != null && s2 != null) {
	    result = s1.equals(s2);
	} else {
	    result = s1 == null && s2 == null;
	}

	return result;
    }
}

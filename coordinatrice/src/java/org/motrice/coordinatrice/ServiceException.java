package org.motrice.coordinatrice;

import java.util.List;

/**
 * Exception thrown in one of the application services.
 * Has to be a RuntimeException to cause transaction rollback.
 */
public class ServiceException extends RuntimeException {
    // Localized message key
    private String key;

    // Args for the localized message
    private List<Object> args;

    public ServiceException(String message) {
	super(message);
    }

    public ServiceException(String message, String key) {
	super(message);
	this.key = key;
	this.args = null;
    }

    public ServiceException(String message, String key, List<Object> args) {
	super(message);
	this.key = key;
	this.args = args;
    }

    public String getKey() {
	return key;
    }

    public List<Object> getArgs() {
	return args;
    }
}

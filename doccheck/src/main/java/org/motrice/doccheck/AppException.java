package org.motrice.doccheck;

/**
 * Application-specific exception.
 * The message string should be a key to an I18n message.
 */
public class AppException extends RuntimeException {

    public AppException(String message) {
	super(message);
    }

    public AppException(String message, Throwable cause) {
	super(message, cause);
    }

}

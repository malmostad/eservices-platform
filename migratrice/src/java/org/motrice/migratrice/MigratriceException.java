package org.motrice.migratrice;

/**
 * Application-specific exception.
 * NOTE: When throwing a RuntimeException (also known as an "unchecked exception")
 * in a service, Grails (actually Spring) rolls back the transaction.
 * The controller must catch the exception.
 * In contrast, if a service throws a checked exception (non-RuntimeException)
 * the controller cannot catch it, and the transaction is not rolled back.
 */
public class MigratriceException extends RuntimeException {
    // 409 is "Conflict"
    public static final int DEFAULT_HTTP_RESPONSE_CODE = 409;
    // Message code
    protected String code;
    public String getCode() {return code;}
    // HTTP response code
    protected int http;
    public int getHttp() {return http;}
    public void setHttp(int http) {this.http = http;}

    public MigratriceException(String code, String message) {
	super(message);
	this.code = code;
	http = DEFAULT_HTTP_RESPONSE_CODE;
    }

    public MigratriceException(String code, String message, Throwable cause) {
	super(message, cause);
	this.code = code;
	http = DEFAULT_HTTP_RESPONSE_CODE;
    }

}

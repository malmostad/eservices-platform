package org.motrice.tdocbox;

/**
 * Exception used in services in order to cause a transaction rollback.
 * No extra properties, but needed for specific exception handling.
 */
public class ServiceException extends RuntimeException {
    // Transaction id if applicable
    private final String transactionId;
    
    public ServiceException(String message) {
	this(message, null, null);
    }
    
    public ServiceException(String message, String transactionId) {
	this(message, transactionId, null);
    }

    public ServiceException(String message, Throwable cause) {
	this(message, null, cause);
    }

    public ServiceException(String message, String transactionId, Throwable cause) {
	super(message, cause);
	this.transactionId = transactionId;
    }

    public String getTransactionId() {
	return transactionId;
    }

}

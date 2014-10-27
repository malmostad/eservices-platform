package org.motrice.signatrice;

/**
 * Exception used in services in order to cause a transaction rollback.
 */
public class ServiceException extends RuntimeException {
    // DocBox message code, a string.
    // The format is "DOCBOX.nnn"
    private final String docboxCode;

    // Transaction id if applicable
    private final String transactionId;
    
    public ServiceException(String docboxCode, String message) {
	this(docboxCode, message, null, null);
    }
    
    public ServiceException(String docboxCode, String message, String transactionId) {
	this(docboxCode, message, transactionId, null);
    }

    public ServiceException(String docboxCode, String message, Throwable cause) {
	this(docboxCode, message, null, cause);
    }

    public ServiceException(String docboxCode, String message,
			    String transactionId, Throwable cause)
    {
	super(message, cause);
	this.docboxCode = docboxCode;
	this.transactionId = transactionId;
    }

    /**
     * Return a canonical string that may be easily decoded.
     */
    public String getCanonical() {
	return docboxCode + '|' + getMessage();
    }

    public String getDocboxCode() {
	return docboxCode;
    }

    public String getTransactionId() {
	return transactionId;
    }

}

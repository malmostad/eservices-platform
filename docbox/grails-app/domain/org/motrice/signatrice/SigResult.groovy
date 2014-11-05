package org.motrice.signatrice

import java.util.concurrent.TimeUnit

/**
 * A test case run.
 * NOTE: In spite of auto timestamping you must assign dateCreated before
 * you add a result to a test case.
 * Comparable is based on dateCreated.
 */
class SigResult implements Comparable {
  // Age window for collecting the result of a signature request.
  static final Long MIN_COLLECT_AGE_MILLIS = TimeUnit.SECONDS.toMillis(3)
  static final Long MAX_COLLECT_AGE_MILLIS = TimeUnit.SECONDS.toMillis(190)

  // Window for inserting a signature after it has been obtained.
  static final Long MIN_FINISH_AGE_MILLIS = TimeUnit.SECONDS.toMillis(1)
  static final Long MAX_FINISH_AGE_MILLIS = TimeUnit.MINUTES.toMillis(30)

  // The point in time when the log was created which is probably also when
  // the test case was run.
  // Used as the basis for sorting.
  Date dateCreated

  // Transaction id used in CGI requests
  String transactionId

  // Another reference to this transaction created by GPR
  String orderRef

  // A token that must be used when starting the BankID program
  String autoStartToken

  // The display name is required in spite of two other transaction identifiers
  SigDisplayname displayName

  // Policy, also known as service ID
  // Predefined policies for test only
  SigPolicy policy

  // Swedish personal identity number (12 digits)
  // Limits signing to this person.
  String personalIdNo

  // Request status returned by the signing service
  SigProgress progressStatus

  // Fault status returned by the signing service, or null
  SigFaultObj faultStatus

  // Outcome of signature request: the signature
  // Returned as String by CollectResponse. Probably Base64.
  String signature

  // Point in time when the signature was picked up and stored
  Date sigTstamp

  // Original docboxRef of the document to be signed, if applicable.
  String docboxRefIn

  // Target docboxRef of the document after the signature was added, if applicable.
  String docboxRefOut

  // Conflict message if a signature has been acquired, but failed to
  // add it to the document.
  // Also indicates end of processing.
  String finishConflict

  static belongsTo = [scheme: SigScheme]
  SortedSet attrs
  static hasMany = [attrs: SigAttribute]
  static transients = ['createdFmt', 'sigSize']
  static constraints = {
    transactionId nullable: true, maxSize: 32
    orderRef nullable: true, maxSize: 120
    autoStartToken nullable: true, maxSize: 120
    displayName nullable: true, maxSize: 120
    policy nullable: true
    personalIdNo nullable: true, maxSize: 24
    progressStatus nullable: true
    faultStatus nullable: true
    signature nullable: true
    sigTstamp nullable: true
    docboxRefIn nullable: true, maxSize: 200
    docboxRefOut nullable: true, maxSize: 200
    finishConflict nullable: true, maxSize: 400
  }
  static mapping = {
    dateCreated index: 'Created_Idx'
    signature type: 'text'
  }

  /**
   * Assign a signature.
   * The signature must be a Base64-encoded text.
   */
  def assignSignature(String sigBase64) {
    signature = sigBase64
    sigTstamp = new Date()
  }

  /**
   * Is this request still being processed for getting a signature?
   * NOTE: This method is mainly for documentation, it is probably
   * never called.
   * It is a model for database queries.
   */
  boolean isInProcess() {
    def now = new Date()
    !faultStatus && progressStatus?.active &&
    (now.time - dateCreated.time) > MIN_COLLECT_AGE_MILLIS &&
    (now.time - dateCreated.time) < MAX_COLLECT_AGE_MILLIS
  }

  /**
   * Is this request ready to be finished, i.e. have its signature inserted
   * and a new document step created?
   * Like the above method this is mainly for documentation, a model for
   * database queries.
   */
  boolean isReadyForFinish() {
    def now = new Date()
    sigTstamp && !finishConflict && !docboxRefOut &&
    (now.time - sigTstamp.time) > MIN_FINISH_AGE_MILLIS &&
    (now.time - sigTstamp.time) < MAX_FINISH_AGE_MILLIS
  }

  /**
   * Condition for end of processing, successful or not.
   */
  boolean isProcessingComplete() {
    def now = new Date()
    faultStatus || finishConflict || docboxRefOut ||
    (sigTstamp && (now.time - sigTstamp.time) > MAX_FINISH_AGE_MILLIS) ||
    (sigTstamp == null && (now.time - dateCreated.time) > MAX_COLLECT_AGE_MILLIS) 
  }

  /**
   * Condition for successful signature request and post-processing.
   */
  boolean isSuccess() {
    sigTstamp && !finishConflict && docboxRefOut
  }

  /**
   * Get the creation timestamp as a formatted string.
   */
  String getCreatedFmt() {
    dateCreated?.format('yyyy-MM-dd HH:mm')
  }

  /**
   * Get the creation timestamp as a formatted string.
   */
  String getCreatedFmtSec() {
    dateCreated?.format('yyyy-MM-dd HH:mm:ss')
  }

  /**
   * Get the size of the signature, or '--' if null.
   */
  String getSigSize() {
    signature? signature.size() : '--'
  }

  /**
   * This object as a property map suitable for JSON conversion.
   * In this conversion all references are converted to String.
   * Using "as JSON" on an object will result in object references.
   */
  Map toMap() {
    [dateCreated: createdFmtSec,
    service: scheme?.service?.alias, transactionId: transactionId,
    autoStartToken: autoStartToken, personalIdNo: personalIdNo ?: '',
    progressStatus: progressStatus?.toString(), faultStatus: faultStatus?.toString(),
    docboxRefIn: docboxRefIn, docboxRefOut: docboxRefOut,
    signature: signature?.size() ?: 0,
    processingComplete: processingComplete, success: success]
  }

  String toString() {
    "${createdFmt} ${transactionId}"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    orderRef.hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof SigResult) {
      def other = (SigResult)obj
      result = orderRef == other.orderRef
    }

    return result
  }

  /**
   * Date-based comparison, latest first.
   */
  int compareTo(Object obj) {
    def other = (SigResult)obj
    -dateCreated.compareTo(other.dateCreated)
  }

}

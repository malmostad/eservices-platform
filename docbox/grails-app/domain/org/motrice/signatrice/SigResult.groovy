package org.motrice.signatrice

/**
 * A test case run.
 * NOTE: In spite of auto timestamping you must assign dateCreated before
 * you add a result to a test case.
 * Comparable is based on dateCreated.
 */
class SigResult implements Comparable {
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
  String personalIdNo

  // Request status
  SigProgress progressStatus

  // Fault status if any
  SigFaultObj faultStatus

  // Outcome of the test case: the signature
  // Returned as String by CollectResponse. Probably Base64.
  String signature

  // Original docboxRef of the document to be signed, if applicable.
  String docboxRefIn

  // Target docboxRef of the document after the signature was added, if applicable.
  String docboxRefOut

  static belongsTo = [tcase: SigTestcase]
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
    docboxRefIn nullable: true, maxSize: 200
    docboxRefOut nullable: true, maxSize: 200
  }
  static mapping = {
    dateCreated index: 'Created_Idx'
    signature type: 'text'
  }

  /**
   * Get the creation timestamp as a formatted string.
   */
  String getCreatedFmt() {
    dateCreated?.format('yyyy-MM-dd HH:mm')
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
    ['class': SigResult.class.name, dateCreated: dateCreated,
    service: tcase?.service?.toString(),
    transactionId: transactionId, orderRef: orderRef,
    autoStartToken: autoStartToken, displayName: displayName?.toString(),
    policy: policy?.toString(), personalIdNo: personalIdNo ?: '',
    progressStatus: progressStatus?.toString(), faultStatus: faultStatus?.toString(),
    signature: signature?.size() ?: null]
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

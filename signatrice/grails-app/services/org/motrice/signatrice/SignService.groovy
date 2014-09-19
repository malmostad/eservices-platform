package org.motrice.signatrice

import java.sql.Timestamp

import org.apache.commons.logging.LogFactory
import grails.converters.JSON

import org.motrice.signatrice.cgi.AuthenticateRequestType
import org.motrice.signatrice.cgi.CollectRequestType
import org.motrice.signatrice.cgi.CollectResponseType
import org.motrice.signatrice.cgi.AuthenticateRequestType
import org.motrice.signatrice.cgi.EndUserInfoType
import org.motrice.signatrice.cgi.GrpFault
import org.motrice.signatrice.cgi.GrpFaultType
import org.motrice.signatrice.cgi.GrpService
import org.motrice.signatrice.cgi.GrpServicePortType
import org.motrice.signatrice.cgi.ObjectFactory
import org.motrice.signatrice.cgi.OrderResponseType
import org.motrice.signatrice.cgi.Property
import org.motrice.signatrice.cgi.SignRequestType

/**
 * Does the signing.
 * Calls methods in 
 */
class SignService {
  // Format string for transaction id
  static final String TXID_FORMAT = 'MOT-%06d'

  // Provider name needed by CGI. The only one defined so far.
  static final String PROVIDER = 'bankid'

  // Min and max SigResult age for calling Collect
  static final Long SECOND = 1000L
  private static final Long MIN_COLLECT_AGE_MILLIS = 3 * SECOND
  private static final Long MAX_COLLECT_AGE_MILLIS = 190 * SECOND
  
  private static final log = LogFactory.getLog(this)

  def auditService

  /**
   * Check a service connection by downloading its WSDL.
   */
  def String checkService(SigService service) {
    if (log.debugEnabled) log.debug "checkService << ${service}"
    String result = null
    try {
      result = service?.serviceUrl?.text
    } catch (Exception exc) {
      if (log.debugEnabled) log.debug "checkService EXCEPTION: ${exc}"
      throw new ServiceException(exc.message)
    }

    if (log.debugEnabled) log.debug "checkService >> ${result.size()}"
    return result
  }

  /**
   * Send a sign request, return a newly created SigResult containing
   * the return values, or throw a ServiceException.
   */
  SigResult sign(SigTestcase testcase, request) {
    if (log.debugEnabled) log.debug "sign << ${testcase} from ${request.remoteAddr}"
    def transactionId = generateTransactionId()
    def result = null
    try {
      def orderResponse = doSign(testcase, transactionId, request)
      result = new SigResult(transactionId: orderResponse.transactionId,
      orderRef: orderResponse.orderRef,
      autoStartToken: orderResponse.autoStartToken,
      displayName: testcase.displayName,
      policy: testcase.policy,
      personalIdNo: testcase.personalIdNo,
      progressStatus: SigProgress.initialState())
      result.dateCreated = new Date()

      // Add the new result to the test case
      testcase.addToResults(result).save()
    } catch (GrpFault fault) {
      if (log.debugEnabled) log.debug "sign EXCEPTION ${fault}"
      def info = fault.faultInfo
      String faultMsg = "${info?.faultStatus?.value()}: ${info.detailedDescription}"
      auditService.logSignEvent(transactionId, true, 'Sign request failed', faultMsg,
				StackTracer.trace(fault), request)
      throw new ServiceException(faultMsg, transactionId, fault)
    }
    if (log.debugEnabled) log.debug "sign >> ${result}"
    return result
  }

  /**
   * The details of a signing request
   */
  private OrderResponseType doSign(SigTestcase testcase, String transactionId, request) {
    def service = new GrpService(testcase.service.serviceUrl, testcase.service.qname)
    def of = new ObjectFactory()
    def signReq = of.createSignRequestType()
    signReq.personalNumber = testcase.personalIdNo
    signReq.policy = testcase.policy as String
    signReq.displayName = testcase.displayName as String
    signReq.transactionId = transactionId
    signReq.provider = PROVIDER
    def endUserInfo = of.createEndUserInfoType()
    endUserInfo.type = "IP_ADDR"
    endUserInfo.value = request.remoteAddr
    signReq.endUserInfo << endUserInfo
    signReq.userVisibleData = testcase.encodedVisibleText
    def port = service.grpServiceServletPort
    port.sign(signReq)
  }

  /**
   * Generate and assign a pseudo-random transaction id
   */
  private String generateTransactionId() {
    Integer rand = Math.random() * 10000000.0D
    String.format(TXID_FORMAT, rand)
  }

  // Candidate SigResults with hardwired progess status id:s
  // The max age limit should not be necessary but is a guard
  // against infinite repetition in case there is a problem.
  private static String COLLECT_Q = 'from SigResult r where ' +
    'r.dateCreated between ? and ? and r.progressStatus.id in (1, 2, 3) and ' +
    'faultStatus is null'

  /**
   * Invoke Collect for all outstanding SigResults
   */
  def collectAll() {
    def now = new Date()
    def beg = new Timestamp(now.time - MAX_COLLECT_AGE_MILLIS)
    def end = new Timestamp(now.time - MIN_COLLECT_AGE_MILLIS)
    def candidates = SigResult.findAll(COLLECT_Q, [beg, end])
    if (log.debugEnabled && candidates?.size() > 0) log.debug "collect candidates: ${candidates?.size()}"

    // Find the service of all candidates
    def serviceSet = new TreeSet()
    candidates.each {candidate ->
      serviceSet.add(candidate.tcase.service)
    }

    def portMap = createPortMap(serviceSet)
    candidates.each {candidate ->
      collect(candidate, portMap)
    }
  }

  /**
   * Create a map of ports from a set of services.
   * Return Map SigService id (Long) -> Map
   * The embedded Map contains the following keys (String),
   * service -> GrpService
   * port -> GrpServicePortType
   */
  private Map createPortMap(SortedSet serviceSet) {
    def portMap = [:]
    serviceSet.each {sigService ->
      def service = new GrpService(sigService.serviceUrl, sigService.qname)
      def port = service.grpServiceServletPort
      portMap[sigService.id] = [service: service, port: port]
    }

    return portMap
  }

  /**
   * Make a Collect poll.
   */
  private collect(SigResult candidate, Map portMap) {
    if (log.debugEnabled) log.debug "collect << ${candidate}"
    try {
      def collectResponse = doCollect(candidate, portMap)
      def progressStatus = SigProgress.lookup(collectResponse?.progressStatus?.value())
      if (log.debugEnabled) log.debug "collect RESPONSE ${candidate}: ${progressStatus}"
      if (progressStatus && progressStatus.id != candidate.progressStatus.id) {
	candidate.progressStatus = progressStatus
      }
      def props = collectResponse.attributes
      if (props) {
	props.each {prop ->
	  def dbProp = new SigAttribute(name: prop.name, value: prop.value)
	  candidate.addToAttrs(dbProp)
	}
      }
      def signature = collectResponse?.signature
      if (signature) {
	candidate.signature = signature
	String resultString = candidate.toMap() as JSON
	auditService.logSignEvent(candidate?.transactionId, 'Signature created',
				  resultString, null)
      }
    } catch (GrpFault fault) {
      if (log.debugEnabled) log.debug "collect FAULT ${candidate}: ${faultToString(fault)}"
      def faultDbObj = SigFaultObj.createFault(fault)
      candidate.faultStatus = faultDbObj
    }

    if (!candidate.save()) log.error "collect ${candidate} save: ${candidate.errors.allErrors.join(', ')}"
  }

  private CollectResponseType doCollect(SigResult candidate, Map portMap) {
    def of = new ObjectFactory()
    def collectReq = of.createCollectRequestType()
    collectReq.policy = candidate.policy.name
    collectReq.provider = PROVIDER
    collectReq.displayName = candidate.displayName.name
    collectReq.transactionId = candidate.transactionId
    collectReq.orderRef = candidate.orderRef
    def serviceId = candidate.tcase.service.id
    def port = portMap[serviceId].port
    port.collect(collectReq)
  }

  private faultToString(GrpFault fault) {
      def info = fault.faultInfo
      def sb = new StringBuilder()
      sb.append('[').append(info.faultStatus)
      def details = info.detailedDescription
      if (details) sb.append(': ').append(details)
      sb.append(']')
      return sb.toString()
  }

}

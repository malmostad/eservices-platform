package org.motrice.signatrice

import java.nio.ByteBuffer
import java.security.SecureRandom
import java.sql.Timestamp

import org.apache.commons.logging.LogFactory
import grails.converters.JSON

import org.motrice.docbox.doc.SignRequestCommand

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
 * Methods related to acquiring a signature from a signature provider.
 */
class SignService {
  // Stuff for generating transaction id.
  // Should be ok, it is thread-safe.
  static RNG = new SecureRandom()
  // Format string for transaction id
  static final String TXID_FORMAT = 'MOT-%08d'

  // Provider name needed by CGI. The only one defined so far.
  static final String PROVIDER = 'bankid'
  
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
      throw new ServiceException('DOCBOX.107', exc.message)
    }

    if (log.debugEnabled) log.debug "checkService >> ${result.size()}"
    return result
  }

  /**
   * Send a sign request, return a newly created SigResult containing
   * the return values, or throw a ServiceException.
   */
  SigResult signatureRequest(SignRequestCommand cmd) {
    if (log.debugEnabled) log.debug "signatureRequest << ${cmd}"
    cmd.transactionId = generateTransactionId()
    def result = null
    try {
      def orderResponse = doSign(cmd)
      result = new SigResult(transactionId: orderResponse.transactionId,
      orderRef: orderResponse.orderRef,
      autoStartToken: orderResponse.autoStartToken,
      displayName: cmd.schemeObj.displayName,
      policy: cmd.schemeObj.policy,
      personalIdNo: cmd.personalId,
      docboxRefIn: cmd.docboxref,
      progressStatus: SigProgress.initialState())
      result.dateCreated = new Date()

      // Add the new result to the test case
      cmd.schemeObj.addToResults(result).save()
    } catch (GrpFault fault) {
      if (log.debugEnabled) log.debug "sign EXCEPTION ${fault}"
      def info = fault.faultInfo
      String faultMsg = "${info?.faultStatus?.value()}: ${info.detailedDescription}"
      throw new ServiceException('DOCBOX.108', faultMsg, cmd.transactionId, fault)
      auditService.logSignEvent(cmd.transactionId, true, 'Sign request failed', faultMsg,
				StackTracer.trace(fault), cmd.httpRequest)
    }
    if (log.debugEnabled) log.debug "sign >> ${result?.toMap()}"
    return result
  }

  /**
   * The details of a signing request
   */
  private OrderResponseType doSign(SignRequestCommand cmd) {
    def service = new GrpService(cmd.schemeObj.service.serviceUrl, cmd.schemeObj.service.qname)
    def of = new ObjectFactory()
    def signReq = of.createSignRequestType()
    signReq.personalNumber = cmd.personalId
    signReq.policy = cmd.schemeObj.policy as String
    signReq.displayName = cmd.schemeObj.displayName as String
    signReq.transactionId = cmd.transactionId
    signReq.provider = PROVIDER
    def endUserInfo = of.createEndUserInfoType()
    endUserInfo.type = "IP_ADDR"
    endUserInfo.value = cmd.remoteAddr
    signReq.endUserInfo << endUserInfo
    signReq.userVisibleData = cmd.bodyB64
    def port = service.grpServiceServletPort
    port.sign(signReq)
  }

  /**
   * Generate and assign a pseudo-random transaction id
   */
  private String generateTransactionId() {
    def bytes = new byte[4]
    RNG.nextBytes(bytes)
    // Limit to 27 bits
    bytes[0] &= 0x07
    def buf = ByteBuffer.wrap(bytes)
    String.format(TXID_FORMAT, buf.getInt())
  }

  // Candidate SigResults with hardwired progess status id:s
  // The max age limit should not be necessary but is a guard
  // against infinite repetition in case there is a problem.
  // The condition must agree with the SigResult.inProcess condition.
  private static final String COLLECT_Q = 'from SigResult r where ' +
    'r.dateCreated between ? and ? and r.progressStatus.id in (1, 2, 3) and ' +
    'faultStatus is null'

  /**
   * Invoke Collect for all outstanding SigResults
   */
  def collectAll() {
    def now = new Date()
    // Age window for collection
    def beg = new Timestamp(now.time - SigResult.MAX_COLLECT_AGE_MILLIS)
    def end = new Timestamp(now.time - SigResult.MIN_COLLECT_AGE_MILLIS)
    def candidates = SigResult.findAll(COLLECT_Q, [beg, end])
    if (log.debugEnabled && candidates?.size() > 0) log.debug "collect candidates: ${candidates?.size()}"

    // Find the service of all candidates
    def serviceSet = new TreeSet()
    candidates.each {candidate ->
      serviceSet.add(candidate.scheme.service)
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

      // Update progress status if there is a change
      if (progressStatus && progressStatus.id != candidate.progressStatus.id) {
	candidate.progressStatus = progressStatus
      }

      // Attach any and all response attributes
      def props = collectResponse.attributes
      if (props) {
	props.each {prop ->
	  def dbProp = new SigAttribute(name: prop.name, value: prop.value)
	  candidate.addToAttrs(dbProp)
	}
      }

      // Store the signature if there is one
      def signature = collectResponse?.signature
      if (signature) {
	candidate.signature = signature
	candidate.sigTstamp = new Date()
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
    def serviceId = candidate.scheme.service.id
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

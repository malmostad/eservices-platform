package org.motrice.docbox.sign

import javax.xml.crypto.*
import javax.xml.crypto.dsig.*
import javax.xml.crypto.dom.*
import javax.xml.crypto.dsig.dom.DOMValidateContext
import javax.xml.crypto.dsig.keyinfo.*
import java.security.*
import java.security.cert.*
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import org.xml.sax.InputSource

/**
 * Validate an XML DSIG signature according to the W3C recommendation
 * "XML Signature Syntax and Processing".
 * The main input must be a Base64-encoded XML signature.
 * This is because the input must be canonical XML, any kind of
 * pretty-printing or formatting will make validation fail.
 * NOTE: Java includes XML signature validation.
 * It requires DOM.
 * For some other purposes we use Groovy's XmlSlurper because it is
 * more powerful.
 * Mixing the two models is not optimal for performance.
 * However, for this application no optimization attempt is made.
 * All exceptions are converted to XMLSignatureException
 */
class XmlDsig {
  // Enable/disable certificate revocation checking
  static REVOCATION_CHECK = false

  // Base64-encoded signature
  final String signatureB64

  // Decoded canonical XML signature
  final byte[] signatureXml

  // Certificate chain (set by the validateSignature method)
  // List of java.security.cert.X509Certificate
  // Note that javax.security.cert.X509Certificate is deprecated
  List certChain

  // Signed text (set by the validateSignature method)
  String signedText

  // Validation report, a sequence of text lines
  // Text is accumulated as validation progresses
  List report = []

  // Log for debugging
  def log

  /**
   * Construct and initialize signature data
   * After instantiation, call validateSignature and then validateCerts
   */
  def XmlDsig(String signatureB64, log) {
    this.signatureB64 = signatureB64
    this.log = log
    try {
      signatureXml = signatureB64.decodeBase64()
    } catch (Exception exc) {
      throw new XMLSignatureException('Problem Base64-decoding signature. ', exc)
    }
  }

  //===================== Get signature info =====================

  /**
   * Get the first certificate of the certificate chain
   * It should be the signing user's certificate
   * @return java.security.cert.X509Certificate
   */
  def getFirstCert() {
    if (certChain == null) certChain = extractCertChain()
    if (log.debugEnabled) log.debug "XmlDsig.certChain entries: ${certChain?.size()}"
    return certChain[0]
  }

  /**
   * Get the last certificate of the certificate chain
   * This is the self-signed trust anchor of the chain
   * @return java.security.cert.X509Certificate
   */
  def getLastCert() {
    if (certChain == null) certChain = extractCertChain()
    if (log.debugEnabled) log.debug "XmlDsig.certChain entries: ${certChain?.size()}"
    return certChain[-1]
  }

  /**
   * Get the text that was shown to the user to sign
   */
  def getSignedText() {
    if (signedText == null) signedText = extractUsrVisibleData()
    return signedText
  }

  //===================== Signature Validation =====================

  /**
   * Validate the signature
   * SIDE EFFECT: sets the certChain field, add text lines to report
   * @return boolean, did validation succeed?
   */
  def validateSignature() {
    // Create the document to be validated
    def is = new ByteArrayInputStream(signatureXml)
    def dbf = DocumentBuilderFactory.newInstance()
    dbf.namespaceAware = true
    def doc = null
    try {
      doc = dbf.newDocumentBuilder().parse(is)
    } catch (Exception exc) {
      throw new XMLSignatureException('Problem parsing XML signature. ', exc)
    }

    // Find the Signature element
    def nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, 'Signature')
    if (nl.length == 0) {
      throw new XMLSignatureException('Cannot find Signature element')
    }

    // There can only be one signature, so pick it
    def signatureElement = nl.item(0)

    // DOM XMLSignatureFactory to be used to unmarshal the document
    // containing the XMLSignature
    def sigFact = XMLSignatureFactory.getInstance('DOM')

    // Extract the signed text
    signedText = getSignedText()

    // We assume the signature is sealed with a certificate chain
    // Get the public key from the first certificate = end user certificate by convention
    def cert = getFirstCert()
    def pubKey = cert.publicKey
    return doSigValidate(sigFact, signatureElement, pubKey)
  }

  // DOM gets in the way
  // Get the signed text (user visible part) from the signature
  // RETURN the signed text
  private String extractUsrVisibleData() {
    def signature = new XmlSlurper().parse(new ByteArrayInputStream(signatureXml))
    def textB64 = signature.Object.bankIdSignedData.usrVisibleData.text()
    return new String(textB64.decodeBase64(), 'UTF-8')
  }

  /**
   * Validation core
   * SIDE EFFECT: Add lines to the validation report
   * RETURN validation outcome (true or false)
   */
  private boolean doSigValidate(XMLSignatureFactory sigFact, signatureElement, pubKey) {
    // Create a DOMValidateContext
    // In this case we assume the key to use is contained in a certificate chain
    def valCtx = new DOMValidateContext(pubKey, signatureElement)

    // Unmarshal the XMLSignature
    def signature = sigFact.unmarshalXMLSignature(valCtx)

    // Validate
    boolean coreValidity = signature.validate(valCtx)

    if (coreValidity) {
      report << "Core signature validation PASSED."
      report << "  Signature integrity checked. Encryption key matched."
      report << "Signed by: ${certChain[0].subjectDN}"
      report << ''
    } else {
      report << "Core signature validation FAILED."
      def sv = signature.signatureValue.validate(valCtx)
      report << "Signature value: Validation status ${sv}"

      // Validation status of each Reference
      // List of Reference
      def refs = signature.signedInfo.references
      refs.eachWithIndex {ref, idx ->
	def valStatus = ref.validate(valCtx)
	report << "Ref ${idx} validity: ${valStatus}"
      }
    }

    return coreValidity
  }

  //===================== Certificate Validation =====================

  /**
   * Validate certificates (except the root because it is self-signed)
   * ASSUMES you have run validateSignature
   * SIDE EFFECT: Adds lines to report
   * @return boolean, was validation successful?
   */
  def validateCertificates() {
    def cf = CertificateFactory.getInstance("X.509")
    // The certificate chain begins with the signer and ends with
    // a root certificate, the trust anchor
    // Eliminate the trust anchor from the cert path
    def certPath = cf.generateCertPath(certChain[0..-2])
    // Get the trust anchor
    def anchorCert = certChain[-1]
    def certStruct = [certPath: certPath, anchorCert: anchorCert]
    boolean success = false
    try {
      def valResult = doCertValidate(certStruct)
      report << "Certificate chain is VALID (revocation check: ${REVOCATION_CHECK})"
      report << '  except that the trust anchor is not verified.'
      report << "Claims to be issued by: ${anchorCert.issuerDN}"
      report << "Fingerprint: ${fingerprint(anchorCert)}"
      //report << ''
      //report << 'Cert validation details follow'
      //report << valResult.toString()
      success = true
    } catch (CertPathValidatorException exc) {
      def certList = certs.certPath.certificates
      def problemCert = certList[exc.index]
      report << "Validation problem: ${exc}"
      report << "Failing certificate:"
      report << "Subject: ${problemCert.subjectX500Principal}"
      report << "Issuer:  ${problemCert.issuerX500Principal}"
    } catch (Exception exc) {
      report << "Exception: ${exc}"
    }

    return success
  }

  // DOM gets in the way
  // Get the certificate chain from the signature file
  // RETURN List of java.security.cert.X509Certificate
  private List extractCertChain() {
    def cf = CertificateFactory.getInstance('X.509')
    def certList = []
    def signature = new XmlSlurper().parse(new ByteArrayInputStream(signatureXml))
    signature.KeyInfo.X509Data.X509Certificate.each {certB64element ->
      def certBytes = certB64element.text().decodeBase64()
      new ByteArrayInputStream(certBytes).withStream {stream ->
	certList << cf.generateCertificate(stream)
      }
    }

    return certList
  }

  /**
   * PKIX certificate path validation (RFC 3280)
   * certs must be a map created by certsForValidation containing a
   * CertPath and the trust anchor cert
   * @throws java.security.cert.CertPathValidatorException if the
   * certificate path does not validate
   */
  private CertPathValidatorResult doCertValidate(Map certs) {
    def anchor = new TrustAnchor(certs.anchorCert, null)
    def params = new PKIXParameters(Collections.singleton(anchor))
    params.revocationEnabled = REVOCATION_CHECK
    def cpv = CertPathValidator.getInstance("PKIX")
    cpv.validate(certs.certPath, params)
  }

  /**
   * Get the fingerprint of a certificate as a hexadecimal string
   */
  private fingerprint(cert) {
    byte[] der = cert.encoded
    def md = MessageDigest.getInstance('SHA-1')
    md.update(der)
    byte[] digest = md.digest()
    return digest.encodeHex()
  }
}

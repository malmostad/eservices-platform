package org.motrice.signatrice

import javax.xml.namespace.QName

/**
 * A CGI web service.
 * Actually versions of the main service.
 */
class SigService implements Comparable {
  // The name of the service as a URL string
  String wsdlLocation

  // URI part of qualified name of the service
  String qNameUri

  // Local part of qualified name of the service
  String qNameLocalPart

  // Default policy
  SigPolicy defaultPolicy

  // Default display name
  SigDisplayname defaultDisplayName

  static hasMany = [cases: SigTestcase]
  static constraints = {
    wsdlLocation size: 5..200, unique: true
    qNameUri size: 5..200
    qNameLocalPart size: 1..120
  }
  static transients = ['qname', 'uname', 'serviceUrl']

  static createService(String wsdlLocationStr, String uri, String localPart,
		       Long policyId, Long displayNameId) {
    if (!SigService.findByWsdlLocation(wsdlLocationStr)) {
      def policy = SigPolicy.get(policyId)
      def displayName = SigDisplayname.get(displayNameId)
      new SigService(wsdlLocation: wsdlLocationStr,
      qNameUri: uri, qNameLocalPart: localPart,
      defaultPolicy: policy, defaultDisplayName: displayName).
      save(failOnError: true)
    }
  }

  static createPredefinedServices() {
    createService(TEST_WSDL_LOCATION, TEST_QNAME_URI, TEST_QNAME_LOCAL, 7, 1)
  }

  /**
   * A unique name composed of WSDL location and the qualified name
   */
  String getUname() {
    wsdlLocation + '|' + qNameUri + qNameLocalPart
  }

  QName getQname() {
    new QName(qNameUri, qNameLocalPart)
  }

  URL getServiceUrl() {
    new URL(wsdlLocation)
  }

  String toString() {
    wsdlLocation
  }

  static final String TEST_WSDL_LOCATION = 'http://grpt.funktionstjanster.se:18899/grp/v1?wsdl'
  static final String TEST_QNAME_URI = 'http://logic.grp.mobilityguard.com/'
  static final String TEST_QNAME_LOCAL = 'GrpService'

  //-------------------- Comparable --------------------

  int hashCode() {
    uname.hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof SigService) {
      def other = (SigService)obj
      result = uname == other.uname
    }

    return result
  }

  /**
   * Date-based comparison, latest first.
   */
  int compareTo(Object obj) {
    def other = (SigService)obj
    -uname.compareTo(other.uname)
  }

}

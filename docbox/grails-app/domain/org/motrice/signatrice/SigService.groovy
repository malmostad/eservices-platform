package org.motrice.signatrice

import javax.xml.namespace.QName

/**
 * A web service.
 * Actually versions of the main service.
 */
class SigService implements Comparable {
  // The name of the service as a URL string
  String wsdlLocation

  // A user friendly alias name (alphanumeric)
  String alias

  // URI part of qualified name of the service
  String qNameUri

  // Local part of qualified name of the service
  String qNameLocalPart

  // Default policy
  SigPolicy defaultPolicy

  // Default display name
  SigDisplayname defaultDisplayName

  static hasMany = [schemes: SigScheme]
  static constraints = {
    wsdlLocation size: 5..200, unique: true
    alias size: 5..24, matches: '[A-Za-z0-9-]+', unique: true
    qNameUri size: 5..200
    qNameLocalPart size: 1..120
  }
  static transients = ['qname', 'serviceUrl']

  static createService(String wsdlLocationStr, String alias, String uri, String localPart,
		       Long policyId, Long displayNameId) {
    if (!SigService.findByWsdlLocation(wsdlLocationStr)) {
      def policy = SigPolicy.get(policyId)
      def displayName = SigDisplayname.get(displayNameId)
      new SigService(wsdlLocation: wsdlLocationStr, alias: alias,
      qNameUri: uri, qNameLocalPart: localPart,
      defaultPolicy: policy, defaultDisplayName: displayName).
      save(failOnError: true)
    }
  }

  static createPredefinedServices() {
    createService(GRPT_HTTP_WSDL_LOCATION, GRPT_HTTP_ALIAS,
		  GRPT_HTTP_QNAME_URI, GRPT_HTTP_QNAME_LOCAL, 7, 1)
    createService(GRPT_HTTPS_WSDL_LOCATION, GRPT_HTTPS_ALIAS,
		  GRPT_HTTPS_QNAME_URI, GRPT_HTTPS_QNAME_LOCAL, 9, 2)
  }

  QName getQname() {
    new QName(qNameUri, qNameLocalPart)
  }

  URL getServiceUrl() {
    new URL(wsdlLocation)
  }

  String toString() {
    "${alias}: ${wsdlLocation}"
  }

  // GRP test over http
  static final String GRPT_HTTP_WSDL_LOCATION = 'http://grpt.funktionstjanster.se:18899/grp/v1?wsdl'
  static final String GRPT_HTTP_ALIAS = 'GRPT-http'
  static final String GRPT_HTTP_QNAME_URI = 'http://logic.grp.mobilityguard.com/'
  static final String GRPT_HTTP_QNAME_LOCAL = 'GrpService'

  // GRP test over https
  static final String GRPT_HTTPS_WSDL_LOCATION = 'https://grpt.funktionstjanster.se:18898/grp/v1?wsdl'
  static final String GRPT_HTTPS_ALIAS = 'GRPT-https'
  static final String GRPT_HTTPS_QNAME_URI = 'http://logic.grp.mobilityguard.com/'
  static final String GRPT_HTTPS_QNAME_LOCAL = 'GrpService'

  //-------------------- Comparable --------------------

  int hashCode() {
    wsdlLocation.hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof SigService) {
      def other = (SigService)obj
      result = wsdlLocation == other.wsdlLocation
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (SigService)obj
    alias.compareTo(other.alias)
  }

}

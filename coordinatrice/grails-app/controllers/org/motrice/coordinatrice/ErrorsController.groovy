package org.motrice.coordinatrice

import org.motrice.jmx.BasicAppManagement

class ErrorsController {
  static final EXCPREFIX = 'org.codehaus.groovy.grails.web.errors.GrailsWrappedRuntimeException: '
  // Inject the MBean as defined in resources.groovy
  def basicJmxManagement

  /**
   * Server errors end up here via UrlMappings
   * The view is different in development vs production
   */
  def internalServerError() {
    // Must be totally exception proof to avoid endless loop
    try {
      String uri = request?.forwardURI
      String exc = request?.exception?.toString()
      if (exc?.startsWith(EXCPREFIX)) exc = exc.substring(EXCPREFIX.size())
      def remoteList = [request?.remoteAddr]
      String forwHeader = request?.getHeader('X-Forwarded-For')
      if (forwHeader) remoteList.add "forwarded for ${forwHeader}"
      String clientHeader = request?.getHeader('Client-IP')
      if (clientHeader) remoteList.add "client ip ${clientHeader}"
      def remote = remoteList.join(', ')
      basicJmxManagement.serverErrorNotification(remote, uri, exc)
      log.error "HTTP 500: Request: ${remote}, ${uri}, exception: ${exc}"
      def trace = request?.exception?.stackTraceText
      if (trace) log.error "   ${trace}"
      return [exception: request?.exception]
    } catch (Throwable thr) {
      log.error('HTTP500 logging caused another exception, see strack trace log file', thr)
    }
  }

}

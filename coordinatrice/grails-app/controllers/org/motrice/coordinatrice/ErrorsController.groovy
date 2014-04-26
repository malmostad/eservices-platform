/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
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

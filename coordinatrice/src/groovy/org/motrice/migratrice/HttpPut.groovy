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
package org.motrice.migratrice

/**
 * Helper for PUT requests with this application as a client
 */
class HttpPut {
  static final String CONTENT_TYPE = 'Content-Type'

  URL url
  URLConnection cnx

  /**
   * Construct from a url
   */ 
  HttpPut(URL url) {
    this.url = url
    cnx = url.openConnection()
    cnx.requestMethod = 'PUT'
  }

  /**
   * Put a byte array body.
   * Return a map containing entries 'responseCode' (int) and 'responseMessage' (String).
   * Throws ConnectException or IOException on failure (nothing is caught here).
   */
  Map putBytes(byte[] body) {
    def result = null
    cnx.setRequestProperty(CONTENT_TYPE, 'application/octet-stream')
    cnx.doOutput = true
    def os = cnx.outputStream
    os.write(body)
    os.flush()
    os.close()
    cnx.connect()
    return [responseCode: cnx.responseCode, responseMessage: cnx.content.text]
  }

  /**
   * Put an XML body.
   * Return a map containing entries 'responseCode' (int) and 'responseMessage' (String).
   * Throws ConnectException or IOException on failure (nothing is caught here).
   */
  Map putXml(String body) {
    def result = null
    cnx.setRequestProperty(CONTENT_TYPE, 'application/xml;charset=UTF-8')
    cnx.doOutput = true
    def writer = new OutputStreamWriter(cnx.outputStream)
    writer.write(body)
    writer.flush()
    writer.close()
    cnx.connect()
    return [responseCode: cnx.responseCode, responseMessage: cnx.content.text]
  }

}

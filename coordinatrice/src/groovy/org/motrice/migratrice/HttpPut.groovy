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

package org.motrice.migratrice

/**
 * A single object for managing a PrintWriter enclosing a StringWriter.
 */
class LogWriter extends PrintWriter {
  // Optional message code
  String code

  // Optional list of message args
  List args

  LogWriter() {
    super(new StringWriter())
  }

  /**
   * Get the final result as a string
   */
  String toString() {
    out.toString()
  }
  
}

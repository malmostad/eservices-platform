package org.motrice.signatrice

import org.codehaus.groovy.runtime.StackTraceUtils

/**
 * Utility for cleaning a stack trace and converting it to String
 */
class StackTracer { 
  PrintWriter pw
  StringWriter sw

  private StackTracer() {
    sw = new StringWriter()
    pw = new PrintWriter(sw)
  }

  /**
   * Sanitize and convert the stack trace to String.
   * NOTE: Modifies the stack trace of the exception.
   */
  static String trace(Throwable thr) {
    def exc = StackTraceUtils.deepSanitize(thr)
    def st = new StackTracer()
    exc.printStackTrace(st.pw)
    st.pw.flush()
    st.pw.close()
    return st.sw.toString()
  }

}

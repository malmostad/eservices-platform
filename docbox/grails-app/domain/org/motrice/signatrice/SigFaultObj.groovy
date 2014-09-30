package org.motrice.signatrice

import org.motrice.signatrice.cgi.FaultStatusType
import org.motrice.signatrice.cgi.GrpFault

/**
 * A GrpFault wrapper.
 */
class SigFaultObj {
  // Fault status (FaultStatusType as String)
  String status

  // Detailed description, almost no size constraint
  String details

  static mapping = {
    details type: 'text'
  }
  static constraints = {
    status size: 1..80
    details nullable: true
  }

  static createFault(String status, String description) {
    new SigFaultObj(status: status, details: description).save()
  }

  static createFault(GrpFault fault) {
    def info = fault.faultInfo
    createFault(info.faultStatus.value(), info.detailedDescription)
  }

  String toString() {
    def sb = new StringBuilder()
    sb.append(status)
    if (details) sb.append(': ').append(details)
    return sb.toString()
  }

}

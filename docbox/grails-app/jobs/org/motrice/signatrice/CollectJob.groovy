package org.motrice.signatrice

import org.apache.commons.logging.LogFactory

/**
 * Send Collect request to CGI for each relevant SigResult.
 * The condition is progress state INIT and at least 3.5 seconds old,
 * or 
 */
class CollectJob {

  def signService

  static triggers = {
    // Execute job every 3 seconds indefinitely
    simple name: 'collectTrigger',
    startDelay: 1000L, repeatInterval: 3100L, repeatCount: -1
  }

  /**
   * All heavy lifting delegated to SignService
   */
  def execute() {
    signService.collectAll()
  }

}

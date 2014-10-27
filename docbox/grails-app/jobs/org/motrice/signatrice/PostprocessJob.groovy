package org.motrice.signatrice

/**
 * Post-process documents, adding a signature, after a valid signature
 * is made available.
 */
class PostprocessJob {

  def signdocService

  static triggers = {
    simple name: 'signFinishTrigger',
    startDelay: 2000L, repeatInterval: 3100L, repeatCount: -1
  }

  def execute() {
    signdocService.sigPostProcessAll()
  }

}

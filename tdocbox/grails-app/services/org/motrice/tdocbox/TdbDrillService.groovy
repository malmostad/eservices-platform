package org.motrice.tdocbox

/**
 * Service for invoking the REST methods of DocBox.
 */
class TdbDrillService {
  // All methods are transactional (actually the default)
  static transactional = true

  def perform(TdbDrill drill) {
    if (log.debugEnabled) log.debug "perform << ${drill}"
    switch (drill.method.id) {
    case TdbMethod.CREATE_PDFA_FROM_ORBEON_ID: createPdfaFromOrbeon(drill)
      break;
    default:
    def msg = "Drill contains invalid method index ${drill?.method?.id}"
    throw new ServiceException(msg)
    }
  }

  def createPdfaFromOrbeon(TdbDrill drill) {
    if (log.debugEnabled) log.debug "createPdfaFromOrbeon << ${drill?.map}"
    def method = TdbMethod.get(drill.method.id)
    def urlString = method.url(drill.map)
    if (log.debugEnabled) log.debug "createPdfaFromOrbeon >> ${urlString}"
  }

}

package org.motrice.coordinatrice

import org.apache.commons.logging.LogFactory

/**
 * Actions related to locale-dependent guide URL patterns.
 * The domain is CrdI18nGuideUrl.
 */
class GuideUrlService {
  // The service creates db records, transactions needed
  static transactional = true

  private static final log = LogFactory.getLog(this)
  static final MAX_PROCDEF_VER_Q = 'select max(p.procdefVer) from CrdI18nGuideUrl p ' +
    'where procdefKey=?'

  /**
   * Duplicate a given pattern.
   * The process version component is incremented to avoid violation
   * of uniqueness constraint.
   */
  CrdI18nGuideUrl duplicatePattern(CrdI18nGuideUrl srcPattern) {
    if (log.debugEnabled) log.debug "duplicatePattern << ${srcPattern}"
    def tgtPattern = CrdI18nGuideUrl.copyPattern(srcPattern)
    // Adjust the process version number
    def list = CrdI18nGuideUrl.executeQuery(MAX_PROCDEF_VER_Q, [srcPattern.procdefKey])
    def maxProcdefVer = list? list[0] : 999
    tgtPattern.procdefVer = maxProcdefVer + 1
    if (!tgtPattern.save()) log.error "CrdI18nGuideUrl insert: ${tgtPattern.errors.allErrors.join(',')}"
    if (log.debugEnabled) log.debug "duplicatePattern >> ${tgtPattern}"
    return tgtPattern
  }

}

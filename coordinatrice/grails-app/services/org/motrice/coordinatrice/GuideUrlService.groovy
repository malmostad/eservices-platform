package org.motrice.coordinatrice

import org.apache.commons.logging.LogFactory
import org.springframework.transaction.annotation.Transactional

/**
 * Actions related to locale-dependent guide URL patterns.
 * The domain is CrdI18nGuideUrl.
 */
class GuideUrlService {
  // Individual methods must be flagged as transactional when needed

  def grailsApplication

  private static final log = LogFactory.getLog(this)
  static final MAX_PROCDEF_VER_Q = 'select max(p.procdefVer) from CrdI18nGuideUrl p ' +
    'where procdefKey=?'

  /**
   * Duplicate a given pattern.
   * The process version component is incremented to avoid violation
   * of uniqueness constraint.
   */
  @Transactional
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

  static final SINGLE_PAT_Q = 'from CrdI18nGuideUrl u where u.procdefKey=? and ' +
    'u.procdefVer <= ? order by u.procdefVer desc'

  /**
   * Generate a URL (String) from a set of parameters.
   * Find a pattern and use it for formatting.
   * Return a map containing the input parameters and the crucial entry
   * "url".
   * If no matching pattern was found the "url" entry will be null.
   */
  @Transactional
  Map generateUrlData(String procdefkey, String locale, String activityname, String version) {
    if (log.debugEnabled) log.debug "generateUrlData << ${procdefkey}, ${locale}, ${activityname}, ${version}"
    Integer versionInt = 1
    try {
      if (version) versionInt = version as Integer
    } catch (NumberFormatException exc) {
      // Ignore
    }

    def result = [procdefKey: procdefkey, procdefVer: versionInt, actdefName: activityname,
    locale: locale, url: generateUrl(procdefkey, locale, activityname, version)]
    if (log.debugEnabled) log.debug "generateUrlData >> ${result}"
    return result
  }

  /**
   * Generate a URL (String) from a set of parameters.
   * Find a pattern and use it for formatting.
   * Return a URL String.
   */
  @Transactional
  String generateUrl(String procdefkey, String locale, String activityname, String version) {
    if (log.debugEnabled) log.debug "generateUrl << ${procdefkey}, ${locale}, ${activityname}, ${version}"
    Integer versionInt = 1
    try {
      if (version) versionInt = version as Integer
    } catch (NumberFormatException exc) {
      // Ignore
    }

    def list = CrdI18nGuideUrl.findAll(SINGLE_PAT_Q, [procdefkey, versionInt])
    def guideUrlInst = list? list[0] : null
    def result = null
    if (guideUrlInst) {
      result = format(guideUrlInst, procdefkey, versionInt, activityname, locale)
    }

    if (log.debugEnabled) log.debug "generateUrl >> ${result}"
    return result
  }

  /**
   * Assign a guide URL (transient) to a list of process definitions.
   * procdefList must be List of Prodef.
   */
  @Transactional
  List assignGuideUrls(List procdefList, String locale, String activityName) {
    if (log.debugEnabled) log.debug
    procdefList.each {procdef ->
      procdef.guideUrl = generateUrl(procdef.procdefKey, locale, activityName,
				     procdef.procdefVer)
    }

    return procdefList
  }

  String format(CrdI18nGuideUrl guideUrl, String procdefKey, Integer procdefVer,
		String actdefName, String locale)
  {
    String baseUri = grailsApplication.config.coordinatrice.guide.base.uri
    def fmt = new UriFormatter(guideUrl.pattern)
    return fmt.format(baseUri, procdefKey, procdefVer, actdefName, locale)
  }

  String format(CrdI18nGuideUrl guideUrl, String procdefKey, String locale) {
    format(guideUrl, procdefKey, 1, null, locale)
  }

  String format(CrdI18nGuideUrl guideUrl, String procdefKey, String actDefName,
		String locale)
  {
    format(guideUrl, procdefKey, 1, actdefName, locale)
  }

}

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

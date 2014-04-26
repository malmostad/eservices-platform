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

/**
 * Actions related to locale-dependent activity labels.
 * The domain is CrdI18nActLabel.
 */
class ActivityLabelService {
  // The service creates labels, transactions needed
  static transactional = true

  // Injection magic, see resources.groovy
  def procdefService
  private static final log = LogFactory.getLog(this)

  /**
   * Create missing labels for a locale, or a number of locales.
   * procdefKey must be the process definition key for the process that
   * needs labels.
   * localeStr may be a locale, a number of locales separated by comma, or
   * empty (which is taken as all locales of this process definition).
   * Return the number of labels created.
   */
  Integer createLabels(String procdefKey, String localeStr) {
    if (log.debugEnabled) log.debug "createLabels << ${procdefKey}, ${localeStr}"
    // Collect locales
    def localeList = doCollectLocales(localeStr)
    if (localeList.isEmpty()) return 0
    // Find all activities
    def procList = procdefService.findProcessDefinitionsFromKey(procdefKey)
    def actMap = [:]
    // Since we got all process definition versions, collect all
    // activities from them.
    procList.each {procdef ->
      procdef.activities.each {actdef ->
	// Only user activities matter
	if (actdef.type.id == TaskType.TYPE_USER_ID) {
	  def actName = actdef.name
	  if (!actMap[actName]) {
	    actMap[actName] = [name: actName, id: actdef.uuid]
	  }
	}
      }
    }

    // Find existing labels
    def existingSet = new TreeSet(CrdI18nActLabel.findAllByProcdefKey(procdefKey))
    // Create labels that don't yet exist
    def labelCount = 0
    actMap.each {entry ->
      localeList.each {locale ->
	def label = new CrdI18nActLabel(procdefKey: procdefKey, procdefVer: 0,
	actdefName: entry.value.name, actdefId: entry.value.id, locale: locale)
	if (!existingSet.contains(label)) {
	  if (label.save()) {
	    existingSet.add(label)
	    labelCount++
	  } else {
	    log.error "CrdI18nActLabel insert: ${label.errors.allErrors.join(',')}"
	  }
	}
      }
    }

    if (log.debugEnabled) log.debug "createLabels >> ${labelCount}"
    return labelCount
  }

  /**
   * Extract locales from a string possibly containing more than one.
   */
  private List doCollectLocales(String locale) {
    def result = []
    if (locale.trim()) {
      def sc = new Scanner(locale)
      sc.useDelimiter('[,; ]')
      while (sc.hasNext()) {
	def str = sc.next()
	if (str) result << str
      }
    } else {
      // Empty string: get all locales in use
      result = CrdI18nActLabel.executeQuery('select distinct locale from CrdI18nActLabel')
    }

    return result
  }

  static final MAXV_Q = 'select max(a.procdefVer) from CrdI18nActLabel a where ' +
    'a.procdefKey=? and a.actdefName=? and a.locale=?'

  /**
   * Create a new activity label, almost a copy of an existing one.
   */
  CrdI18nActLabel createVersion(CrdI18nActLabel orig) {
    if (log.debugEnabled) log.debug "createVersion << ${orig}"
    def list = CrdI18nActLabel.
    executeQuery(MAXV_Q, [orig.procdefKey, orig.actdefName, orig.locale])
    Integer nextVersion = list? list[0] + 1 : 1
    def label = CrdI18nActLabel.copyLabel(orig)
    label.procdefVer = nextVersion
    if (!label.save()) log.error "CrdI18nActLabel insert: ${label.errors.allErrors.join(',')}"
    if (log.debugEnabled) log.debug "createVersion >> ${label}"
    return label
  }

  /**
   * Delete activity labels shown for editing.
   */
  Integer deleteEditedLabels(params) {
    if (log.debugEnabled) log.debug "deleteEditedLabels << ${params}"
    def labelList = findEditedLabels(params)
    def deleteCount = 0
    labelList.each {label ->
      label.delete()
      deleteCount++
    }

    if (log.debugEnabled) log.debug "deleteEditedLabels >> ${deleteCount}"
    return deleteCount
  }

  /**
   * Find all edited labels from parameters.
   * The 'idList' parameter is expected to contain a list of label ids.
   * Return a list of label instances retrieved from the database.
   */
  List findEditedLabels(params) {
    if (log.debugEnabled) log.debug "findEditedLabels << ${params}"
    def idStr = params.idList
    if (!idStr) return []
    def result = new String(idStr.decodeBase64()).split(/\|/).collect {it ->
      CrdI18nActLabel.get(it as Long)
    }
    
    if (log.debugEnabled) log.debug "findEditedLabels >> ${result}"
    return result
  }

  static final SINGLE_ACT_Q = 'from CrdI18nActLabel l where l.procdefKey=? and ' +
    'l.locale=? and l.actdefName=? and l.procdefVer <= ? ' +
    'order by l.procdefKey asc, l.actdefName asc, l.procdefVer desc'
  static final ALL_ACT_Q = 'from CrdI18nActLabel l where procdefKey=? and ' +
    'locale=? and procdefVer <= ? ' +
    'order by l.procdefKey asc, l.actdefName asc, l.procdefVer desc'

  /**
   * Find activity labels
   */
  List findLabels(String procdefkey, String locale, String activityname, String version) {
    if (log.debugEnabled) log.debug "findLabels << ${procdefkey}, ${locale}, ${activityname}, ${version}"
    Integer versionInt = 0
    try {
      if (version) versionInt = version as Integer
    } catch (NumberFormatException exc) {
      // Ignore
    }

    if (log.debugEnabled) log.debug "findLabels versionInt: ${versionInt}"
    def list = null
    if (activityname) {
      list = CrdI18nActLabel.findAll(SINGLE_ACT_Q,
				     [procdefkey, locale, activityname, versionInt])
    } else {
      list = CrdI18nActLabel.findAll(ALL_ACT_Q, [procdefkey, locale, versionInt])
    }

    if (log.debugEnabled) log.debug "findLabels query: ${list}"
    // Process the list: remove null labels and duplicates.
    // Duplicate removal depends on the query result being ordered the right way.
    // Convert to list of maps.
    def keySet = new TreeSet()
    def resultList = []
    list.each {label ->
      String key = "${label.procdefKey}:${label.procdefVer}:${label.actdefName}"
      if (!keySet.contains(key) && label.label != null) {
	keySet << key
	def entry = [procdefKey: label.procdefKey, procdefVer: label.procdefVer,
	actdefName: label.actdefName, locale: label.locale, label: label.label]
	resultList << entry
      }
    }

    if (log.debugEnabled) log.debug "findLabels >> ${resultList?.size()}"
    return resultList
  }

  /**
   * Update edited activity labels.
   * Return the number of updated activity labels.
   */
  Integer updateEditedLabels(params) {
    if (log.debugEnabled) log.debug "updateEditedLabels << ${params}"
    def labelList = findEditedLabels(params)
    def updateCount = 0
    // Update the labels
    labelList.each {label ->
      def id = label.id
      def ver = params["ver-${id}"] as Long
      def procdefVer = params["procdefVer-${id}"] as Integer
      def ltext = params["label-${id}"].trim()
      if (label.version > ver) {
	throw new ServiceException("Activity label optimistic locking failure (${label})",
				   'default.optimistic.locking.failure', [label])
      }

      label.procdefVer = procdefVer
      label.label = ltext ?: null
      if (label.save()) {
	updateCount++
      } else {
	log.error "CrdI18nActLabel update: ${label.errors.allErrors.join(',')}"
      }
    }

    if (log.debugEnabled) log.debug "updateEditedLabels >> ${updateCount}"
    return updateCount
  }

}

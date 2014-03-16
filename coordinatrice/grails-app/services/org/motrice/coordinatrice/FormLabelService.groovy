package org.motrice.coordinatrice

import org.apache.commons.logging.LogFactory

/**
 * Actions related to locale-dependent activity labels.
 * The domain is CrdI18nFormLabel.
 */
class FormLabelService {
  // The service creates labels, transactions needed
  static transactional = true

  private static final log = LogFactory.getLog(this)

  /**
   * Create missing labels for a locale, or a number of locales.
   * formdefKey must be a form definition id
   * localeStr may be a locale, a number of locales separated by comma, or
   * empty (which is taken as all locales of this process definition).
   * Return the number of labels created.
   */
  Integer createLabels(String formdefKey, String localeStr) {
    if (log.debugEnabled) log.debug "createLabels << ${formdefKey}, ${localeStr}"
    Integer formdefId = null
    try {
      formdefId = Long.parseLong(formdefKey)
    } catch (NumberFormatException exc) {
      formdefId = null
    }
    if (formdefId == null) return 0
    
    // Collect locales
    def localeList = doCollectLocales(localeStr)
    if (localeList.isEmpty()) return 0

    // Find existing labels
    def existingSet = new TreeSet(CrdI18nFormLabel.findAllByFormdefId(formdefId))
    // Create labels that don't yet exist
    def labelCount = 0
    localeList.each {locale ->
      def label = new CrdI18nFormLabel(formdefId: formdefId, formdefVer: 0, locale: locale)
      if (!existingSet.contains(label)) {
	if (label.save()) {
	  existingSet.add(label)
	  labelCount++
	} else {
	  log.error "CrdI18nFormLabel insert: ${label.errors.allErrors.join(',')}"
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
      result = CrdI18nFormLabel.executeQuery('select distinct locale from CrdI18nFormLabel')
    }

    return result
  }

  static final MAXV_Q = 'select max(f.formdefVer) from CrdI18nFormLabel f where ' +
    'f.formdefId=? and f.locale=?'

  /**
   * Create a new form label, almost a copy of an existing one.
   */
  CrdI18nFormLabel createVersion(CrdI18nFormLabel orig) {
    if (log.debugEnabled) log.debug "createVersion << ${orig}"
    def list = CrdI18nFormLabel.executeQuery(MAXV_Q, [orig.formdefId, orig.locale])
    Integer nextVersion = list? list[0] + 1 : 1
    def label = CrdI18nFormLabel.copyLabel(orig)
    label.formdefVer = nextVersion
    if (!label.save()) log.error "CrdI18nFormLabel insert: ${label.errors.allErrors.join(',')}"
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
      CrdI18nFormLabel.get(it as Long)
    }
    
    if (log.debugEnabled) log.debug "findEditedLabels >> ${result}"
    return result
  }

}
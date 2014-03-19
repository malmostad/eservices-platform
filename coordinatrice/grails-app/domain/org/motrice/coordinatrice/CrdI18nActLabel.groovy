package org.motrice.coordinatrice

/**
 * An internationalization label for a BPMN process activity.
 * Given a process definition key, an activity name and a locale you may find
 * a label for that locale.
 */
class CrdI18nActLabel implements Comparable {

  // Process definition key (id minus version, spaces and odd characters)
  String procdefKey

  // Process version number.
  // Makes it possible to have more than one label for a given activity.
  // The version number indicates the first version for which a new definition
  // should be used.
  // For instance, if procdefVer is 4 it means that the definition is valid for
  // process definitions version 4 and higher.
  // The first process version is 1 but the default value here is 0.
  // Just a convention for knowing if it has been set, no change in logic.
  Integer procdefVer

  // Activity definition name
  String actdefName

  // Activity definition id
  // NOTE: If an activity changes name it may keep its id.
  // It cannot be part of uniqueness constraints, but takes part in comparison.
  String actdefId

  // Locale string
  String locale

  // The internationalized label.
  // It is nullable because we want to be able to generate a full set of empty labels
  // for a new locale.
  String label

  static mapping = {
    table 'crd_i18n_act_label'
    // This seems to have no effect in Grails 2.2.4
    procdefVer defaultValue: 0
  }
  // A lot of indexes are justified because this is a read-mostly structure.
  // Simplify read at the expense of inserts and updates.
  static constraints = {
    procdefKey maxSize: 255
    procdefVer min: 0, unique: ['procdefKey', 'actdefName', 'locale']
    actdefName maxSize: 255, unique: ['procdefKey', 'procdefVer', 'locale']
    actdefId maxSize: 255
    locale maxSize: 24, unique: ['procdefKey', 'procdefVer', 'actdefName']
    label maxSize: 255, nullable: true, unique: ['procdefKey', 'procdefVer', 'actdefName', 'locale']
  }

  static copyLabel(CrdI18nActLabel srcLabel) {
    new CrdI18nActLabel(procdefKey: srcLabel.procdefKey, procdefVer: srcLabel.procdefVer,
    actdefName: srcLabel.actdefName, actdefId: srcLabel.actdefId, locale: srcLabel.locale,
    label: srcLabel.label)
  }

  String toString() {
    "${procdefKey}|${procdefVer}|${actdefName}|${actdefId}|${locale} '${label}'"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    (procdefKey + procdefVer + actdefName + actdefId + locale + label).hashCode()
  }

  boolean equals(Object obj) {
    boolean result = obj instanceof CrdI18nActLabel
    if (result) {
      def other = (CrdI18nActLabel)obj
      result = (procdefKey == other.procdefKey) && (procdefVer == other.procdefVer) &&
      (actdefName == other.actdefName) && (actdefId == other.actdefId) &&
      (locale == other.locale)
      if (result) result = (label == null && other.label == null) ||
      (label && other.label && label == other.label)
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (CrdI18nActLabel)obj
    int result = procdefKey.compareTo(other.procdefKey)
    // Version in descending order
    if (result == 0) result = -procdefVer.compareTo(other.procdefVer)
    if (result == 0) result = actdefName.compareTo(other.actdefName)
    if (result == 0) result = actdefId.compareTo(other.actdefId)
    if (result == 0) result = locale.compareTo(other.locale)
    if (result == 0 && label) result = label.compareTo(other.label)
    return result
  }

}

package org.motrice.coordinatrice

/**
 * A BPMN process may have associated guides in several locales.
 * Given a process definition key, define an URL pattern for those guides.
 * It is possible to define that a pattern is valid beginning from a
 * specific process definition version.
 * An URL pattern has placeholders.
 */
class CrdI18nGuideUrl implements Comparable {

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

  // The URL pattern
  String pattern

  static mapping = {
    table 'crd_i18n_guide_url'
    // This seems to have no effect in Grails 2.2.4
    procdefVer defaultValue: 0
  }
  // A lot of indexes are justified because this is a read-mostly structure.
  // Simplify read at the expense of inserts and updates.
  static constraints = {
    procdefKey maxSize: 255
    procdefVer min: 0, unique: ['procdefKey']
    pattern maxSize: 400, nullable: true, unique: ['procdefKey', 'procdefVer']
  }

  /**
   * The equivalent of a copy constructor.
   */
  static CrdI18nGuideUrl copyPattern(CrdI18nGuideUrl srcPattern) {
    new CrdI18nGuideUrl(procdefKey: srcPattern.procdefKey, procdefVer: srcPattern.procdefVer,
    pattern: srcPattern.pattern)
  }

  String toString() {
    "${procdefKey}|${procdefVer} '${pattern}'"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    (procdefKey + procdefVer + pattern).hashCode()
  }

  boolean equals(Object obj) {
    boolean result = obj instanceof CrdI18nGuideUrl
    if (result) {
      def other = (CrdI18nGuideUrl)obj
      result = (procdefKey == other.procdefKey) && (procdefVer == other.procdefVer)
      if (result) result = (pattern == null && other.pattern == null) ||
      (pattern && other.pattern && pattern == other.pattern)
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (CrdI18nGuideUrl)obj
    int result = procdefKey.compareTo(other.procdefKey)
    // Version in descending order
    if (result == 0) result = -procdefVer.compareTo(other.procdefVer)
    if (result == 0 && pattern) result = pattern.compareTo(other.pattern)
    return result
  }

}

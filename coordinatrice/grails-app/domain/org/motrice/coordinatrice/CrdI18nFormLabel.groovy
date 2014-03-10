package org.motrice.coordinatrice

/**
 * A locale-dependent label for a form (used as a start form).
 */
class CrdI18nFormLabel implements Comparable {

  // Form definition path (app-name/form-name)
  String formdefPath

  // Form definition version number.
  // Makes it possible to have more than one label for a given form.
  // The version number indicates the first version for which this label
  // should be used.
  // For instance, if formdefVer is 4 it means that the definition is valid for
  // form definitions version 4 and higher.
  // The first process version is 1 but the default value here is 0.
  Integer formdefVer

  // Locale string
  String locale

  // The form definition label
  String label

  static mapping = {
    table 'crd_i18n_form_label'
    // This seems to have no effect in Grails 2.2.4
    formdefVer defaultValue: 0
  }
  // A lot of indexes are justified because this is a read-mostly structure.
  // Simplify read at the expense of inserts and updates.
  static constraints = {
    formdefPath maxSize: 255
    formdefVer min: 0, unique: ['formdefPath']
    label maxSize: 255, nullable: true, unique: ['formdefPath', 'formdefVer']
  }

  /**
   * The equivalent of a copy constructor.
   */
  static CrdI18nFormLabel copyLabel(CrdI18nFormLabel srcLabel) {
    new CrdI18nFormLabel(formdefPath: srcLabel.formdefPath, formdefVer: srcLabel.formdefVer,
    label: srcLabel.label)
  }

  String toString() {
    "${formdefPath}|${formdefVer} '${label}'"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    (formdefPath + formdefVer + label).hashCode()
  }

  boolean equals(Object obj) {
    boolean result = obj instanceof CrdI18nFormLabel
    if (result) {
      def other = (CrdI18nFormLabel)obj
      result = (formdefPath == other.formdefPath) && (formdefVer == other.formdefVer)
      if (result) result = (label == null && other.label == null) ||
      (label && other.label && label == other.label)
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (CrdI18nFormLabel)obj
    int result = formdefPath.compareTo(other.formdefPath)
    // Version in descending order
    if (result == 0) result = -formdefVer.compareTo(other.formdefVer)
    if (result == 0 && label) result = label.compareTo(other.label)
    return result
  }

}

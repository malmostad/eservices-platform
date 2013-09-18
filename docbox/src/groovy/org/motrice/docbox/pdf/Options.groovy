package org.motrice.docbox.pdf

/**
 * An enumeration of possible values for a control, either exclusively
 * or multi-choice
 * Each value has a locale-dependent presentation string (label)
 * and a key string (value) that is stored in form data
 */
class Options {
  // List of key strings
  List keyList

  // Map key string to value
  Map kvMap

  Options() {
    keyList = []
    kvMap = [:]
  }

  /**
   * Add a key-value pair
   * Used when building options
   */
  def addKeyValue(String key, String value) {
    keyList << key
    kvMap[key] = value
  }

  boolean isEmpty() {
    keyList.isEmpty()
  }

  String display(String key) {
    kvMap[key]
  }

  String toString() {
    "{Options keys=${keyList} map=${kvMap}}"
  }

}

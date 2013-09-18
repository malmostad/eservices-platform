package org.motrice.docbox

/**
 * Various utilities
 */
class Util {
  /**
   * Remove any 'action' entry from a map
   * Typically used for 'params' in controller debugging messages
   */
  static Map clean(params) {
    def result = params.clone()
    result.remove('action')
    result.remove('controller')
    return result
  }
}

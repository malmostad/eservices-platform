package org.motrice.migratrice

/**
 * A service just to provide the local site name.
 * No database access, not transactional.
 */
class SiteService {
  static transactional = false
  
  def grailsApplication

  /**
   * The name of this Motrice site/instance.
   */
  String localSiteName() {
    grailsApplication.config.motrice.site.name ?: '***UNDEFINED***'
  }

}

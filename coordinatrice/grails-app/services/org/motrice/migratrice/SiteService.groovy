package org.motrice.migratrice

import org.motrice.coordinatrice.pxd.PxdFormdef

/**
 * A service just to provide the local site name.
 * No database access, not transactional.
 */
class SiteService {
  static transactional = false
  
  def grailsApplication
  // Datasource injected to allow direct SQL query
  javax.sql.DataSource dataSource
  // Injected service defined in spring/resources.groovy
  def activitiManagementService
  def packageService

  /**
   * The name of this Motrice site/instance.
   */
  String localSiteName() {
    grailsApplication.config.motrice.site.name ?: '***UNDEFINED***'
  }

  /**
   * Create a list of important configuration settings, some with liveness
   */
  List configDisplay() {
    def list = []
    list << [name: 'config.item.applicationName', value: grailsApplication.metadata.'app.name']
    list << [name: 'config.item.applicationVersion', value: grailsApplication.metadata.'app.version']
    list << [name: 'config.item.siteName', value: localSiteName()]
    def item = [name: 'config.item.dataSource', value: grailsApplication.config.dataSource.url]
    item.live = dataSourceLive()
    list << item
    def actSchema = activitiDatabaseSchema()
    list << [name: 'config.item.activiti.schema', value: actSchema ?: '???', live: actSchema != null]
    item = [name: 'config.item.formBuilderBaseUri',
    value: grailsApplication.config.coordinatrice.orbeon.builder.base.uri]
    item.live = orbeonLive()
    list << item
    item = [name: 'config.item.postxdbBaseUri', value: grailsApplication.config.migratrice.postxdb.uri]
    item.live = postxdbLive()
    list << item

    return list
  }

  /**
   * Check that Activiti is live, get the schema version.
   * There isn't a lot of properties to choose from.
   */
  private String activitiDatabaseSchema() {
    def result = null
    try {
      def props = activitiManagementService.properties
      result = props['schema.version']
    } catch (Exception exc) {
      println "activitiDatabaseSchema: ${exc?.message}"
    }

    return result
  }

  /**
   * Check if the datasource is live
   */
  private boolean dataSourceLive() {
    def result = false
    try {
      def db = new groovy.sql.Sql(dataSource)
      def tup = db.firstRow('select 1 as one')
      def one = tup.ONE as Integer
      result = one == 1
    } catch (Exception exc) {
      println "dataSourceLive: ${exc?.message}"
    }

    return result
  }

  /**
   * Check orbeon liveness
   */
  private boolean orbeonLive() {
    def result = false
    try {
      String urlStr = "${grailsApplication.config.coordinatrice.orbeon.builder.base.uri}/summary"
      def url = new URL(urlStr)
      def summary = url.getText('UTF-8')
      result = summary?.length() > 0
    } catch (Exception exc) {
      println "orbeonLive: ${exc?.message}"
    }

    return result
  }

  /**
   * Check postxdb liveness.
   * TODO: If the formdef counts are different the two applications may
   * be connected to different databases.
   */
  private boolean postxdbLive() {
    def result = false
    try {
      def formdefList = packageService.allLocalFormdefs()
      def formdefCount = PxdFormdef.count()
      result = formdefList?.size() == formdefCount
    } catch (Exception exc) {
      println "postxdbLive: ${exc?.message}"
    }

    return result
  }

}

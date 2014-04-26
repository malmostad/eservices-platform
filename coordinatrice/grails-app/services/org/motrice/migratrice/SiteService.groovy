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
package org.motrice.migratrice

import org.motrice.coordinatrice.pxd.PxdFormdef

/**
 * A service just to provide the local site name.
 * No database access, not transactional.
 */
class SiteService {
  static transactional = false
  
  // States of configured resources
  static final STATE_CONFIGURED = 1
  static final STATE_NOT_CONFIGURED = 2
  static final STATE_OPERATIONAL = 3
  static final STATE_INOPERATIVE = 4
  static final STATE_DIAGNOSTIC = 5

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
    // If you ask for a non-configured configuration item the response is an empty Map.
    // Use ?: to convert to null and avoid complications.
    list << configItem('config.item.applicationName',
		       grailsApplication.metadata.'app.name' ?: null)
    list << configItem('config.item.applicationVersion',
		       grailsApplication.metadata.'app.version' ?: null)
    list << configItem('config.item.siteName',
		       grailsApplication.config.motrice.site.name ?: null)
    def item = configItem('config.item.dataSource',
			  grailsApplication.config.dataSource.url ?: null)
    dataSourceLive(list, item)
    activitiDatabaseSchema(list)
    item = configItem('config.item.postxdbBaseUri',
		      grailsApplication.config.migratrice.postxdb.uri ?: null)
    postxdbLive(list, item)
    item = configItem('config.item.formBuilderBaseUri',
		      grailsApplication.config.coordinatrice.orbeon.builder.base.uri ?: null)
    orbeonLive(list, item)

    return list
  }

  private Map configItem(String resourceName, String value) {
    def item = [name: resourceName, value: value]
    if (item.value) {
      item.state = STATE_CONFIGURED
    } else {
      item.value = '-???-'
      item.state = STATE_NOT_CONFIGURED
    }

    return item
  }

  private Map problemItem(String message) {
    [name: 'config.liveness.problem', value: message, state: STATE_DIAGNOSTIC]
  }

  /**
   * Check that Activiti is live, get the schema version.
   * There isn't a lot of properties to choose from.
   * SIDE EFFECT: Adds one or two config items (Map) to the list.
   */
  private activitiDatabaseSchema(List list) {
    def item = [name: 'config.item.activiti.schema']
    try {
      def props = activitiManagementService.properties
      item.value = props['schema.version']
      item.state = STATE_OPERATIONAL
      list << item
    } catch (Exception exc) {
      item.value = '???'
      item.state = STATE_INOPERATIVE
      list << item
      list << [name: 'config.liveness.problem', value: exc.message, state: STATE_DIAGNOSTIC]
    }
  }

  /**
   * Check if the datasource is live.
   * item must be the result of configItem
   * SIDE EFFECT: Adds one or two config items (Map) to the list.
   */
  private dataSourceLive(List list, Map item) {
    if (item.state == STATE_CONFIGURED) {
      def db = null
      try {
	db = new groovy.sql.Sql(dataSource)
	def tup = db.firstRow('select 1 as one')
	def one = tup.ONE as Integer
	item.state = STATE_OPERATIONAL
	list << item
      } catch (Exception exc) {
	item.state = STATE_INOPERATIVE
	list << item
	list << problemItem(exc.message)
      } finally {
	try {
	  db?.close()
	} catch (Exception exc2) {
	  // Ignore
	}
      }
    } else {
      list << item
    }
  }

  /**
   * Check orbeon liveness.
   * item must be the result of configItem
   * SIDE EFFECT: Adds one or two config items (Map) to the list.
   */
  private orbeonLive(List list, Map item) {
    if (item.state == STATE_CONFIGURED) {
      try {
	String urlStr = "${item.value}/summary"
	def url = new URL(urlStr)
	def summary = url.getText('UTF-8')
	item.state = (summary?.length() > 0)? STATE_OPERATIONAL : STATE_INOPERATIVE
	list << item
      } catch (Exception exc) {
	item.state = STATE_INOPERATIVE
	list << item
	list << problemItem(exc.message)
      }
    } else {
      list << item
    }
  }

  /**
   * Check postxdb liveness.
   * item must be the result of configItem
   * SIDE EFFECT: Adds one or two config items (Map) to the list.
   */
  private postxdbLive(List list, Map item) {
    if (item.state == STATE_CONFIGURED) {
      try {
	def formdefList = packageService.allLocalFormdefs()
	def formdefCount = PxdFormdef.count()
	if (formdefList?.size() == formdefCount) {
	  item.state = STATE_OPERATIONAL
	  list << item
	} else {
	  item.state = STATE_INOPERATIVE
	  list << item
	  item = problemItem('Postxdb uses a different database')
	  list << item
	}
      } catch (Exception exc) {
	item.state = STATE_INOPERATIVE
	list << item
	list << problemItem(exc.message)
      }
    } else {
      list << item
    }
  }

}

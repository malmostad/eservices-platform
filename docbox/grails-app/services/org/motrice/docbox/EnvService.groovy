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
package org.motrice.docbox

import org.apache.commons.logging.LogFactory

import org.motrice.docbox.form.PxdItem
import org.motrice.signatrice.audit.AuditRecord

/**
 * Methods for checking the environment
 */
class EnvService {
  
  // States of configured resources
  static final STATE_CONFIGURED = 1
  static final STATE_NOT_CONFIGURED = 2
  static final STATE_OPERATIONAL = 3
  static final STATE_INOPERATIVE = 4
  static final STATE_DIAGNOSTIC = 5

  def grailsApplication
  // Datasource injected to allow direct SQL query
  javax.sql.DataSource dataSource

  private static final log = LogFactory.getLog(this)

  // Required packages for running docbox
  public static final String PACKAGES =
    'docbook-xml docbook-xsl xsltproc fop libxml2-utils xmlstarlet fonts-liberation fonts-opensymbol'
    // Shell command to check if packages are installed
    public static final String CHECK = "for p in ${PACKAGES} ; " +
    'do dpkg -s "$p" >/dev/null 2>&1 && { echo "OK/$p" ; } || { echo "ERR/$p" ; } ; done'

    /**
     * Validate the environment where docbox is installed, mainly that
     * neede packages are present.
     * @return a map containing the following items
     * errorCount: number of errors during validation
     * pkgMessage: message containing the package status
     */
    Map validate() {
      // Check that packages are installed
      def process = ["/bin/bash", "-c", CHECK].execute()
      def pkgok = []
      def pkgerr = []
      process.in.eachLine {line -> 
	if (line.startsWith('ERR')) {
	  pkgerr << line
	} else {
	  pkgok << line
	}
      }

      def map = [:]
      map.put('errorCount', pkgerr.size())
      String msg = "Packages: ${(pkgerr + pkgok).join(', ')}"
      map.put('pkgMessage', msg)
      return map
    }

  /**
   * Create a list of important configuration settings, some with liveness.
   * Return a list of items.
   * See private method configItem for a description of items.
   */
  List configDisplay() {
    if (log.debugEnabled) log.debug "configDisplay <<"
    def list = []
    // If you ask for a non-configured configuration item the response is an empty Map.
    // Use ?: to convert to null and avoid complications.
    list << configItem('config.item.applicationName',
		       grailsApplication.metadata.'app.name' ?: null)
    list << configItem('config.item.applicationVersion',
		       grailsApplication.metadata.'app.version' ?: null)
    list << configItem('config.item.siteName',
		       grailsApplication.config.motrice.site.name ?: null)
    def strictProp = grailsApplication.config.docbox.signed.text.check.strict
    boolean lenientFlag = strictProp == 'false'
    list << configItem('config.item.strict.checking', lenientFlag? 'Off':'Strict')
    list << configItem('config.item.qrcode.base.url',
		       grailsApplication.config.docbox.signed.doc.base.url ?: null)
    packageItem(list)
    def item = configItem('config.item.dataSource',
			  grailsApplication.config.dataSource.url ?: null)
    def dbLive = dataSourceLive(list, item)

    // Check auxiliary datasources
    if (dbLive) {
      // Forms datasource
      def dbItem = configItem('config.forms.datasource',
			      grailsApplication.config.dataSource_forms.url ?: null)
      try {
	def rcount = PxdItem.count()
	dbItem.state = STATE_OPERATIONAL
	list << dbItem
      } catch (Exception exc) {
	dbItem.state = STATE_INOPERATIVE
	list << dbItem
	list << problemItem(exc.message)
      }

      // Audit datasource
      dbItem = configItem('config.audit.datasource',
			  grailsApplication.config.dataSource_audit.url ?: null)
      try {
	def rcount = AuditRecord.count()
	dbItem.state = STATE_OPERATIONAL
	list << dbItem
      } catch (Exception exc) {
	dbItem.state = STATE_INOPERATIVE
	list << dbItem
	list << problemItem(exc.message)
      }
    }

    if (log.debugEnabled) log.debug "configDisplay >> ${list?.size()}"
    return list
  }

  /**
   * A config item is a map with the following entries.
   * name: A resource name (translated through i18n) (String).
   * value: A value (String) or null if not configured.
   * state: One of the state constants declared at the top (int).
   */
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
   * Check packages, add the result to a list of config tests.
   */
  private boolean packageItem(List list) {
    boolean result = false
    def item = [name: 'config.item.packages', value: 'Operating System']
    def validation = validate()
    if (validation.errorCount == 0) {
      item.state = STATE_OPERATIONAL
      list << item
      result = true
    } else {
      item.state = STATE_INOPERATIVE
      list << item
      list << problemItem(validation.pkgMessage)
    }

    return result
  }

  /**
   * Check if the datasource is live and adds the result to a list
   * of previous results.
   * item must be the result of configItem
   * SIDE EFFECT: Adds one or two config items (Map) to the list.
   */
  private boolean dataSourceLive(List list, Map item) {
    boolean result = false
    if (item.state == STATE_CONFIGURED) {
      def db = null
      try {
	db = new groovy.sql.Sql(dataSource)
	def tup = db.firstRow('select 1 as one')
	def one = tup.ONE as Integer
	item.state = STATE_OPERATIONAL
	result = true
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

    return result
  }

}

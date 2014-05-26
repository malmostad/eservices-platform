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

import grails.converters.*

import org.motrice.coordinatrice.ServiceException

class RestMigrationController {
  // Injection magic
  def packageService

  /**
   * Upload and install a migration package from a file.
   * The file must be stored locally in the Tomcat root directory.
   * PUT $PREFIX/rest/migration/file/$filepath [?mode=installMode]
   * The install mode may be one of:
   * latestPublished
   * allPublished
   * compareVersions
   */
  def installPackageFromFile(String filepath) {
    if (log.debugEnabled) log.debug "INSTALL PACKAGE FROM FILE ${params}"
    def pack = null
    def msg = null
    def responseMap = [file: filepath]
    
    try {
      pack = packageService.importPackageFromFile(filepath)
    } catch (MigratriceException exc) {
      response.status = 409
      msg = message(code: exc.code)
    }

    if (msg) {
      responseMap.status = 'Not installed'
      responseMap.message = msg
      render responseMap as JSON
      return
    }
    def installModeCode = params.mode
    def installMode = 'migPackage.install.option.compareVersions'

    switch (params.mode) {
    case 'allPublished':
    installMode = 'migPackage.install.option.allPublished'
    break
    case 'latestPublished':
    installMode = 'migPackage.install.option.latestPublished'
    break
    case 'compareVersions':
    installMode = 'migPackage.install.option.compareVersions'
    }

    def installModeMsg = message(code: installMode)

    def lw = null
    try {
      lw = packageService.installPackage(pack, installMode, installModeMsg)
      def reportText = lw.toString()
      pack.createReport(reportText)
      responseMap.report = reportText
      response.status = 200
      responseMap.status = 'Installed'

      if (lw?.code) {
	msg = message(code: lw.code, args: lw.args)
	response.status = 409
	responseMap.status = 'Conflict'
      }
    } catch (MigratriceException exc) {
      response.status = 409
      responseMap.status = 'Conflict'
      if (lw?.code) msg = message(code: lw.code, args: lw.args)
    }

    if (msg) responseMap.message = msg
    render responseMap as JSON
  }

}

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
   * The file must be stored locally in 
   */
  def installPackageFromFile(String filepath) {
    if (log.debugEnabled) log.debug "INSTALL PACKAGE FROM FILE ${params}"
    def pack = null
    def msg = null
    def responseMap = [file: filepath]
    
    try {
      pack = packageService.importPackageFromFile(filepath)
    } catch (MigratriceException exc) {
      msg = message(code: exc.code)
    }

    if (msg) {
      request.status = 409
      responseMap.status = 'Not installed'
      responseMap.message = msg
      render responseMap as JSON
      return
    }

    def lw = null
    def installMode = 'migPackage.install.option.compareVersions'
    def installModeMsg = message(code: installMode)
    try {
      lw = packageService.installPackage(pack, installMode, installModeMsg)
      def reportText = lw.toString()
      pack.createReport(reportText)
      msg = message(code: lw.code, args: lw.args)
      request.status = 200
      responseMap.status = 'Installed'
      responseMap.report = reportText
    } catch (MigratriceException exc) {
      request.status = 409
      responseMap.status = 'Conflict'
      msg = message(code: exc.code)
    }

    responseMap.message = msg
    render responseMap as JSON
  }

}

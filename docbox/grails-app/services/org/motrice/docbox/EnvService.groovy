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

/**
 * Methods for checking the environment
 */
class EnvService {
  // Required packages for running docbox
  public static final String PACKAGES =
    'docbook-xml docbook-xsl xsltproc fop libxml2-utils xmlstarlet fonts-liberation fonts-opensymbol'
  // Shell command to check if packages are installed
  public static final String CHECK = "for p in ${PACKAGES} ; " +
    'do dpkg -s "$p" >/dev/null 2>&1 && { echo "OK/$p" ; } || { echo "ERR/$p" ; } ; done'

    /**
     * Validate the environment where docbox is installed
     * @return a map containing the following items
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
}

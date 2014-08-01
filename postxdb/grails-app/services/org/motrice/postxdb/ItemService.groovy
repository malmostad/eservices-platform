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
package org.motrice.postxdb

// The only way to create a logger with a predictable name
import org.apache.commons.logging.LogFactory

/**
 * Methods for supporting item operations.
 */
class ItemService {
  private static final log = LogFactory.getLog(this)

  /**
   * Do search and replace on the text of a PxdItem.
   * RETURN the new text, or null if no change.
   */
  String searchReplace(SearchReplaceCommand cmd) {
    if (log.debugEnabled) log.debug "searchReplace << ${cmd}"
    String result = null
    String srcText = cmd?.pxdItemObj?.text
    if (srcText) {
      result = cmd.globalFlag? srcText.replaceAll(cmd.search, cmd.replace) :
      srcText.replaceFirst(cmd.search, cmd.replace)
    }
    if (log.debugEnabled) log.debug "searchReplace >> (${result?.size()})"
    return result
  }

}

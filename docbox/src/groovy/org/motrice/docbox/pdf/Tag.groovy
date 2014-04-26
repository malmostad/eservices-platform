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
package org.motrice.docbox.pdf

/**
 * A tag descriptor
 * For our purposes a tag has a name and may have attributes and text
 */
class Tag {
  // Tag name
  String name

  // Map attribute name to attribute value (both String)
  // May be empty, but not null
  Map attr

  // Any text content, or null
  String text

  Tag(String name, String tagText, Map tagAttr) {
    this.name = name
    text = tagText
    attr = tagAttr
  }

  String getMediatype() {
    attr.mediatype
  }

  def setMediatype(String mediatype) {
    attr['mediatype'] = mediatype
  }

  /**
   * Our implicit way of determining if this is a media object
   */
  boolean isMedia() {
    attr.mediatype != null
  }

  boolean isFixedMedia() {
    isMedia() && attr.mediatype.size() > 0
  }

  String toString() {
    def textDisplay = (text != null)? "'${text}'" : 'null'
    def sb = new StringBuilder()
    sb.append('{Tag ').append(name).append(':')
    if (!attr?.isEmpty()) sb.append(' ').append(attr)
    if (text) sb.append(" '").append(text).append("'")
    sb.append('}')
    return sb.toString()
  }

}

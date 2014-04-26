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

import java.text.SimpleDateFormat

/**
 * Form metadata
 */
class FormMeta {
  public static final String FMT = 'yyyy-MM-dd HH:mm:ss'

  // application-name
  String appName

  // form-name
  String formName

  // title
  String title

  // title language (two-letter abbreviation)
  String titleLang

  // description
  String descr

  // description language (two-letter abbreviation)
  String descrLang

  // author
  String author

  // logo
  Attachment logo

  // Uuid identifying the form instance
  String instanceUuid

  // Document number of this form instance
  String docNo

  // PDF creation timestamp
  Date created

  // PDF format spec (an URL)
  String pdfFormatSpec

  /**
   * Construct from a map of metadata tags (String name to Tag)
   * We pick known metadata
   */
  FormMeta(Map metas) {
    def tag = metas['application-name']
    if (tag) appName = tag.text
    tag = metas['form-name']
    if (tag) formName = tag.text
    tag = metas['title']
    if (tag) {
      title = tag.text
      def keys = new ArrayList(tag.attr.keySet())
      if (!keys.isEmpty()) titleLang = tag.attr[keys[0]]
    }
    tag = metas['description']
    if (tag) {
      descr = tag.text
      def keys = new ArrayList(tag.attr.keySet())
      if (!keys.isEmpty()) descrLang = tag.attr[keys[0]]
    }
    tag = metas['author']
    if (tag) author = tag.text
    tag = metas['logo']
    if (tag) logo = new Attachment(tag)
  }

  String getCreatedFormatted() {
    created?.format(FMT)
  }

  String getFormdefPath() {
    "${appName}/${formName}"
  }

  String toString() {
    def sb = new StringBuilder()
    sb.append('{FormMeta ').append(formdefPath)
    sb.append(' [').append(title).append('][').append(descr).append(']')
    sb.append(' docNo=').append(docNo)
    sb.append(' created=').append(createdFormatted)
    sb.append(' format=').append(pdfFormatSpec)
    sb.append('}')
    return sb.toString()
  }

}

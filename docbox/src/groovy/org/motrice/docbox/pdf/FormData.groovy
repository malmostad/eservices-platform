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
 * Data that has been entered into a form
 * It is structured as a number of sections containing
 * a number of controls
 * A tag in this case is the name of a section or control element,
 * like "section-5", "control-7"
 */
class FormData {
  // Map tag name (String) to a tag descriptor (Tag)
  Map tag

  /**
   * Construct from a text containing the XML form data
   */
  FormData(String data) {
    if (!data) throw new IllegalArgumentException('Form data cannot be null')
    tag = [:]
    def form = new XmlSlurper().parseText(data)

    form.'*'.each {section ->
      section.'*'.each {control ->
	def name = control.name()
	tag[name] = new Tag(name, control.text(), control.attributes())
      }
    }
  }

  String toString() {
    "{FormData ${tag}}"
  }

}

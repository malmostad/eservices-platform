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
 * A section contains a list of controls (Tag)
 * ASSUMES sections have no attributes or text
 */
class Section {
  // Section name
  String name

  // List of controls (Control)
  List controls

  // Label text
  String label

  // Help text or null
  // TODO: Is it ever used?
  String help

  /**
   * Construct from a section node and a bind map
   * The bind map contains additional information about controls
   */
  Section(sectNode, Map bind) {
    def list = []
    name = sectNode.name()
    sectNode.'*'.each {contr ->
      def tag = new Tag(contr.name(), contr.text(), contr.attributes())
      def control = new Control(tag)
      control.addBind(bind[tag.name])
      list.add(control)
    }

    controls = list
  }

  String toString() {
    def sb = new StringBuilder()
    sb.append('{Section ').append(name).append(':')
    if (label) sb.append(" label='").append(label).append("'")
    if (!controls?.isEmpty()) sb.append(' controls=').append(controls)
    sb.append('}')
    return sb.toString()
  }
}

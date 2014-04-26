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
 * A control is a Tag plus some extra information
 */
class Control {
  // The name of the control, typically (but not necessarily)
  // "control-5"
  String name

  // Fixed text part of the control, expected attributes
  // The text may be fixed text or default text depending on the control type
  Tag tag

  // Expected value for this control, or null
  // Not all controls have a specified type
  String type

  // Is a value required?
  // Often unspecified, i.e. null
  Boolean required

  // Label text
  String label

  // Hint text or null
  String hint

  // Help text or null (seems not to be in active use)
  String help

  // Alert text or null
  // TODO: How is it used?
  String alert

  // Optional: Possible values for this control, similar to an enumeration
  // Null if the control does not have this kind of value set
  Options opts

  /**
   * Construct from a Tag
   */
  Control(Tag tag) {
    name = tag.name
    this.tag = tag
  }

  /**
   * Add bind information (type, required) during build
   * tag must be a bind tag
   */
  def addBind(Tag bindtag) {
    if (bindtag) {
      type = bindtag.attr['type']
      required = bindtag.attr['required']?.size() > 0
      if (bindtag.isFixedMedia() && !tag.isFixedMedia()) {
	tag.mediatype = bindtag.mediatype
      }
    }
  }

  /**
   * Get the text content of this control, possibly translated by means of
   * the Options
   * The result may be a String or List of String
   */
  def display(String formDataText) {
    def result = null

    if (opts) {
      result = []
      formDataText.split(' ').toList().collect(result) {key ->
	opts.display(key.trim())
      }
    } else {
      result = formDataText
    }

    return result
  }

  def isFixedMedia() {
    tag.isFixedMedia()
  }

  String toString() {
    "{Control ${name}: ${tag} type=${type} required=${required} label='${label}' hint='${hint}'}"
    def sb = new StringBuilder()
    sb.append('{Control ').append(name).append(': ').append(tag)
    if (type) sb.append(' type=').append(type)
    if (required) sb.append(' required')
    if (label) sb.append(" label='").append(label).append("'")
    if (hint) sb.append(" hint='").append(hint).append("'")
    if (!opts?.isEmpty()) sb.append(' ').append(opts)
    sb.append('}')
    return sb.toString()
  }
}

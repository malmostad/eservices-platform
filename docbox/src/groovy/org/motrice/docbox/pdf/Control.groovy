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
  def addBind(Tag tag) {
    if (tag) {
      type = tag.attr['type']
      required = tag.attr['required']?.size() > 0
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
    tag.isMedia() && tag.attr.mediatype.size() > 0
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

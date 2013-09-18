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

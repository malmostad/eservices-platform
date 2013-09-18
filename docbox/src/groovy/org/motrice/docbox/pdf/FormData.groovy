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

package org.motrice.docbox.pdf

import groovy.xml.MarkupBuilder

/**
 * An XForms form definition according to Orbeon, an xhtml document
 * The purpose is to support PDF generation
 */
class FormDef {
  // Document prelude
  static PRELUDE = '<?xml version="1.0" encoding="UTF-8"?>'
  static DOCBOOKNS = 'http://docbook.org/ns/docbook'

  // We use only a few of the dozen namespaces in an Orbeon form
  static NAMESPACES = [xforms:'http://www.w3.org/2002/xforms',
  xhtml:'http://www.w3.org/1999/xhtml', xbl:'http://www.w3.org/ns/xbl']

  static LOGOSPACE = '30mm'
  static HINTTEXT = 'Anvisning'

  // The form as a list of sections (Tag)
  List sections

  // Same information as the form list, but as a map
  // Key: tag name; Value: a Section or a Control (or null)
  Map controlMap

  // Resource list, List of Attachment
  // Paths of all resources required by this form definition
  List resourceList

  // Form metadata
  FormMeta meta

  // All form definition text
  String text

  FormDef(File inputFile) {
    text = inputFile.text
  }

  FormDef(String text) {
    this.text = text
  }

  /**
   * Parse the form definition, set up a data structure representing the
   * form definition (without filled-in data)
   */
  def build() {
    def html = new XmlSlurper().parseText(text)
    // There is only one model
    def model = html.'**'.find {it.@id.text() == 'fr-form-model'}

    // This can happen if we try to process a saved form in orbeon/builder
    // data.xml is a form definition in this case, an unfortunate mixture of ideas
    if (!model) throw new IllegalStateException('Failure. Is this in orbeon/builder? Does not work.')

    // Find the bind information
    def bindInst = model.bind.find {it.@id.text() == 'fr-form-binds'}
    def bindMap = buildBindings(bindInst)

    // Find the form instance, a list of sections, each containing a list of controls
    // The form instance is defined in the form definition as a kind of template
    // for filled-in forms
    def formInst = model.instance.find {it.@id.text() == 'fr-form-instance'}
    sections = formInst.form.'*'.collect {new Section(it, bindMap)}

    // Post-process to create the control map
    controlMap = createControlsDictionary(sections)

    // Find form metadata
    def formMeta = model.instance.find {it.@id.text() == 'fr-form-metadata'}
    meta = buildMeta(formMeta)

    // Find form resources
    // TODO: We assume there is only one set of resources
    // Potentially there may be one set per supported language
    def formRes = model.instance.find {it.@id.text() == 'fr-form-resources'}
    // Add resource information to controls and sections
    addResources(formRes)
  }

  /**
   * Add resources (= text) to sections and controls
   */
  private addResources(formRes) {
    formRes.resources.resource.'*'.each {ctrl ->
      def cname = ctrl.name()
      def gadget = controlMap[cname]
      def opts = new Options()
      ctrl.'*'.each {elem ->
	def ename = elem.name()
	def text = elem.text()
	if (ename == 'label') {
	  gadget.label = text
	} else if (ename == 'hint') {
	  gadget.hint = text
	} else if (ename == 'help') {
	  gadget.help = text
	} else if (ename == 'alert') {
	  gadget.alert = text
	} else if (ename == 'item') {
	  def value = elem.label.text()
	  def key = elem.value.text()
	  opts.addKeyValue(key, value)
	}
      }

      if (!opts.isEmpty()) gadget.opts = opts
    }
  }

  /**
   * Traverse bindings, save the control bindings in a map (String, Tag)
   * where the key is the control name (like "control-5")
   */
  private buildBindings(bindInst) {
    def map = [:]
    bindInst.bind.each {sectBind ->
      sectBind.bind.each {ctrlBind ->
	def name = ctrlBind.@name.text()
	map[name] = new Tag(name, ctrlBind.text(), ctrlBind.attributes())
      }
    }

    return map
  }

  /**
   * Traverse metadata to build a map of Tag as input for a FormMeta
   */
  private buildMeta(formMeta) {
    def metas = [:]
    formMeta.metadata.'*'.each {metaTag ->
      def name = metaTag.name()
      def tag = new Tag(name, metaTag.text(), metaTag.attributes())
      metas[name] = tag
      if (tag.isMedia() && tag.text) {
	resourceList.add(new Attachment(tag))
      }
    }

    return new FormMeta(metas)
  }

  /**
   * Traverse sections and controls to create a map
   * A different way of organizing the same information
   * Also builds the resource list
   */
  private createControlsDictionary(List sections) {
    def map = [:]
    resourceList = []
    sections.each {section ->
      map[section.name] = section
      section.controls.each {control ->
	map[control.name] = control
	// The text of the tag is the path
	if (control.isFixedMedia()) {
	  resourceList.add(new Attachment(control.tag))
	}
      }
    }

    return map
  }

  /**
   * Generate DocBook XML from the filled-in form
   * RETURN a Map containing the following entries
   * xml: DocBook XML text
   * resources: Resources needed by the XML text
   * The key is a string with the following format: <data>/<resource name>
   * The resource itself must be retrieved from the database
   * Data names are assumed to be unique
   * The key is the last part (basename) of the resource
   */
  def generateDocBook(FormData fd) {
    def sw = new StringWriter()
    def pw = new PrintWriter(sw)
    pw.println(PRELUDE)
    pw.flush()
    def xml = new MarkupBuilder(sw)
    def map = [:]
    xml.article(xmlns: DOCBOOKNS, version: '5.0') {
      title(meta?.title ?: '--Form Title--')
      subtitle() {
	if (meta?.logo) {
	  def logo = meta.logo
	  // Add logo resource
	  map[logo.basename()] = logo
	  inlinemediaobject() {
	    imageobject() {
	      imagedata(fileref: logo.basename(), width: LOGOSPACE, scalefit: '0')
	    }
	  }
	  mkp.yield(meta?.descr ?: '')
	}
      } // subtitle

      sections.each {sect ->
	section() {
	  title(sect.label ?: '--Section Title--') {}
	  variablelist() {
	    sect.controls.each {ctrl ->
	      def tag = fd.tag[ctrl.name]
	      varlistentry() {
		term(ctrl.label? "${ctrl.label}: " : '') {}
		listitem() {
		  if (tag.isMedia()) {
		    def att = new Attachment(tag)
		    map[att.basename()] = att
		    mediaobject() {
		      imageobject() {
			imagedata(fileref: att.basename())
		      }
		      if (ctrl.hint && ctrl.isFixedMedia()) {
			caption() {
			  para(ctrl.hint) {}
			}
		      }
		    } // mediaobject
		  } // isMedia
		  else if (tag.text) {
		    def display = ctrl.display(tag.text)
		    if (display instanceof List) {
		      if (display.size() == 1) {
			para(display[0]) {}
		      }
		      else {
			itemizedlist(spacing: 'compact') {
			  display.each {item ->
			    listitem() {
			      para(item) {}
			    }
			  }
			}
		      } // else display size
		    } // if display instanceof List
		    else {
		      para(display) {}
		    }
		  }
		  if (ctrl.hint && !ctrl.isFixedMedia()) {
		    blockquote() {
		      para() {
			emphasis("${HINTTEXT}: ") {}
			mkp.yield(ctrl.hint)
		      }
		    }
		  } // if ctrl.hint
		} // listitem (varlistentry)
	      } // varlistentry
	    } // each ctrl
	  } // variablelist
	} // section
      } // each sections
    } // article

    sw.flush()
    map.xml = sw.toString()
    return map
  }

  /**
   * Generate a text preview, an unformatted rendering of all relevant text
   * RETURN a StringWriter containing the preview text
   */
  def generateTextPreview(FormData fd) {
    def sw = new StringWriter()
    def pw = new PrintWriter(sw)
    pw.println "PREVIEW ${meta?.appName}/${meta?.formName}"
    pw.println "  (language apparently '${meta?.titleLang}')"
    if (meta?.logo) pw.println "Logo: ${meta.logo}"
    pw.println "Title: ${meta?.title}"
    pw.println "Descr: ${meta?.descr}"
    pw.println "Auth:  ${meta?.author}"
    sections.each {sect ->
      pw.println ""
      pw.println sect.label.toUpperCase()
      sect.controls.each {ctrl ->
	def label = ctrl.label? "${ctrl.label}: " : ''
	def tag = fd.tag[ctrl.name]
	def sb = new StringBuilder()
	if (!tag?.attr?.isEmpty()) sb.append(tag.attr)
	if (tag.text) {
	  if (sb.length() > 0) sb.append(' ')
	  def display = ctrl.display(tag.text)
	  if (display instanceof List) {
	    if (display.size() == 1) {
	      sb.append(display[0])
	    } else {
	      display.each {sb.append('\n-- ').append(it)}
	    }
	  } else {
	    sb.append(display)
	  }
	}
	pw.println ""
	pw.println "${label}${sb}"
	if (ctrl.hint) pw.println "(${ctrl.hint})"
      }
    }

    pw.flush()
    return sw
  }

 String toString() {
    "{FormDef meta=${meta} sections=${sections}}"
  }

}

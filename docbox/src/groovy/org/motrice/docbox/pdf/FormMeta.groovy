package org.motrice.docbox.pdf

/**
 * Form metadata
 */
class FormMeta {
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

  String toString() {
    "{FormMeta ${appName}/${formName} '${title}'/'${descr}' logo=${logo}}"
  }

}

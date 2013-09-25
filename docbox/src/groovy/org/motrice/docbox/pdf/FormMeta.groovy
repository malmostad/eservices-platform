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

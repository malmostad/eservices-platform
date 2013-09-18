package org.motrice.docbox.pdf

/**
 * A tag descriptor
 * For our purposes a tag has a name and may have attributes and text
 */
class Tag {
  // Tag name
  String name

  // Map attribute name to attribute value (both String)
  // May be empty, but not null
  Map attr

  // Any text content, or null
  String text

  Tag(String name, String tagText, Map tagAttr) {
    this.name = name
    text = tagText
    attr = tagAttr
  }

  /**
   * Our implicit way of determining if this is a media object
   */
  boolean isMedia() {
    attr.mediatype != null
  }

  String toString() {
    def textDisplay = (text != null)? "'${text}'" : 'null'
    def sb = new StringBuilder()
    sb.append('{Tag ').append(name).append(':')
    if (!attr?.isEmpty()) sb.append(' ').append(attr)
    if (text) sb.append(" '").append(text).append("'")
    sb.append('}')
    return sb.toString()
  }

}

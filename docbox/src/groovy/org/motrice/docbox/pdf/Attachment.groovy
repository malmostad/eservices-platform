package org.motrice.docbox.pdf

/**
 * A form attachment
 */
class Attachment {
  // Database path (according to Orbeon conventions)
  // Use this path to find the attachment
  String path

  // Media type
  String mediaType

  // Original file name
  String origFilename

  // Size
  Integer size

  Attachment(Tag tag) {
    path = tag.text
    mediaType = tag.attr['mediatype']
    origFilename = tag.attr['filename']
    try {
      size = Integer.parseInt(tag.attr['size'])
    } catch (NumberFormatException exc) {
      // Ignore
    }
  }

  /**
   * Get the data name from the path
   */
  String dataname() {
    path.split('/')[-2]
  }

  /**
   * Get the base name from the path
   */
  String basename() {
    path.split('/')[-1]
  }

  String toString() {
    "[Attachment '${origFilename}' ${mediaType} size=${size} '${path}']"
  }
}

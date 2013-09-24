package org.motrice.docbox

import org.motrice.docbox.form.PxdFormdefVer
import org.motrice.docbox.form.PxdItem

/**
 * Basic document data as retrieved from the database.
 * Convenience class.
 */
class DocData {
  // The uuid (generated by Orbeon) that identifies the form instance
  String uuid

  // Form data as XML
  PxdItem dataItem

  // Form data attachments (List of PxdItem). May be empty.
  List auxItems

  // Form definition XML
  PxdItem formDef

  // Form definition items ("fixed items") except the XML. Example: logo.
  // List of PxdItem
  List fixedItems

  // Form definition metadata
  PxdFormdefVer formMeta

  DocData(String uuid, PxdItem dataItem) {
    this.uuid = uuid
    this.dataItem = dataItem
  }

  String toString() {
    "[DocData ${dataItem}: auxItems: ${auxItems?.size()}, form: ${formDef}, meta: ${formMeta}]"
  }

}
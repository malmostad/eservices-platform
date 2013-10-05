package org.motrice.docbox

import com.itextpdf.text.pdf.PdfDictionary
import com.itextpdf.text.pdf.PdfName

/**
 * Handles the special dictionaries that DocBox stores in  Pdf documents
 */
class PdfDictFactory {
  static final DOCBOXTYPE_KEY = new PdfName('DocBoxType')
}

package org.motrice.docbox

import com.itextpdf.text.pdf.PdfDictionary
import com.itextpdf.text.pdf.PdfName

/**
 * Handles the special dictionaries that DocBox stores in  Pdf documents
 */
class PdfDictFactory {
  // Entries that occur in all dictionaries
  static final DOCBOXTYPE_KEY = new PdfName('DocBoxType')
  static final FORMAT_KEY = new PdfName('Format')
  static final TIMESTAMP_KEY = new PdfName('Timestamp')
}
